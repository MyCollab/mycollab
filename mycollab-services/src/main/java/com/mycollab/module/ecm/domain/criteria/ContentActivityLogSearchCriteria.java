package com.mycollab.module.ecm.domain.criteria;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

public class ContentActivityLogSearchCriteria extends SearchCriteria {
	private StringSearchField createdUser;
	
	private StringSearchField fromPath;

	public StringSearchField getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(StringSearchField createdUser) {
		this.createdUser = createdUser;
	}

	public StringSearchField getFromPath() {
		return fromPath;
	}

	public void setFromPath(StringSearchField fromPath) {
		this.fromPath = fromPath;
	}
}
