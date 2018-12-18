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
import java.time.LocalDateTime

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
data class SimpleTask(var projectName: String? = null,
                      var projectShortname: String? = null,
                      var milestoneName: String? = null,
                      var assignUserAvatarId: String? = null,
                      var assignUserTimeZone: String? = null,
                      var logByAvatarId: String? = null,
                      var parentTaskKey: Int? = null,
                      var parentTaskName: String? = null,
                      var numSubTasks: Int? = null,
                      var billableHours: Double? = null,
                      var nonBillableHours: Double? = null,
                      var numFollowers: Int? = null,
                      var logByUserTimeZone: String? = null,
                      var numComments: Int? = null,
                      var comment: String? = null) : Task() {

    var assignUserFullName: String = ""
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(assignuser)
        } else field

    var logByFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(createduser)
        } else field

    val isCompleted: Boolean
        get() = StatusI18nEnum.Closed.name == status

    val isPending: Boolean
        get() = StatusI18nEnum.Pending.name == status

    val isOverdue: Boolean
        get() {
            if (!isCompleted) {
                val now = DateTimeUtils.getCurrentDateWithoutMS()
                return duedate != null && duedate.isBefore(now)
            }

            return false
        }

    val deadlineRoundPlusOne: LocalDateTime?
        get() {
            val value = duedate
            return if (value != null) value.plusDays(1) else null
        }

    enum class Field {
        selected, logByFullName, assignUserFullName, milestoneName;

        fun equalTo(value: Any): Boolean = name == value
    }
}
