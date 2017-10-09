package com.mycollab.mobile.module.crm.view.lead

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.event.LeadEvent
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.Lead

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class LeadUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", LeadListUrlResolver())
        this.addSubResolver("preview", LeadPreviewUrlResolver())
        this.addSubResolver("add", LeadAddUrlResolver())
        this.addSubResolver("edit", LeadEditUrlResolver())
    }

    class LeadListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this,
                        CrmModuleScreenData.GotoModule(arrayOf())))
    }

    class LeadAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(LeadEvent.GotoAdd(this, Lead()))
    }

    class LeadEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val leadId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(LeadEvent.GotoEdit(this, leadId))
        }
    }

    class LeadPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val leadId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(LeadEvent.GotoRead(this, leadId))
        }
    }
}