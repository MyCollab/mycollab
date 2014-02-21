package com.esofthead.mycollab.core.arguments;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class NoValueSearchField extends SearchField {

	private String queryCount;
	
	private String querySelect;

	public NoValueSearchField(String oper, String expression) {
		this.operation = oper;
		this.queryCount = expression;
		this.querySelect = expression;
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
}
