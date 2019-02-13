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
package com.mycollab.module.project.domain

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.arguments.ValuedBean
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.tracker.domain.SimpleBug
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectTicket : ValuedBean(), Serializable {

    var name: String? = null

    var description: String? = null

    var assignUser: String? = null

    var assignUserFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(assignUser)
        } else field

    var assignUserAvatarId: String? = null

    var createdUser: String? = null

    var createdUserFullName: String? = null

    var createdUserAvatarId: String? = null

    var dueDate: LocalDate? = null

    var projectId: Int? = null

    lateinit var projectName: String

    lateinit var projectShortName: String

    lateinit var type: String

    var typeId: Int? = null

    var extraTypeId: Int? = null

    var status: String? = null

    var priority: String? = null

    var createdTime: LocalDateTime? = null

    var lastUpdatedTime: LocalDateTime? = null

    var saccountid: Int? = null

    var billableHours: Double? = null

    var nonBillableHours: Double? = null

    var numFollowers: Int? = null

    private var startDate: LocalDate? = null

    private var endDate: LocalDate? = null

    var milestoneId: Int? = null

    var milestoneName: String? = null

    var numComments: Int? = null

    var originalestimate: Double? = null

    var remainestimate: Double? = null

    val isBug: Boolean
        get() = ProjectTypeConstants.BUG == type

    val isMilestone: Boolean
        get() = ProjectTypeConstants.MILESTONE == type

    val isTask: Boolean
        get() = ProjectTypeConstants.TASK == type

    val isRisk: Boolean
        get() = ProjectTypeConstants.RISK == type

    val isOverdue: Boolean
        get() {
            if (dueDate != null && !isClosed) {
                return LocalDate.now().isAfter(dueDate!!)
            }
            return false
        }

    val isClosed: Boolean
        get() = StatusI18nEnum.Closed.name == status || StatusI18nEnum.Verified.name == status

    val dueDatePlusOne: LocalDate?
        get() {
            return dueDate?.plusDays(1)
        }

    fun getStartDate(): LocalDate? {
        return if (startDate != null) {
            startDate
        } else {
            if (endDate != null && dueDate != null) {
                if (endDate!!.isBefore(dueDate!!)) endDate else dueDate
            } else {
                if (endDate != null) endDate else dueDate
            }
        }
    }

    fun setStartDate(startDate: LocalDate) {
        this.startDate = startDate
    }

    fun getEndDate(): LocalDate? {
        return if (endDate != null) {
            endDate
        } else {
            if (startDate != null && dueDate != null) {
                if (startDate!!.isBefore(dueDate!!)) dueDate else startDate
            } else {
                if (startDate != null) startDate else dueDate
            }
        }
    }

    fun setEndDate(endDate: LocalDate) {
        this.endDate = endDate
    }

    companion object {
        private const val serialVersionUID = 1L

        @JvmStatic
        fun buildTask(bean: ProjectTicket): Task {
            val task = Task()
            task.id = bean.typeId
            task.projectid = bean.projectId
            task.name = bean.name
            task.startdate = bean.getStartDate()
            task.enddate = bean.getEndDate()
            task.duedate = bean.dueDate
            task.status = bean.status
            task.saccountid = bean.saccountid
            task.priority = bean.priority
            task.assignuser = bean.assignUser
            task.milestoneid = bean.milestoneId
            return task
        }

        @JvmStatic
        fun buildBug(bean: ProjectTicket): SimpleBug {
            val bug = SimpleBug()
            bug.id = bean.typeId
            bug.projectid = bean.projectId
            bug.name = bean.name
            bug.startdate = bean.getStartDate()
            bug.enddate = bean.getEndDate()
            bug.duedate = bean.dueDate
            bug.status = bean.status
            bug.priority = bean.priority
            bug.saccountid = bean.saccountid
            bug.assignuser = bean.assignUser
            bug.milestoneid = bean.milestoneId
            return bug
        }

        @JvmStatic
        fun buildRisk(bean: ProjectTicket): Risk {
            val risk = Risk()
            risk.id = bean.typeId
            risk.projectid = bean.projectId
            risk.name = bean.name
            risk.startdate = bean.getStartDate()
            risk.enddate = bean.getEndDate()
            risk.duedate = bean.dueDate
            risk.status = bean.status
            risk.saccountid = bean.saccountid
            risk.priority = bean.priority
            risk.assignuser = bean.assignUser
            risk.milestoneid = bean.milestoneId
            return risk
        }
    }
}
