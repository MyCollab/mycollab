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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.*;
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.NumberParam;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final PropertyListParam<String> p_assignee = new PropertyListParam<>("task-assignuser",
            GenericI18Enum.FORM_ASSIGNEE, "m_prj_task", "assignUser");

    public static final PropertyListParam<Integer> p_milestoneId = new PropertyListParam<>("task-milestone",
            TaskI18nEnum.FORM_PHASE, "m_prj_task", "milestoneId");

    public static final DateParam p_duedate = new DateParam("task-duedate", GenericI18Enum.FORM_DUE_DATE, "m_prj_task",
            "deadline");

    public static final DateParam p_lastupdatedtime = new DateParam("task-lastupdatedtime", GenericI18Enum.FORM_LAST_UPDATED_TIME,
            "m_prj_task", "lastUpdatedTime");

    public static final DateParam p_createtime = new DateParam("task-createtime",
            GenericI18Enum.FORM_CREATED_TIME, "m_prj_task", "createdTime");

    public static final PropertyListParam p_status = new PropertyListParam("task-status",
            GenericI18Enum.FORM_STATUS, "m_prj_task", "status");

    public static final DateParam p_startdate = new DateParam("task-startdate", GenericI18Enum.FORM_START_DATE, "m_prj_task", "startdate");

    public static final DateParam p_enddate = new DateParam("task-enddate", GenericI18Enum.FORM_END_DATE, "m_prj_task", "enddate");

    public static final NumberParam p_taskkey = new NumberParam("task-key", TaskI18nEnum.FORM_TASK_KEY, "m_prj_task", "taskkey");

    public static final PropertyListParam p_projectIds = new PropertyListParam("task-projectid", null, "m_prj_task", "projectid");

    private NumberSearchField projectId;
    private NumberSearchField parentTaskId;
    private StringSearchField taskName;
    private DateSearchField dueDate;
    private NumberSearchField milestoneId;
    private StringSearchField assignUser;
    private SetSearchField<String> statuses;
    private SetSearchField<String> priorities;
    private BooleanSearchField hasParentTask;

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

    public BooleanSearchField getHasParentTask() {
        return hasParentTask;
    }

    public void setHasParentTask(BooleanSearchField hasParentTask) {
        this.hasParentTask = hasParentTask;
    }
}
