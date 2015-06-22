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

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.billing.view.IBillingPresenter;
import com.esofthead.mycollab.module.user.accountsettings.customize.view.ICustomizePresenter;
import com.esofthead.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.localization.SettingCommonI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.profile.view.ProfilePresenter;
import com.esofthead.mycollab.module.user.accountsettings.setup.view.SetupPresenter;
import com.esofthead.mycollab.module.user.accountsettings.team.view.UserPermissionManagementPresenter;
import com.esofthead.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.esofthead.mycollab.module.user.accountsettings.view.parameters.BillingScreenData;
import com.esofthead.mycollab.module.user.ui.SettingUIConstants;
import com.esofthead.mycollab.module.user.ui.components.UserVerticalTabsheet;
import com.esofthead.mycollab.premium.module.user.accountsettings.view.UserAccountController;
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
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class AccountModuleImpl extends AbstractCssPageView implements AccountModule {
    private static final long serialVersionUID = 1L;

    private UserVerticalTabsheet accountTab;

    private ProfilePresenter profilePresenter;
    private UserPermissionManagementPresenter userPermissionPresenter;
    private IBillingPresenter billingPresenter;
    private ICustomizePresenter customizePresenter;
    private SetupPresenter setupPresenter;

    public AccountModuleImpl() {
        super(true);
        ControllerRegistry.addController(new UserAccountController(this));
        this.setWidth("100%");
        this.addStyleName("main-content-wrapper");
        this.addStyleName("accountViewContainer");

        MHorizontalLayout topPanel = new MHorizontalLayout().withWidth("100%").withStyleName("top-panel")
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
        this.accountTab.getNavigatorWrapper().addComponentAsFirst(introTextWrap);

        this.buildComponents();

        this.addComponent(this.accountTab);
    }

    private Label generateIntroText() {
        return new Label(AppContext.getMessage(SettingCommonI18nEnum.OPT_ADVER_INFO));
    }

    private void buildComponents() {
        accountTab.addTab(constructUserInformationComponent(),
                SettingUIConstants.PROFILE, AppContext.getMessage(AdminI18nEnum.VIEW_PROFILE));

        accountTab.addTab(constructAccountSettingsComponent(),
                SettingUIConstants.BILLING, AppContext.getMessage(AdminI18nEnum.VIEW_BILLING));

        accountTab.addTab(constructUserRoleComponent(), SettingUIConstants.USERS,
                AppContext.getMessage(AdminI18nEnum.VIEW_USERS_AND_ROLES));

        accountTab.addTab(constructThemeComponent(), SettingUIConstants.CUSTOMIZATION,
                AppContext.getMessage(AdminI18nEnum.VIEW_CUSTOMIZE));

        if (SiteConfiguration.getDeploymentMode() == DeploymentMode.standalone) {
            accountTab.addTab(constructSetupComponent(), SettingUIConstants.SETUP,
                    AppContext.getMessage(AdminI18nEnum.VIEW_SETUP));
        }

        accountTab.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                Tab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                String tabId = ((TabImpl) tab).getTabId();
                if (SettingUIConstants.PROFILE.equals(tabId)) {
                    profilePresenter.go(AccountModuleImpl.this, null);
                } else if (SettingUIConstants.BILLING.equals(tabId)) {
                    billingPresenter.go(AccountModuleImpl.this,
                            new BillingScreenData.BillingSummary());
                } else if (SettingUIConstants.USERS.equals(tabId)) {
                    userPermissionPresenter.go(AccountModuleImpl.this, null);
                } else if (SettingUIConstants.CUSTOMIZATION.equals(tabId)) {
                    customizePresenter.go(AccountModuleImpl.this, null);
                } else if (SettingUIConstants.SETUP.equals(tabId)) {
                    setupPresenter.go(AccountModuleImpl.this, null);
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

    private ComponentContainer constructSetupComponent() {
        setupPresenter = PresenterResolver.getPresenter(SetupPresenter.class);
        return setupPresenter.getView();
    }

    private ComponentContainer constructThemeComponent() {
        this.customizePresenter = PresenterResolver
                .getPresenter(ICustomizePresenter.class);
        return this.customizePresenter.getView();
    }

    @Override
    public void gotoSubView(String viewId) {
        this.accountTab.selectTab(viewId);
    }

    @Override
    public void gotoUserProfilePage() {
        EventBusFactory.getInstance().post(
                new ProfileEvent.GotoProfileView(this, null));
    }
}
