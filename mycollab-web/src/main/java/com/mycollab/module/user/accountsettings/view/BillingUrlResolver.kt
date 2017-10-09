package com.mycollab.module.user.accountsettings.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.user.accountsettings.view.event.AccountBillingEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class BillingUrlResolver : AccountSettingUrlResolver() {
    init {
        this.defaultUrlResolver = SummaryUrlResolver()
        this.addSubResolver("history", HistoryUrlResolver())
        this.addSubResolver("cancel", CancelUrlResolver())
    }

    private class SummaryUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountBillingEvent.GotoSummary(this, null))
    }

    private class HistoryUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountBillingEvent.GotoHistory(this, null))
    }

    private class CancelUrlResolver : AccountSettingUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountBillingEvent.CancelAccount(this, null))
    }

    override fun defaultPageErrorHandler() = handlePage()
}