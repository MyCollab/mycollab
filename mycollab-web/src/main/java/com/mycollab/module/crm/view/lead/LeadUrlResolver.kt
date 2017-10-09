package com.mycollab.module.crm.view.lead

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Lead
import com.mycollab.module.crm.event.LeadEvent
import com.mycollab.module.crm.view.CrmUrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class LeadUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", ListUrlResolver())
        this.addSubResolver("preview", PreviewUrlResolver())
        this.addSubResolver("add", AddUrlResolver())
        this.addSubResolver("edit", EditUrlResolver())
    }

    class ListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(LeadEvent.GotoList(this, null))
    }

    class AddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(LeadEvent.GotoAdd(this, Lead()))
    }

    class EditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val leadId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(LeadEvent.GotoEdit(this, leadId))
        }
    }

    class PreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val leadId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(LeadEvent.GotoRead(this, leadId))
        }
    }
}