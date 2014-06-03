package com.esofthead.mycollab.reporting;

import java.util.Date;

import net.sf.dynamicreports.report.definition.ReportParameters;

import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class DateTimeExpression extends AbstractFieldExpression {
	private static final long serialVersionUID = 1L;

	public DateTimeExpression(String field) {
		super(field);
	}

	@Override
	public String evaluate(ReportParameters reportParameters) {
		Date date = reportParameters.getFieldValue(field);
		return AppContext.formatDateTime(date);
	}

}
