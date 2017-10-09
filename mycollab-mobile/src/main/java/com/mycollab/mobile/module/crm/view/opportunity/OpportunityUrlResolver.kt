package com.mycollab.mobile.module.crm.view.opportunity

import com.mycollab.common.UrlTokenizer
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.event.OpportunityEvent
import com.mycollab.mobile.module.crm.view.CrmModuleScreenData
import com.mycollab.mobile.module.crm.view.CrmUrlResolver
import com.mycollab.module.crm.domain.Opportunity

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class OpportunityUrlResolver : CrmUrlResolver() {
    init {
        this.addSubResolver("list", OpportunityListUrlResolver())
        this.addSubResolver("add", OpportunityAddUrlResolver())
        this.addSubResolver("edit", OpportunityEditUrlResolver())
        this.addSubResolver("preview", OpportunityPreviewUrlResolver())
    }

    class OpportunityListUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
                EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this,
                        CrmModuleScreenData.GotoModule(arrayOf())))
    }

    class OpportunityAddUrlResolver : CrmUrlResolver() {
        override fun handlePage(vararg params: String) =
            EventBusFactory.getInstance().post(OpportunityEvent.GotoAdd(this, Opportunity()))
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