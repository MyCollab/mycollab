/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.reporting;

import com.google.common.collect.Ordering;
import com.mycollab.common.domain.AuditChangeItem;
import com.mycollab.common.domain.SimpleAuditLog;
import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.AuditLogSearchCriteria;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.AuditLogService;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.form.view.builder.type.AbstractDynaField;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.reporting.ReportExportType;
import com.mycollab.reporting.ReportTemplateExecutor;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.formatter.DefaultFieldDisplayHandler;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.*;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
public class FormReportTemplateExecutor<B> extends ReportTemplateExecutor {
    private static final int FORM_CAPTION = 100;
    private static final Logger LOG = LoggerFactory.getLogger(FormReportTemplateExecutor.class);
    private static Ordering dateComparator = new Ordering() {
        @Override
        public int compare(Object o1, Object o2) {
            try {
                Date createTime1 = (Date) PropertyUtils.getProperty(o1, "createdtime");
                Date createTime2 = (Date) PropertyUtils.getProperty(o2, "createdtime");
                return createTime1.compareTo(createTime2);
            } catch (Exception e) {
                return 0;
            }
        }
    };
    private JasperReportBuilder reportBuilder;
    private MultiPageListBuilder titleContent;

    public FormReportTemplateExecutor(String reportTitle) {
        super(UserUIContext.getUserTimeZone(), UserUIContext.getUserLocale(), reportTitle, ReportExportType.PDF);
    }

    @Override
    public void initReport() throws Exception {
        reportBuilder = report();
        titleContent = cmp.multiPageList();
        titleContent.add(defaultTitleComponent());
        reportBuilder.setParameters(parameters);
        reportBuilder.title(titleContent)
                .setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
                .pageFooter(cmp.pageXofY().setStyle(reportStyles.getBoldCenteredStyle()))
                .setLocale(locale);
    }

    @Override
    public void fillReport() throws DRException {
        printForm();
        printActivities();
    }

    private void printForm() {
        Map<String, Object> parameters = this.getParameters();
        B bean = (B) parameters.get("bean");
        FormReportLayout formReportLayout = (FormReportLayout) parameters.get("layout");
        FieldGroupFormatter fieldGroupFormatter = AuditLogRegistry.getFieldGroupFormatterOfType(formReportLayout.getModuleName());

        try {
            String titleValue = (String) PropertyUtils.getProperty(bean, formReportLayout.getTitleField());
            HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text(titleValue)
                    .setStyle(reportStyles.getH2Style()));
            titleContent.add(historyHeader, cmp.verticalGap(10));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MyCollabException("Error", e);
        }

        DynaForm dynaForm = formReportLayout.getDynaForm();
        int sectionCount = dynaForm.getSectionCount();
        for (int i = 0; i < sectionCount; i++) {
            DynaSection section = dynaForm.getSection(i);
            if (section.isDeletedSection()) {
                continue;
            }

            if (section.getHeader() != null) {
                HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text(UserUIContext.getMessage(section.getHeader()))
                        .setStyle(reportStyles.getH3Style()));
                titleContent.add(historyHeader, reportStyles.line(), cmp.verticalGap(10));
            }

            if (section.isDeletedSection() || section.getFieldCount() == 0) {
                continue;
            }

