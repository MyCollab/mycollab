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
package com.mycollab.module.user.ui

import com.mycollab.core.MyCollabException
import com.vaadin.icons.VaadinIcons

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
object SettingAssetsManager {
    private val resources = mapOf(
            SettingUIConstants.PROFILE to VaadinIcons.BOOK,
            SettingUIConstants.BILLING to VaadinIcons.CREDIT_CARD,
            SettingUIConstants.USERS to VaadinIcons.USERS,
            SettingUIConstants.GENERAL_SETTING to VaadinIcons.COG,
            SettingUIConstants.THEME_CUSTOMIZE to VaadinIcons.MAGIC
    )

    @JvmStatic fun getAsset(resId: String): VaadinIcons =
            resources[resId] ?: throw MyCollabException("Can not find resource $resId")
}
