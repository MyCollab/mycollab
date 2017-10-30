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
package com.mycollab.module.user.domain

import com.mycollab.core.arguments.NotBindable
import com.mycollab.core.utils.StringUtils
import com.mycollab.security.PermissionMap
import com.google.common.base.MoreObjects

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleUser : User() {

    var roleid: Int? = null

    var roleName: String? = null

    @NotBindable
    var permissionMaps: PermissionMap? = null

    @NotBindable
    var isAccountOwner: Boolean? = null

    var subdomain: String? = null
    var accountId: Int? = null
    var registerstatus: String? = null
    var inviteUser: String? = null
    var lastModuleVisit: String? = null
    var inviteUserFullName: String? = null
    var displayName: String? = null
        get() {
            if (StringUtils.isBlank(field)) {
                val result = firstname + " " + lastname
                if (StringUtils.isBlank(result)) {
                    val displayName = username
                    return StringUtils.extractNameFromEmail(displayName)
                }
                return result
            }
            return field
        }
    var dateFormat: String? = null
        get() = MoreObjects.firstNonNull(field, "MM/dd/yyyy")
    var shortDateFormat: String? = null
        get() = MoreObjects.firstNonNull(field, "MM/dd")
    var longDateFormat: String? = null
        get() = MoreObjects.firstNonNull(field, "E, dd MMM yyyy")
    var showEmailPublicly: Boolean? = null

    @NotBindable
    var canSendEmail: Boolean? = null

    enum class Field {
        displayName, roleName, roleid;

        fun equalTo(value: Any): Boolean = name == value
    }
}
