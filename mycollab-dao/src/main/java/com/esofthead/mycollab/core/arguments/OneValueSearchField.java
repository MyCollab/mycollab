package com.esofthead.mycollab.core.arguments;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class OneValueSearchField extends SearchField {

	private String queryCount;

	private String querySelect;

	private Object value;

	public OneValueSearchField(String oper, String expression, Object value) {
		this.operation = oper;
		this.queryCount = expression;
		this.querySelect = expression;
		this.value = value;
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
}
