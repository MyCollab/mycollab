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
import com.esofthead.mycollab.module.user.ui.SettingUIConstants;
import com.esofthead.mycollab.module.user.ui.components.UserVerticalTabsheet;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.*;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet;
import com.esofthead.mycollab.vaadin.ui.VerticalTabsheet.TabImpl;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.maddon.layouts.MHorizontalLayout;

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

	public AccountModuleImpl() {
        super(true);
		ControllerRegistry.addController(new UserAccountController(this));
		this.setWidth("100%");
		this.addStyleName("main-content-wrapper");
		this.addStyleName("accountViewContainer");

		final MHorizontalLayout topPanel = new MHorizontalLayout().withWidth("100%").withStyleName("top-panel")
                .withMargin(new MarginInfo(true, true, true, false));
        AccountSettingBreadcrumb breadcrumb = ViewManager
				.getCacheComponent(AccountSettingBreadcrumb.class);

		topPanel.addComponent(breadcrumb);

		this.accountTab = new UserVerticalTabsheet();
		this.accountTab.setWidth("100%");
		this.accountTab.setNavigatorWidth("250px");
		this.accountTab.setNavigatorStyleName("sidebar-menu");
		this.accountTab.setContainerStyleName("tab-content");
		this.accountTab.setHeight(null);

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
				SettingUIConstants.PROFILE, AppContext.getMessage(AdminI18nEnum.VIEW_PROFILE));

		this.accountTab.addTab(this.constructAccountSettingsComponent(),
				SettingUIConstants.BILLING, AppContext.getMessage(AdminI18nEnum.VIEW_BILLING));

		this.accountTab.addTab(this.constructUserRoleComponent(),SettingUIConstants.USERS,
				AppContext.getMessage(AdminI18nEnum.VIEW_USERS_AND_ROLES));

		this.accountTab.addTab(this.constructThemeComponent(), SettingUIConstants.CUSTOMIZATION,
				AppContext.getMessage(AdminI18nEnum.VIEW_CUSTOMIZE));

		this.accountTab
				.addSelectedTabChangeListener(new SelectedTabChangeListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void selectedTabChange(SelectedTabChangeEvent event) {
						final Tab tab = ((VerticalTabsheet) event.getSource())
								.getSelectedTab();
						final String tabId = ((TabImpl) tab).getTabId();
						if (SettingUIConstants.PROFILE.equals(tabId)) {
							profilePresenter.go(AccountModuleImpl.this, null);
						} else if (SettingUIConstants.BILLING.equals(tabId)) {
							billingPresenter.go(AccountModuleImpl.this,
									new BillingScreenData.BillingSummary());
						} else if (SettingUIConstants.USERS.equals(tabId)) {
							userPermissionPresenter.go(AccountModuleImpl.this,
									null);
						} else if (SettingUIConstants.CUSTOMIZATION.equals(tabId)) {
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
