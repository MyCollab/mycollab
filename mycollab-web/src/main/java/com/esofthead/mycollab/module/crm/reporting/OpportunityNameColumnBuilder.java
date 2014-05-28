package com.esofthead.mycollab.module.crm.reporting;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.definition.ReportParameters;

import com.esofthead.mycollab.reporting.AbstractColumnFieldComponentBuilder;
import com.esofthead.mycollab.reporting.AbstractReportTemplate;
import com.esofthead.mycollab.reporting.ReportTemplateFactory;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class OpportunityNameColumnBuilder extends
		AbstractColumnFieldComponentBuilder {

	public OpportunityNameColumnBuilder(String fieldName) {
		super(fieldName);
	}

	@Override
	public ComponentBuilder getComponentBuilder() {
		HorizontalListBuilder componentBuilder = cmp.horizontalList();
		TextFieldBuilder textBuilder = null;
		AbstractReportTemplate reportTemplate = ReportTemplateFactory
				.getTemplate(AppContext.getLanguageSupport());

		ConditionalStyleBuilder overDueStyle = stl.conditionalStyle(
				new IsOpportunityOverDueExpression()).setForegroundColor(
				Color.RED);
		ConditionalStyleBuilder isCompleteStyle = stl.conditionalStyle(
				new IsOpportunityCompleteExpression()).setStrikeThrough(true);

		StyleBuilder styleBuilder = stl
				.style(reportTemplate.getUnderlineStyle())
				.addConditionalStyle(overDueStyle)
				.addConditionalStyle(isCompleteStyle);

//		textBuilder = cmp
//				.text(new StringExpression(fieldName))
//				.setHyperLink(
//						hyperLink(new CrmFieldComponetBuilderExpression()))
//				.setStyle(styleBuilder);
		componentBuilder.add(textBuilder);
		return componentBuilder;
	}

	private static class IsOpportunityCompleteExpression extends
			AbstractSimpleExpression<Boolean> {
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean evaluate(ReportParameters reportParameters) {
			String saleStage = reportParameters.getFieldValue("salesstage");
			if ("Closed Won".equals(saleStage)
					|| "Closed Lost".equals(saleStage)) {
				return true;
			}
			return false;
		}
	}

	private static class IsOpportunityOverDueExpression extends
			AbstractSimpleExpression<Boolean> {
		private static final long serialVersionUID = 1L;

		@Override
		public Boolean evaluate(ReportParameters reportParameters) {
			Date expectedCloseDate = reportParameters
					.getFieldValue("expectedcloseddate");
			if (expectedCloseDate != null
					&& (expectedCloseDate.before(new GregorianCalendar()
							.getTime()))) {
				String saleStage = reportParameters.getFieldValue("salesstage");
				if ("Closed Won".equals(saleStage)
						|| "Closed Lost".equals(saleStage)) {
					return false;
				}
				return true;
			}
			return false;
		}
	}

}
