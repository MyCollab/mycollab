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

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.mobile.module.crm.CrmModuleScreenData;
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
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserPreference;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserPreferenceService;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.google.common.eventbus.Subscribe;
import com.vaadin.addon.touchkit.extensions.LocalStorage;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class CrmModuleController extends AbstractController {
	private static final long serialVersionUID = 6995176903239247669L;
	final private NavigationManager crmViewNavigation;

	private static final Logger LOG = LoggerFactory
			.getLogger(CrmModuleController.class);

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

		this.register(new ApplicationEventListener<CrmEvent.GotoLogin>() {

			private static final long serialVersionUID = 8085525190643870881L;

			@Subscribe
			@Override
			public void handle(CrmEvent.GotoLogin event) {
				CrmLoginPresenter presenter = PresenterResolver
						.getPresenter(CrmLoginPresenter.class);
				presenter.go(crmViewNavigation, null);
			}
		});

		this.register(new ApplicationEventListener<CrmEvent.PlainLogin>() {

			private static final long serialVersionUID = 916898284643597069L;

			@Subscribe
			@Override
			public void handle(CrmEvent.PlainLogin event) {
				String[] data = (String[]) event.getData();
				try {
					doLogin(data[0], data[1], Boolean.valueOf(data[2]));
				} catch (MyCollabException exception) {
					EventBusFactory.getInstance().post(
							new CrmEvent.GotoLogin(this, null));
				}
			}
		});

		this.register(new ApplicationEventListener<CrmEvent.GotoContainer>() {

			private static final long serialVersionUID = -3626315180394209108L;

			@Subscribe
			@Override
			public void handle(CrmEvent.GotoContainer event) {
				CrmContainerPresenter presenter = PresenterResolver
						.getPresenter(CrmContainerPresenter.class);
				presenter.go(crmViewNavigation,
						(CrmModuleScreenData.GotoModule) event.getData());
			}
		});
	}

	private void bindAccountEvents() {
		this.register(new ApplicationEventListener<AccountEvent.GotoList>() {
			private static final long serialVersionUID = -3451799893080539849L;

			@Subscribe
			@Override
			public void handle(AccountEvent.GotoList event) {
				AccountListPresenter presenter = PresenterResolver
						.getPresenter(AccountListPresenter.class);
				AccountSearchCriteria criteria = new AccountSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(SearchField.AND,
						AppContext.getAccountId()));
				presenter.go(crmViewNavigation,
						new ScreenData.Search<AccountSearchCriteria>(criteria));
			}

		});

		this.register(new ApplicationEventListener<AccountEvent.GotoAdd>() {
			private static final long serialVersionUID = -3309942209489453346L;

			@Subscribe
			@Override
			public void handle(AccountEvent.GotoAdd event) {
				AccountAddPresenter presenter = PresenterResolver
						.getPresenter(AccountAddPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Add<SimpleAccount>(new SimpleAccount()));
			}
		});

		this.register(new ApplicationEventListener<AccountEvent.GotoEdit>() {
			private static final long serialVersionUID = 5328513173395719936L;

			@Subscribe
			@Override
			public void handle(AccountEvent.GotoEdit event) {
				AccountAddPresenter presenter = PresenterResolver
						.getPresenter(AccountAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<AccountEvent.GotoRead>() {
			private static final long serialVersionUID = -5805283303669877715L;

			@Subscribe
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void handle(AccountEvent.GotoRead event) {
				AccountReadPresenter presenter = PresenterResolver
						.getPresenter(AccountReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<AccountEvent.GoToRelatedItems>() {
			private static final long serialVersionUID = 259904372741221966L;

			@Subscribe
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
		this.register(new ApplicationEventListener<ActivityEvent.GotoList>() {
			private static final long serialVersionUID = 6101515891859134103L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.GotoList event) {
				ActivityListPresenter presenter = PresenterResolver
						.getPresenter(ActivityListPresenter.class);
				ActivitySearchCriteria criteria = new ActivitySearchCriteria();
				criteria.setSaccountid(new NumberSearchField(SearchField.AND,
						AppContext.getAccountId()));
				presenter
						.go(crmViewNavigation,
								new ScreenData.Search<ActivitySearchCriteria>(
										criteria));
			}
		});
		this.register(new ApplicationEventListener<ActivityEvent.TaskRead>() {
			private static final long serialVersionUID = -3723195748802950651L;

			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(ActivityEvent.TaskRead event) {
				AssignmentReadPresenter presenter = PresenterResolver
						.getPresenter(AssignmentReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.TaskAdd>() {
			private static final long serialVersionUID = -670224728085519779L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.TaskAdd event) {
				AssignmentAddPresenter presenter = PresenterResolver
						.getPresenter(AssignmentAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Add<SimpleTask>(
						new SimpleTask()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.TaskEdit>() {
			private static final long serialVersionUID = -670224728085519779L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.TaskEdit event) {
				AssignmentAddPresenter presenter = PresenterResolver
						.getPresenter(AssignmentAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.CallRead>() {
			private static final long serialVersionUID = -3723195748802950651L;

			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(ActivityEvent.CallRead event) {
				CallReadPresenter presenter = PresenterResolver
						.getPresenter(CallReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.CallAdd>() {
			private static final long serialVersionUID = 8759218728614616964L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.CallAdd event) {
				CallAddPresenter presenter = PresenterResolver
						.getPresenter(CallAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Add<SimpleCall>(
						new SimpleCall()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.CallEdit>() {
			private static final long serialVersionUID = -5416887922292705051L;

			@Subscribe
			@Override
			public void handle(CallEdit event) {
				CallAddPresenter presenter = PresenterResolver
						.getPresenter(CallAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.MeetingRead>() {
			private static final long serialVersionUID = -3723195748802950651L;

			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(ActivityEvent.MeetingRead event) {
				MeetingReadPresenter presenter = PresenterResolver
						.getPresenter(MeetingReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.MeetingAdd>() {
			private static final long serialVersionUID = -7369637977421183110L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.MeetingAdd event) {
				MeetingAddPresenter presenter = PresenterResolver
						.getPresenter(MeetingAddPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Add<SimpleMeeting>(new SimpleMeeting()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.MeetingEdit>() {
			private static final long serialVersionUID = 1784955912645269021L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.MeetingEdit event) {
				MeetingAddPresenter presenter = PresenterResolver
						.getPresenter(MeetingAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ActivityEvent.GoToRelatedItems>() {
			private static final long serialVersionUID = -2245568910777045010L;

			@Subscribe
			@Override
			public void handle(ActivityEvent.GoToRelatedItems event) {
				if (event.getData() instanceof CrmRelatedItemsScreenData) {
					crmViewNavigation
							.navigateTo(((CrmRelatedItemsScreenData) event
									.getData()).getParams());
				}
			}
		});
	}

	private void bindContactEvents() {
		this.register(new ApplicationEventListener<ContactEvent.GotoList>() {
			private static final long serialVersionUID = 3327061919614830145L;

			@Subscribe
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

		this.register(new ApplicationEventListener<ContactEvent.GotoAdd>() {
			private static final long serialVersionUID = -9082569633338794831L;

			@Subscribe
			@Override
			public void handle(ContactEvent.GotoAdd event) {
				ContactAddPresenter presenter = PresenterResolver
						.getPresenter(ContactAddPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Add<SimpleContact>(new SimpleContact()));
			}
		});

		this.register(new ApplicationEventListener<ContactEvent.GotoEdit>() {
			private static final long serialVersionUID = 1465740039647654585L;

			@Subscribe
			@Override
			public void handle(ContactEvent.GotoEdit event) {
				ContactAddPresenter presenter = PresenterResolver
						.getPresenter(ContactAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ContactEvent.GotoRead>() {
			private static final long serialVersionUID = -5099988781106338890L;

			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(ContactEvent.GotoRead event) {
				ContactReadPresenter presenter = PresenterResolver
						.getPresenter(ContactReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ContactEvent.GoToRelatedItems>() {
			private static final long serialVersionUID = -8341031306697617759L;

			@Subscribe
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
		this.register(new ApplicationEventListener<CampaignEvent.GotoList>() {
			private static final long serialVersionUID = 1553727404269228168L;

			@Subscribe
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

		this.register(new ApplicationEventListener<CampaignEvent.GotoAdd>() {
			private static final long serialVersionUID = 1240143124315010237L;

			@Subscribe
			@Override
			public void handle(CampaignEvent.GotoAdd event) {
				CampaignAddPresenter presenter = PresenterResolver
						.getPresenter(CampaignAddPresenter.class);
				presenter
						.go(crmViewNavigation,
								new ScreenData.Add<SimpleCampaign>(
										new SimpleCampaign()));
			}
		});

		this.register(new ApplicationEventListener<CampaignEvent.GotoEdit>() {
			private static final long serialVersionUID = 7877885891797325699L;

			@Subscribe
			@Override
			public void handle(CampaignEvent.GotoEdit event) {
				CampaignAddPresenter presenter = PresenterResolver
						.getPresenter(CampaignAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<CampaignEvent.GotoRead>() {
			private static final long serialVersionUID = -9221302504462965422L;

			@Subscribe
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void handle(CampaignEvent.GotoRead event) {
				CampaignReadPresenter presenter = PresenterResolver
						.getPresenter(CampaignReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<CampaignEvent.GoToRelatedItems>() {
			private static final long serialVersionUID = -1867638793934682142L;

			@Subscribe
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
		this.register(new ApplicationEventListener<CaseEvent.GotoList>() {
			private static final long serialVersionUID = -3618797051826954301L;

			@Subscribe
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

		this.register(new ApplicationEventListener<CaseEvent.GotoAdd>() {
			private static final long serialVersionUID = 1735667150147480819L;

			@Subscribe
			@Override
			public void handle(CaseEvent.GotoAdd event) {
				CaseAddPresenter presenter = PresenterResolver
						.getPresenter(CaseAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Add<SimpleCase>(
						new SimpleCase()));
			}
		});

		this.register(new ApplicationEventListener<CaseEvent.GotoEdit>() {
			private static final long serialVersionUID = 2353880791537378472L;

			@Subscribe
			@Override
			public void handle(CaseEvent.GotoEdit event) {
				CaseAddPresenter presenter = PresenterResolver
						.getPresenter(CaseAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<CaseEvent.GotoRead>() {
			private static final long serialVersionUID = -5491126759925853548L;

			@Subscribe
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void handle(CaseEvent.GotoRead event) {
				CaseReadPresenter presenter = PresenterResolver
						.getPresenter(CaseReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<CaseEvent.GoToRelatedItems>() {
			private static final long serialVersionUID = 1019540906038925888L;

			@Subscribe
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
		this.register(new ApplicationEventListener<LeadEvent.GotoList>() {
			private static final long serialVersionUID = 9037270302083265873L;

			@Subscribe
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

		this.register(new ApplicationEventListener<LeadEvent.GotoRead>() {
			private static final long serialVersionUID = 9113847281543934181L;

			@Subscribe
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void handle(LeadEvent.GotoRead event) {
				LeadReadPresenter presenter = PresenterResolver
						.getPresenter(LeadReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<LeadEvent.GotoAdd>() {
			private static final long serialVersionUID = -211740720642515595L;

			@Subscribe
			@Override
			public void handle(LeadEvent.GotoAdd event) {
				LeadAddPresenter presenter = PresenterResolver
						.getPresenter(LeadAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Add<SimpleLead>(
						new SimpleLead()));
			}
		});

		this.register(new ApplicationEventListener<LeadEvent.GotoEdit>() {
			private static final long serialVersionUID = 915856771120600013L;

			@Subscribe
			@Override
			public void handle(LeadEvent.GotoEdit event) {
				LeadAddPresenter presenter = PresenterResolver
						.getPresenter(LeadAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<LeadEvent.GoToRelatedItems>() {
			private static final long serialVersionUID = -1655170606113750709L;

			@Subscribe
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
		this.register(new ApplicationEventListener<OpportunityEvent.GotoList>() {
			private static final long serialVersionUID = -2575430958965270606L;

			@Subscribe
			@Override
			public void handle(OpportunityEvent.GotoList event) {
				OpportunityListPresenter presenter = PresenterResolver
						.getPresenter(OpportunityListPresenter.class);
				OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
				searchCriteria.setSaccountid(new NumberSearchField(
						SearchField.AND, AppContext.getAccountId()));
				presenter.go(crmViewNavigation,
						new ScreenData.Search<OpportunitySearchCriteria>(
								searchCriteria));

			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GotoRead>() {
			private static final long serialVersionUID = -4783961655267073679L;

			@Subscribe
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void handle(OpportunityEvent.GotoRead event) {
				OpportunityReadPresenter presenter = PresenterResolver
						.getPresenter(OpportunityReadPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Preview(event.getData()));
			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GotoAdd>() {
			private static final long serialVersionUID = -1102539216312225338L;

			@Subscribe
			@Override
			public void handle(OpportunityEvent.GotoAdd event) {
				OpportunityAddPresenter presenter = PresenterResolver
						.getPresenter(OpportunityAddPresenter.class);
				presenter.go(crmViewNavigation,
						new ScreenData.Add<SimpleOpportunity>(
								new SimpleOpportunity()));
			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GotoEdit>() {
			private static final long serialVersionUID = -5752644127546011966L;

			@Subscribe
			@Override
			public void handle(OpportunityEvent.GotoEdit event) {
				OpportunityAddPresenter presenter = PresenterResolver
						.getPresenter(OpportunityAddPresenter.class);
				presenter.go(crmViewNavigation, new ScreenData.Edit<Object>(
						event.getData()));
			}
		});

		this.register(new ApplicationEventListener<OpportunityEvent.GoToRelatedItems>() {
			private static final long serialVersionUID = 2389909957063829985L;

			@Subscribe
			@Override
			public void handle(OpportunityEvent.GoToRelatedItems event) {
				if (event.getData() instanceof CrmRelatedItemsScreenData)
					crmViewNavigation
							.navigateTo(((CrmRelatedItemsScreenData) event
									.getData()).getParams());
			}
		});
	}

	public static void doLogin(String username, String password,
			boolean isRememberPassword) throws MyCollabException {
		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		SimpleUser user = userService.authentication(username, password,
				AppContext.getSubDomain(), false);

		BillingAccountService billingAccountService = ApplicationContextUtil
				.getSpringBean(BillingAccountService.class);

		SimpleBillingAccount billingAccount = billingAccountService
				.getBillingAccountById(AppContext.getAccountId());

		LOG.debug("Get billing account successfully: "
				+ BeanUtility.printBeanObj(billingAccount));

		UserPreferenceService preferenceService = ApplicationContextUtil
				.getSpringBean(UserPreferenceService.class);
		UserPreference pref = preferenceService.getPreferenceOfUser(username,
				AppContext.getAccountId());

		LOG.debug("Login to system successfully. Save user and preference "
				+ pref + " to session");

		if (isRememberPassword) {
			LocalStorage storage = LocalStorage.get();
			String storeVal = username + "$"
					+ PasswordEncryptHelper.encyptText(password);
			storage.put(MobileApplication.LOGIN_DATA, storeVal);
		}

		AppContext.getInstance().setSessionVariables(user, pref, billingAccount);
		pref.setLastaccessedtime(new Date());
		preferenceService.updateWithSession(pref, AppContext.getUsername());
		EventBusFactory.getInstance().post(
				new CrmEvent.GotoContainer(UI.getCurrent(), null));
	}
}
