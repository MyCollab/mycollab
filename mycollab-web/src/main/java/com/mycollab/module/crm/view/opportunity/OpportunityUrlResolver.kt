package com.mycollab.module.crm.view.opportunity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.event.OpportunityEvent
import com.mycollab.module.crm.view.CrmUrlResolver

class OpportunityUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", OpportunityListUrlResolver())
        this.addSubResolver("add", OpportunityAddUrlResolver())
        this.addSubResolver("edit", OpportunityEditUrlResolver())
        this.addSubResolver("preview", OpportunityPreviewUrlResolver())
    }

    class OpportunityListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(OpportunityEvent.GotoList(this, null))

    }

    class OpportunityAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(OpportunityEvent.GotoAdd(this, Account()))
    }

    class OpportunityEditUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val opportunityId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(OpportunityEvent.GotoEdit(this, opportunityId))
        }
    }

    class OpportunityPreviewUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            val opportunityId = UrlTokenizer(params[0]).getInt()
            EventBusFactory.getInstance().post(OpportunityEvent.GotoRead(this, opportunityId))
        }
    }
}