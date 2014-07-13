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
package com.esofthead.mycollab.module.user.accountsettings.view;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.accountsettings.billing.view.IBillingPresenter;
import com.esofthead.mycollab.module.user.accountsettings.customize.view.ICustomizePresenter;
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfilePresenter;
import com.esofthead.mycollab.module.user.accountsettings.team.view.UserPermissionManagementPresenter;
import com.esofthead.mycollab.module.user.accountsettings.view.events.AccountBillingEvent;
import com.esofthead.mycollab.module.user.accountsettings.view.events.AccountCustomizeEvent;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.BillingScreenData;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.CustomizeScreenData;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.ProfileScreenData;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.RoleScreenData;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.UserScreenData;
import com.esofthead.mycollab.module.user.domain.Role;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.events.RoleEvent;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class UserAccountController implements IController {
	private static final long serialVersionUID = 1L;

	private AccountModule container;
	private EventBus eventBus;

	public UserAccountController(AccountModule container) {
		this.container = container;

		this.eventBus = EventBusFactory.getInstance();

		bindProfileEvents();
		bindBillingEvents();
		bindRoleEvents();
		bindUserEvents();
		bindCustomizeEvents();
	}

	private void bindBillingEvents() {
		eventBus.register(new ApplicationEventListener<AccountBillingEvent.CancelAccount>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(AccountBillingEvent.CancelAccount event) {
				IBillingPresenter presenter = PresenterResolver
						.getPresenter(IBillingPresenter.class);
				presenter.go(container, new BillingScreenData.CancelAccount());
			}
		});

		eventBus.register(new ApplicationEventListener<AccountBillingEvent.GotoSummary>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(AccountBillingEvent.GotoSummary event) {
				IBillingPresenter presenter = PresenterResolver
						.getPresenter(IBillingPresenter.class);
				presenter.go(container, new BillingScreenData.BillingSummary());
			}
		});
	}

	private void bindProfileEvents() {
		eventBus.register(new ApplicationEventListener<ProfileEvent.GotoUploadPhoto>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProfileEvent.GotoUploadPhoto event) {
				ProfilePresenter presenter = PresenterResolver
						.getPresenter(ProfilePresenter.class);
				presenter.go(container, new ProfileScreenData.UploadPhoto(
						(byte[]) event.getData()));
			}
		});

		eventBus.register(new ApplicationEventListener<ProfileEvent.GotoProfileView>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProfileEvent.GotoProfileView event) {
				ProfilePresenter presenter = PresenterResolver
						.getPresenter(ProfilePresenter.class);
				presenter.go(container, null);
			}
		});
	}

	private void bindUserEvents() {
		eventBus.register(new ApplicationEventListener<UserEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(UserEvent.GotoAdd event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);
				presenter.go(container,
						new UserScreenData.Add(new SimpleUser()));
			}
		});

		eventBus.register(new ApplicationEventListener<UserEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(UserEvent.GotoEdit event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);

				SimpleUser user = (SimpleUser) event.getData();
				presenter.go(container, new UserScreenData.Edit(user));
			}
		});

		eventBus.register(new ApplicationEventListener<UserEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(UserEvent.GotoRead event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);
				presenter.go(container,
						new UserScreenData.Read((String) event.getData()));
			}
		});

		eventBus.register(new ApplicationEventListener<UserEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(UserEvent.GotoList event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);

				UserSearchCriteria criteria = new UserSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(SearchField.AND,
						AppContext.getAccountId()));
				criteria.setRegisterStatuses(new SetSearchField<String>(
						SearchField.AND,
						new String[] {
								RegisterStatusConstants.ACTIVE,
								RegisterStatusConstants.SENT_VERIFICATION_EMAIL,
								RegisterStatusConstants.VERIFICATING }));

				presenter.go(container, new UserScreenData.Search(criteria));
			}
		});
	}

	private void bindRoleEvents() {
		eventBus.register(new ApplicationEventListener<RoleEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(RoleEvent.GotoAdd event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);
				presenter.go(container, new RoleScreenData.Add(new Role()));
			}
		});

		eventBus.register(new ApplicationEventListener<RoleEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(RoleEvent.GotoEdit event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);

				Role role = (Role) event.getData();
				presenter.go(container, new RoleScreenData.Edit(role));
			}
		});

		eventBus.register(new ApplicationEventListener<RoleEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(RoleEvent.GotoRead event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);
				presenter.go(container,
						new RoleScreenData.Read((Integer) event.getData()));
			}
		});

		eventBus.register(new ApplicationEventListener<RoleEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(RoleEvent.GotoList event) {
				UserPermissionManagementPresenter presenter = PresenterResolver
						.getPresenter(UserPermissionManagementPresenter.class);

				RoleSearchCriteria criteria = new RoleSearchCriteria();
				criteria.setSaccountid(new NumberSearchField(SearchField.AND,
						AppContext.getAccountId()));
				presenter.go(container, new RoleScreenData.Search(criteria));
			}
		});
	}

	private void bindCustomizeEvents() {
		eventBus.register(new ApplicationEventListener<AccountCustomizeEvent.GotoCustomize>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(AccountCustomizeEvent.GotoCustomize event) {
				ICustomizePresenter presenter = PresenterResolver
						.getPresenter(ICustomizePresenter.class);
				CustomizeScreenData.ThemeCustomize screenData = new CustomizeScreenData.ThemeCustomize();
				if (event.getData() != null) {
					screenData.setParams(event.getData());
				}
				presenter.go(container, screenData);
			}
		});
	}
}
