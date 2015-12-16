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

import com.esofthead.mycollab.common.i18n.SecurityI18nEnum;

/**
 * Access permission flag
 *
 * @since 1.0
 */
public class AccessPermissionFlag implements PermissionFlag {
    public static final int NO_ACCESS = 0;
    public static final int READ_ONLY = 1;
    public static final int READ_WRITE = 2;
    public static final int ACCESS = 4;

    /**
     * Check whether <code>flag</code> implies read permission
     *
     * @param flag
     * @return true of <code>flag</code> implies read permission
     */
    public static boolean canRead(Integer flag) {
        return ((flag & READ_ONLY) == READ_ONLY)
                || ((flag & READ_WRITE) == READ_WRITE)
                || ((flag & ACCESS) == ACCESS);
    }

    /**
     * Check whether <code>flag</code> implies write permission
     *
     * @param flag
     * @return true of <code>flag</code> implies write permission
     */
    public static boolean canWrite(int flag) {
        return ((flag & READ_WRITE) == READ_WRITE) || ((flag & ACCESS) == ACCESS);
    }

    /**
     * Check whether <code>flag</code> implies access permission
     *
     * @param flag
     * @return true of <code>flag</code> implies access permission
     */
    public static boolean canAccess(Integer flag) {
        return ((flag & ACCESS) == ACCESS);
    }

    public static SecurityI18nEnum toKey(Integer flag) {
        if (flag == null && flag == NO_ACCESS) {
            return SecurityI18nEnum.NO_ACCESS;
        } else if (flag == READ_ONLY) {
            return SecurityI18nEnum.READONLY;
        } else if (flag == READ_WRITE) {
            return SecurityI18nEnum.READ_WRITE;
        } else if (flag == ACCESS) {
            return SecurityI18nEnum.ACCESS;
        } else {
            return SecurityI18nEnum.NO_ACCESS;
        }
    }
}
