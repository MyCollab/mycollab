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

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.StringListParam;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_assignee = new PropertyListParam("task-assignuser",
			"Assignee", "m_prj_task", "assignUser");
	public static Param p_tasklist = new PropertyListParam("task-list",
			"Task List", "m_prj_task", "tasklistid");
	public static Param p_duedate = new DateParam("task-duedate", "Due Date",
			"m_prj_task", "deadline");
	public static Param p_lastupdatedtime = new DateParam(
			"task-lastupdatedtime", "Last Updated Time", "m_prj_task",
			"lastUpdatedTime");
	public static Param p_createtime = new DateParam("task-createtime",
			"Create Time", "m_prj_task", "createTime");

	public static Param p_status = new StringListParam("task-status", "Status",
			"m_prj_task", "status", Arrays.asList("Open", "Pending", "Closed"));

	private NumberSearchField projectid;
	private NumberSearchField taskListId;
	private StringSearchField taskName;
	private NumberSearchField milestoneId;
	private NumberSearchField id;
	private StringSearchField assignUser;
	private SetSearchField<String> statuses;
	private SetSearchField<String> priorities;

	public StringSearchField getTaskName() {
		return taskName;
	}

	public void setTaskName(StringSearchField taskName) {
		this.taskName = taskName;
	}

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
