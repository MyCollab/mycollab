/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view

import com.google.common.eventbus.Subscribe
import com.mycollab.core.MyCollabException
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.module.crm.data.CustomViewScreenData
import com.mycollab.module.crm.data.NotificationSettingScreenData
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.domain.criteria.*
import com.mycollab.module.crm.event.*
import com.mycollab.module.crm.service.LeadService
import com.mycollab.module.crm.view.account.AccountAddPresenter
import com.mycollab.module.crm.view.account.AccountListPresenter
import com.mycollab.module.crm.view.account.AccountReadPresenter
import com.mycollab.module.crm.view.activity.*
import com.mycollab.module.crm.view.campaign.CampaignAddPresenter
import com.mycollab.module.crm.view.campaign.CampaignListPresenter
import com.mycollab.module.crm.view.campaign.CampaignReadPresenter
import com.mycollab.module.crm.view.cases.CaseAddPresenter
import com.mycollab.module.crm.view.cases.CaseListPresenter
import com.mycollab.module.crm.view.cases.CaseReadPresenter
import com.mycollab.module.crm.view.contact.ContactAddPresenter
import com.mycollab.module.crm.view.contact.ContactListPresenter
import com.mycollab.module.crm.view.contact.ContactReadPresenter
import com.mycollab.module.crm.view.file.FileDashboardPresenter
import com.mycollab.module.crm.view.lead.LeadAddPresenter
import com.mycollab.module.crm.view.lead.LeadConvertReadPresenter
import com.mycollab.module.crm.view.lead.LeadListPresenter
import com.mycollab.module.crm.view.lead.LeadReadPresenter
import com.mycollab.module.crm.view.opportunity.ContactRoleEditPresenter
import com.mycollab.module.crm.view.opportunity.OpportunityAddPresenter
import com.mycollab.module.crm.view.opportunity.OpportunityListPresenter
import com.mycollab.module.crm.view.opportunity.OpportunityReadPresenter
import com.mycollab.module.crm.view.parameters.ActivityScreenData
import com.mycollab.module.crm.view.parameters.AssignmentScreenData
import com.mycollab.module.crm.view.parameters.CallScreenData
import com.mycollab.module.crm.view.parameters.MeetingScreenData
import com.mycollab.module.crm.view.setting.CrmSettingPresenter
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PresenterResolver
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class CrmController(val container: CrmModule) : AbstractController() {
    init {
        bindCrmEvents()
        bindAccountEvents()
        bindActivityEvents()
        bindCampaignEvents()
        bindContactEvents()
        bindLeadEvents()
        bindOpportunityEvents()
        bindCasesEvents()
        bindDocumentEvents()
        bindSettingEvents()
    }

    private fun bindCrmEvents() {
        this.register(object : ApplicationEventListener<CrmEvent.GotoHome> {
            @Subscribe override fun handle(event: CrmEvent.GotoHome) {
                val presenter = PresenterResolver.getPresenter(CrmHomePresenter::class.java)
                presenter.go(container, null)
            }
        })
    }

    private fun bindAccountEvents() {
        this.register(object : ApplicationEventListener<AccountEvent.GotoList> {
            @Subscribe override fun handle(event: AccountEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(AccountListPresenter::class.java)
                val criteria = AccountSearchCriteria()
                criteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(container, ScreenData.Search<AccountSearchCriteria>(criteria))
            }
        })

        this.register(object : ApplicationEventListener<AccountEvent.GotoAdd> {
            @Subscribe override fun handle(event: AccountEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(AccountAddPresenter::class.java)
                presenter.go(container, ScreenData.Add<SimpleAccount>(SimpleAccount()))
            }
        })

        this.register(object : ApplicationEventListener<AccountEvent.GotoEdit> {
            @Subscribe override fun handle(event: AccountEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(AccountAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<AccountEvent.GotoRead> {
            @Subscribe override fun handle(event: AccountEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(AccountReadPresenter::class.java)
                presenter.go(container, ScreenData.Preview<Any>(event.data))
            }
        })
    }

    private fun bindActivityEvents() {
        this.register(object : ApplicationEventListener<ActivityEvent.GotoCalendar> {
            @Subscribe override fun handle(event: ActivityEvent.GotoCalendar) {
                val presenter = PresenterResolver.getPresenter(ActivityCalendarPresenter::class.java)
                presenter.go(container, ActivityScreenData.GotoCalendar())
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.GotoTodoList> {
            @Subscribe override fun handle(event: ActivityEvent.GotoTodoList) {
                val presenter = PresenterResolver.getPresenter(ActivityListPresenter::class.java)
                val searchCriteria = ActivitySearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(container, ActivityScreenData.GotoActivityList(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.TaskAdd> {
            @Subscribe override fun handle(event: ActivityEvent.TaskAdd) {
                val presenter = PresenterResolver.getPresenter(AssignmentAddPresenter::class.java)
                presenter.go(container, AssignmentScreenData.Add(SimpleCrmTask()))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.TaskEdit> {
            @Subscribe override fun handle(event: ActivityEvent.TaskEdit) {
                val presenter = PresenterResolver.getPresenter(AssignmentAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.TaskRead> {
            @Subscribe override fun handle(event: ActivityEvent.TaskRead) {
                val presenter = PresenterResolver.getPresenter(AssignmentReadPresenter::class.java)
                presenter.go(container, AssignmentScreenData.Read(event.data as Int))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.MeetingAdd> {
            @Subscribe override fun handle(event: ActivityEvent.MeetingAdd) {
                val presenter = PresenterResolver.getPresenter(MeetingAddPresenter::class.java)
                presenter.go(container, MeetingScreenData.Add(SimpleMeeting()))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.MeetingEdit> {
            @Subscribe override fun handle(event: ActivityEvent.MeetingEdit) {
                val presenter = PresenterResolver.getPresenter(MeetingAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.MeetingRead> {
            @Subscribe override fun handle(event: ActivityEvent.MeetingRead) {
                val presenter = PresenterResolver.getPresenter(MeetingReadPresenter::class.java)
                presenter.go(container, MeetingScreenData.Read(event.data as Int))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.CallAdd> {
            @Subscribe override fun handle(event: ActivityEvent.CallAdd) {
                val presenter = PresenterResolver.getPresenter(CallAddPresenter::class.java)
                presenter.go(container, CallScreenData.Add(SimpleCall()))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.CallEdit> {
            @Subscribe override fun handle(event: ActivityEvent.CallEdit) {
                val presenter = PresenterResolver.getPresenter(CallAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ActivityEvent.CallRead> {
            @Subscribe override fun handle(event: ActivityEvent.CallRead) {
                val presenter = PresenterResolver.getPresenter(CallReadPresenter::class.java)
                presenter.go(container, CallScreenData.Read(event.data as Int))
            }
        })
    }

    private fun bindCampaignEvents() {
        this.register(object : ApplicationEventListener<CampaignEvent.GotoList> {
            @Subscribe override fun handle(event: CampaignEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(CampaignListPresenter::class.java)
                val searchCriteria = CampaignSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(container, ScreenData.Search<CampaignSearchCriteria>(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<CampaignEvent.GotoAdd> {
            @Subscribe override fun handle(event: CampaignEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(CampaignAddPresenter::class.java)
                presenter.go(container, ScreenData.Add<SimpleCampaign>(SimpleCampaign()))
            }
        })

        this.register(object : ApplicationEventListener<CampaignEvent.GotoEdit> {
            @Subscribe override fun handle(event: CampaignEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(CampaignAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<CampaignEvent.GotoRead> {
            @Subscribe override fun handle(event: CampaignEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(CampaignReadPresenter::class.java)
                presenter.go(container, ScreenData.Preview<Any>(event.data))
            }
        })
    }

    private fun bindContactEvents() {
        this.register(object : ApplicationEventListener<ContactEvent.GotoList> {
            @Subscribe override fun handle(event: ContactEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(ContactListPresenter::class.java)
                val searchCriteria = ContactSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(container, ScreenData.Search<ContactSearchCriteria>(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<ContactEvent.GotoAdd> {
            @Subscribe override fun handle(event: ContactEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(ContactAddPresenter::class.java)
                presenter.go(container, ScreenData.Add<SimpleContact>(SimpleContact()))
            }
        })

        this.register(object : ApplicationEventListener<ContactEvent.GotoEdit> {
            @Subscribe override fun handle(event: ContactEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(ContactAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ContactEvent.GotoRead> {
            @Subscribe override fun handle(event: ContactEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(ContactReadPresenter::class.java)
                presenter.go(container, ScreenData.Preview<Any>(event.data))
            }
        })
    }

    private fun bindDocumentEvents() {
        this.register(object : ApplicationEventListener<DocumentEvent.GotoDashboard> {
            @Subscribe override fun handle(event: DocumentEvent.GotoDashboard) {
                val presenter = PresenterResolver.getPresenter(FileDashboardPresenter::class.java)
                presenter.go(container, null)
            }
        })
    }

    private fun bindSettingEvents() {
        this.register(object : ApplicationEventListener<CrmSettingEvent.GotoNotificationSetting> {
            @Subscribe override fun handle(event: CrmSettingEvent.GotoNotificationSetting) {
                val presenter = PresenterResolver.getPresenter(CrmSettingPresenter::class.java)
                presenter.go(container, NotificationSettingScreenData.Read())
            }
        })

        this.register(object : ApplicationEventListener<CrmSettingEvent.GotoCustomViewSetting> {
            @Subscribe override fun handle(event: CrmSettingEvent.GotoCustomViewSetting) {
                val presenter = PresenterResolver.getPresenter(CrmSettingPresenter::class.java)
                presenter.go(container, CustomViewScreenData.Read())
            }
        })
    }

    private fun bindLeadEvents() {
        this.register(object : ApplicationEventListener<LeadEvent.GotoList> {
            @Subscribe override fun handle(event: LeadEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(LeadListPresenter::class.java)
                val searchCriteria = LeadSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(container, ScreenData.Search<LeadSearchCriteria>(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<LeadEvent.GotoAdd> {
            @Subscribe override fun handle(event: LeadEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(LeadAddPresenter::class.java)
                presenter.go(container, ScreenData.Add<SimpleLead>(SimpleLead()))
            }
        })

        this.register(object : ApplicationEventListener<LeadEvent.GotoEdit> {
            @Subscribe override fun handle(event: LeadEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(LeadAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<LeadEvent.GotoRead> {
            @Subscribe
            override fun handle(event: LeadEvent.GotoRead) {
                val value = event.data
                val lead = when (value) {
                    is Int -> {
                        val leadService = AppContextUtil.getSpringBean(LeadService::class.java)
                        leadService.findById(value, AppUI.accountId)
                    }
                    is SimpleLead -> value
                    else -> throw MyCollabException("Do not support such param type")
                }
                if ("Converted" == lead!!.status) {
                    val presenter = PresenterResolver.getPresenter(LeadConvertReadPresenter::class.java)
                    presenter.go(container, ScreenData.Preview(lead))
                } else {
                    val presenter = PresenterResolver.getPresenter(LeadReadPresenter::class.java)
                    presenter.go(container, ScreenData.Preview(lead))
                }
            }
        })
    }

    private fun bindOpportunityEvents() {
        this.register(object : ApplicationEventListener<OpportunityEvent.GotoList> {
            @Subscribe override fun handle(event: OpportunityEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(OpportunityListPresenter::class.java)
                val searchCriteria: OpportunitySearchCriteria
                when {
                    event.data != null -> searchCriteria = event.data as OpportunitySearchCriteria
                    else -> {
                        searchCriteria = OpportunitySearchCriteria()
                        searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                    }
                }
                presenter.go(container, ScreenData.Search<OpportunitySearchCriteria>(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoAdd> {
            @Subscribe override fun handle(event: OpportunityEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(OpportunityAddPresenter::class.java)
                presenter.go(container, ScreenData.Add<SimpleOpportunity>(SimpleOpportunity()))
            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoEdit> {
            @Subscribe override fun handle(event: OpportunityEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(OpportunityAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoRead> {
            @Subscribe override fun handle(event: OpportunityEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(OpportunityReadPresenter::class.java)
                presenter.go(container, ScreenData.Preview<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<OpportunityEvent.GotoContactRoleEdit> {
            @Subscribe override fun handle(event: OpportunityEvent.GotoContactRoleEdit) {
                val presenter = PresenterResolver.getPresenter(ContactRoleEditPresenter::class.java)
                presenter.go(container, ScreenData(event.data))
            }
        })
    }

    private fun bindCasesEvents() {
        this.register(object : ApplicationEventListener<CaseEvent.GotoList> {
            @Subscribe override fun handle(event: CaseEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(CaseListPresenter::class.java)
                val searchCriteria = CaseSearchCriteria()
                searchCriteria.saccountid = NumberSearchField(AppUI.accountId)
                presenter.go(container, ScreenData.Search<CaseSearchCriteria>(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<CaseEvent.GotoAdd> {
            @Subscribe override fun handle(event: CaseEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(CaseAddPresenter::class.java)
                presenter.go(container, ScreenData.Add<SimpleCase>(SimpleCase()))
            }
        })

        this.register(object : ApplicationEventListener<CaseEvent.GotoEdit> {
            @Subscribe override fun handle(event: CaseEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(CaseAddPresenter::class.java)
                presenter.go(container, ScreenData.Edit<Any>(event.data))
            }
        })

        this.register(object : ApplicationEventListener<CaseEvent.GotoRead> {
            @Subscribe override fun handle(event: CaseEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(CaseReadPresenter::class.java)
                presenter.go(container, ScreenData.Preview<Any>(event.data))
            }
        })
    }
}