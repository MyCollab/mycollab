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

import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.StringUtils
import com.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleMilestone : Milestone() {

    var ownerAvatarId: String? = null
    var ownerFullName: String = ""
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(assignuser)
        } else field

    var createdUserAvatarId: String? = null
    var createdUserFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(createduser)
        } else field

    var numOpenTasks: Int? = null
    var numTasks: Int? = null

    var numOpenBugs: Int? = null
    var numBugs: Int? = null

    var numOpenRisks: Int? = null
    var numRisks: Int? = null

    var totalTaskBillableHours: Double? = null
    var totalTaskNonBillableHours: Double? = null
    var totalBugBillableHours: Double? = null
    var totalBugNonBillableHours: Double? = null

    var projectShortName: String? = null
    var projectName: String? = null

    val isOverdue: Boolean
        get() {
            if (MilestoneStatus.Closed.name != status) {
                val now = DateTimeUtils.getCurrentDateWithoutMS()
                return enddate != null && enddate.before(now)
            }

            return false
        }

    val isCompleted: Boolean
        get() = MilestoneStatus.Closed.name == status

    enum class Field {
        numOpenTasks, numTasks, numOpenBugs, numBugs, ownerFullName, totalTaskBillableHours,
        totalTaskNonBillableHours, totalBugBillableHours, totalBugNonBillableHours, totalBillableHours, totalNonBillableHours;

        fun equalTo(value: Any): Boolean = name == value
    }
}
