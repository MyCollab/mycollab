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
