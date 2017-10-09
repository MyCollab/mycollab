package com.mycollab.mobile.module.crm.view.account

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.AccountEvent
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.Account

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

    class AccountListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountEvent.GotoList(this, null))
    }

    class AccountAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(AccountEvent.GotoAdd(this, Account()))
    }

    class AccountEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(AccountEvent.GotoEdit(this, accountId))
        }
    }

    class AccountPreviewUrlResolver() : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val accountId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(AccountEvent.GotoRead(this, accountId))
        }
    }
}