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

class SimpleRisk : Risk() {

    var risksource: String? = null

    var raisedByUserAvatarId: String? = null

    var raisedByUserFullName: String = ""
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(createduser)
        } else field

    var assignToUserAvatarId: String? = null

    var assignedToUserFullName: String = ""
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(assignuser)
        } else field

    lateinit var projectShortName: String

    lateinit var projectName: String

    var milestoneName: String? = null

    var billableHours: Double? = null

    var nonBillableHours: Double? = null

    val isCompleted: Boolean
        get() = StatusI18nEnum.Closed.name == status

    val isOverdue: Boolean
        get() {
            val now = DateTimeUtils.getCurrentDateWithoutMS()
            return StatusI18nEnum.Open.name == status && duedate != null && duedate.before(now)
        }

    enum class Field {
        assignedToUserFullName;

        fun equalTo(value: Any): Boolean = name == value
    }
}
