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
import java.time.LocalDateTime

import java.util.Date

class SimpleItemTimeLogging : ItemTimeLogging() {

    var logUserAvatarId: String? = null

    var logUserFullName: String? = null

    var logUserRate: Double? = null

    var logUserOvertimeRate: Double? = null

    var projectName: String? = null
        get() = if (field == null) "" else field

    var projectShortName: String? = null
        get() = if (field == null) "" else field

    var name: String? = null

    var percentageComplete: Double? = null

    var status: String? = null

    var dueDate: LocalDateTime? = null

    var extraTypeId: Int? = null

    val isOverdue: Boolean
        get() = if (dueDate != null) {
            dueDate!!.isBefore(DateTimeUtils.getCurrentDateWithoutMS())
        } else false

    enum class Field {
        summary, projectName, logUserFullName;

        fun equalTo(value: Any): Boolean = name == value
    }
}
