package com.esofthead.mycollab.core.arguments;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class BetweenValuesSearchField extends SearchField {
	private static final long serialVersionUID = 1L;

	private String queryCount;

	private String querySelect;

	private Object value;

	private Object secondValue;

	public BetweenValuesSearchField(String oper, String expression,
			Object value, Object secondValue) {
		this.operation = oper;
		this.queryCount = expression;
		this.querySelect = expression;
		this.value = value;
		this.secondValue = secondValue;
	}

	public String getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(String queryCount) {
		this.queryCount = queryCount;
	}

	public String getQuerySelect() {
		return querySelect;
	}

	public void setQuerySelect(String querySelect) {
		this.querySelect = querySelect;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(Object secondValue) {
		this.secondValue = secondValue;
	}
}
