package com.mycollab.module.crm.view.account

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.event.AccountEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class AccountUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", AccountListUrlResolver())
        this.addSubResolver("preview", AccountPreviewUrlResolver())
        this.addSubResolver("add", AccountAddUrlResolver())
        this.addSubResolver("edit", AccountEditUrlResolver())
    }

    private class AccountListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountEvent.GotoList(this, null))
    }

    private class AccountAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountEvent.GotoAdd(this, Account()))
    }

    private class AccountEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(AccountEvent.GotoEdit(this, accountId))
        }
    }

    private class AccountPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(AccountEvent.GotoRead(this, accountId))
        }
    }
}