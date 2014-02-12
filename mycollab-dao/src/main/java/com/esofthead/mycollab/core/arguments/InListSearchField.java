package com.esofthead.mycollab.core.arguments;

import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class InListSearchField extends ExtSearchField {
	private String expression;

	private List<?> list;

	public InListSearchField(String oper, String expression, List<?> list) {
		this.operation = oper;
		this.expression = expression;
		this.list = list;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}
}
