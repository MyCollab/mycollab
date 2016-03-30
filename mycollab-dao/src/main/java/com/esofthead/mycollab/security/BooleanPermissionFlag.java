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
 * Boolean permission flag
 *
 * @author MyCollab Ltd
 * @version 1.0
 */
public class BooleanPermissionFlag extends PermissionFlag {
    public static final int TRUE = 128;
    public static final int FALSE = 129;

    /**
     * Check whether <code>flag</code> is true permission
     *
     * @param flag
     * @return
     */
    public static boolean beTrue(Integer flag) {
        return (flag == TRUE);
    }

    /**
     * Check whether <code>flag</code> is false permission
     *
     * @param flag
     * @return
     */
    public static boolean beFalse(Integer flag) {
        return (flag == FALSE);
    }

    public static SecurityI18nEnum toKey(Integer flag) {
        return (flag == TRUE) ? SecurityI18nEnum.YES : SecurityI18nEnum.NO;
    }
}
