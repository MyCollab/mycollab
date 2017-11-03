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

import com.mycollab.core.arguments.NotBindable
import com.mycollab.core.utils.StringUtils
import com.mycollab.security.PermissionMap

import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleProjectMember : ProjectMember() {

    var memberAvatarId: String? = null

    var memberFullName: String? = null

    var roleName: String? = null

    @NotBindable
    var permissionMaps: PermissionMap? = null

    var numOpenTasks: Int? = null

    var numOpenBugs: Int? = null

    var projectName: String? = null

    var email: String? = null

    var lastAccessTime: Date? = null

    var totalBillableLogTime: Double? = null

    var totalNonBillableLogTime: Double? = null

    val displayName: String?
        get() = if (StringUtils.isBlank(memberFullName)) {
            StringUtils.extractNameFromEmail(username)
        } else memberFullName

    val isProjectOwner: Boolean
        get() = java.lang.Boolean.TRUE == isadmin

    fun canRead(permissionItem: String): Boolean = permissionMaps != null && permissionMaps!!.canRead(permissionItem)

    fun canWrite(permissionItem: String): Boolean = permissionMaps != null && permissionMaps!!.canWrite(permissionItem)

    fun canAccess(permissionItem: String): Boolean =
            permissionMaps != null && permissionMaps!!.canAccess(permissionItem)

    enum class Field {
        roleName, memberFullName, totalBillableLogTime, totalNonBillableLogTime, projectName, numOpenTasks, numOpenBugs;

        fun equalTo(value: Any): Boolean = name == value
    }
}
