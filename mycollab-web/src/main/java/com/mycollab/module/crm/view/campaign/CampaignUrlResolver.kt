package com.mycollab.module.crm.view.campaign

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.event.CampaignEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CampaignUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
    }

    class ListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CampaignEvent.GotoList(this, null))
    }

    class AddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CampaignEvent.GotoAdd(this, Account()))
    }

    class EditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoEdit(this, campaignId))
        }
    }

    class PreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoRead(this, campaignId))
        }
    }
}