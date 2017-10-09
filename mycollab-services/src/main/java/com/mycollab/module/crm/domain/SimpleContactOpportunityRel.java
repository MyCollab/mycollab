package com.mycollab.module.crm.domain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class SimpleContactOpportunityRel extends SimpleContact {
	private static final long serialVersionUID = 1L;

	private String decisionRole;

	public String getDecisionRole() {
		return decisionRole;
	}

	public void setDecisionRole(String decisionRole) {
		this.decisionRole = decisionRole;
	}
}
