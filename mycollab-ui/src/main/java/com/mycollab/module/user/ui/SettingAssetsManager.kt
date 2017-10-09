package com.mycollab.module.user.ui

import com.mycollab.core.MyCollabException
import com.vaadin.server.FontAwesome

/**
 * @author MyCollab Ltd.
 * @since 5.0.0
 */
object SettingAssetsManager {
    private val resources: Map<String, FontAwesome> = mapOf(
            SettingUIConstants.PROFILE to FontAwesome.BOOK,
            SettingUIConstants.BILLING to FontAwesome.CREDIT_CARD,
            SettingUIConstants.USERS to FontAwesome.USERS,
            SettingUIConstants.GENERAL_SETTING to FontAwesome.GEAR,
            SettingUIConstants.THEME_CUSTOMIZE to FontAwesome.MAGIC,
            SettingUIConstants.SETUP to FontAwesome.WRENCH
    )

    @JvmStatic fun getAsset(resId: String): FontAwesome {
        return resources[resId] ?: throw MyCollabException("Can not find resource $resId")
    }
}
