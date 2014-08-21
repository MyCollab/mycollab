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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.billing.view.IBillingPresenter;
import com.esofthead.mycollab.module.user.accountsettings.customize.view.ICustomizePresenter;
import com.esofthead.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.localization.SettingCommonI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfilePresenter;
import com.esofthead.mycollab.module.user.accountsettings.team.view.UserPermissionManagementPresenter;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.BillingScreenData;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.CustomizeScreenData;
import com.esofthead.mycollab.module.user.ui.components.UserVerticalTabsheet;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractCssPageView;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet.TabImpl;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class AccountModuleImpl extends AbstractCssPageView implements
		AccountModule {
	private static final long serialVersionUID = 1L;

	private final UserVerticalTabsheet accountTab;

	private ProfilePresenter profilePresenter;
	private UserPermissionManagementPresenter userPermissionPresenter;
	private IBillingPresenter billingPresenter;
	private ICustomizePresenter customizePresenter;

	private final AccountSettingBreadcrumb breadcrumb;

	public AccountModuleImpl() {
		ControllerRegistry.addController(new UserAccountController(this));
		this.setWidth("100%");
		this.addStyleName("main-content-wrapper");
		this.addStyleName("accountViewContainer");

		final HorizontalLayout topPanel = new HorizontalLayout();
		topPanel.setWidth("100%");
		topPanel.setMargin(true);
		topPanel.setStyleName("top-panel");

		this.breadcrumb = ViewManager
				.getCacheComponent(AccountSettingBreadcrumb.class);

		topPanel.addComponent(this.breadcrumb);

		this.accountTab = new UserVerticalTabsheet();
		this.accountTab.setWidth("100%");
		this.accountTab.setNavigatorWidth("250px");
		this.accountTab.setNavigatorStyleName("sidebar-menu");
		this.accountTab.setContainerStyleName("tab-content");
		this.accountTab.setHeight(null);
		this.setVerticalTabsheetFix(true);

		VerticalLayout contentWrapper = this.accountTab.getContentWrapper();
		contentWrapper.addStyleName("main-content");
		contentWrapper.addComponentAsFirst(topPanel);

		VerticalLayout introTextWrap = new VerticalLayout();
		introTextWrap.setStyleName("intro-text-wrap");
		introTextWrap.setMargin(new MarginInfo(true, true, false, true));
		introTextWrap.setWidth("100%");
		introTextWrap.addComponent(generateIntroText());

		this.accountTab.getNavigatorWrapper().setWidth("250px");
		this.accountTab.getNavigatorWrapper()
				.addComponentAsFirst(introTextWrap);

		this.buildComponents();

		this.addComponent(this.accountTab);
	}

	private Label generateIntroText() {
		return new Label(
				AppContext.getMessage(SettingCommonI18nEnum.OPT_ADVER_INFO));
	}

	private void buildComponents() {
		this.accountTab.addTab(this.constructUserInformationComponent(),
				"profile", AppContext.getMessage(AdminI18nEnum.VIEW_PROFILE));

		this.accountTab.addTab(this.constructAccountSettingsComponent(),
				"billing", AppContext.getMessage(AdminI18nEnum.VIEW_BILLING));

		this.accountTab.addTab(this.constructUserRoleComponent(), "users",
				AppContext.getMessage(AdminI18nEnum.VIEW_USERS_AND_ROLES));

		this.accountTab.addTab(this.constructThemeComponent(), "customize",
				AppContext.getMessage(AdminI18nEnum.VIEW_CUSTOMIZE));

		this.accountTab
				.addSelectedTabChangeListener(new SelectedTabChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void selectedTabChange(SelectedTabChangeEvent event) {
						final Tab tab = ((VerticalTabsheet) event.getSource())
								.getSelectedTab();
						final String tabId = ((TabImpl) tab).getTabId();
						if ("profile".equals(tabId)) {
							profilePresenter.go(AccountModuleImpl.this, null);
						} else if ("billing".equals(tabId)) {
							billingPresenter.go(AccountModuleImpl.this,
									new BillingScreenData.BillingSummary());
						} else if ("users".equals(tabId)) {
							userPermissionPresenter.go(AccountModuleImpl.this,
									null);
						} else if ("customize".equals(tabId)) {
							customizePresenter.go(AccountModuleImpl.this,
									new CustomizeScreenData.ThemeCustomize());
						}

					}
				});
	}

	private ComponentContainer constructAccountSettingsComponent() {
		this.billingPresenter = PresenterResolver
				.getPresenter(IBillingPresenter.class);
		return this.billingPresenter.getView();
	}

	private ComponentContainer constructUserInformationComponent() {
		this.profilePresenter = PresenterResolver
				.getPresenter(ProfilePresenter.class);
		return this.profilePresenter.getView();
	}

	private ComponentContainer constructUserRoleComponent() {
		this.userPermissionPresenter = PresenterResolver
				.getPresenter(UserPermissionManagementPresenter.class);
		return this.userPermissionPresenter.getView();
	}

	private ComponentContainer constructThemeComponent() {
		this.customizePresenter = PresenterResolver
				.getPresenter(ICustomizePresenter.class);
		return this.customizePresenter.getView();
	}

	@Override
	public void gotoSubView(final String viewId) {
		this.accountTab.selectTab(viewId);
	}

	@Override
	public void gotoUserProfilePage() {
		EventBusFactory.getInstance().post(
				new ProfileEvent.GotoProfileView(this, null));
	}
}
