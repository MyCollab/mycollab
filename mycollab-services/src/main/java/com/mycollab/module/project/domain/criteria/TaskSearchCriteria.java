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
package com.mycollab.module.project.domain.criteria;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.db.arguments.*;
import com.mycollab.db.query.*;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final StringParam p_taskname = CacheParamMapper.register(ProjectTypeConstants.TASK,
            GenericI18Enum.FORM_NAME, new StringParam("taskname", "m_prj_task", "name"));

    public static final PropertyListParam<String> p_assignee = CacheParamMapper.register(ProjectTypeConstants.TASK,
            GenericI18Enum.FORM_ASSIGNEE, new PropertyListParam<String>("assignuser", "m_prj_task", "assignUser"));

    public static final PropertyListParam<String> p_createdUser = CacheParamMapper.register(ProjectTypeConstants.TASK,
            ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE, new PropertyListParam<String>("createduser", "m_prj_task", "createdUser"));

    public static final PropertyListParam<Integer> p_milestoneId = CacheParamMapper.register(ProjectTypeConstants.TASK, MilestoneI18nEnum.SINGLE,
            new PropertyListParam<Integer>("milestone", "m_prj_task", "milestoneId"));

    public static final DateParam p_duedate = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_DUE_DATE,
            new DateParam("duedate", "m_prj_task", "dueDate"));

    public static final DateParam p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_LAST_UPDATED_TIME,
            new DateParam("lastupdatedtime", "m_prj_task", "lastUpdatedTime"));

    public static final DateParam p_createtime = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createtime", "m_prj_task", "createdTime"));

    public static final PropertyListParam p_status = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_STATUS,
            new PropertyListParam("status", "m_prj_task", "status"));

    public static final DateParam p_startdate = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_START_DATE,
            new DateParam("startdate", "m_prj_task", "startdate"));

    public static final DateParam p_enddate = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_END_DATE,
            new DateParam("enddate", "m_prj_task", "enddate"));

    public static final NumberParam p_taskkey = CacheParamMapper.register(ProjectTypeConstants.TASK, TaskI18nEnum.FORM_TASK_KEY,
            new NumberParam("key", "m_prj_task", "taskkey"));

    public static final PropertyListParam p_projectIds = CacheParamMapper.register(ProjectTypeConstants.TASK, null,
            new PropertyListParam("projectid", "m_prj_task", "projectid"));

    private NumberSearchField projectId;
    private NumberSearchField parentTaskId;
    private StringSearchField name;
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

    public StringSearchField getName() {
        return name;
    }

    public void setName(StringSearchField name) {
        this.name = name;
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
