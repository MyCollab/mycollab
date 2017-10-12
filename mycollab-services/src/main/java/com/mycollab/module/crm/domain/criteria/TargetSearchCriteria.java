/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
