package com.mycollab.module.crm.view.setting

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.event.CrmSettingEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CrmSettingUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("notification", NotificationSettingUrlResolver())
        this.addSubResolver("customlayout", CustomLayoutUrlResolver())
    }

    class NotificationSettingUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CrmSettingEvent.GotoNotificationSetting(this, null))
    }

    class CustomLayoutUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CrmSettingEvent.GotoCustomViewSetting(this, null))
    }

}