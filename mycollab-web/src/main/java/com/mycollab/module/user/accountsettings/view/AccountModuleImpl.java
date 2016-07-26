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
package com.mycollab.module.user.accountsettings.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.user.accountsettings.billing.view.IBillingPresenter;
import com.mycollab.module.user.accountsettings.customize.view.AccountSettingPresenter;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.accountsettings.profile.view.ProfilePresenter;
import com.mycollab.module.user.accountsettings.setup.view.SetupPresenter;
import com.mycollab.module.user.accountsettings.team.view.UserPermissionManagementPresenter;
import com.mycollab.module.user.accountsettings.view.events.ProfileEvent;
import com.mycollab.module.user.accountsettings.view.parameters.BillingScreenData;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.module.user.ui.components.UserVerticalTabsheet;
import com.mycollab.premium.module.user.accountsettings.view.UserAccountController;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.*;
import com.mycollab.vaadin.web.ui.ServiceMenu;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.mycollab.vaadin.web.ui.VerticalTabsheet.TabImpl;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class AccountModuleImpl extends AbstractCssPageView implements AccountModule {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout serviceMenuContainer;
    private ServiceMenu serviceMenu;

    private UserVerticalTabsheet accountTab;

    private ProfilePresenter profilePresenter;
    private UserPermissionManagementPresenter userPermissionPresenter;
    private IBillingPresenter billingPresenter;
    private AccountSettingPresenter customizePresenter;
    private SetupPresenter setupPresenter;

    public AccountModuleImpl() {
        super();
        ControllerRegistry.addController(new UserAccountController(this));
        this.setWidth("100%");
        this.addStyleName("accountViewContainer");

        MHorizontalLayout topPanel = new MHorizontalLayout().withFullWidth().withStyleName("top-panel")
                .withMargin(new MarginInfo(true, true, true, false));
        AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);

        Button helpBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_HELP));
        helpBtn.setIcon(FontAwesome.MORTAR_BOARD);
        helpBtn.addStyleName(WebUIConstants.BUTTON_LINK);
        ExternalResource helpRes = new ExternalResource("https://community.mycollab.com/docs/account-management/");
        BrowserWindowOpener helpOpener = new BrowserWindowOpener(helpRes);
        helpOpener.extend(helpBtn);

        topPanel.with(breadcrumb, helpBtn).expand(breadcrumb);

        accountTab = new UserVerticalTabsheet();
        accountTab.setSizeFull();
        accountTab.setNavigatorStyleName("sidebar-menu");
        accountTab.setContainerStyleName("tab-content");
        VerticalLayout contentWrapper = accountTab.getContentWrapper();
        contentWrapper.addStyleName("main-content");
        contentWrapper.addComponentAsFirst(topPanel);
        accountTab.setNavigatorWidth("200px");
        accountTab.getNavigatorWrapper().setWidth("200px");

        this.buildComponents();
        this.addComponent(accountTab);
    }

    private void buildComponents() {
        accountTab.addTab(constructUserInformationComponent(),
                SettingUIConstants.PROFILE, AppContext.getMessage(AdminI18nEnum.VIEW_PROFILE));

        if (!SiteConfiguration.isCommunityEdition()) {
            accountTab.addTab(constructAccountSettingsComponent(),
                    SettingUIConstants.BILLING, AppContext.getMessage(AdminI18nEnum.VIEW_BILLING));
        }

        accountTab.addTab(constructUserRoleComponent(), SettingUIConstants.USERS,
                AppContext.getMessage(AdminI18nEnum.VIEW_USERS_AND_ROLES));

        accountTab.addTab(constructThemeComponent(), SettingUIConstants.GENERAL_SETTING,
                AppContext.getMessage(AdminI18nEnum.VIEW_SETTING));

        if (!SiteConfiguration.isDemandEdition()) {
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
                    billingPresenter.go(AccountModuleImpl.this, new BillingScreenData.BillingSummary());
                } else if (SettingUIConstants.USERS.equals(tabId)) {
                    userPermissionPresenter.go(AccountModuleImpl.this, null);
                } else if (SettingUIConstants.GENERAL_SETTING.equals(tabId)) {
                    customizePresenter.go(AccountModuleImpl.this, null);
                } else if (SettingUIConstants.SETUP.equals(tabId)) {
                    setupPresenter.go(AccountModuleImpl.this, null);
                }
            }
        });
    }

    private ComponentContainer constructAccountSettingsComponent() {
        billingPresenter = PresenterResolver.getPresenter(IBillingPresenter.class);
        return billingPresenter.getView();
    }

    private ComponentContainer constructUserInformationComponent() {
        profilePresenter = PresenterResolver.getPresenter(ProfilePresenter.class);
        return profilePresenter.getView();
    }

    private ComponentContainer constructUserRoleComponent() {
        userPermissionPresenter = PresenterResolver.getPresenter(UserPermissionManagementPresenter.class);
        return userPermissionPresenter.getView();
    }

    private ComponentContainer constructSetupComponent() {
        setupPresenter = PresenterResolver.getPresenter(SetupPresenter.class);
        return setupPresenter.getView();
    }

    private ComponentContainer constructThemeComponent() {
        customizePresenter = PresenterResolver.getPresenter(AccountSettingPresenter.class);
        return customizePresenter.getView();
    }

    @Override
    public void gotoSubView(String viewId) {
        accountTab.selectTab(viewId);
    }

    @Override
    public void gotoUserProfilePage() {
        EventBusFactory.getInstance().post(new ProfileEvent.GotoProfileView(this));
    }

    @Override
    public MHorizontalLayout buildMenu() {
        if (serviceMenuContainer == null) {
            serviceMenuContainer = new MHorizontalLayout();
            serviceMenu = new ServiceMenu();
            serviceMenu.addService(AppContext.getMessage(GenericI18Enum.MODULE_PROJECT), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{"dashboard"}));
                    serviceMenu.selectService(0);
                }
            });

            serviceMenu.addService(AppContext.getMessage(GenericI18Enum.MODULE_CRM), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
                }
            });

            serviceMenu.addService(AppContext.getMessage(GenericI18Enum.MODULE_DOCUMENT), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoFileModule(this, null));
                }
            });


            serviceMenu.addService(AppContext.getMessage(GenericI18Enum.MODULE_PEOPLE), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));

                }
            });
            serviceMenuContainer.with(serviceMenu);
        }
        serviceMenu.selectService(3);
        return serviceMenuContainer;
    }
}
