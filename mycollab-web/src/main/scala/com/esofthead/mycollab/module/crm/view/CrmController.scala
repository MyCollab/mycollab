/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.view

import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.core.arguments.{NumberSearchField, SearchField}
import com.esofthead.mycollab.eventmanager.ApplicationEventListener
import com.esofthead.mycollab.module.crm.data.{CustomViewScreenData, NotificationSettingScreenData}
import com.esofthead.mycollab.module.crm.domain._
import com.esofthead.mycollab.module.crm.domain.criteria._
import com.esofthead.mycollab.module.crm.events._
import com.esofthead.mycollab.module.crm.service.LeadService
import com.esofthead.mycollab.module.crm.view.account.{AccountAddPresenter, AccountListPresenter, AccountReadPresenter}
import com.esofthead.mycollab.module.crm.view.activity._
import com.esofthead.mycollab.module.crm.view.campaign.{CampaignAddPresenter, CampaignListPresenter, CampaignReadPresenter}
import com.esofthead.mycollab.module.crm.view.cases.{CaseAddPresenter, CaseListPresenter, CaseReadPresenter}
import com.esofthead.mycollab.module.crm.view.contact.{ContactAddPresenter, ContactListPresenter, ContactReadPresenter}
import com.esofthead.mycollab.module.crm.view.file.FileDashboardPresenter
import com.esofthead.mycollab.module.crm.view.lead.{LeadAddPresenter, LeadConvertReadPresenter, LeadListPresenter, LeadReadPresenter}
import com.esofthead.mycollab.module.crm.view.opportunity.{ContactRoleEditPresenter, OpportunityAddPresenter, OpportunityListPresenter, OpportunityReadPresenter}
import com.esofthead.mycollab.module.crm.view.parameters.{ActivityScreenData, AssignmentScreenData, CallScreenData, MeetingScreenData}
import com.esofthead.mycollab.module.crm.view.setting.CrmSettingPresenter
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.{AbstractController, PresenterResolver, ScreenData}
import com.google.common.eventbus.Subscribe

/**
 * @author MyCollab Ltd.
 * @since 5.0.9
 */
class CrmController(val container: CrmModule) extends AbstractController {
    bindCrmEvents
    bindAccountEvents
    bindActivityEvents
    bindCampaignEvents
    bindContactEvents
    bindLeadEvents
    bindOpportunityEvents
    bindCasesEvents
    bindDocumentEvents
    bindSettingEvents

    private def bindCrmEvents(): Unit = {
        this.register(new ApplicationEventListener[CrmEvent.GotoHome]() {
            @Subscribe def handle(event: CrmEvent.GotoHome) {
                val presenter: CrmHomePresenter = PresenterResolver.getPresenter(classOf[CrmHomePresenter])
                presenter.go(container, null)
            }
        })
    }

