package com.mycollab.module.user.accountsettings.view.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object AccountBillingEvent {
    class CancelAccount(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoSummary(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoHistory(source: Any, val data: Any?) : ApplicationEvent(source)
}