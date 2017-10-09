package com.mycollab.module.project.view.page;

import com.google.common.collect.Ordering;
import com.mycollab.common.domain.SimpleComment;
import com.mycollab.common.domain.criteria.CommentSearchCriteria;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.service.CommentService;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.page.domain.Page;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.reporting.ReportExportType;
import com.mycollab.reporting.ReportTemplateExecutor;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.MultiPageListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

/**
 * @author MyCollab Ltd
 * @since 5.4.0
 */
class PageReportTemplateExecutor extends ReportTemplateExecutor {

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

    PageReportTemplateExecutor(String reportTitle) {
        super(UserUIContext.getUserTimeZone(), UserUIContext.getUserLocale(), reportTitle, ReportExportType.PDF);
    }

    @Override
    public void initReport() throws Exception {
        reportBuilder = report();
        titleContent = cmp.multiPageList();
        titleContent.add(defaultTitleComponent());
        reportBuilder.setParameters(parameters);
        reportBuilder.title(titleContent).setPageFormat(PageType.A4, PageOrientation.PORTRAIT)
                .pageFooter(cmp.pageXofY().setStyle(reportStyles.getBoldCenteredStyle()))
                .setLocale(locale);
    }

    @Override
    public void fillReport() throws DRException {
        Map<String, Object> parameters = this.getParameters();
        Page bean = (Page) parameters.get("bean");
        printForm(bean);
        printActivities(bean);
    }

    @Override
    public void outputReport(OutputStream outputStream) throws IOException, DRException {
        reportBuilder.toPdf(outputStream);
    }

    private void printForm(Page bean) {
        HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text(bean.getSubject())
                .setStyle(reportStyles.getH3Style()));
        titleContent.add(historyHeader, reportStyles.line(), cmp.verticalGap(10));
        titleContent.add(cmp.text(StringUtils.formatRichText(bean.getContent())));
    }

    private void printActivities(Page bean) {
        CommentService commentService = AppContextUtil.getSpringBean(CommentService.class);
        final CommentSearchCriteria commentCriteria = new CommentSearchCriteria();
        commentCriteria.setType(StringSearchField.and(ProjectTypeConstants.PAGE));
        commentCriteria.setTypeId(StringSearchField.and(bean.getPath()));
        final int commentCount = commentService.getTotalCount(commentCriteria);
        HorizontalListBuilder historyHeader = cmp.horizontalList().add(cmp.text("Comments (" + commentCount + ")")
                .setStyle(reportStyles.getH3Style()));
        titleContent.add(historyHeader, reportStyles.line(), cmp.verticalGap(10));

        List<SimpleComment> comments = (List<SimpleComment>) commentService.findPageableListByCriteria(new BasicSearchRequest<>(commentCriteria));
        Collections.sort(comments, dateComparator.reverse());
        for (SimpleComment activity : comments) {
            titleContent.add(buildCommentBlock(activity), cmp.verticalGap(10));
        }
    }

    private ComponentBuilder buildCommentBlock(SimpleComment comment) {
        TextFieldBuilder<String> authorField = cmp.text(StringUtils.trimHtmlTags(UserUIContext.getMessage(GenericI18Enum.EXT_ADDED_COMMENT, comment.getOwnerFullName(),
                UserUIContext.formatPrettyTime(comment.getCreatedtime())), Integer.MAX_VALUE)).setStyle(reportStyles.getMetaInfoStyle());
        HorizontalListBuilder infoHeader = cmp.horizontalFlowList().add(authorField);
        return cmp.verticalList(infoHeader, cmp.text(StringUtils.trimHtmlTags(comment.getComment(), Integer.MAX_VALUE)))
                .setStyle(reportStyles.getBorderStyle());
    }
}
