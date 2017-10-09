package com.mycollab.mobile.module.crm.view

import com.mycollab.vaadin.EventBusFactory
import com.mycollab.mobile.module.crm.event.CrmEvent
import com.mycollab.mobile.module.crm.view.account.AccountUrlResolver
import com.mycollab.mobile.module.crm.view.activity.ActivityUrlResolver
import com.mycollab.mobile.module.crm.view.campaign.CampaignUrlResolver
import com.mycollab.mobile.module.crm.view.cases.CaseUrlResolver
import com.mycollab.mobile.module.crm.view.contact.ContactUrlResolver
import com.mycollab.mobile.module.crm.view.lead.LeadUrlResolver
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityUrlResolver
import com.mycollab.mobile.shell.ModuleHelper
import com.mycollab.mobile.shell.event.ShellEvent
import com.mycollab.vaadin.mvp.UrlResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class CrmUrlResolver() : UrlResolver() {
    fun build(): UrlResolver {
        this.addSubResolver("dashboard", DashboardUrlResolver())
        this.addSubResolver("account", AccountUrlResolver())
        this.addSubResolver("contact", ContactUrlResolver())
        this.addSubResolver("campaign", CampaignUrlResolver())
        this.addSubResolver("lead", LeadUrlResolver())
        this.addSubResolver("opportunity", OpportunityUrlResolver())
        this.addSubResolver("cases", CaseUrlResolver())
        this.addSubResolver("activity", ActivityUrlResolver())
        return this;
    }

    override fun handle(vararg params: String) {
        if (!ModuleHelper.isCurrentCrmModule) {
            EventBusFactory.getInstance().post(ShellEvent.GotoCrmModule(this, params))
        } else {
            super.handle(*params)
        }
    }

    override fun handlePage(vararg params: String) {
        super.handlePage(*params)
        EventBusFactory.getInstance().post(ShellEvent.GotoCrmModule(this, null))
    }

    override fun defaultPageErrorHandler() {
        EventBusFactory.getInstance().post(ShellEvent.GotoCrmModule(this, null))
    }

    class DashboardUrlResolver() : CrmUrlResolver() {
        override fun handlePage(vararg params: String) {
            EventBusFactory.getInstance().post(CrmEvent.GotoActivitiesView(this, null))
        }
    }

}