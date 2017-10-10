/**
 * mycollab-mobile - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view

import com.google.common.eventbus.Subscribe
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.mobile.module.crm.event.*
import com.mycollab.mobile.module.crm.event.ActivityEvent.CallEdit
import com.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData
import com.mycollab.mobile.module.crm.view.account.AccountListPresenter
import com.mycollab.mobile.module.crm.view.account.AccountReadPresenter
import com.mycollab.mobile.module.crm.view.account.IAccountAddPresenter
import com.mycollab.mobile.module.crm.view.activity.*
import com.mycollab.mobile.module.crm.view.campaign.CampaignListPresenter
import com.mycollab.mobile.module.crm.view.campaign.CampaignReadPresenter
import com.mycollab.mobile.module.crm.view.campaign.ICampaignAddPresenter
import com.mycollab.mobile.module.crm.view.cases.CaseListPresenter
import com.mycollab.mobile.module.crm.view.cases.CaseReadPresenter
import com.mycollab.mobile.module.crm.view.cases.ICaseAddPresenter
import com.mycollab.mobile.module.crm.view.contact.ContactListPresenter
import com.mycollab.mobile.module.crm.view.contact.ContactReadPresenter
import com.mycollab.mobile.module.crm.view.contact.IContactAddPresenter
import com.mycollab.mobile.module.crm.view.lead.ILeadAddPresenter
import com.mycollab.mobile.module.crm.view.lead.LeadListPresenter
import com.mycollab.mobile.module.crm.view.lead.LeadReadPresenter
import com.mycollab.mobile.module.crm.view.opportunity.IOpportunityAddPresenter
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityListPresenter
import com.mycollab.mobile.module.crm.view.opportunity.OpportunityReadPresenter
import com.mycollab.mobile.mvp.view.PresenterOptionUtil
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.domain.criteria.*
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PresenterResolver
import com.mycollab.vaadin.mvp.ScreenData
import com.vaadin.addon.touchkit.ui.NavigationManager

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class CrmModuleController(private val crmViewNavigation: NavigationManager) : AbstractController() {

    init {
        bindCrmEvents()
        bindAccountEvents()
        bindActivityEvents()
        bindContactEvents()
        bindCampaignEvents()
        bindCaseEvents()
        bindLeadEvents()
        bindOpportunityEvents()
    }

    private fun bindCrmEvents() {
        this.register(object : ApplicationEventListener<CrmEvent.GotoActivitiesView> {

            @Subscribe
            override fun handle(event: CrmEvent.GotoActivitiesView) {
                val presenter = PresenterResolver.getPresenter(AllActivitiesPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData(null))
            }
        })
    }

    private fun bindAccountEvents() {
        this.register(object : ApplicationEventListener<AccountEvent.GotoList> {

            @Subscribe
            override fun handle(event: AccountEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(AccountListPresenter::class.java)
                val criteria = AccountSearchCriteria()
                criteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(crmViewNavigation, ScreenData.Search(criteria))
            }

        })

        this.register(object : ApplicationEventListener<AccountEvent.GotoAdd> {

            @Subscribe
            override fun handle(event: AccountEvent.GotoAdd) {
                val presenter = PresenterOptionUtil.getPresenter(IAccountAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleAccount()))
            }
        })

        this.register(object : ApplicationEventListener<AccountEvent.GotoEdit> {

            @Subscribe
            override fun handle(event: AccountEvent.GotoEdit) {
                val presenter = PresenterOptionUtil.getPresenter(IAccountAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<AccountEvent.GotoRead> {

            @Subscribe
            override fun handle(event: AccountEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(AccountReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<AccountEvent.GotoRelatedItems> {

            @Subscribe
            override fun handle(event: AccountEvent.GotoRelatedItems) {
                if (event.data is CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(event.data.params!!)
            }
        })
    }

    private fun bindActivityEvents() {
        this.register(object : ApplicationEventListener<ActivityEvent.GotoList> {

            @Subscribe
            override fun handle(event: ActivityEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(ActivityListPresenter::class.java)
                val criteria = ActivitySearchCriteria()
                criteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(crmViewNavigation, ScreenData.Search(criteria))
            }
        })
        this.register(object : ApplicationEventListener<ActivityEvent.TaskRead> {

            @Subscribe
            override fun handle(event: ActivityEvent.TaskRead) {
                val presenter = PresenterResolver.getPresenter(AssignmentReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.TaskAdd> {

            @Subscribe
            override fun handle(event: ActivityEvent.TaskAdd) {
                val presenter = PresenterResolver.getPresenter(AssignmentAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleCrmTask()))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.TaskEdit> {

            @Subscribe
            override fun handle(event: ActivityEvent.TaskEdit) {
                val presenter = PresenterResolver.getPresenter(AssignmentAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.CallRead> {

            @Subscribe
            override fun handle(event: ActivityEvent.CallRead) {
                val presenter = PresenterResolver.getPresenter(CallReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.CallAdd> {

            @Subscribe
            override fun handle(event: ActivityEvent.CallAdd) {
                val presenter = PresenterResolver.getPresenter(CallAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleCall()))
            }
        })

        this.register(object : ApplicationEventListener<CallEdit> {

            @Subscribe
            override fun handle(event: CallEdit) {
                val presenter = PresenterResolver.getPresenter(CallAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.MeetingRead> {

            @Subscribe
            override fun handle(event: ActivityEvent.MeetingRead) {
                val presenter = PresenterResolver.getPresenter(MeetingReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.MeetingAdd> {

            @Subscribe
            override fun handle(event: ActivityEvent.MeetingAdd) {
                val presenter = PresenterResolver.getPresenter(MeetingAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleMeeting()))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.MeetingEdit> {

            @Subscribe
            override fun handle(event: ActivityEvent.MeetingEdit) {
                val presenter = PresenterResolver.getPresenter(MeetingAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.GotoRelatedItems> {

            @Subscribe
            override fun handle(event: ActivityEvent.GotoRelatedItems) {
                if (event.data is CrmRelatedItemsScreenData) {
                    crmViewNavigation.navigateTo(event.data.params!!)
                }
            }
        })
    }

    private fun bindContactEvents() {
        this.register(object : ApplicationEventListener<ContactEvent.GotoList> {

            @Subscribe
            override fun handle(event: ContactEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(ContactListPresenter::class.java)

                val searchCriteria = ContactSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(crmViewNavigation, ScreenData.Search(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<ContactEvent.GotoAdd> {

            @Subscribe
            override fun handle(event: ContactEvent.GotoAdd) {
                val presenter = PresenterOptionUtil.getPresenter(IContactAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleContact()))
            }
        })

        this.register(object : ApplicationEventListener<ContactEvent.GotoEdit> {

            @Subscribe
            override fun handle(event: ContactEvent.GotoEdit) {
                val presenter = PresenterOptionUtil.getPresenter(IContactAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ContactEvent.GotoRead> {

            @Subscribe
            override fun handle(event: ContactEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(ContactReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ContactEvent.GotoRelatedItems> {

            @Subscribe
            override fun handle(event: ContactEvent.GotoRelatedItems) {
                if (event.data is CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(event.data.params!!)
            }
        })
    }

    private fun bindCampaignEvents() {
        this.register(object : ApplicationEventListener<CampaignEvent.GotoList> {

            @Subscribe
            override fun handle(event: CampaignEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(CampaignListPresenter::class.java)
                val searchCriteria = CampaignSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)

                presenter.go(crmViewNavigation, ScreenData.Search(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<CampaignEvent.GotoAdd> {

            @Subscribe
            override fun handle(event: CampaignEvent.GotoAdd) {
                val presenter = PresenterOptionUtil.getPresenter(ICampaignAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleCampaign()))
            }
        })

        this.register(object : ApplicationEventListener<CampaignEvent.GotoEdit> {

            @Subscribe
            override fun handle(event: CampaignEvent.GotoEdit) {
                val presenter = PresenterOptionUtil.getPresenter(ICampaignAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<CampaignEvent.GotoRead> {

            @Subscribe
            override fun handle(event: CampaignEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(CampaignReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<CampaignEvent.GotoRelatedItems> {

            @Subscribe
            override fun handle(event: CampaignEvent.GotoRelatedItems) {
                if (event.data is CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(event.data.params!!)
            }
        })
    }

    private fun bindCaseEvents() {
        this.register(object : ApplicationEventListener<CaseEvent.GotoList> {

            @Subscribe
            override fun handle(event: CaseEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(CaseListPresenter::class.java)

                val searchCriteria = CaseSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(crmViewNavigation, ScreenData.Search(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<CaseEvent.GotoAdd> {

            @Subscribe
            override fun handle(event: CaseEvent.GotoAdd) {
                val presenter = PresenterOptionUtil.getPresenter(ICaseAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleCase()))
            }
        })

        this.register(object : ApplicationEventListener<CaseEvent.GotoEdit> {

            @Subscribe
            override fun handle(event: CaseEvent.GotoEdit) {
                val presenter = PresenterOptionUtil.getPresenter(ICaseAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<CaseEvent.GotoRead> {

            @Subscribe
            override fun handle(event: CaseEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(CaseReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<CaseEvent.GotoRelatedItems> {

            @Subscribe
            override fun handle(event: CaseEvent.GotoRelatedItems) {
                if (event.data is CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(event.data.params!!)
            }
        })
    }

    private fun bindLeadEvents() {
        this.register(object : ApplicationEventListener<LeadEvent.GotoList> {

            @Subscribe
            override fun handle(event: LeadEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(LeadListPresenter::class.java)
                val searchCriteria = LeadSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(crmViewNavigation, ScreenData.Search(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<LeadEvent.GotoRead> {

            @Subscribe
            override fun handle(event: LeadEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(LeadReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<LeadEvent.GotoAdd> {

            @Subscribe
            override fun handle(event: LeadEvent.GotoAdd) {
                val presenter = PresenterOptionUtil.getPresenter(ILeadAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleLead()))
            }
        })

        this.register(object : ApplicationEventListener<LeadEvent.GotoEdit> {

            @Subscribe
            override fun handle(event: LeadEvent.GotoEdit) {
                val presenter = PresenterOptionUtil.getPresenter(ILeadAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<LeadEvent.GotoRelatedItems> {

            @Subscribe
            override fun handle(event: LeadEvent.GotoRelatedItems) {
                if (event.data is CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(event.data.params!!)
            }
        })
    }

    private fun bindOpportunityEvents() {
        this.register(object : ApplicationEventListener<OpportunityEvent.GotoList> {

            @Subscribe
            override fun handle(event: OpportunityEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(OpportunityListPresenter::class.java)
                val searchCriteria = OpportunitySearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(crmViewNavigation, ScreenData.Search(searchCriteria))

            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoRead> {

            @Subscribe
            override fun handle(event: OpportunityEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(OpportunityReadPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Preview(event.data))
            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoAdd> {

            @Subscribe
            override fun handle(event: OpportunityEvent.GotoAdd) {
                val presenter = PresenterOptionUtil.getPresenter(IOpportunityAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Add(SimpleOpportunity()))
            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoEdit> {

            @Subscribe
            override fun handle(event: OpportunityEvent.GotoEdit) {
                val presenter = PresenterOptionUtil.getPresenter(IOpportunityAddPresenter::class.java)
                presenter.go(crmViewNavigation, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoRelatedItems> {

            @Subscribe
            override fun handle(event: OpportunityEvent.GotoRelatedItems) {
                if (event.data is CrmRelatedItemsScreenData)
                    crmViewNavigation.navigateTo(event.data.params!!)
            }
        })
    }
}
