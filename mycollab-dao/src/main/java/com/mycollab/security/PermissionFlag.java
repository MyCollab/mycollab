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
package com.mycollab.security;

import com.mycollab.common.i18n.SecurityI18nEnum;

import static com.mycollab.security.AccessPermissionFlag.*;
import static com.mycollab.security.BooleanPermissionFlag.FALSE;
import static com.mycollab.security.BooleanPermissionFlag.TRUE;

/**
 * Signal interface of Permission flag
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public class PermissionFlag {
    public static SecurityI18nEnum toVal(Integer flag) {
        if (flag == NO_ACCESS) {
            return SecurityI18nEnum.NO_ACCESS;
        } else if (flag == READ_ONLY) {
            return SecurityI18nEnum.READONLY;
        } else if (flag == READ_WRITE) {
            return SecurityI18nEnum.READ_WRITE;
        } else if (flag == ACCESS) {
            return SecurityI18nEnum.ACCESS;
        } else if (flag==TRUE) {
            return SecurityI18nEnum.YES;
        } else if (flag == FALSE) {
            return SecurityI18nEnum.NO;
        } else {
            return SecurityI18nEnum.UNDEFINE;
        }
    }
}
