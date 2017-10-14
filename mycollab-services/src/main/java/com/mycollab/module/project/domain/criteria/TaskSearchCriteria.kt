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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.domain.criteria

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.db.arguments.*
import com.mycollab.db.query.*
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum
import com.mycollab.module.project.i18n.TaskI18nEnum

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class TaskSearchCriteria : SearchCriteria() {

    var projectId: NumberSearchField? = null
    var parentTaskId: NumberSearchField? = null
    var name: StringSearchField? = null
    var dueDate: DateSearchField? = null
    var milestoneId: NumberSearchField? = null
    var assignUser: StringSearchField? = null
    var statuses: SetSearchField<String>? = null
    var priorities: SetSearchField<String>? = null
    var hasParentTask: BooleanSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField
        val p_taskname = CacheParamMapper.register(ProjectTypeConstants.TASK,
                GenericI18Enum.FORM_NAME, StringParam("taskname", "m_prj_task", "name"))

        @JvmField
        val p_assignee = CacheParamMapper.register(ProjectTypeConstants.TASK,
                GenericI18Enum.FORM_ASSIGNEE, PropertyListParam<String>("assignuser", "m_prj_task", "assignUser"))

        @JvmField
        val p_createdUser = CacheParamMapper.register(ProjectTypeConstants.TASK,
                ProjectCommonI18nEnum.ITEM_CREATED_PEOPLE, PropertyListParam<String>("createduser", "m_prj_task", "createdUser"))

        @JvmField
        val p_milestoneId = CacheParamMapper.register(ProjectTypeConstants.TASK, MilestoneI18nEnum.SINGLE,
                PropertyListParam<Int>("milestone", "m_prj_task", "milestoneId"))

        @JvmField
        val p_duedate = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_DUE_DATE,
                DateParam("duedate", "m_prj_task", "dueDate"))

        @JvmField
        val p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_LAST_UPDATED_TIME,
                DateParam("lastupdatedtime", "m_prj_task", "lastUpdatedTime"))

        @JvmField
        val p_createtime = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createtime", "m_prj_task", "createdTime"))

        @JvmField
        val p_status = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_STATUS,
                PropertyListParam<String>("status", "m_prj_task", "status"))

        @JvmField
        val p_startdate = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_START_DATE,
                DateParam("startdate", "m_prj_task", "startdate"))

        @JvmField
        val p_enddate = CacheParamMapper.register(ProjectTypeConstants.TASK, GenericI18Enum.FORM_END_DATE,
                DateParam("enddate", "m_prj_task", "enddate"))

        @JvmField
        val p_taskkey = CacheParamMapper.register(ProjectTypeConstants.TASK, TaskI18nEnum.FORM_TASK_KEY,
                NumberParam("key", "m_prj_task", "taskkey"))

        @JvmField
        val p_projectIds: PropertyListParam<*> = CacheParamMapper.register(ProjectTypeConstants.TASK, null,
                PropertyListParam<Int>("projectid", "m_prj_task", "projectid"))
    }
}
