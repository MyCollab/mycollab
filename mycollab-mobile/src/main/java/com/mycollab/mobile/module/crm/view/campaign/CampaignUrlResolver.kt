package com.mycollab.mobile.module.crm.view.campaign

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CampaignEvent
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.CampaignWithBLOBs

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CampaignUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", CampaignListUrlResolver())
        this.addSubResolver("add", CampaignAddUrlResolver())
        this.addSubResolver("edit", CampaignEditUrlResolver())
        this.addSubResolver("preview", CampaignPreviewUrlResolver())
    }

    class CampaignListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this,
                    CrmModuleScreenData.GotoModule(arrayOf())))
        }
    }

    class CampaignAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CampaignEvent.GotoAdd(this, CampaignWithBLOBs()))
    }

    class CampaignEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoEdit(this, campaignId))
        }
    }

    class CampaignPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val campaignId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(CampaignEvent.GotoRead(this, campaignId))
        }
    }
}