package com.mycollab.module.crm.domain.criteria;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

public class TargetSearchCriteria extends SearchCriteria {
    private StringSearchField targetName;

    private StringSearchField assignUserName;
    
    private StringSearchField assignUser;

    private StringSearchField accountName;

    private StringSearchField username;

    private NumberSearchField targetListId;

	public StringSearchField getTargetName() {
		return targetName;
	}

	public void setTargetName(StringSearchField targetName) {
		this.targetName = targetName;
	}

	public StringSearchField getAssignUserName() {
		return assignUserName;
	}

	public void setAssignUserName(StringSearchField assignUserName) {
		this.assignUserName = assignUserName;
	}

	public StringSearchField getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(StringSearchField assignUser) {
		this.assignUser = assignUser;
	}

	public StringSearchField getAccountName() {
		return accountName;
	}

	public void setAccountName(StringSearchField accountName) {
		this.accountName = accountName;
	}

	public StringSearchField getUsername() {
		return username;
	}

	public void setUsername(StringSearchField username) {
		this.username = username;
	}

	public NumberSearchField getTargetListId() {
		return targetListId;
	}

	public void setTargetListId(NumberSearchField targetListId) {
		this.targetListId = targetListId;
	}
}
