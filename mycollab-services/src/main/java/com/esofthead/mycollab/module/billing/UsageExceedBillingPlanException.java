package com.esofthead.mycollab.module.billing;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class UsageExceedBillingPlanException extends MyCollabException {
	private static final long serialVersionUID = 1L;

	public UsageExceedBillingPlanException() {
		super("");
	}

	public UsageExceedBillingPlanException(String msg) {
		super(msg);
	}
}
