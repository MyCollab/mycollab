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
package com.esofthead.mycollab.module.crm.view;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.module.crm.data.CustomViewScreenData;
import com.esofthead.mycollab.module.crm.data.NotificationSettingScreenData;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.events.AccountEvent.GotoRead;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.module.crm.events.ActivityEvent.GotoCalendar;
import com.esofthead.mycollab.module.crm.events.ActivityEvent.GotoTodoList;
import com.esofthead.mycollab.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.events.CrmEvent.GotoHome;
import com.esofthead.mycollab.module.crm.events.CrmSettingEvent;
import com.esofthead.mycollab.module.crm.events.CrmSettingEvent.GotoNotificationSetting;
import com.esofthead.mycollab.module.crm.events.DocumentEvent;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.view.account.AccountAddPresenter;
import com.esofthead.mycollab.module.crm.view.account.AccountListPresenter;
import com.esofthead.mycollab.module.crm.view.account.AccountReadPresenter;
import com.esofthead.mycollab.module.crm.view.activity.ActivityCalendarPresenter;
import com.esofthead.mycollab.module.crm.view.activity.ActivityListPresenter;
import com.esofthead.mycollab.module.crm.view.activity.AssignmentAddPresenter;
import com.esofthead.mycollab.module.crm.view.activity.AssignmentReadPresenter;
import com.esofthead.mycollab.module.crm.view.activity.CallAddPresenter;
import com.esofthead.mycollab.module.crm.view.activity.CallReadPresenter;
import com.esofthead.mycollab.module.crm.view.activity.MeetingAddPresenter;
import com.esofthead.mycollab.module.crm.view.activity.MeetingReadPresenter;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignAddPresenter;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignListPresenter;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignReadPresenter;
import com.esofthead.mycollab.module.crm.view.cases.CaseAddPresenter;
import com.esofthead.mycollab.module.crm.view.cases.CaseListPresenter;
import com.esofthead.mycollab.module.crm.view.cases.CaseReadPresenter;
import com.esofthead.mycollab.module.crm.view.contact.ContactAddPresenter;
import com.esofthead.mycollab.module.crm.view.contact.ContactListPresenter;
import com.esofthead.mycollab.module.crm.view.contact.ContactReadPresenter;
import com.esofthead.mycollab.module.crm.view.file.FileDashboardPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadAddPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadConvertReadPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadListPresenter;
import com.esofthead.mycollab.module.crm.view.lead.LeadReadPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.ContactRoleEditPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityAddPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityListPresenter;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityReadPresenter;
import com.esofthead.mycollab.module.crm.view.parameters.ActivityScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.AssignmentScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.CallScreenData;
import com.esofthead.mycollab.module.crm.view.parameters.MeetingScreenData;
import com.esofthead.mycollab.module.crm.view.setting.CrmSettingPresenter;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private CrmModule container;

	public CrmController(CrmModule container) {
		this.container = container;

		bindCrmEvents();
		bindAccountEvents();
		bindActivityEvents();
		bindCampaignEvents();
		bindContactEvents();
		bindLeadEvents();
		bindOpportunityEvents();
		bindCasesEvents();
		bindDocumentEvents();
		bindSettingEvents();
	}

	@SuppressWarnings("serial")
	private void bindCrmEvents() {
		this.register(new ApplicationEventListener<GotoHome>() {

			@Subscribe
			@Override
			public void handle(GotoHome event) {
				CrmHomePresenter presenter = PresenterResolver
						.getPresenter(CrmHomePresenter.class);
				presenter.go(container, null);
			}
		});
	}

	@SuppressWarnings("serial")
	private void bindAccountEvents() {
		this.register(new ApplicationEventListener<AccountEvent.GotoList>() {

			@Subscribe
			@Override
			public void handle(AccountEvent.GotoList event) {
				AccountListPresenter presenter = PresenterResolver
						.getPresenter(AccountListPresenter.class);

				AccountSearchCriteria criteria = new AccountSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(SearchField.AND,
						AppContext.getAccountId()));
				presenter.go(container,
						new ScreenData.Search<AccountSearchCriteria>(criteria));
			}
		});

		this.register(new ApplicationEventListener<AccountEvent.GotoAdd>() {

			@Subscribe
			@Override
			public void handle(AccountEvent.GotoAdd event) {
				AccountAddPresenter presenter = PresenterResolver
						.getPresenter(AccountAddPresenter.class);
				presenter.go(container, new ScreenData.Add<SimpleAccount>(
						new SimpleAccount()));
			}
		});

		this.register(new ApplicationEventListener<AccountEvent.GotoEdit>() {

			@Subscribe
			@Override
			public void handle(AccountEvent.GotoEdit event) {
				AccountAddPresenter presenter = PresenterResolver
						.getPresenter(AccountAddPresenter.class);
				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<AccountEvent.GotoRead>() {

			@Subscribe
			@Override
			public void handle(GotoRead event) {
				AccountReadPresenter presenter = PresenterResolver
						.getPresenter(AccountReadPresenter.class);
				presenter.go(container, new ScreenData.Preview(event.getData()));
			}
		});
	}

	private void bindActivityEvents() {
		this.register(new ApplicationEventListener<ActivityEvent.GotoCalendar>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(GotoCalendar event) {
				ActivityCalendarPresenter presenter = PresenterResolver
						.getPresenter(ActivityCalendarPresenter.class);
				presenter.go(container, new ActivityScreenData.GotoCalendar());
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.GotoTodoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(GotoTodoList event) {
				ActivityListPresenter presenter = PresenterResolver
						.getPresenter(ActivityListPresenter.class);
				ActivitySearchCriteria searchCriteria = new ActivitySearchCriteria();
				searchCriteria.setSaccountid(new NumberSearchField(
						SearchField.AND, AppContext.getAccountId()));
				presenter
						.go(container, new ActivityScreenData.GotoActivityList(
								searchCriteria));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.TaskAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.TaskAdd event) {
				AssignmentAddPresenter presenter = PresenterResolver
						.getPresenter(AssignmentAddPresenter.class);
				presenter.go(container, new AssignmentScreenData.Add(
						new SimpleTask()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.TaskEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.TaskEdit event) {
				AssignmentAddPresenter presenter = PresenterResolver
						.getPresenter(AssignmentAddPresenter.class);
				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.TaskRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.TaskRead event) {
				AssignmentReadPresenter presenter = PresenterResolver
						.getPresenter(AssignmentReadPresenter.class);
				presenter.go(container, new AssignmentScreenData.Read(
						(Integer) event.getData()));

			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.MeetingAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.MeetingAdd event) {
				MeetingAddPresenter presenter = PresenterResolver
						.getPresenter(MeetingAddPresenter.class);
				presenter.go(container, new MeetingScreenData.Add(
						new SimpleMeeting()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.MeetingEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.MeetingEdit event) {
				MeetingAddPresenter presenter = PresenterResolver
						.getPresenter(MeetingAddPresenter.class);

				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.MeetingRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.MeetingRead event) {
				MeetingReadPresenter presenter = PresenterResolver
						.getPresenter(MeetingReadPresenter.class);
				presenter.go(container, new MeetingScreenData.Read(
						(Integer) event.getData()));

			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.CallAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.CallAdd event) {
				CallAddPresenter presenter = PresenterResolver
						.getPresenter(CallAddPresenter.class);
				presenter.go(container,
						new CallScreenData.Add(new SimpleCall()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.CallEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.CallEdit event) {
				CallAddPresenter presenter = PresenterResolver
						.getPresenter(CallAddPresenter.class);

				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.CallRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.CallRead event) {
				CallReadPresenter presenter = PresenterResolver
						.getPresenter(CallReadPresenter.class);
				presenter.go(container,
						new CallScreenData.Read((Integer) event.getData()));

			}
		});

	}

	@SuppressWarnings("serial")
	private void bindCampaignEvents() {
		this.register(new ApplicationEventListener<CampaignEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(CampaignEvent.GotoList event) {
				CampaignListPresenter presenter = PresenterResolver
						.getPresenter(CampaignListPresenter.class);
				CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
				searchCriteria.setSaccountid(new NumberSearchField(
						SearchField.AND, AppContext.getAccountId()));

				presenter.go(container,
						new ScreenData.Search<CampaignSearchCriteria>(
								searchCriteria));
			}
		});

		this.register(new ApplicationEventListener<CampaignEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(CampaignEvent.GotoAdd event) {
				CampaignAddPresenter presenter = PresenterResolver
						.getPresenter(CampaignAddPresenter.class);
				presenter.go(container, new ScreenData.Add<SimpleCampaign>(
						new SimpleCampaign()));
			}
		});

		this.register(new ApplicationEventListener<CampaignEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(CampaignEvent.GotoEdit event) {
				CampaignAddPresenter presenter = PresenterResolver
						.getPresenter(CampaignAddPresenter.class);
				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<CampaignEvent.GotoRead>() {
			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(CampaignEvent.GotoRead event) {
				CampaignReadPresenter presenter = PresenterResolver
						.getPresenter(CampaignReadPresenter.class);
				presenter.go(container, new ScreenData.Preview(event.getData()));
			}
		});
	}

	@SuppressWarnings("serial")
	private void bindContactEvents() {
		this.register(new ApplicationEventListener<ContactEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(ContactEvent.GotoList event) {
				ContactListPresenter presenter = PresenterResolver
						.getPresenter(ContactListPresenter.class);

				ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
				searchCriteria.setSaccountid(new NumberSearchField(
						SearchField.AND, AppContext.getAccountId()));
				presenter.go(container,
						new ScreenData.Search<ContactSearchCriteria>(
								searchCriteria));
			}
		});

		this.register(new ApplicationEventListener<ContactEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(ContactEvent.GotoAdd event) {
				ContactAddPresenter presenter = PresenterResolver
						.getPresenter(ContactAddPresenter.class);
				presenter.go(container, new ScreenData.Add<SimpleContact>(
						new SimpleContact()));
			}
		});

		this.register(new ApplicationEventListener<ContactEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(ContactEvent.GotoEdit event) {
				ContactAddPresenter presenter = PresenterResolver
						.getPresenter(ContactAddPresenter.class);
				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ContactEvent.GotoRead>() {
			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(ContactEvent.GotoRead event) {
				ContactReadPresenter presenter = PresenterResolver
						.getPresenter(ContactReadPresenter.class);
				presenter.go(container, new ScreenData.Preview(event.getData()));
			}
		});
	}

	private void bindDocumentEvents() {
		this.register(new ApplicationEventListener<DocumentEvent.GotoDashboard>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(DocumentEvent.GotoDashboard event) {
				FileDashboardPresenter presenter = PresenterResolver
						.getPresenter(FileDashboardPresenter.class);
				presenter.go(container, null);
			}
		});
	}

	private void bindSettingEvents() {
		this.register(new ApplicationEventListener<CrmSettingEvent.GotoNotificationSetting>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(GotoNotificationSetting event) {
				CrmSettingPresenter presenter = PresenterResolver
						.getPresenter(CrmSettingPresenter.class);
				presenter.go(container,
						new NotificationSettingScreenData.Read());
			}
		});

		this.register(new ApplicationEventListener<CrmSettingEvent.GotoCustomViewSetting>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(CrmSettingEvent.GotoCustomViewSetting event) {
				CrmSettingPresenter presenter = PresenterResolver
						.getPresenter(CrmSettingPresenter.class);
				presenter.go(container, new CustomViewScreenData.Read());
			}
		});
	}

	@SuppressWarnings("serial")
	private void bindLeadEvents() {
		this.register(new ApplicationEventListener<LeadEvent.GotoList>() {

			@Subscribe
			@Override
			public void handle(LeadEvent.GotoList event) {
				LeadListPresenter presenter = PresenterResolver
						.getPresenter(LeadListPresenter.class);
				LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
				searchCriteria.setSaccountid(new NumberSearchField(
						SearchField.AND, AppContext.getAccountId()));
				presenter.go(container,
						new ScreenData.Search<LeadSearchCriteria>(
								searchCriteria));
			}
		});

		this.register(new ApplicationEventListener<LeadEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(LeadEvent.GotoAdd event) {
				LeadAddPresenter presenter = PresenterResolver
						.getPresenter(LeadAddPresenter.class);
				presenter.go(container, new ScreenData.Add<SimpleLead>(
						new SimpleLead()));
			}
		});

		this.register(new ApplicationEventListener<LeadEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(LeadEvent.GotoEdit event) {
				LeadAddPresenter presenter = PresenterResolver
						.getPresenter(LeadAddPresenter.class);
				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<LeadEvent.GotoRead>() {
			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(LeadEvent.GotoRead event) {
				Object value = event.getData();
				SimpleLead lead;
				if (value instanceof Integer) {
					LeadService leadService = ApplicationContextUtil
							.getSpringBean(LeadService.class);
					lead = leadService.findById((Integer) value,
							AppContext.getAccountId());
				} else if (value instanceof SimpleLead) {
					lead = (SimpleLead) value;
				} else {
					throw new MyCollabException(
							"Do not support such param type");
				}

				if ("Converted".equals(lead.getStatus())) {
					LeadConvertReadPresenter presenter = PresenterResolver
							.getPresenter(LeadConvertReadPresenter.class);
					presenter.go(container, new ScreenData.Preview(lead));
				} else {
					LeadReadPresenter presenter = PresenterResolver
							.getPresenter(LeadReadPresenter.class);
					presenter.go(container, new ScreenData.Preview(lead));
				}

			}
		});
	}

	@SuppressWarnings("serial")
	private void bindOpportunityEvents() {
		this.register(new ApplicationEventListener<OpportunityEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(OpportunityEvent.GotoList event) {
				OpportunityListPresenter presenter = PresenterResolver
						.getPresenter(OpportunityListPresenter.class);
				OpportunitySearchCriteria searchCriteria;
				if (event.getData() != null) {
					searchCriteria = (OpportunitySearchCriteria) event
							.getData();
				} else {
					searchCriteria = new OpportunitySearchCriteria();
					searchCriteria.setSaccountid(new NumberSearchField(
							SearchField.AND, AppContext.getAccountId()));
				}

				presenter.go(container,
						new ScreenData.Search<OpportunitySearchCriteria>(
								searchCriteria));
			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(OpportunityEvent.GotoAdd event) {
				OpportunityAddPresenter presenter = PresenterResolver
						.getPresenter(OpportunityAddPresenter.class);
				presenter.go(container, new ScreenData.Add<SimpleOpportunity>(
						new SimpleOpportunity()));
			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(OpportunityEvent.GotoEdit event) {
				OpportunityAddPresenter presenter = PresenterResolver
						.getPresenter(OpportunityAddPresenter.class);
				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GotoRead>() {
			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(OpportunityEvent.GotoRead event) {
				OpportunityReadPresenter presenter = PresenterResolver
						.getPresenter(OpportunityReadPresenter.class);
				presenter.go(container, new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GotoContactRoleEdit>() {
			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(OpportunityEvent.GotoContactRoleEdit event) {
				ContactRoleEditPresenter presenter = PresenterResolver
						.getPresenter(ContactRoleEditPresenter.class);
				presenter.go(container, new ScreenData(event.getData()));
			}
		});
	}

	@SuppressWarnings("serial")
	private void bindCasesEvents() {
		this.register(new ApplicationEventListener<CaseEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(CaseEvent.GotoList event) {
				CaseListPresenter presenter = PresenterResolver
						.getPresenter(CaseListPresenter.class);

				CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
				searchCriteria.setSaccountid(new NumberSearchField(
						SearchField.AND, AppContext.getAccountId()));
				presenter.go(container,
						new ScreenData.Search<CaseSearchCriteria>(
								searchCriteria));
			}
		});

		this.register(new ApplicationEventListener<CaseEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(CaseEvent.GotoAdd event) {
				CaseAddPresenter presenter = PresenterResolver
						.getPresenter(CaseAddPresenter.class);
				presenter.go(container, new ScreenData.Add<SimpleCase>(
						new SimpleCase()));
			}
		});

		this.register(new ApplicationEventListener<CaseEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(CaseEvent.GotoEdit event) {
				CaseAddPresenter presenter = PresenterResolver
						.getPresenter(CaseAddPresenter.class);
				presenter.go(container,
						new ScreenData.Edit<Object>(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<CaseEvent.GotoRead>() {
			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(CaseEvent.GotoRead event) {
				CaseReadPresenter presenter = PresenterResolver
						.getPresenter(CaseReadPresenter.class);
				presenter.go(container, new ScreenData.Preview(event.getData()));
			}
		});
	}
}
