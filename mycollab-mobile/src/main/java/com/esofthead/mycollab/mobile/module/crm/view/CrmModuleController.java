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
import com.esofthead.mycollab.mobile.module.crm.events.AccountEvent.GoToRelatedItems;
import com.esofthead.mycollab.mobile.module.crm.events.AccountEvent.GotoRead;
import com.esofthead.mycollab.mobile.module.crm.events.ContactEvent;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.module.crm.ui.AccountRelatedItemsScreenData;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmNavigationMenu;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountAddPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.account.AccountReadPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactListPresenter;
import com.esofthead.mycollab.mobile.module.crm.view.contact.ContactReadPresenter;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class CrmModuleController implements IController {
	private static final long serialVersionUID = 6995176903239247669L;
	final private MobileNavigationManager crmViewNavigation;

	public CrmModuleController(MobileNavigationManager navigationManager) {
		this.crmViewNavigation = navigationManager;

		bindCrmEvents();
		bindAccountEvents();
		bindContactEvents();
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
						if (crmViewNavigation.getNavigationMenu() == null)
							crmViewNavigation
									.setNavigationMenu(new CrmNavigationMenu());

						ActivityStreamPresenter presenter = PresenterResolver
								.getPresenter(ActivityStreamPresenter.class);
						presenter.go(crmViewNavigation, null);
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
					public void handle(GotoRead event) {
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
					public void handle(GoToRelatedItems event) {
						if (event.getData() instanceof AccountRelatedItemsScreenData)
							crmViewNavigation
									.navigateTo(((AccountRelatedItemsScreenData) event
											.getData()).getParams());
					}
				});
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

		/*
		 * EventBus.getInstance().addListener( new
		 * ApplicationEventListener<ContactEvent.GotoAdd>() {
		 * 
		 * @Override public Class<? extends ApplicationEvent> getEventType() {
		 * return ContactEvent.GotoAdd.class; }
		 * 
		 * @Override public void handle(ContactEvent.GotoAdd event) {
		 * ContactAddPresenter presenter = PresenterResolver
		 * .getPresenter(ContactAddPresenter.class); presenter.go(container, new
		 * ScreenData.Add<SimpleContact>( new SimpleContact())); } });
		 * 
		 * EventBus.getInstance().addListener( new
		 * ApplicationEventListener<ContactEvent.GotoEdit>() {
		 * 
		 * @Override public Class<? extends ApplicationEvent> getEventType() {
		 * return ContactEvent.GotoEdit.class; }
		 * 
		 * @Override public void handle(ContactEvent.GotoEdit event) {
		 * ContactAddPresenter presenter = PresenterResolver
		 * .getPresenter(ContactAddPresenter.class); presenter.go(container, new
		 * ScreenData.Edit<Object>( event.getData())); } });
		 */

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

	}
}
