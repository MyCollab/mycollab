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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.security

import com.mycollab.core.MyCollabException

/**
 * Utility to check permission value
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
object PermissionChecker {
    /**
     * @param flag
     * @return true if `flag` is boolean permission flag
     */
    @JvmStatic fun isBooleanPermission(flag: Int?): Boolean {
        return flag!! shr 7 == 1
    }

    /**
     * @param flag
     * @return true of `flag` is access permission
     */
    @JvmStatic fun isAccessPermission(flag: Int?): Boolean {
        return flag!! shr 3 == 0
    }

    /**
     * Check whether permission value `flag` implies permission value
     * `impliedVal`
     *
     * @param flag
     * @param impliedVal
     * @return
     */
    @JvmStatic fun isImplied(flag: Int, impliedVal: Int): Boolean {
        return when {
            isBooleanPermission(flag) -> flag == impliedVal
            isAccessPermission(flag) -> when (impliedVal) {
                AccessPermissionFlag.READ_ONLY -> AccessPermissionFlag.canRead(flag)
                AccessPermissionFlag.READ_WRITE -> AccessPermissionFlag.canWrite(flag)
                AccessPermissionFlag.ACCESS -> AccessPermissionFlag.canAccess(flag)
                else -> true
            }
            else -> throw MyCollabException("Do not support permission category except boolean and access permission, the check permission is " + flag)
        }
    }
}
