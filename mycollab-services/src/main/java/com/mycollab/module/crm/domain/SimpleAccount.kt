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
package com.mycollab.module.crm.domain

import com.mycollab.core.utils.StringUtils

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleAccount : Account() {

    var createdUserAvatarId: String? = null

    var createdUserFullName: String? = null
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(createduser)
        } else field

    var assignUserAvatarId: String? = null

    var assignUserFullName: String = ""
        get() = if (StringUtils.isBlank(field)) {
            StringUtils.extractNameFromEmail(assignuser)
        } else field

    var numProjects: Int? = null

    enum class Field {
        assignUserFullName;

        fun equalTo(value: Any): Boolean = name == value
    }
}
