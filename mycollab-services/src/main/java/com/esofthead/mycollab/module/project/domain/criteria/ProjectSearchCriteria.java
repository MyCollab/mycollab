/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.domain.criteria;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;

public class ProjectSearchCriteria extends SearchCriteria {

	private StringSearchField username;
	private StringSearchField ownerName;
	private StringSearchField accountName;
	private SetSearchField<String> projectStatuses;
	private StringSearchField projectType;
	private StringSearchField projectName;
	private StringSearchField involvedMember;

	public StringSearchField getUsername() {
		return username;
	}

	public void setUsername(StringSearchField username) {
		this.username = username;
	}

	public StringSearchField getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(StringSearchField ownerName) {
		this.ownerName = ownerName;
	}

	public StringSearchField getAccountName() {
		return accountName;
	}

	public void setAccountName(StringSearchField accountName) {
		this.accountName = accountName;
	}

	public SetSearchField<String> getProjectStatuses() {
		return projectStatuses;
	}

	public void setProjectStatuses(SetSearchField<String> projectStatuses) {
		this.projectStatuses = projectStatuses;
	}

	public StringSearchField getProjectType() {
		return projectType;
	}

	public void setProjectType(StringSearchField projectType) {
		this.projectType = projectType;
	}

	public void setProjectName(StringSearchField projectName) {
		this.projectName = projectName;
	}

	public StringSearchField getProjectName() {
		return projectName;
	}

	public StringSearchField getInvolvedMember() {
		return involvedMember;
	}

	public void setInvolvedMember(StringSearchField involvedMember) {
		this.involvedMember = involvedMember;
	}
}
