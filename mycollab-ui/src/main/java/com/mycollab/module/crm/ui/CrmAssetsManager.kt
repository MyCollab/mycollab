/**
 * mycollab-ui - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.ui

import com.mycollab.core.MyCollabException
import com.mycollab.module.crm.CrmTypeConstants
import com.vaadin.server.FontAwesome

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
object CrmAssetsManager {
    private val resources = mutableMapOf(CrmTypeConstants.ACCOUNT to FontAwesome.INSTITUTION,
            CrmTypeConstants.CONTACT to FontAwesome.USER,
            CrmTypeConstants.OPPORTUNITY to FontAwesome.MONEY,
            CrmTypeConstants.CASE to FontAwesome.BUG,
            CrmTypeConstants.LEAD to FontAwesome.BUILDING,
            CrmTypeConstants.ACTIVITY to FontAwesome.CALENDAR,
            CrmTypeConstants.TASK to FontAwesome.LIST_ALT,
            CrmTypeConstants.CALL to FontAwesome.PHONE,
            CrmTypeConstants.MEETING to FontAwesome.PLANE,
            CrmTypeConstants.CAMPAIGN to FontAwesome.TROPHY,
            CrmTypeConstants.DETAIL to FontAwesome.LIST,
            CrmTypeConstants.NOTE to FontAwesome.PENCIL
    )

    @JvmStatic fun getAsset(resId: String): FontAwesome {
        return resources[resId] ?: throw MyCollabException("Can not find the resource with id $resId")
    }

    @JvmStatic fun toHexString(resId: String): String {
        return "&#x" + Integer.toHexString(resources[resId]!!.getCodepoint())
    }
}
