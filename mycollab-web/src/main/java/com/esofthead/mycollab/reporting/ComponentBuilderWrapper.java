package com.esofthead.mycollab.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.definition.expression.DRIExpression;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class ComponentBuilderWrapper {
	public static ComponentBuilder buildHyperLink(DRIExpression titleExpr,
			DRIExpression hrefExpr) {
		return cmp
				.text(titleExpr)
				.setHyperLink(hyperLink(hrefExpr))
				.setStyle(
						ReportTemplateFactory.getTemplate(null).underlineStyle);
	}
}
