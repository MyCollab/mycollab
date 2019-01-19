/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.accountsettings.billing.view.IBillingPresenter;
import com.mycollab.module.user.accountsettings.customize.view.GeneralSettingPresenter;
import com.mycollab.module.user.accountsettings.customize.view.IThemeCustomizePresenter;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.mycollab.module.user.accountsettings.profile.view.ProfilePresenter;
import com.mycollab.module.user.accountsettings.team.view.RoleListPresenter;
import com.mycollab.module.user.accountsettings.team.view.UserListPresenter;
import com.mycollab.module.user.accountsettings.view.event.ProfileEvent;
import com.mycollab.module.user.accountsettings.view.parameters.BillingScreenData;
import com.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.module.user.ui.SettingAssetsManager;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.*;
import com.mycollab.vaadin.web.ui.ServiceMenu;
import com.mycollab.vaadin.web.ui.VerticalTabsheet;
import com.mycollab.vaadin.web.ui.VerticalTabsheet.ButtonTab;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public class AccountModuleImpl extends AbstractSingleContainerPageView implements AccountModule {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout serviceMenuContainer;
    private ServiceMenu serviceMenu;

    private VerticalTabsheet tabSheet;

    public AccountModuleImpl() {
        addStyleName("module");
        ControllerRegistry.addController(new UserAccountController(this));

        MHorizontalLayout topPanel = new MHorizontalLayout().withFullWidth().withMargin(true).withStyleName(WebThemes.BORDER_BOTTOM).withId("tab-content-header");
        AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);

        MButton helpBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.ACTION_HELP))
                .withIcon(VaadinIcons.ACADEMY_CAP).withStyleName(WebThemes.BUTTON_LINK);
        ExternalResource helpRes = new ExternalResource("https://community.mycollab.com/docs/account-management/");
        BrowserWindowOpener helpOpener = new BrowserWindowOpener(helpRes);
        helpOpener.extend(helpBtn);

        topPanel.with(breadcrumb, helpBtn).withAlign(helpBtn, Alignment.TOP_RIGHT);

        tabSheet = new VerticalTabsheet();
        tabSheet.getContentWrapper().addStyleName("content-height");
        tabSheet.setSizeFull();
        tabSheet.addToggleNavigatorControl();
        CssLayout contentWrapper = tabSheet.getContentWrapper();
        contentWrapper.addComponentAsFirst(topPanel);

        this.buildComponents();
        this.setContent(tabSheet);
    }

    private void buildComponents() {
        tabSheet.addTab(null, SettingUIConstants.PROFILE,
                UserUIContext.getMessage(AdminI18nEnum.VIEW_PROFILE), null, SettingAssetsManager.getAsset(SettingUIConstants.PROFILE));

        if (!SiteConfiguration.isCommunityEdition()) {
            tabSheet.addTab(null, SettingUIConstants.BILLING,
                    UserUIContext.getMessage(AdminI18nEnum.VIEW_BILLING), null, SettingAssetsManager.getAsset(SettingUIConstants.BILLING));
        }

        tabSheet.addTab(null, SettingUIConstants.SETTING,
                UserUIContext.getMessage(AdminI18nEnum.VIEW_SETTING), null, SettingAssetsManager.getAsset(SettingUIConstants.SETTING));

        tabSheet.addTab(SettingUIConstants.SETTING, SettingUIConstants.USERS,
                UserUIContext.getMessage(UserI18nEnum.LIST), null, SettingAssetsManager.getAsset(SettingUIConstants.USERS));

        tabSheet.addTab(SettingUIConstants.SETTING, SettingUIConstants.ROLES,
                UserUIContext.getMessage(RoleI18nEnum.LIST), null, SettingAssetsManager.getAsset(SettingUIConstants.ROLES));

        tabSheet.addTab(SettingUIConstants.SETTING, SettingUIConstants.GENERAL_SETTING,
                UserUIContext.getMessage(AdminI18nEnum.VIEW_SETTING), null, SettingAssetsManager.getAsset(SettingUIConstants.GENERAL_SETTING));

        tabSheet.addTab(SettingUIConstants.SETTING, SettingUIConstants.THEME_CUSTOMIZE,
                UserUIContext.getMessage(AdminI18nEnum.VIEW_THEME), null, SettingAssetsManager.getAsset(SettingUIConstants.THEME_CUSTOMIZE));

        tabSheet.addSelectedTabChangeListener(new SelectedTabChangeListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void selectedTabChange(SelectedTabChangeEvent event) {
                ButtonTab tab = ((VerticalTabsheet) event.getSource()).getSelectedTab();
                String tabId = tab.getTabId();
                if (SettingUIConstants.PROFILE.equals(tabId)) {
                    ProfilePresenter presenter = PresenterResolver.getPresenter(ProfilePresenter.class);
                    presenter.go(AccountModuleImpl.this, null);
                } else if (SettingUIConstants.BILLING.equals(tabId)) {
                    IBillingPresenter presenter = PresenterResolver.getPresenter(IBillingPresenter.class);
                    presenter.go(AccountModuleImpl.this, new BillingScreenData.BillingSummary());
                } else if (SettingUIConstants.USERS.equals(tabId)) {
                    UserListPresenter presenter = PresenterResolver.getPresenter(UserListPresenter.class);
                    UserSearchCriteria criteria = new UserSearchCriteria();
                    criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
                    criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE, RegisterStatusConstants.NOT_LOG_IN_YET));
                    presenter.go(AccountModuleImpl.this, new ScreenData.Search<>(criteria));
                } else if (SettingUIConstants.ROLES.equals(tabId)) {
                    RoleListPresenter presenter = PresenterResolver.getPresenter(RoleListPresenter.class);
                    presenter.go(AccountModuleImpl.this, new ScreenData.Search<>(new RoleSearchCriteria()));
                } else if (SettingUIConstants.GENERAL_SETTING.equals(tabId)) {
                    GeneralSettingPresenter presenter = PresenterResolver.getPresenter(GeneralSettingPresenter.class);
                    presenter.go(AccountModuleImpl.this, null);
                } else if (SettingUIConstants.THEME_CUSTOMIZE.equals(tabId)) {
                    IThemeCustomizePresenter presenter = PresenterResolver.getPresenter(IThemeCustomizePresenter.class);
                    presenter.go(AccountModuleImpl.this, null);
                }
            }
        });
    }

    @Override
    public void gotoSubView(String viewId, Component viewDisplay) {
        tabSheet.selectTab(viewId, viewDisplay);
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
            serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_PROJECT), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{"dashboard"}));
                    serviceMenu.selectService(0);
                }
            });
            serviceMenuContainer.with(serviceMenu);
        }
        serviceMenu.selectService(3);
        return serviceMenuContainer;
    }
}
