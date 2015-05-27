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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static final Param p_assignee = new PropertyListParam("task-assignuser",
			GenericI18Enum.FORM_ASSIGNEE, "m_prj_task", "assignUser");

	public static final Param p_tasklist = new PropertyListParam("task-list",
			TaskI18nEnum.FORM_TASKGROUP, "m_prj_task", "tasklistid");

	public static final Param p_duedate = new DateParam("task-duedate",
			TaskI18nEnum.FORM_DEADLINE, "m_prj_task", "deadline");

	public static final Param p_lastupdatedtime = new DateParam(
			"task-lastupdatedtime", GenericI18Enum.FORM_LAST_UPDATED_TIME,
			"m_prj_task", "lastUpdatedTime");

	public static final Param p_createtime = new DateParam("task-createtime",
			GenericI18Enum.FORM_CREATED_TIME, "m_prj_task", "createTime");

	public static final Param p_status = new StringListParam("task-status",
			TaskI18nEnum.FORM_STATUS, "m_prj_task", "status", Arrays.asList(
					"Open", "Pending", "Closed"));

	public static final Param p_startdate = new DateParam("task-startdate",
			TaskI18nEnum.FORM_START_DATE, "m_prj_task", "startdate");

	public static final Param p_actualstartdate = new DateParam(
			"task-actualstartdate", TaskI18nEnum.FORM_ACTUAL_START_DATE,
			"m_prj_task", "actualStartDate");

	public static final Param p_enddate = new DateParam("task-enddate",
			TaskI18nEnum.FORM_END_DATE, "m_prj_task", "enddate");

	public static final Param p_actualenddate = new DateParam("task-actualenddate",
			TaskI18nEnum.FORM_ACTUAL_END_DATE, "m_prj_task", "actualEndDate");

	private NumberSearchField projectid;
	private NumberSearchField taskListId;
	private NumberSearchField parentTaskId;
	private StringSearchField taskName;
    private DateSearchField dueDate;
	private NumberSearchField milestoneId;
	private NumberSearchField id;
	private StringSearchField assignUser;
	private SetSearchField<String> statuses;
	private SetSearchField<String> priorities;

	public NumberSearchField getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(NumberSearchField parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

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

    public DateSearchField getDueDate() {
        return dueDate;
    }

    public void setDueDate(DateSearchField dueDate) {
        this.dueDate = dueDate;
    }
}
