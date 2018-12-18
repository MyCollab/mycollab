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
package com.mycollab.module.tracker.domain

import com.mycollab.core.arguments.NotBindable
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.StringUtils

import java.util.Calendar
import java.util.Date

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.utils.StringUtils.isBlank
import java.time.LocalDateTime

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleBug : BugWithBLOBs() {

    var loguserFullName: String = ""
        get() = if (isBlank(field)) {
            StringUtils.extractNameFromEmail(createduser)
        } else field
    var loguserAvatarId: String? = null
    var assignUserAvatarId: String? = null
    var assignuserFullName: String = ""
        get() {
            if (isBlank(field)) {
                val displayName = assignuser
                return StringUtils.extractNameFromEmail(displayName)
            }
            return field
        }
    var projectname: String? = null
    var projectShortName: String? = null
    var numComments: Int? = null
    var billableHours: Double? = null
    var nonBillableHours: Double? = null
    var numFollowers: Int? = null

    @NotBindable
    var affectedVersions: List<Version>? = null

    @NotBindable
    var fixedVersions: List<Version>? = null

    @NotBindable
    var components: List<Component>? = null

    var comment: String? = null
    var milestoneName: String? = null

    val isCompleted: Boolean
        get() = isCompleted(this)

    val isOverdue: Boolean
        get() = isOverdue(this)

    val dueDateRoundPlusOne: LocalDateTime?
        get() {
            val value = duedate
            return if (value != null) value.plusDays(1) else null
        }

    enum class Field {
        selected,
        components,
        fixedVersions,
        affectedVersions,
        loguserFullName,
        assignuserFullName,
        milestoneName;

        fun equalTo(value: Any): Boolean = name == value
    }

    companion object {
        private val serialVersionUID = 1L

        @JvmStatic
        fun isCompleted(bug: BugWithBLOBs): Boolean = StatusI18nEnum.Verified.name == bug.status

        @JvmStatic
        fun isOverdue(bug: BugWithBLOBs): Boolean {
            if (StatusI18nEnum.Verified.name == bug.status) {
                return false
            }

            return when {
                bug.duedate != null -> {
                    // TODO: today must be the 0:0 time
                    val todayDate = LocalDateTime.now()
                    todayDate.isAfter(bug.duedate)
                }
                else -> false
            }
        }
    }
}
