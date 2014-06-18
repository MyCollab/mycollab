/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.AccountEvent;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent.CallEdit;
import com.esofthead.mycollab.mobile.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.mobile.module.crm.events.CaseEvent;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.events.LeadEvent;
import com.esofthead.mycollab.mobile.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmRelatedItemsScreenData;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.ActivityListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.AssignmentAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.AssignmentReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.CallAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.CallReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.MeetingAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.activity.MeetingReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityReadPresenter;
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
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationView;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class CrmModuleController implements IController {
	private static final long serialVersionUID = 6995176903239247669L;
	final private NavigationManager crmViewNavigation;

	public CrmModuleController(NavigationManager navigationManager) {
		this.crmViewNavigation = navigationManager;

		bindCrmEvents();
		bindAccountEvents();
		bindActivityEvents();
		bindContactEvents();
		bindCampaignEvents();
		bindCaseEvents();
		bindLeadEvents();
		bindOpportunityEvents();
	}

	private void bindCrmEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<CrmEvent.GotoHome>() {
					private static final long serialVersionUID = -2434410171305636265L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CrmEvent.GotoHome.class;
					}

					@Override
					public void handle(CrmEvent.GotoHome event) {
						/*
						 * TODO: put setNavigationMenu here seems not right with
						 * current structure, need to move it to somewhere else
						 */
						// if (crmViewNavigation.getNavigationMenu() == null)
						// crmViewNavigation
						// .setNavigationMenu(new CrmNavigationMenu());
						EventBus.getInstance()
								.fireEvent(
										new AccountEvent.GotoList(this, event
												.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CrmEvent.PushView>() {
					private static final long serialVersionUID = -7516440510015076475L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CrmEvent.PushView.class;
					}

					@Override
					public void handle(CrmEvent.PushView event) {
						if (event.getData() instanceof NavigationView) {
							crmViewNavigation.navigateTo((NavigationView) event
									.getData());
						}
					}
				});
	}

	private void bindAccountEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoList>() {

					private static final long serialVersionUID = -3451799893080539849L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoList.class;
					}

					@Override
					public void handle(AccountEvent.GotoList event) {
						AccountListPresenter presenter = PresenterResolver
								.getPresenter(AccountListPresenter.class);
						AccountSearchCriteria criteria = new AccountSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(crmViewNavigation,
								new ScreenData.Search<AccountSearchCriteria>(
										criteria));
					}

				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoAdd>() {
					private static final long serialVersionUID = -3309942209489453346L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoAdd.class;
					}

					@Override
					public void handle(AccountEvent.GotoAdd event) {
						AccountAddPresenter presenter = PresenterResolver
								.getPresenter(AccountAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Add<SimpleAccount>(
										new SimpleAccount()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoEdit>() {
					private static final long serialVersionUID = 5328513173395719936L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoEdit.class;
					}

					@Override
					public void handle(AccountEvent.GotoEdit event) {
						AccountAddPresenter presenter = PresenterResolver
								.getPresenter(AccountAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GotoRead>() {
					private static final long serialVersionUID = -5805283303669877715L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GotoRead.class;
					}

					@SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
					public void handle(AccountEvent.GotoRead event) {
						AccountReadPresenter presenter = PresenterResolver
								.getPresenter(AccountReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<AccountEvent.GoToRelatedItems>() {
					private static final long serialVersionUID = 259904372741221966L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return AccountEvent.GoToRelatedItems.class;
					}

					@Override
					public void handle(AccountEvent.GoToRelatedItems event) {
						if (event.getData() instanceof CrmRelatedItemsScreenData)
							crmViewNavigation
									.navigateTo(((CrmRelatedItemsScreenData) event
											.getData()).getParams());
					}
				});
	}

	private void bindActivityEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.GotoList>() {
					private static final long serialVersionUID = 6101515891859134103L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.GotoList.class;
					}

					@Override
					public void handle(ActivityEvent.GotoList event) {
						ActivityListPresenter presenter = PresenterResolver
								.getPresenter(ActivityListPresenter.class);
						ActivitySearchCriteria criteria = new ActivitySearchCriteria();
						criteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(crmViewNavigation,
								new ScreenData.Search<ActivitySearchCriteria>(
										criteria));
					}
				});
		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.TaskRead>() {
					private static final long serialVersionUID = -3723195748802950651L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.TaskRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(ActivityEvent.TaskRead event) {
						AssignmentReadPresenter presenter = PresenterResolver
								.getPresenter(AssignmentReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.TaskAdd>() {
					private static final long serialVersionUID = -670224728085519779L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.TaskAdd.class;
					}

					@Override
					public void handle(ActivityEvent.TaskAdd event) {
						AssignmentAddPresenter presenter = PresenterResolver
								.getPresenter(AssignmentAddPresenter.class);
						presenter
								.go(crmViewNavigation,
										new ScreenData.Add<SimpleTask>(
												new SimpleTask()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.TaskEdit>() {
					private static final long serialVersionUID = -670224728085519779L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.TaskEdit.class;
					}

					@Override
					public void handle(ActivityEvent.TaskEdit event) {
						AssignmentAddPresenter presenter = PresenterResolver
								.getPresenter(AssignmentAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.CallRead>() {
					private static final long serialVersionUID = -3723195748802950651L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.CallRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(ActivityEvent.CallRead event) {
						CallReadPresenter presenter = PresenterResolver
								.getPresenter(CallReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.CallAdd>() {
					private static final long serialVersionUID = 8759218728614616964L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.CallAdd.class;
					}

					@Override
					public void handle(ActivityEvent.CallAdd event) {
						CallAddPresenter presenter = PresenterResolver
								.getPresenter(CallAddPresenter.class);
						presenter
								.go(crmViewNavigation,
										new ScreenData.Add<SimpleCall>(
												new SimpleCall()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.CallEdit>() {
					private static final long serialVersionUID = -5416887922292705051L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.CallEdit.class;
					}

					@Override
					public void handle(CallEdit event) {
						CallAddPresenter presenter = PresenterResolver
								.getPresenter(CallAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.MeetingRead>() {
					private static final long serialVersionUID = -3723195748802950651L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.MeetingRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(ActivityEvent.MeetingRead event) {
						MeetingReadPresenter presenter = PresenterResolver
								.getPresenter(MeetingReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.MeetingAdd>() {
					private static final long serialVersionUID = -7369637977421183110L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.MeetingAdd.class;
					}

					@Override
					public void handle(ActivityEvent.MeetingAdd event) {
						MeetingAddPresenter presenter = PresenterResolver
								.getPresenter(MeetingAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Add<SimpleMeeting>(
										new SimpleMeeting()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.MeetingEdit>() {
					private static final long serialVersionUID = 1784955912645269021L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.MeetingEdit.class;
					}

					@Override
					public void handle(ActivityEvent.MeetingEdit event) {
						MeetingAddPresenter presenter = PresenterResolver
								.getPresenter(MeetingAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ActivityEvent.GoToRelatedItems>() {
					private static final long serialVersionUID = -2245568910777045010L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ActivityEvent.GoToRelatedItems.class;
					}

					@Override
					public void handle(ActivityEvent.GoToRelatedItems event) {
						if (event.getData() instanceof CrmRelatedItemsScreenData) {
							crmViewNavigation
									.navigateTo(((CrmRelatedItemsScreenData) event
											.getData()).getParams());
						}
					}
				});
		;
	}

	private void bindContactEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoList>() {
					private static final long serialVersionUID = 3327061919614830145L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoList.class;
					}

					@Override
					public void handle(ContactEvent.GotoList event) {
						ContactListPresenter presenter = PresenterResolver
								.getPresenter(ContactListPresenter.class);

						ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(crmViewNavigation,
								new ScreenData.Search<ContactSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoAdd>() {
					private static final long serialVersionUID = -9082569633338794831L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoAdd.class;
					}

					@Override
					public void handle(ContactEvent.GotoAdd event) {
						ContactAddPresenter presenter = PresenterResolver
								.getPresenter(ContactAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Add<SimpleContact>(
										new SimpleContact()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoEdit>() {
					private static final long serialVersionUID = 1465740039647654585L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoEdit.class;
					}

					@Override
					public void handle(ContactEvent.GotoEdit event) {
						ContactAddPresenter presenter = PresenterResolver
								.getPresenter(ContactAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GotoRead>() {
					private static final long serialVersionUID = -5099988781106338890L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GotoRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(ContactEvent.GotoRead event) {
						ContactReadPresenter presenter = PresenterResolver
								.getPresenter(ContactReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ContactEvent.GoToRelatedItems>() {
					private static final long serialVersionUID = -8341031306697617759L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ContactEvent.GoToRelatedItems.class;
					}

					@Override
					public void handle(ContactEvent.GoToRelatedItems event) {
						if (event.getData() instanceof CrmRelatedItemsScreenData)
							crmViewNavigation
									.navigateTo(((CrmRelatedItemsScreenData) event
											.getData()).getParams());
					}

				});

	}

	private void bindCampaignEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoList>() {
					private static final long serialVersionUID = 1553727404269228168L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoList.class;
					}

					@Override
					public void handle(CampaignEvent.GotoList event) {
						CampaignListPresenter presenter = PresenterResolver
								.getPresenter(CampaignListPresenter.class);
						CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));

						presenter.go(crmViewNavigation,
								new ScreenData.Search<CampaignSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoAdd>() {
					private static final long serialVersionUID = 1240143124315010237L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoAdd.class;
					}

					@Override
					public void handle(CampaignEvent.GotoAdd event) {
						CampaignAddPresenter presenter = PresenterResolver
								.getPresenter(CampaignAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Add<SimpleCampaign>(
										new SimpleCampaign()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoEdit>() {
					private static final long serialVersionUID = 7877885891797325699L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoEdit.class;
					}

					@Override
					public void handle(CampaignEvent.GotoEdit event) {
						CampaignAddPresenter presenter = PresenterResolver
								.getPresenter(CampaignAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GotoRead>() {
					private static final long serialVersionUID = -9221302504462965422L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GotoRead.class;
					}

					@SuppressWarnings({ "unchecked", "rawtypes" })
					@Override
					public void handle(CampaignEvent.GotoRead event) {
						CampaignReadPresenter presenter = PresenterResolver
								.getPresenter(CampaignReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CampaignEvent.GoToRelatedItems>() {
					private static final long serialVersionUID = -1867638793934682142L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CampaignEvent.GoToRelatedItems.class;
					}

					@Override
					public void handle(CampaignEvent.GoToRelatedItems event) {
						if (event.getData() instanceof CrmRelatedItemsScreenData)
							crmViewNavigation
									.navigateTo(((CrmRelatedItemsScreenData) event
											.getData()).getParams());
					}
				});
	}

	private void bindCaseEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoList>() {
					private static final long serialVersionUID = -3618797051826954301L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoList.class;
					}

					@Override
					public void handle(CaseEvent.GotoList event) {
						CaseListPresenter presenter = PresenterResolver
								.getPresenter(CaseListPresenter.class);

						CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(crmViewNavigation,
								new ScreenData.Search<CaseSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoAdd>() {
					private static final long serialVersionUID = 1735667150147480819L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoAdd.class;
					}

					@Override
					public void handle(CaseEvent.GotoAdd event) {
						CaseAddPresenter presenter = PresenterResolver
								.getPresenter(CaseAddPresenter.class);
						presenter
								.go(crmViewNavigation,
										new ScreenData.Add<SimpleCase>(
												new SimpleCase()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoEdit>() {
					private static final long serialVersionUID = 2353880791537378472L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoEdit.class;
					}

					@Override
					public void handle(CaseEvent.GotoEdit event) {
						CaseAddPresenter presenter = PresenterResolver
								.getPresenter(CaseAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GotoRead>() {
					private static final long serialVersionUID = -5491126759925853548L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GotoRead.class;
					}

					@SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
					public void handle(CaseEvent.GotoRead event) {
						CaseReadPresenter presenter = PresenterResolver
								.getPresenter(CaseReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<CaseEvent.GoToRelatedItems>() {
					private static final long serialVersionUID = 1019540906038925888L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return CaseEvent.GoToRelatedItems.class;
					}

					@Override
					public void handle(CaseEvent.GoToRelatedItems event) {
						if (event.getData() instanceof CrmRelatedItemsScreenData)
							crmViewNavigation
									.navigateTo(((CrmRelatedItemsScreenData) event
											.getData()).getParams());
					}
				});
	}

	private void bindLeadEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoList>() {
					private static final long serialVersionUID = 9037270302083265873L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoList.class;
					}

					@Override
					public void handle(LeadEvent.GotoList event) {
						LeadListPresenter presenter = PresenterResolver
								.getPresenter(LeadListPresenter.class);
						LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter.go(crmViewNavigation,
								new ScreenData.Search<LeadSearchCriteria>(
										searchCriteria));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoRead>() {
					private static final long serialVersionUID = 9113847281543934181L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoRead.class;
					}

					@SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
					public void handle(LeadEvent.GotoRead event) {
						LeadReadPresenter presenter = PresenterResolver
								.getPresenter(LeadReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoAdd>() {
					private static final long serialVersionUID = -211740720642515595L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoAdd.class;
					}

					@Override
					public void handle(LeadEvent.GotoAdd event) {
						LeadAddPresenter presenter = PresenterResolver
								.getPresenter(LeadAddPresenter.class);
						presenter
								.go(crmViewNavigation,
										new ScreenData.Add<SimpleLead>(
												new SimpleLead()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GotoEdit>() {
					private static final long serialVersionUID = 915856771120600013L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GotoEdit.class;
					}

					@Override
					public void handle(LeadEvent.GotoEdit event) {
						LeadAddPresenter presenter = PresenterResolver
								.getPresenter(LeadAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<LeadEvent.GoToRelatedItems>() {
					private static final long serialVersionUID = -1655170606113750709L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return LeadEvent.GoToRelatedItems.class;
					}

					@Override
					public void handle(LeadEvent.GoToRelatedItems event) {
						if (event.getData() instanceof CrmRelatedItemsScreenData)
							crmViewNavigation
									.navigateTo(((CrmRelatedItemsScreenData) event
											.getData()).getParams());
					}
				});
	}

	private void bindOpportunityEvents() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoList>() {
					private static final long serialVersionUID = -2575430958965270606L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoList.class;
					}

					@Override
					public void handle(OpportunityEvent.GotoList event) {
						OpportunityListPresenter presenter = PresenterResolver
								.getPresenter(OpportunityListPresenter.class);
						OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
						searchCriteria.setSaccountid(new NumberSearchField(
								SearchField.AND, AppContext.getAccountId()));
						presenter
								.go(crmViewNavigation,
										new ScreenData.Search<OpportunitySearchCriteria>(
												searchCriteria));

					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoRead>() {
					private static final long serialVersionUID = -4783961655267073679L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoRead.class;
					}

					@SuppressWarnings({ "rawtypes", "unchecked" })
					@Override
					public void handle(OpportunityEvent.GotoRead event) {
						OpportunityReadPresenter presenter = PresenterResolver
								.getPresenter(OpportunityReadPresenter.class);
						presenter.go(crmViewNavigation, new ScreenData.Preview(
								event.getData()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoAdd>() {
					private static final long serialVersionUID = -1102539216312225338L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoAdd.class;
					}

					@Override
					public void handle(OpportunityEvent.GotoAdd event) {
						OpportunityAddPresenter presenter = PresenterResolver
								.getPresenter(OpportunityAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Add<SimpleOpportunity>(
										new SimpleOpportunity()));
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<OpportunityEvent.GotoEdit>() {
					private static final long serialVersionUID = -5752644127546011966L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return OpportunityEvent.GotoEdit.class;
					}

					@Override
					public void handle(OpportunityEvent.GotoEdit event) {
						OpportunityAddPresenter presenter = PresenterResolver
								.getPresenter(OpportunityAddPresenter.class);
						presenter.go(crmViewNavigation,
								new ScreenData.Edit<Object>(event.getData()));
					}
				});

		EventBus.getInstance()
				.addListener(
						new ApplicationEventListener<OpportunityEvent.GoToRelatedItems>() {
							private static final long serialVersionUID = 2389909957063829985L;

							@Override
							public Class<? extends ApplicationEvent> getEventType() {
								return OpportunityEvent.GoToRelatedItems.class;
							}

							@Override
							public void handle(
									OpportunityEvent.GoToRelatedItems event) {
								if (event.getData() instanceof CrmRelatedItemsScreenData)
									crmViewNavigation
											.navigateTo(((CrmRelatedItemsScreenData) event
													.getData()).getParams());
							}
						});
	}
}
