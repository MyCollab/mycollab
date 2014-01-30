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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;

/**
 * 
 * @author MyCollab Ltd.
 */
public class ProjectGenericTaskSearchCriteria extends SearchCriteria {
	public static final String OPEN_STATUS = "Open";

	public static final String CLOSE_STATUS = "Closed";

	private NumberSearchField projectId;

	private StringSearchField assignUser;

	private SearchField isOpenned;

	private StringSearchField name;

	public NumberSearchField getProjectId() {
		return projectId;
	}

	public void setProjectId(NumberSearchField projectId) {
		this.projectId = projectId;
	}

	public StringSearchField getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(StringSearchField assignUser) {
		this.assignUser = assignUser;
	}

	public SearchField getIsOpenned() {
		return isOpenned;
	}

	public void setIsOpenned(SearchField isOpenned) {
		this.isOpenned = isOpenned;
	}

	public StringSearchField getName() {
		return name;
	}

	public void setName(StringSearchField name) {
		this.name = name;
	}

}
