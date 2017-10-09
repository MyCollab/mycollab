package com.mycollab.module.crm.domain;

public class SimpleQuote extends Quote {
    private String assignUserFullName;
    
    private String opportunityName;

    private String billingAccountName;

    private String billingContactName;

    private String shippingAccountName;

    private String shippingContactName;

    public String getAssignUserFullName() {
		return assignUserFullName;
	}

	public void setAssignUserFullName(String assignUserFullName) {
		this.assignUserFullName = assignUserFullName;
	}

	public String getBillingAccountName() {
        return billingAccountName;
    }

    public void setBillingAccountName(String billingAccountName) {
        this.billingAccountName = billingAccountName;
    }

    public String getBillingContactName() {
        return billingContactName;
    }

    public void setBillingContactName(String billingContactName) {
        this.billingContactName = billingContactName;
    }

    public String getShippingAccountName() {
        return shippingAccountName;
    }

    public void setShippingAccountName(String shippingAccountName) {
        this.shippingAccountName = shippingAccountName;
    }

    public String getShippingContactName() {
        return shippingContactName;
    }

    public void setShippingContactName(String shippingContactName) {
        this.shippingContactName = shippingContactName;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }
}
