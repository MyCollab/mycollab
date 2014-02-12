package com.esofthead.mycollab.core.arguments;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class OneValueSearchField extends ExtSearchField {

	private String expression;

	private Object value;

	public OneValueSearchField(String oper, String expression, Object value) {
		this.operation = oper;
		this.expression = expression;
		this.value = value;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
