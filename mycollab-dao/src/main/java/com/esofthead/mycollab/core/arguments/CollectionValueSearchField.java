package com.esofthead.mycollab.core.arguments;

import java.util.Collection;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class CollectionValueSearchField extends SearchField {
	private String queryCount;

	private String querySelect;

	private Collection<?> value;

	public CollectionValueSearchField(String oper, String expression,
			Collection<?> value) {
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

	public Collection<?> getValue() {
		return value;
	}

	public void setValue(Collection<?> value) {
		this.value = value;
	}
}
