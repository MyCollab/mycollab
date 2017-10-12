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

import com.mycollab.common.i18n.SecurityI18nEnum

/**
 * Signal interface of Permission flag
 *
 * @author MyCollab Ltd
 * @since 1.0.0
 */
open class PermissionFlag {
    companion object {
        @JvmStatic fun toVal(flag: Int?): SecurityI18nEnum {
            return when {
                flag == null || flag == AccessPermissionFlag.NO_ACCESS -> SecurityI18nEnum.NO_ACCESS
                flag == AccessPermissionFlag.READ_ONLY -> SecurityI18nEnum.READONLY
                flag == AccessPermissionFlag.READ_WRITE -> SecurityI18nEnum.READ_WRITE
                flag == AccessPermissionFlag.ACCESS -> SecurityI18nEnum.ACCESS
                flag == BooleanPermissionFlag.TRUE -> SecurityI18nEnum.YES
                flag == BooleanPermissionFlag.FALSE -> SecurityI18nEnum.NO
                else -> SecurityI18nEnum.UNDEFINE
            }
        }
    }
}
