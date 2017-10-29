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
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.StringUtils

import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleTask : Task() {
    lateinit var projectName: String
    lateinit var projectShortname: String
    var milestoneName: String? = null
    var assignUserAvatarId: String? = null
    var assignUserFullName: String = ""
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(assignuser)
        } else field
    var assignUserTimeZone: String? = null
    var logByAvatarId: String? = null
    var logByFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(createduser)
        } else field
    var logByUserTimeZone: String? = null
    var numComments: Int? = null
    var comment: String? = null
    var parentTaskName: String? = null
    var parentTaskKey: Int? = null
    var numSubTasks: Int? = null
    var billableHours: Double? = null
    var nonBillableHours: Double? = null
    var numFollowers: Int? = null

    val isCompleted: Boolean
        get() = StatusI18nEnum.Closed.name == status

    val isPending: Boolean
        get() = StatusI18nEnum.Pending.name == status

    val isOverdue: Boolean
        get() {
            if (!isCompleted) {
                val now = DateTimeUtils.getCurrentDateWithoutMS()
                return duedate != null && duedate.before(now)
            }

            return false
        }

    val deadlineRoundPlusOne: Date?
        get() {
            val value = duedate
            return if (value != null) DateTimeUtils.subtractOrAddDayDuration(value, 1) else null
        }

    enum class Field {
        selected, logByFullName, assignUserFullName, milestoneName;

        fun equalTo(value: Any): Boolean = name == value
    }
}
