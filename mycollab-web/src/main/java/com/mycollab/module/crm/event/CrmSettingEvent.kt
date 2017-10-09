package com.mycollab.module.crm.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object CrmSettingEvent {
    class GotoNotificationSetting(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoCustomViewSetting(source: Any, val data: Any?) : ApplicationEvent(source)
}