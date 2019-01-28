/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.vaadin.reporting;

import com.google.common.collect.Ordering;
import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SearchCriteria.OrderField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.form.view.LayoutType;
import com.mycollab.form.view.builder.type.AbstractDynaField;
import com.mycollab.form.view.builder.type.DynaForm;
import com.mycollab.form.view.builder.type.DynaSection;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.reporting.ReportExportType;
import com.mycollab.reporting.ReportTemplateExecutor;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter;
import com.mycollab.vaadin.ui.registry.AuditLogRegistry;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
                LocalDateTime createTime1 = (LocalDateTime) PropertyUtils.getProperty(o1, "createdtime");
                LocalDateTime createTime2 = (LocalDateTime) PropertyUtils.getProperty(o2, "createdtime");
                return createTime1.compareTo(createTime2);
            } catch (Exception e) {
                return 0;
            }
        }
    };
    private JasperReportBuilder reportBuilder;
    private MultiPageListBuilder titleContent;

    public FormReportTemplateExecutor(String reportTitle, ZoneId zoneId, Locale locale) {
        super(zoneId, locale, reportTitle, ReportExportType.PDF);
    }

    @Override
    public void initReport() throws Exception {
        reportBuilder = report();
        titleContent = cmp.multiPageList();
        titleContent.add(defaultTitleComponent());
        reportBuilder.setParameters(getParameters());
        reportBuilder.title(titleContent)
                .setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
                .pageFooter(cmp.pageXofY().setStyle(getReportStyles().getBoldCenteredStyle()))
                .setLocale(getLocale());
    }

    @Override
    public void fillReport() throws DRException {
        printForm();
        printActivities();
    }

    private void printForm() {
        Map<String, Object> parameters = this.getParameters();
        B bean = (B) parameters.get("bean");
        SimpleUser user = (SimpleUser) parameters.get("user");
        FormReportLayout formReportLayout = (FormReportLayout) parameters.get("layout");
        FieldGroupFormatter fieldGroupFormatter = AuditLogRegistry.getFieldGroupFormatterOfType(formReportLayout.getModuleName());

        try {
            String titleValue = (String) PropertyUtils.getProperty(bean, formReportLayout.getTitleField());
            HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text(titleValue)
                    .setStyle(getReportStyles().getH2Style()));
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
                HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text(LocalizationHelper.getMessage(getLocale(), section.getHeader()))
                        .setStyle(getReportStyles().getH3Style()));
                titleContent.add(historyHeader, getReportStyles().line(), cmp.verticalGap(10));
            }

            if (section.isDeletedSection() || section.getFieldCount() == 0) {
                continue;
            }

            if (section.getLayoutType() == LayoutType.ONE_COLUMN) {
                for (int j = 0; j < section.getFieldCount(); j++) {
                    AbstractDynaField dynaField = section.getField(j);
                    if (!formReportLayout.getExcludeFields().contains(dynaField.getFieldName()) && !dynaField.getFieldName().startsWith("section-")) {
                        String value = "";
                        try {
                            Object tmpVal = PropertyUtils.getProperty(bean, dynaField.getFieldName());
                            if (tmpVal != null) {
                                if (tmpVal instanceof LocalDate) {
                                    value = DateTimeUtils.formatDateToW3C((LocalDate) tmpVal);
                                } else {
                                    value = String.valueOf(tmpVal);
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Error while getting property {}", dynaField.getFieldName(), e);
                        }
                        HorizontalListBuilder newRow = cmp.horizontalList().add(cmp.text(LocalizationHelper.getMessage(getLocale(), dynaField.getDisplayName()))
                                        .setFixedWidth(FORM_CAPTION).setStyle(getReportStyles().getFormCaptionStyle()),
                                cmp.text(fieldGroupFormatter.getFieldDisplayHandler
                                        (dynaField.getFieldName()).getFormat().toString(user, value, false, "")));
                        titleContent.add(newRow);
                    }
                }
            } else if (section.getLayoutType() == LayoutType.TWO_COLUMN) {
                int columnIndex = 0;
                HorizontalListBuilder tmpRow = null;
                for (int j = 0; j < section.getFieldCount(); j++) {
                    AbstractDynaField dynaField = section.getField(j);
                    if (!formReportLayout.getExcludeFields().contains(dynaField.getFieldName())) {
                        String value = "";
                        try {
                            Object tmpVal = PropertyUtils.getProperty(bean, dynaField.getFieldName());
                            if (tmpVal != null) {
                                if (tmpVal instanceof LocalDate) {
                                    value = DateTimeUtils.formatDateToW3C((LocalDate) tmpVal);
                                } else {
                                    value = String.valueOf(tmpVal);
                                }
                            }
                        } catch (Exception e) {
                            LOG.error("Error while getting property {}", dynaField.getFieldName(), e);
                        }

                        try {
                            System.out.println("Field: " + dynaField.getFieldName() + " -- " + fieldGroupFormatter.getFieldDisplayHandler(dynaField.getFieldName()).getFormat());

                            if (dynaField.isColSpan()) {
                                HorizontalListBuilder newRow = cmp.horizontalList().add(cmp.text(LocalizationHelper.getMessage(getLocale(), dynaField.getDisplayName()))
                                                .setFixedWidth(FORM_CAPTION).setStyle(getReportStyles().getFormCaptionStyle()),
                                        cmp.text(fieldGroupFormatter.getFieldDisplayHandler
                                                (dynaField.getFieldName()).getFormat().toString(user, value, false, "")));
                                titleContent.add(newRow);
                                columnIndex = 0;
                            } else {
                                if (columnIndex == 0) {
                                    tmpRow = cmp.horizontalList().add(cmp.text(LocalizationHelper.getMessage(getLocale(), dynaField.getDisplayName()))
                                                    .setFixedWidth(FORM_CAPTION).setStyle(getReportStyles().getFormCaptionStyle()),
                                            cmp.text(fieldGroupFormatter.getFieldDisplayHandler(dynaField.getFieldName())
                                                    .getFormat().toString(user, value, false, "")));
                                    titleContent.add(tmpRow);
                                } else {
                                    tmpRow.add(cmp.text(LocalizationHelper.getMessage(getLocale(), dynaField.getDisplayName())).setFixedWidth(FORM_CAPTION)
                                                    .setStyle(getReportStyles().getFormCaptionStyle()),
                                            cmp.text(fieldGroupFormatter.getFieldDisplayHandler(dynaField.getFieldName())
                                                    .getFormat().toString(user, value, false, "")));
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
        SimpleUser user = (SimpleUser) parameters.get("user");
        Integer typeId, saccountid;
        try {
            typeId = (Integer) PropertyUtils.getProperty(bean, "id");
            saccountid = (Integer) PropertyUtils.getProperty(bean, "saccountid");
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error("Error", e);
            return;
        }

        FormReportLayout formReportLayout = (FormReportLayout) parameters.get("layout");

        CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
        CommentSearchCriteria commentCriteria = new CommentSearchCriteria();
        commentCriteria.setType(StringSearchField.and(formReportLayout.getModuleName()));
        commentCriteria.setTypeId(StringSearchField.and(typeId + ""));
        commentCriteria.setSaccountid(NumberSearchField.equal(saccountid));
        commentCriteria.addOrderField(new OrderField("createdtime", SearchCriteria.DESC));
        int commentCount = commentService.getTotalCount(commentCriteria);
        HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text(LocalizationHelper.getMessage(user.getLocale(), GenericI18Enum.OPT_COMMENTS_VALUE, commentCount))
                .setStyle(getReportStyles().getH3Style()));
        titleContent.add(historyHeader, getReportStyles().line(), cmp.verticalGap(10));

        List<SimpleComment> comments = (List<SimpleComment>) commentService.findPageableListByCriteria(new BasicSearchRequest<>(commentCriteria));
        Collections.sort(comments, dateComparator.reverse());
        comments.forEach(comment -> titleContent.add(buildCommentBlock(comment), cmp.verticalGap(10)));
    }

    private ComponentBuilder buildCommentBlock(SimpleComment comment) {
        TextFieldBuilder<String> authorField = cmp.text(StringUtils.trimHtmlTags(LocalizationHelper.getMessage(getLocale(), GenericI18Enum.EXT_ADDED_COMMENT, comment.getOwnerFullName(),
                DateTimeUtils.getPrettyDateValue(comment.getCreatedtime(), getTimeZone(), getLocale())), Integer.MAX_VALUE)).setStyle(getReportStyles().getMetaInfoStyle());
        HorizontalListBuilder infoHeader = cmp.horizontalFlowList().add(authorField);
        return cmp.verticalList(infoHeader, cmp.text(StringUtils.trimHtmlTags(comment.getComment(), Integer.MAX_VALUE)))
                .setStyle(getReportStyles().getBorderStyle());
    }

    @Override
    public void outputReport(OutputStream outputStream) throws IOException, DRException {
        reportBuilder.toPdf(outputStream);
    }
}
