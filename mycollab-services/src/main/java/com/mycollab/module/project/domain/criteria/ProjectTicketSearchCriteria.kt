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
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectTicketSearchCriteria : SearchCriteria() {

    var projectIds: SetSearchField<Int>? = null

    var assignUser: StringSearchField? = null

    var isOpenned: SearchField? = null

    var unAssignee: SearchField? = null

    var name: StringSearchField? = null

    var dueDate: DateSearchField? = null

    var dateInRange: RangeDateSearchField? = null

    var milestoneId: NumberSearchField? = null

    var types: SetSearchField<String>? = null

    var typeIds: SetSearchField<Int>? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField
        val p_types = CacheParamMapper.register(ProjectTypeConstants.TICKET,
                GenericI18Enum.FORM_TYPE, object : SearchCriteriaBridgeParam<ProjectTicketSearchCriteria>("type") {
            override fun injectCriteriaInList(searchCriteria: ProjectTicketSearchCriteria, oper: String, value: Collection<String>): ProjectTicketSearchCriteria {
                searchCriteria.types = SetSearchField(oper, value)
                return searchCriteria
            }

            override fun injectCriteriaNotInList(searchCriteria: ProjectTicketSearchCriteria, oper: String, value: Collection<String>): ProjectTicketSearchCriteria {
                searchCriteria.types = SetSearchField(oper, value)
                return searchCriteria
            }
        })

        @JvmField
        val p_name = CacheParamMapper.register(ProjectTypeConstants.TICKET,
                GenericI18Enum.FORM_NAME, StringParam("name", "mainTbl", "name"))

        @JvmField
        val p_priority = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_PRIORITY,
                I18nStringListParam("priority", "mainTbl", "priority",
                        Arrays.asList(Priority.Urgent, Priority.High, Priority.Medium, Priority.Low, Priority.None)))

        @JvmField
        val p_milestones = CacheParamMapper.register(ProjectTypeConstants.TICKET, MilestoneI18nEnum.SINGLE,
                PropertyListParam<Int>("milestone", "mainTbl", "milestoneId"))

        @JvmField
        val p_assignee = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_ASSIGNEE,
                PropertyListParam<String>("assignuser", "mainTbl", "assignUser"))

        @JvmField
        val p_createdUser = CacheParamMapper.register(ProjectTypeConstants.TICKET,
                GenericI18Enum.OPT_CREATED_BY, PropertyListParam<String>("createduser", "mainTbl", "createdUser"))

        @JvmField
        val p_startDate = CacheParamMapper.register(ProjectTypeConstants.TICKET,
                GenericI18Enum.FORM_START_DATE, DateParam("startdate", "mainTbl", "startDate"))

        @JvmField
        val p_endDate = CacheParamMapper.register(ProjectTypeConstants.TICKET,
                GenericI18Enum.FORM_END_DATE, DateParam("enddate", "mainTbl", "endDate"))

        @JvmField
        val p_dueDate = CacheParamMapper.register(ProjectTypeConstants.TICKET,
                GenericI18Enum.FORM_DUE_DATE, DateParam("duedate", "mainTbl", "duedate"))

        @JvmField
        val p_createtime = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createtime", "mainTbl", "createdTime"))

        @JvmField
        val p_lastupdatedtime = CacheParamMapper.register(ProjectTypeConstants.TICKET, GenericI18Enum.FORM_LAST_UPDATED_TIME,
                DateParam("lastupdatedtime", "mainTbl", "lastUpdatedTime"))

        @JvmField
        val p_status = CacheParamMapper.register(ProjectTypeConstants.TICKET,
                GenericI18Enum.FORM_STATUS, I18nStringListParam("status", "mainTbl", "status", null))

        @JvmField
        val p_projectIds = CacheParamMapper.register(ProjectTypeConstants.TICKET, null,
                PropertyListParam<Int>("projectid", "mainTbl", "projectId"))
    }
}