    private def bindAccountEvents(): Unit = {
        this.register(new ApplicationEventListener[AccountEvent.GotoList]() {
            @Subscribe def handle(event: AccountEvent.GotoList) {
                val presenter: AccountListPresenter = PresenterResolver.getPresenter(classOf[AccountListPresenter])
                val criteria: AccountSearchCriteria = new AccountSearchCriteria
                criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId))
                presenter.go(container, new ScreenData.Search[AccountSearchCriteria](criteria))
            }
        })

        this.register(new ApplicationEventListener[AccountEvent.GotoAdd]() {
            @Subscribe def handle(event: AccountEvent.GotoAdd) {
                val presenter: AccountAddPresenter = PresenterResolver.getPresenter(classOf[AccountAddPresenter])
                presenter.go(container, new ScreenData.Add[SimpleAccount](new SimpleAccount))
            }
        })

        this.register(new ApplicationEventListener[AccountEvent.GotoEdit]() {
            @Subscribe def handle(event: AccountEvent.GotoEdit) {
                val presenter: AccountAddPresenter = PresenterResolver.getPresenter(classOf[AccountAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[AccountEvent.GotoRead]() {
            @SuppressWarnings(Array("unchecked", "rawtypes"))
            @Subscribe def handle(event: AccountEvent.GotoRead) {
                val presenter: AccountReadPresenter = PresenterResolver.getPresenter(classOf[AccountReadPresenter])
                presenter.go(container, new ScreenData.Preview[AnyRef](event.getData))
            }
        })
    }

    private def bindActivityEvents(): Unit = {
        this.register(new ApplicationEventListener[ActivityEvent.GotoCalendar]() {
            @Subscribe def handle(event: ActivityEvent.GotoCalendar) {
                val presenter: ActivityCalendarPresenter = PresenterResolver.getPresenter(classOf[ActivityCalendarPresenter])
                presenter.go(container, new ActivityScreenData.GotoCalendar)
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.GotoTodoList]() {
            @Subscribe def handle(event: ActivityEvent.GotoTodoList) {
                val presenter: ActivityListPresenter = PresenterResolver.getPresenter(classOf[ActivityListPresenter])
                val searchCriteria: ActivitySearchCriteria = new ActivitySearchCriteria
                searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId))
                presenter.go(container, new ActivityScreenData.GotoActivityList(searchCriteria))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.TaskAdd]() {
            @Subscribe def handle(event: ActivityEvent.TaskAdd) {
                val presenter: AssignmentAddPresenter = PresenterResolver.getPresenter(classOf[AssignmentAddPresenter])
                presenter.go(container, new AssignmentScreenData.Add(new SimpleTask))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.TaskEdit]() {
            @Subscribe def handle(event: ActivityEvent.TaskEdit) {
                val presenter: AssignmentAddPresenter = PresenterResolver.getPresenter(classOf[AssignmentAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.TaskRead]() {
            @Subscribe def handle(event: ActivityEvent.TaskRead) {
                val presenter: AssignmentReadPresenter = PresenterResolver.getPresenter(classOf[AssignmentReadPresenter])
                presenter.go(container, new AssignmentScreenData.Read(event.getData.asInstanceOf[Integer]))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.MeetingAdd]() {
            @Subscribe def handle(event: ActivityEvent.MeetingAdd) {
                val presenter: MeetingAddPresenter = PresenterResolver.getPresenter(classOf[MeetingAddPresenter])
                presenter.go(container, new MeetingScreenData.Add(new SimpleMeeting))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.MeetingEdit]() {
            @Subscribe def handle(event: ActivityEvent.MeetingEdit) {
                val presenter: MeetingAddPresenter = PresenterResolver.getPresenter(classOf[MeetingAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.MeetingRead]() {
            @Subscribe def handle(event: ActivityEvent.MeetingRead) {
                val presenter: MeetingReadPresenter = PresenterResolver.getPresenter(classOf[MeetingReadPresenter])
                presenter.go(container, new MeetingScreenData.Read(event.getData.asInstanceOf[Integer]))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.CallAdd]() {
            @Subscribe def handle(event: ActivityEvent.CallAdd) {
                val presenter: CallAddPresenter = PresenterResolver.getPresenter(classOf[CallAddPresenter])
                presenter.go(container, new CallScreenData.Add(new SimpleCall))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.CallEdit]() {
            @Subscribe def handle(event: ActivityEvent.CallEdit) {
                val presenter: CallAddPresenter = PresenterResolver.getPresenter(classOf[CallAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[ActivityEvent.CallRead]() {
            @Subscribe def handle(event: ActivityEvent.CallRead) {
                val presenter: CallReadPresenter = PresenterResolver.getPresenter(classOf[CallReadPresenter])
                presenter.go(container, new CallScreenData.Read(event.getData.asInstanceOf[Integer]))
            }
        })
    }

    private def bindCampaignEvents(): Unit = {
        this.register(new ApplicationEventListener[CampaignEvent.GotoList]() {
            @Subscribe def handle(event: CampaignEvent.GotoList) {
                val presenter: CampaignListPresenter = PresenterResolver.getPresenter(classOf[CampaignListPresenter])
                val searchCriteria: CampaignSearchCriteria = new CampaignSearchCriteria
                searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId))
                presenter.go(container, new ScreenData.Search[CampaignSearchCriteria](searchCriteria))
            }
        })

        this.register(new ApplicationEventListener[CampaignEvent.GotoAdd]() {
            @Subscribe def handle(event: CampaignEvent.GotoAdd) {
                val presenter: CampaignAddPresenter = PresenterResolver.getPresenter(classOf[CampaignAddPresenter])
                presenter.go(container, new ScreenData.Add[SimpleCampaign](new SimpleCampaign))
            }
        })

        this.register(new ApplicationEventListener[CampaignEvent.GotoEdit]() {
            @Subscribe def handle(event: CampaignEvent.GotoEdit) {
                val presenter: CampaignAddPresenter = PresenterResolver.getPresenter(classOf[CampaignAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[CampaignEvent.GotoRead]() {
            @Subscribe
            @SuppressWarnings(Array("unchecked", "rawtypes")) def handle(event: CampaignEvent.GotoRead) {
                val presenter: CampaignReadPresenter = PresenterResolver.getPresenter(classOf[CampaignReadPresenter])
                presenter.go(container, new ScreenData.Preview[AnyRef](event.getData))
            }
        })
    }

    private def bindContactEvents(): Unit = {
        this.register(new ApplicationEventListener[ContactEvent.GotoList]() {
            @Subscribe def handle(event: ContactEvent.GotoList) {
                val presenter: ContactListPresenter = PresenterResolver.getPresenter(classOf[ContactListPresenter])
                val searchCriteria: ContactSearchCriteria = new ContactSearchCriteria
                searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId))
                presenter.go(container, new ScreenData.Search[ContactSearchCriteria](searchCriteria))
            }
        })

        this.register(new ApplicationEventListener[ContactEvent.GotoAdd]() {
            @Subscribe def handle(event: ContactEvent.GotoAdd) {
                val presenter: ContactAddPresenter = PresenterResolver.getPresenter(classOf[ContactAddPresenter])
                presenter.go(container, new ScreenData.Add[SimpleContact](new SimpleContact))
            }
        })

        this.register(new ApplicationEventListener[ContactEvent.GotoEdit]() {
            @Subscribe def handle(event: ContactEvent.GotoEdit) {
                val presenter: ContactAddPresenter = PresenterResolver.getPresenter(classOf[ContactAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[ContactEvent.GotoRead]() {
            @Subscribe
            @SuppressWarnings(Array("unchecked", "rawtypes")) def handle(event: ContactEvent.GotoRead) {
                val presenter: ContactReadPresenter = PresenterResolver.getPresenter(classOf[ContactReadPresenter])
                presenter.go(container, new ScreenData.Preview[AnyRef](event.getData))
            }
        })
    }

    private def bindDocumentEvents(): Unit = {
        this.register(new ApplicationEventListener[DocumentEvent.GotoDashboard]() {
            @Subscribe def handle(event: DocumentEvent.GotoDashboard) {
                val presenter: FileDashboardPresenter = PresenterResolver.getPresenter(classOf[FileDashboardPresenter])
                presenter.go(container, null)
            }
        })
    }

    private def bindSettingEvents(): Unit = {
        this.register(new ApplicationEventListener[CrmSettingEvent.GotoNotificationSetting]() {
            @Subscribe def handle(event: CrmSettingEvent.GotoNotificationSetting) {
                val presenter: CrmSettingPresenter = PresenterResolver.getPresenter(classOf[CrmSettingPresenter])
                presenter.go(container, new NotificationSettingScreenData.Read)
            }
        })

        this.register(new ApplicationEventListener[CrmSettingEvent.GotoCustomViewSetting]() {
            @Subscribe def handle(event: CrmSettingEvent.GotoCustomViewSetting) {
                val presenter: CrmSettingPresenter = PresenterResolver.getPresenter(classOf[CrmSettingPresenter])
                presenter.go(container, new CustomViewScreenData.Read)
            }
        })
    }

    private def bindLeadEvents(): Unit = {
        this.register(new ApplicationEventListener[LeadEvent.GotoList]() {
            @Subscribe def handle(event: LeadEvent.GotoList) {
                val presenter: LeadListPresenter = PresenterResolver.getPresenter(classOf[LeadListPresenter])
                val searchCriteria: LeadSearchCriteria = new LeadSearchCriteria
                searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId))
                presenter.go(container, new ScreenData.Search[LeadSearchCriteria](searchCriteria))
            }
        })

        this.register(new ApplicationEventListener[LeadEvent.GotoAdd]() {
            @Subscribe def handle(event: LeadEvent.GotoAdd) {
                val presenter: LeadAddPresenter = PresenterResolver.getPresenter(classOf[LeadAddPresenter])
                presenter.go(container, new ScreenData.Add[SimpleLead](new SimpleLead))
            }
        })

        this.register(new ApplicationEventListener[LeadEvent.GotoEdit]() {
            @Subscribe def handle(event: LeadEvent.GotoEdit) {
                val presenter: LeadAddPresenter = PresenterResolver.getPresenter(classOf[LeadAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[LeadEvent.GotoRead]() {
            @Subscribe
            @SuppressWarnings(Array("unchecked", "rawtypes")) def handle(event: LeadEvent.GotoRead) {
                val value: AnyRef = event.getData
                var lead: SimpleLead = null
                if (value.isInstanceOf[Integer]) {
                    val leadService: LeadService = ApplicationContextUtil.getSpringBean(classOf[LeadService])
                    lead = leadService.findById(value.asInstanceOf[Integer], AppContext.getAccountId)
                }
                else if (value.isInstanceOf[SimpleLead]) {
                    lead = value.asInstanceOf[SimpleLead]
                }
                else {
                    throw new MyCollabException("Do not support such param type")
                }
                if ("Converted" == lead.getStatus) {
                    val presenter: LeadConvertReadPresenter = PresenterResolver.getPresenter(classOf[LeadConvertReadPresenter])
                    presenter.go(container, new ScreenData.Preview[AnyRef](lead))
                }
                else {
                    val presenter: LeadReadPresenter = PresenterResolver.getPresenter(classOf[LeadReadPresenter])
                    presenter.go(container, new ScreenData.Preview[AnyRef](lead))
                }
            }
        })
    }

    private def bindOpportunityEvents(): Unit = {
        this.register(new ApplicationEventListener[OpportunityEvent.GotoList]() {
            @Subscribe def handle(event: OpportunityEvent.GotoList) {
                val presenter: OpportunityListPresenter = PresenterResolver.getPresenter(classOf[OpportunityListPresenter])
                var searchCriteria: OpportunitySearchCriteria = null
                if (event.getData != null) {
                    searchCriteria = event.getData.asInstanceOf[OpportunitySearchCriteria]
                }
                else {
                    searchCriteria = new OpportunitySearchCriteria
                    searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId))
                }
                presenter.go(container, new ScreenData.Search[OpportunitySearchCriteria](searchCriteria))
            }
        })

        this.register(new ApplicationEventListener[OpportunityEvent.GotoAdd]() {
            @Subscribe def handle(event: OpportunityEvent.GotoAdd) {
                val presenter: OpportunityAddPresenter = PresenterResolver.getPresenter(classOf[OpportunityAddPresenter])
                presenter.go(container, new ScreenData.Add[SimpleOpportunity](new SimpleOpportunity))
            }
        })

        this.register(new ApplicationEventListener[OpportunityEvent.GotoEdit]() {
            @Subscribe def handle(event: OpportunityEvent.GotoEdit) {
                val presenter: OpportunityAddPresenter = PresenterResolver.getPresenter(classOf[OpportunityAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[OpportunityEvent.GotoRead]() {
            @Subscribe
            @SuppressWarnings(Array("unchecked", "rawtypes")) def handle(event: OpportunityEvent.GotoRead) {
                val presenter: OpportunityReadPresenter = PresenterResolver.getPresenter(classOf[OpportunityReadPresenter])
                presenter.go(container, new ScreenData.Preview[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[OpportunityEvent.GotoContactRoleEdit]() {
            @Subscribe
            @SuppressWarnings(Array("unchecked", "rawtypes")) def handle(event: OpportunityEvent.GotoContactRoleEdit) {
                val presenter: ContactRoleEditPresenter = PresenterResolver.getPresenter(classOf[ContactRoleEditPresenter])
                presenter.go(container, new ScreenData[AnyRef](event.getData))
            }
        })
    }

    private def bindCasesEvents(): Unit = {
        this.register(new ApplicationEventListener[CaseEvent.GotoList]() {
            @Subscribe def handle(event: CaseEvent.GotoList) {
                val presenter: CaseListPresenter = PresenterResolver.getPresenter(classOf[CaseListPresenter])
                val searchCriteria: CaseSearchCriteria = new CaseSearchCriteria
                searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId))
                presenter.go(container, new ScreenData.Search[CaseSearchCriteria](searchCriteria))
            }
        })

        this.register(new ApplicationEventListener[CaseEvent.GotoAdd]() {
            @Subscribe def handle(event: CaseEvent.GotoAdd) {
                val presenter: CaseAddPresenter = PresenterResolver.getPresenter(classOf[CaseAddPresenter])
                presenter.go(container, new ScreenData.Add[SimpleCase](new SimpleCase))
            }
        })

        this.register(new ApplicationEventListener[CaseEvent.GotoEdit]() {
            @Subscribe def handle(event: CaseEvent.GotoEdit) {
                val presenter: CaseAddPresenter = PresenterResolver.getPresenter(classOf[CaseAddPresenter])
                presenter.go(container, new ScreenData.Edit[AnyRef](event.getData))
            }
        })

        this.register(new ApplicationEventListener[CaseEvent.GotoRead]() {
            @Subscribe
            @SuppressWarnings(Array("unchecked", "rawtypes")) def handle(event: CaseEvent.GotoRead) {
                val presenter: CaseReadPresenter = PresenterResolver.getPresenter(classOf[CaseReadPresenter])
                presenter.go(container, new ScreenData.Preview[AnyRef](event.getData))
            }
        })
    }
}
