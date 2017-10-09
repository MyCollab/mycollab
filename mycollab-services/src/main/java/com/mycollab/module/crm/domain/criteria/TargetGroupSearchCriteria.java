package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

public class TargetGroupSearchCriteria extends SearchCriteria {
    private StringSearchField assignUserName;

    private StringSearchField listName;

    private StringSearchField assignUser;

    private NumberSearchField targetId;

    private NumberSearchField contactId;

    private NumberSearchField leadId;
    
    private NumberSearchField campaignId;

	public StringSearchField getAssignUserName() {
		return assignUserName;
	}

	public void setAssignUserName(StringSearchField assignUserName) {
		this.assignUserName = assignUserName;
	}

	public StringSearchField getListName() {
		return listName;
	}

	public void setListName(StringSearchField listName) {
		this.listName = listName;
	}

	public StringSearchField getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(StringSearchField assignUser) {
		this.assignUser = assignUser;
	}

	public NumberSearchField getTargetId() {
		return targetId;
	}

	public void setTargetId(NumberSearchField targetId) {
		this.targetId = targetId;
	}

	public NumberSearchField getContactId() {
		return contactId;
	}

	public void setContactId(NumberSearchField contactId) {
		this.contactId = contactId;
	}

	public NumberSearchField getLeadId() {
		return leadId;
	}

	public void setLeadId(NumberSearchField leadId) {
		this.leadId = leadId;
	}

	public NumberSearchField getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(NumberSearchField campaignId) {
		this.campaignId = campaignId;
	}
    
}
