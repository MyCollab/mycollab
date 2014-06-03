package com.esofthead.mycollab.reporting;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1.2
 * 
 */
public abstract class AbstractFieldExpression extends
		AbstractSimpleExpression<String> {
	private static final long serialVersionUID = 1L;
	protected String field;

	public AbstractFieldExpression(String field) {
		this.field = field;
	}
}
