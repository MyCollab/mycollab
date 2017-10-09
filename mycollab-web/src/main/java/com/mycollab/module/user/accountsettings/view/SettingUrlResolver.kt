package com.mycollab.module.user.accountsettings.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.user.accountsettings.view.event.SettingEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class SettingUrlResolver : AccountSettingUrlResolver() {
    init {
        this.addSubResolver("general", GeneralUrlResolver())
        this.addSubResolver("theme", ThemeUrlResolver())
    }

    private class GeneralUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(SettingEvent.GotoGeneralSetting(this, null))
    }

    private class ThemeUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(SettingEvent.GotoTheme(this, null))
    }
}