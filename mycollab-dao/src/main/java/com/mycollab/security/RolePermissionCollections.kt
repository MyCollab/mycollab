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

import com.google.common.collect.ImmutableList
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum

/**
 * Keep all permissions of MyCollab
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
object RolePermissionCollections {
    const val ACCOUNT_USER = "User"

    const val ACCOUNT_ROLE = "Role"

    const val ACCOUNT_BILLING = "Billing"

    const val ACCOUNT_THEME = "Theme"

    const val CREATE_NEW_PROJECT = "CreateNewProject"

    const val GLOBAL_PROJECT_SETTINGS = "GlobalProjectSettings"

    @JvmField
    val ACCOUNT_PERMISSION_ARR: List<PermissionDefItem> = ImmutableList.of(
            PermissionDefItem(ACCOUNT_USER, UserI18nEnum.SINGLE, AccessPermissionFlag::class.java),
            PermissionDefItem(ACCOUNT_ROLE, RoleI18nEnum.SINGLE, AccessPermissionFlag::class.java),
            PermissionDefItem(ACCOUNT_BILLING, RoleI18nEnum.OPT_BILLING_MANAGEMENT, BooleanPermissionFlag::class.java),
            PermissionDefItem(ACCOUNT_THEME, RoleI18nEnum.OPT_THEME, BooleanPermissionFlag::class.java))

    @JvmField
    val PROJECT_PERMISSION_ARR: List<PermissionDefItem> = ImmutableList.of(PermissionDefItem(
            CREATE_NEW_PROJECT, RoleI18nEnum.OPT_CREATE_NEW_PROJECT, BooleanPermissionFlag::class.java), PermissionDefItem(
            GLOBAL_PROJECT_SETTINGS, RoleI18nEnum.OPT_GLOBAL_PROJECT_SETTINGS, BooleanPermissionFlag::class.java))

}
