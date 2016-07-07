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
package com.mycollab.shell.view;

import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.LicenseI18nEnum;
import com.mycollab.common.ui.components.notification.RequestUploadAvatarNotification;
import com.mycollab.common.ui.components.notification.SmtpSetupNotification;
import com.mycollab.configuration.IDeploymentMode;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.ApplicationEventListener;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.license.LicenseInfo;
import com.mycollab.license.LicenseResolver;
import com.mycollab.module.billing.AccountStatusConstants;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.domain.SimpleBillingAccount;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.ui.SettingAssetsManager;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.shell.view.components.AbstractAboutWindow;
import com.mycollab.shell.view.components.AdRequestWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.ThemeManager;
import com.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.mycollab.vaadin.web.ui.ModuleHelper;
import com.mycollab.vaadin.web.ui.NotificationComponent;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.web.AdWindow;
import com.mycollab.web.BuyPremiumSoftwareWindow;
import com.mycollab.web.CustomLayoutExt;
import com.mycollab.web.IDesktopModule;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public final class MainViewImpl extends AbstractPageView implements MainView {
    private static final long serialVersionUID = 1L;

    private CustomLayout headerLayout;
    private HorizontalLayout bodyLayout;
    private MHorizontalLayout accountLayout;

    private ApplicationEventListener<ShellEvent.RefreshPage> pageRefreshHandler = new ApplicationEventListener<ShellEvent.RefreshPage>() {
        @Override
        @Subscribe
        public void handle(ShellEvent.RefreshPage event) {
            buildAccountMenuLayout();
        }
    };

    public MainViewImpl() {
        this.setSizeFull();
        ControllerRegistry.addController(new MainViewController(this));
        ThemeManager.loadDesktopTheme(AppContext.getAccountId());
    }

    @Override
    public void attach() {
        super.attach();
        EventBusFactory.getInstance().register(pageRefreshHandler);
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(pageRefreshHandler);
        super.detach();
    }

    @Override
    public void display() {
        this.removeAllComponents();
        bodyLayout = new HorizontalLayout();
        bodyLayout.addStyleName("main-view");
        bodyLayout.setId("main-body");
        bodyLayout.setSizeFull();
        this.with(createTopMenu(), bodyLayout, createFooter()).expand(bodyLayout);
    }

    @Override
    public void addModule(IDesktopModule module) {
        headerLayout.removeComponent("serviceMenu");
        ModuleHelper.setCurrentModule(module);
        bodyLayout.removeAllComponents();
        bodyLayout.addComponent(module);

        IDeploymentMode mode = AppContextUtil.getSpringBean(IDeploymentMode.class);
        if (mode.isCommunityEdition()) {
            SliderPanel sliderPanel = CommunitySliderPanel.buildCommunitySliderPanel();
            bodyLayout.addComponent(sliderPanel);
        }
        bodyLayout.setExpandRatio(module, 1.0f);

        MHorizontalLayout serviceMenu = module.buildMenu();
        if (serviceMenu != null) {
            headerLayout.addComponent(serviceMenu, "serviceMenu");
        }
    }

    private ComponentContainer createFooter() {
        MHorizontalLayout footer = new MHorizontalLayout().withFullWidth().withMargin(new MarginInfo(false, true, false, true));
        footer.setStyleName("footer");
        footer.setHeight("30px");

        Div companyInfoDiv = new Div().appendText("Powered by ").appendChild(new A("https://www.mycollab.com",
                "_blank").appendText("MyCollab")).appendText(" &copy; " + new LocalDate().getYear());
        ELabel companyInfoLbl = new ELabel(companyInfoDiv.write(), ContentMode.HTML).withWidth("-1px");
        footer.with(companyInfoLbl).withAlign(companyInfoLbl, Alignment.MIDDLE_LEFT);

        Div socialLinksDiv = new Div().appendText(FontAwesome.RSS.getHtml())
                .appendChild(new A("https://www.mycollab.com/blog", "_blank").appendText(" Blog"))
                .appendText("  " + FontAwesome.REPLY_ALL.getHtml())
                .appendChild(new A("http://support.mycollab.com", "_blank").appendText(" Support"));

        if (SiteConfiguration.isCommunityEdition()) {
            socialLinksDiv.appendText("  " + FontAwesome.THUMBS_O_UP.getHtml())
                    .appendChild(new A("http://sourceforge.net/projects/mycollab/reviews/new", "_blank").appendText("" +
                            " Rate us"));
        }

        socialLinksDiv.appendText("  " + FontAwesome.FACEBOOK.getHtml())
                .appendChild(new A("https://www.facebook.com/mycollab2", "_blank").appendText(" FB page"));
        socialLinksDiv.appendText("  " + FontAwesome.TWITTER.getHtml())
                .appendChild(new A("https://twitter.com/intent/tweet?text=I am using MyCollab to manage all project " +
                        "activities, accounts and it works great @mycollabdotcom &source=webclient", "_blank")
                        .appendText(" Tweet"));

        ELabel socialsLbl = new ELabel(socialLinksDiv.write(), ContentMode.HTML).withWidth("-1px");
        footer.with(socialsLbl).withAlign(socialsLbl, Alignment.MIDDLE_RIGHT);
        return footer;
    }

    private CustomLayout createTopMenu() {
        headerLayout = CustomLayoutExt.createLayout("topNavigation");
        headerLayout.setStyleName("topNavigation");
        headerLayout.setHeight("45px");
        headerLayout.setWidth("100%");

        final PopupButton modulePopup = new PopupButton("");

        modulePopup.setHeightUndefined();
        modulePopup.setDirection(Alignment.BOTTOM_LEFT);
        modulePopup.setIcon(AccountAssetsResolver.createLogoResource(AppContext.getBillingAccount().getLogopath(), 150));
        OptionPopupContent modulePopupContent = new OptionPopupContent();
        modulePopup.setContent(modulePopupContent);

        MButton projectModuleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_PROJECT), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
        }).withIcon(VaadinIcons.TASKS);
        modulePopupContent.addOption(projectModuleBtn);

        MButton crmModuleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_CRM), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
        }).withIcon(VaadinIcons.MONEY);
        modulePopupContent.addOption(crmModuleBtn);

        MButton fileModuleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_DOCUMENT), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoFileModule(this, null));
        }).withIcon(VaadinIcons.SUITCASE);
        modulePopupContent.addOption(fileModuleBtn);

        MButton peopleBtn = new MButton(AppContext.getMessage(GenericI18Enum.MODULE_PEOPLE), clickEvent -> {
            modulePopup.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));
        }).withIcon(VaadinIcons.USERS);
        modulePopupContent.addOption(peopleBtn);

        headerLayout.addComponent(new MHorizontalLayout().with(modulePopup).withAlign(modulePopup, Alignment.MIDDLE_LEFT), "mainLogo");

        accountLayout = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false)).withHeight("45px");
        accountLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        buildAccountMenuLayout();

        headerLayout.addComponent(accountLayout, "accountMenu");
        return headerLayout;
    }

    private MHorizontalLayout buildAccountMenuLayout() {
        accountLayout.removeAllComponents();

        if (SiteConfiguration.isDemandEdition()) {
            // display trial box if user in trial mode
            SimpleBillingAccount billingAccount = AppContext.getBillingAccount();
            if (AccountStatusConstants.TRIAL.equals(billingAccount.getStatus())) {
                if ("Free".equals(billingAccount.getBillingPlan().getBillingtype())) {
                    Label informLbl = new Label("<div class='informBlock'>FREE CHARGE<br>UPGRADE</div><div class='informBlock'>&gt;&gt;</div>", ContentMode.HTML);
                    informLbl.addStyleName("trialEndingNotification");
                    informLbl.setHeight("100%");
                    MHorizontalLayout informBox = new MHorizontalLayout(informLbl).withFullHeight().withStyleName("trialInformBox");
                    informBox.setMargin(new MarginInfo(false, true, false, false));
                    informBox.addLayoutClickListener(layoutClickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"})));
                    accountLayout.with(informBox).withAlign(informBox, Alignment.MIDDLE_LEFT);
                } else {
                    Label informLbl = new Label("", ContentMode.HTML);
                    informLbl.addStyleName("trialEndingNotification");
                    informLbl.setHeight("100%");
                    MHorizontalLayout informBox = new MHorizontalLayout(informLbl).withStyleName("trialInformBox")
                            .withMargin(new MarginInfo(false, true, false, false)).withFullHeight();
                    informBox.addLayoutClickListener(layoutClickEvent -> EventBusFactory.getInstance().post(
                            new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"})));
                    accountLayout.with(informBox).withAlign(informBox, Alignment.MIDDLE_LEFT);

                    Duration dur = new Duration(new DateTime(billingAccount.getCreatedtime()), new DateTime());
                    int daysLeft = dur.toStandardDays().getDays();
                    if (daysLeft > 30) {
                        informLbl.setValue("<div class='informBlock'>Trial ended<br></div>");
                        AppContext.getInstance().setIsValidAccount(false);
                    } else {
                        informLbl.setValue(String.format("<div class='informBlock'>Trial ending<br>%d days " +
                                "left</div><div class='informBlock'>&gt;&gt;</div>", 30 - daysLeft));
                    }
                }
            }
        }

        Label accountNameLabel = new Label(AppContext.getSubDomain());
        accountNameLabel.addStyleName("subdomain");
        accountLayout.addComponent(accountNameLabel);

        if (SiteConfiguration.isCommunityEdition()) {
            MButton buyPremiumBtn = new MButton("Upgrade to Pro edition", clickEvent -> UI.getCurrent().addWindow(new AdWindow()))
                    .withIcon(FontAwesome.SHOPPING_CART).withStyleName("ad");
            accountLayout.addComponent(buyPremiumBtn);
        }

        LicenseResolver licenseResolver = AppContextUtil.getSpringBean(LicenseResolver.class);
        if (licenseResolver != null) {
            LicenseInfo licenseInfo = licenseResolver.getLicenseInfo();
            if (licenseInfo != null) {
                if (licenseInfo.isExpired()) {
                    MButton buyPremiumBtn = new MButton(AppContext.getMessage(LicenseI18nEnum.EXPIRE_NOTIFICATION),
                            clickEvent -> UI.getCurrent().addWindow(new BuyPremiumSoftwareWindow()))
                            .withIcon(FontAwesome.SHOPPING_CART).withStyleName("ad");
                    accountLayout.addComponent(buyPremiumBtn);
                } else if (licenseInfo.isTrial()) {
                    Duration dur = new Duration(new DateTime(), new DateTime(licenseInfo.getExpireDate()));
                    int days = dur.toStandardDays().getDays();
                    MButton buyPremiumBtn = new MButton(AppContext.getMessage(LicenseI18nEnum.TRIAL_NOTIFICATION, days),
                            clickEvent -> UI.getCurrent().addWindow(new BuyPremiumSoftwareWindow()))
                            .withIcon(FontAwesome.SHOPPING_CART).withStyleName("ad");
                    accountLayout.addComponent(buyPremiumBtn);
                }
            }
        }

        NotificationComponent notificationComponent = new NotificationComponent();
        accountLayout.addComponent(notificationComponent);

        if (StringUtils.isBlank(AppContext.getUser().getAvatarid())) {
            EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this, new RequestUploadAvatarNotification()));
        }

        if (!SiteConfiguration.isDemandEdition()) {
            ExtMailService mailService = AppContextUtil.getSpringBean(ExtMailService.class);
            if (!mailService.isMailSetupValid()) {
                EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this, new SmtpSetupNotification()));
            }

            SimpleUser user = AppContext.getUser();
            GregorianCalendar tenDaysAgo = new GregorianCalendar();
            tenDaysAgo.add(Calendar.DATE, -10);

            if (Boolean.TRUE.equals(user.getRequestad()) && user.getRegisteredtime().before(tenDaysAgo.getTime())) {
                UI.getCurrent().addWindow(new AdRequestWindow(user));
            }
        }

        Resource userAvatarRes = UserAvatarControlFactory.createAvatarResource(AppContext.getUserAvatarId(), 24);
        final PopupButton accountMenu = new PopupButton("");
        accountMenu.setIcon(userAvatarRes);
        accountMenu.setDescription(AppContext.getUserDisplayName());

        OptionPopupContent accountPopupContent = new OptionPopupContent();

        MButton myProfileBtn = new MButton(AppContext.getMessage(AdminI18nEnum.VIEW_PROFILE), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.PROFILE));
        accountPopupContent.addOption(myProfileBtn);

        MButton userMgtBtn = new MButton(AppContext.getMessage(AdminI18nEnum.VIEW_USERS_AND_ROLES), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.USERS));
        accountPopupContent.addOption(userMgtBtn);

        MButton generalSettingBtn = new MButton(AppContext.getMessage(AdminI18nEnum.VIEW_SETTING), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "general"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.GENERAL_SETTING));
        accountPopupContent.addOption(generalSettingBtn);

        MButton themeCustomizeBtn = new MButton(AppContext.getMessage(AdminI18nEnum.VIEW_THEME), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "theme"}));
        }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.THEME_CUSTOMIZE));
        accountPopupContent.addOption(themeCustomizeBtn);


        if (!SiteConfiguration.isDemandEdition()) {
            MButton setupBtn = new MButton(AppContext.getMessage(AdminI18nEnum.VIEW_SETUP), clickEvent -> {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
            }).withIcon(FontAwesome.WRENCH);
            accountPopupContent.addOption(setupBtn);
        }

        accountPopupContent.addSeparator();

        MButton helpBtn = new MButton(AppContext.getMessage(GenericI18Enum.ACTION_HELP)).withIcon(FontAwesome.MORTAR_BOARD);
        ExternalResource helpRes = new ExternalResource("https://community.mycollab.com/meet-mycollab/");
        BrowserWindowOpener helpOpener = new BrowserWindowOpener(helpRes);
        helpOpener.extend(helpBtn);
        accountPopupContent.addOption(helpBtn);

        MButton supportBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SUPPORT)).withIcon(FontAwesome.LIFE_SAVER);
        ExternalResource supportRes = new ExternalResource("http://support.mycollab.com/");
        BrowserWindowOpener supportOpener = new BrowserWindowOpener(supportRes);
        supportOpener.extend(supportBtn);
        accountPopupContent.addOption(supportBtn);

        MButton translateBtn = new MButton(AppContext.getMessage(GenericI18Enum.ACTION_TRANSLATE)).withIcon(FontAwesome.PENCIL);
        ExternalResource translateRes = new ExternalResource("https://community.mycollab.com/docs/developing-mycollab/translating/");
        BrowserWindowOpener translateOpener = new BrowserWindowOpener(translateRes);
        translateOpener.extend(translateBtn);
        accountPopupContent.addOption(translateBtn);

        if (!SiteConfiguration.isCommunityEdition()) {
            MButton myAccountBtn = new MButton(AppContext.getMessage(AdminI18nEnum.VIEW_BILLING), clickEvent -> {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
            }).withIcon(SettingAssetsManager.getAsset(SettingUIConstants.BILLING));
            accountPopupContent.addOption(myAccountBtn);
        }

        accountPopupContent.addSeparator();
        MButton aboutBtn = new MButton("About MyCollab", clickEvent -> {
            accountMenu.setPopupVisible(false);
            Window aboutWindow = ViewManager.getCacheComponent(AbstractAboutWindow.class);
            UI.getCurrent().addWindow(aboutWindow);
        }).withIcon(FontAwesome.INFO_CIRCLE);
        accountPopupContent.addOption(aboutBtn);

        Button releaseNotesBtn = new Button("Release Notes");
        ExternalResource releaseNotesRes = new ExternalResource("https://community.mycollab.com/docs/hosting-mycollab-on-your-own-server/releases/");
        BrowserWindowOpener releaseNotesOpener = new BrowserWindowOpener(releaseNotesRes);
        releaseNotesOpener.extend(releaseNotesBtn);

        releaseNotesBtn.setIcon(FontAwesome.BULLHORN);
        accountPopupContent.addOption(releaseNotesBtn);

        MButton signoutBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), clickEvent -> {
            accountMenu.setPopupVisible(false);
            EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null));
        }).withIcon(FontAwesome.SIGN_OUT);
        accountPopupContent.addSeparator();
        accountPopupContent.addOption(signoutBtn);

        accountMenu.setContent(accountPopupContent);
        accountLayout.addComponent(accountMenu);
        return accountLayout;
    }
}
