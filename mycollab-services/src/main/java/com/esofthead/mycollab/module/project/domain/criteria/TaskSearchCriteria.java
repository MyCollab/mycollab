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
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	private NumberSearchField projectid;
	private NumberSearchField taskListId;
	private NumberSearchField milestoneId;
	private NumberSearchField id;
	private StringSearchField assignUser;
	private SetSearchField<String> statuses;
	private SetSearchField<String> priorities;

	public SetSearchField<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(SetSearchField<String> statuses) {
		this.statuses = statuses;
	}

	public NumberSearchField getProjectid() {
		return projectid;
	}

	public void setProjectid(NumberSearchField projectid) {
		this.projectid = projectid;
	}

	public NumberSearchField getTaskListId() {
		return taskListId;
	}

	public void setTaskListId(NumberSearchField taskListId) {
		this.taskListId = taskListId;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public NumberSearchField getId() {
		return id;
	}

	public StringSearchField getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(StringSearchField assignUser) {
		this.assignUser = assignUser;
	}

	public NumberSearchField getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(NumberSearchField milestoneId) {
		this.milestoneId = milestoneId;
	}

	public SetSearchField<String> getPriorities() {
		return priorities;
	}

	public void setPriorities(SetSearchField<String> priorities) {
		this.priorities = priorities;
	}
}
