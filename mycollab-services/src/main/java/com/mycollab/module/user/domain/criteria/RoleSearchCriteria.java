package com.mycollab.module.user.domain.criteria;

import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.StringSearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class RoleSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	private StringSearchField username;
	private StringSearchField roleName;

	public StringSearchField getUsername() {
		return username;
	}

	public void setUsername(StringSearchField username) {
		this.username = username;
	}

	public void setRoleName(StringSearchField roleName) {
		this.roleName = roleName;
	}

	public StringSearchField getRoleName() {
		return roleName;
	}

}
