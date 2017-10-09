package com.mycollab.module.user.accountsettings.view.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object SetupEvent {
    class GotoSetupPage(source: Any, val data: Any?) : ApplicationEvent(source)
}