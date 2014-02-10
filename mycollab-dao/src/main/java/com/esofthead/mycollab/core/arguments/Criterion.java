package com.esofthead.mycollab.core.arguments;

import java.io.Serializable;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public abstract class Criterion implements Serializable {
	private static final long serialVersionUID = 1L;

	protected String operation;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}
