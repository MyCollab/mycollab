package com.esofthead.mycollab.reporting;

import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public class StringExpression extends AbstractExpression {

	private static final long serialVersionUID = 1L;

	public StringExpression(String field) {
		super(field);
	}

	@Override
	public String evaluate(ReportParameters param) {
		return param.getFieldValue(field).toString();
	}
}
