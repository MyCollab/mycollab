/**
 * This file is part of mycollab-dao.
 *
 * mycollab-dao is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-dao is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-dao.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.security;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * Utility to check permission value
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public class PermissionChecker {
    /**
     * @param flag
     * @return true if <code>flag</code> is boolean permission flag
     */
    public static boolean isBooleanPermission(Integer flag) {
        return (flag >> 7) == 1;
    }

    /**
     * @param flag
     * @return true of <code>flag</code> is access permission
     */
    public static boolean isAccessPermission(Integer flag) {
        return (flag >> 3) == 0;
    }

    /**
     * Check whether permission value <code>flag</code> implies permission value
     * <code>impliedVal</code>
     *
     * @param flag
     * @param impliedVal
     * @return
     */
    public static boolean isImplied(int flag, int impliedVal) {
        if (isBooleanPermission(flag)) {
            return flag == impliedVal;
        } else if (isAccessPermission(flag)) {
            if (impliedVal == AccessPermissionFlag.READ_ONLY) {
                return AccessPermissionFlag.canRead(flag);
            } else if (impliedVal == AccessPermissionFlag.READ_WRITE) {
                return AccessPermissionFlag.canWrite(flag);
            } else if (impliedVal == AccessPermissionFlag.ACCESS) {
                return AccessPermissionFlag.canAccess(flag);
            } else {
                return true;
            }
        } else {
            throw new MyCollabException(
                    "Do not support permission category except boolean and access permission, the check permission is "
                            + flag);
        }
    }
}
