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
import com.mycollab.core.reporting.NotInReport
import com.mycollab.core.utils.StringUtils
import com.mycollab.security.PermissionMap
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class SimpleRole : Role() {

    var permissionVal: String? = null

    @NotBindable
    @NotInReport
    var permissionMap: PermissionMap? = null
        get() = if (field == null) {
            if (StringUtils.isBlank(permissionVal)) {
                PermissionMap()
            } else {
                try {
                    PermissionMap.fromJsonString(permissionVal!!)
                } catch (e: Exception) {
                    LOG.error("Error while get permission", e)
                    PermissionMap()
                }

            }
        } else field

    val isSystemRole: Boolean
        get() = java.lang.Boolean.TRUE == issystemrole

    companion object {

        private val LOG = LoggerFactory.getLogger(SimpleRole::class.java)

        @JvmField
        val ADMIN = "Administrator"

        @JvmField
        val EMPLOYEE = "Employee"

        @JvmField
        val GUEST = "Guest"
    }
}
