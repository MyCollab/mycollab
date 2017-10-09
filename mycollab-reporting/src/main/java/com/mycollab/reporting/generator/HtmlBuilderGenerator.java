package com.mycollab.reporting.generator;

import com.mycollab.reporting.ReportStyles;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class HtmlBuilderGenerator implements ComponentBuilderGenerator {
    private AbstractSimpleExpression expression;

    public HtmlBuilderGenerator(AbstractSimpleExpression expression) {
        this.expression = expression;
    }

    @Override
    public ComponentBuilder getCompBuilder(ReportStyles reportStyles) {
        return cmp.text(expression).setStyle(reportStyles.getHtmlStyle());
    }
}
