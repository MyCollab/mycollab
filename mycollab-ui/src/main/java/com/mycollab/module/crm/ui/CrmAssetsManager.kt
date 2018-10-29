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
package com.mycollab.module.crm.ui

import com.mycollab.core.MyCollabException
import com.mycollab.module.crm.CrmTypeConstants
import com.vaadin.icons.VaadinIcons

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
object CrmAssetsManager {
    private val resources = mutableMapOf(CrmTypeConstants.ACCOUNT to VaadinIcons.INSTITUTION,
            CrmTypeConstants.CONTACT to VaadinIcons.USER,
            CrmTypeConstants.OPPORTUNITY to VaadinIcons.MONEY,
            CrmTypeConstants.CASE to VaadinIcons.BUG,
            CrmTypeConstants.LEAD to VaadinIcons.BUILDING,
            CrmTypeConstants.ACTIVITY to VaadinIcons.CALENDAR,
            CrmTypeConstants.TASK to VaadinIcons.LINES_LIST,
            CrmTypeConstants.CALL to VaadinIcons.PHONE,
            CrmTypeConstants.MEETING to VaadinIcons.CALENDAR_O,
            CrmTypeConstants.CAMPAIGN to VaadinIcons.TROPHY,
            CrmTypeConstants.DETAIL to VaadinIcons.LIST,
            CrmTypeConstants.NOTE to VaadinIcons.CLIPBOARD
    )

    @JvmStatic fun getAsset(resId: String): VaadinIcons =
            resources[resId] ?: throw MyCollabException("Can not find the resource with id $resId")

    @JvmStatic fun toHexString(resId: String): String = "&#x" + Integer.toHexString(resources[resId]!!.getCodepoint())
}
