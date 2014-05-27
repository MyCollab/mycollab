package com.esofthead.mycollab.reporting;

import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class MailExpression extends AbstractExpression {
	private static final long serialVersionUID = 1L;

	public MailExpression(String field) {
		super(field);
	}

	@Override
	public String evaluate(ReportParameters reportParameters) {
		String stringValue = reportParameters.getFieldValue(field).toString();
		return "mailto:" + stringValue;
	}

}