            if (section.getLayoutType() == DynaSection.LayoutType.ONE_COLUMN) {
                for (int j = 0; j < section.getFieldCount(); j++) {
                    AbstractDynaField dynaField = section.getField(j);
                    if (!formReportLayout.getExcludeFields().contains(dynaField.getFieldName())) {
                        String value = "";
                        try {
                            Object tmpVal = PropertyUtils.getProperty(bean, dynaField.getFieldName());
                            if (tmpVal != null) {
                                if (tmpVal instanceof Date) {
                                    value = DateTimeUtils.formatDateToW3C((Date) tmpVal);
                                } else {
                                    value = String.valueOf(tmpVal);
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Error while getting property {}", dynaField.getFieldName(), e);
                        }
                        HorizontalListBuilder newRow = cmp.horizontalList().add(cmp.text(UserUIContext.getMessage(dynaField.getDisplayName()))
                                        .setFixedWidth(FORM_CAPTION).setStyle(reportStyles.getFormCaptionStyle()),
                                cmp.text(fieldGroupFormatter.getFieldDisplayHandler
                                        (dynaField.getFieldName()).getFormat().toString(value, false, "")));
                        titleContent.add(newRow);
                    }
                }
            } else if (section.getLayoutType() == DynaSection.LayoutType.TWO_COLUMN) {
                int columnIndex = 0;
                HorizontalListBuilder tmpRow = null;
                for (int j = 0; j < section.getFieldCount(); j++) {
                    AbstractDynaField dynaField = section.getField(j);
                    if (!formReportLayout.getExcludeFields().contains(dynaField.getFieldName())) {
                        String value = "";
                        try {
                            Object tmpVal = PropertyUtils.getProperty(bean, dynaField.getFieldName());
                            if (tmpVal != null) {
                                if (tmpVal instanceof Date) {
                                    value = DateTimeUtils.formatDateToW3C((Date) tmpVal);
                                } else {
                                    value = String.valueOf(tmpVal);
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Error while getting property {}", dynaField.getFieldName(), e);
                        }

                        try {
                            if (dynaField.isColSpan()) {
                                HorizontalListBuilder newRow = cmp.horizontalList().add(cmp.text(UserUIContext.getMessage(dynaField.getDisplayName()))
                                                .setFixedWidth(FORM_CAPTION).setStyle(reportStyles.getFormCaptionStyle()),
                                        cmp.text(fieldGroupFormatter.getFieldDisplayHandler
                                                (dynaField.getFieldName()).getFormat().toString(value, false, "")));
                                titleContent.add(newRow);
                                columnIndex = 0;
                            } else {
                                if (columnIndex == 0) {
                                    tmpRow = cmp.horizontalList().add(cmp.text(UserUIContext.getMessage(dynaField.getDisplayName()))
                                                    .setFixedWidth(FORM_CAPTION).setStyle(reportStyles.getFormCaptionStyle()),
                                            cmp.text(fieldGroupFormatter.getFieldDisplayHandler(dynaField.getFieldName())
                                                    .getFormat().toString(value, false, "")));
                                    titleContent.add(tmpRow);
                                } else {
                                    tmpRow.add(cmp.text(UserUIContext.getMessage(dynaField.getDisplayName())).setFixedWidth(FORM_CAPTION)
                                                    .setStyle(reportStyles.getFormCaptionStyle()),
                                            cmp.text(fieldGroupFormatter.getFieldDisplayHandler(dynaField.getFieldName())
                                                    .getFormat().toString(value, false, "")));
                                }

                                columnIndex++;
                                if (columnIndex == 2) {
                                    columnIndex = 0;
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Error while generate field " + BeanUtility.printBeanObj(dynaField), e);
                        }
                    }
                }
            } else {
                throw new MyCollabException("Does not support attachForm layout except 1 or 2 columns");
            }
        }
    }

    private void printActivities() {
        Map<String, Object> parameters = this.getParameters();
        B bean = (B) parameters.get("bean");
        Integer typeId;
        try {
            typeId = (Integer) PropertyUtils.getProperty(bean, "id");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Error", e);
            return;
        }

        FormReportLayout formReportLayout = (FormReportLayout) parameters.get("layout");

        CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
        final CommentSearchCriteria commentCriteria = new CommentSearchCriteria();
        commentCriteria.setType(StringSearchField.and(formReportLayout.getModuleName()));
        commentCriteria.setTypeId(StringSearchField.and(typeId + ""));
        final int commentCount = commentService.getTotalCount(commentCriteria);

        AuditLogService auditLogService = AppContextUtil.getSpringBean(AuditLogService.class);
        final AuditLogSearchCriteria logCriteria = new AuditLogSearchCriteria();
        logCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        logCriteria.setType(StringSearchField.and(formReportLayout.getModuleName()));
        logCriteria.setTypeId(StringSearchField.and(typeId + ""));
        final int logCount = auditLogService.getTotalCount(logCriteria);
        int totalNums = commentCount + logCount;
        HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text("History (" + totalNums + ")")
                .setStyle(reportStyles.getH3Style()));
        titleContent.add(historyHeader, reportStyles.line(), cmp.verticalGap(10));

        List<SimpleComment> comments = commentService.findPageableListByCriteria(new BasicSearchRequest<>(commentCriteria));
        List<SimpleAuditLog> auditLogs = auditLogService.findPageableListByCriteria(new BasicSearchRequest<>(logCriteria));
        List activities = new ArrayList(commentCount + logCount);
        activities.addAll(comments);
        activities.addAll(auditLogs);
        Collections.sort(activities, dateComparator.reverse());
        for (Object activity : activities) {
            if (activity instanceof SimpleComment) {
                titleContent.add(buildCommentBlock((SimpleComment) activity), cmp.verticalGap(10));
            } else if (activity instanceof SimpleAuditLog) {
                ComponentBuilder component = buildAuditBlock((SimpleAuditLog) activity);
                if (component != null) {
                    titleContent.add(component, cmp.verticalGap(10));
                }
            } else {
                LOG.error("Do not support activity " + activity);
            }
        }
    }

    private ComponentBuilder buildCommentBlock(SimpleComment comment) {
        TextFieldBuilder<String> authorField = cmp.text(StringUtils.trimHtmlTags(UserUIContext.getMessage(GenericI18Enum.EXT_ADDED_COMMENT, comment.getOwnerFullName(),
                UserUIContext.formatPrettyTime(comment.getCreatedtime())), Integer.MAX_VALUE)).setStyle(reportStyles.getMetaInfoStyle());
        HorizontalListBuilder infoHeader = cmp.horizontalFlowList().add(authorField);
        return cmp.verticalList(infoHeader, cmp.text(StringUtils.trimHtmlTags(comment.getComment(), Integer.MAX_VALUE)))
                .setStyle(reportStyles.getBorderStyle());
    }

    private ComponentBuilder buildAuditBlock(SimpleAuditLog auditLog) {
        List<AuditChangeItem> changeItems = auditLog.getChangeItems();
        FormReportLayout formReportLayout = (FormReportLayout) parameters.get("layout");
        FieldGroupFormatter fieldGroupFormatter = AuditLogRegistry.getFieldGroupFormatterOfType(formReportLayout.getModuleName());
        if (CollectionUtils.isNotEmpty(changeItems)) {
            TextFieldBuilder<String> authorField = cmp.text(StringUtils.trimHtmlTags(UserUIContext.getMessage(
                    GenericI18Enum.EXT_MODIFIED_ITEM, auditLog.getPostedUserFullName(), UserUIContext.formatPrettyTime
                            (auditLog.getPosteddate())), Integer.MAX_VALUE)).setStyle(reportStyles.getMetaInfoStyle());
            HorizontalListBuilder infoHeader = cmp.horizontalFlowList().add(authorField);
            VerticalListBuilder block = cmp.verticalList().add(infoHeader).setStyle(reportStyles.getBorderStyle());
            for (int i = 0; i < changeItems.size(); i++) {
                AuditChangeItem item = changeItems.get(i);
                String fieldName = item.getField();

                DefaultFieldDisplayHandler fieldDisplayHandler = fieldGroupFormatter.getFieldDisplayHandler(fieldName);
                if (fieldDisplayHandler != null) {
                    HorizontalListBuilder changeBlock = cmp.horizontalFlowList();
                    TextFieldBuilder<String> fieldLbl = cmp.text(UserUIContext.getMessage(fieldDisplayHandler
                            .getDisplayName())).setStyle(reportStyles.getMetaInfoStyle());
                    TextFieldBuilder<String> oldValue = cmp.text(fieldDisplayHandler.getFormat().toString(item.getOldvalue(), false, ""));
                    TextFieldBuilder<String> newValue = cmp.text(fieldDisplayHandler.getFormat().toString(item.getNewvalue(), false, ""));
                    changeBlock.add(fieldLbl, oldValue, cmp.text(" -> "), newValue);
                    block.add(changeBlock);
                }
            }
            return block;
        }
        return null;
    }

    @Override
    public void outputReport(OutputStream outputStream) throws IOException, DRException {
        reportBuilder.toPdf(outputStream);
    }
}
