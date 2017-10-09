package com.mycollab.reporting.generator;

import com.mycollab.reporting.ReportStyles;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;

/**
 * @author MyCollab Ltd.
 * @since 4.3.1
 */
public class HyperlinkBuilderGenerator implements ComponentBuilderGenerator {
    private DRIExpression title;
    private DRIExpression href;
    private StyleBuilder style;

    public HyperlinkBuilderGenerator(DRIExpression title, DRIExpression href) {
        this.title = title;
        this.href = href;
    }

    public DRIExpression getTitle() {
        return title;
    }

    public DRIExpression getHref() {
        return href;
    }

    public StyleBuilder getStyle() {
        return style;
    }

    public HyperlinkBuilderGenerator setStyle(StyleBuilder style) {
        this.style = style;
        return this;
    }

    @Override
    public ComponentBuilder getCompBuilder(ReportStyles reportStyles) {
        ComponentBuilder compBuilder = cmp.text(title).setHyperLink(hyperLink(href)).setStyle(reportStyles.getUnderlineStyle());
        if (style != null) {
            compBuilder.setStyle(style);
        }
        return compBuilder;
    }
}
