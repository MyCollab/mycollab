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
package com.esofthead.mycollab.shell.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.LicenseI18nEnum;
import com.esofthead.mycollab.common.ui.components.notification.RequestUploadAvatarNotification;
import com.esofthead.mycollab.common.ui.components.notification.SmtpSetupNotification;
import com.esofthead.mycollab.configuration.IDeploymentMode;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.license.LicenseInfo;
import com.esofthead.mycollab.license.LicenseResolver;
import com.esofthead.mycollab.module.billing.AccountStatusConstants;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.ui.SettingAssetsManager;
import com.esofthead.mycollab.module.user.ui.SettingUIConstants;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.view.components.AbstractAboutWindow;
import com.esofthead.mycollab.shell.view.components.AdRequestWindow;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.AccountAssetsResolver;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.ThemeManager;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.esofthead.mycollab.vaadin.web.ui.ModuleHelper;
import com.esofthead.mycollab.vaadin.web.ui.NotificationComponent;
import com.esofthead.mycollab.vaadin.web.ui.OptionPopupContent;
import com.esofthead.mycollab.web.AdWindow;
import com.esofthead.mycollab.web.BuyPremiumSoftwareWindow;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.esofthead.mycollab.web.IDesktopModule;
import com.google.common.eventbus.Subscribe;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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

        MButton projectModuleBtn = new MButton().withCaption(AppContext.getMessage(GenericI18Enum.MODULE_PROJECT))
                .withIcon(VaadinIcons.TASKS).withListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        modulePopup.setPopupVisible(false);
                        EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
                    }
                });
        modulePopupContent.addOption(projectModuleBtn);

        MButton crmModuleBtn = new MButton().withCaption(AppContext.getMessage(GenericI18Enum.MODULE_CRM)).withIcon(
                VaadinIcons.MONEY).withListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                modulePopup.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
            }
        });
        modulePopupContent.addOption(crmModuleBtn);

        MButton fileModuleBtn = new MButton().withCaption(AppContext.getMessage(GenericI18Enum.MODULE_DOCUMENT))
                .withIcon(VaadinIcons.SUITCASE).withListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {
                        modulePopup.setPopupVisible(false);
                        EventBusFactory.getInstance().post(new ShellEvent.GotoFileModule(this, null));
                    }
                });
        modulePopupContent.addOption(fileModuleBtn);

        MButton peopleBtn = new MButton().withCaption(AppContext.getMessage(GenericI18Enum.MODULE_PEOPLE)).withIcon
                (VaadinIcons.USERS).withListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                modulePopup.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));
            }
        });
        modulePopupContent.addOption(peopleBtn);

        headerLayout.addComponent(new MHorizontalLayout().with(modulePopup).withAlign(modulePopup, Alignment.MIDDLE_LEFT), "mainLogo");

        accountLayout = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false));
        accountLayout.setHeight("45px");
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
                    HorizontalLayout informBox = new HorizontalLayout();
                    informBox.addStyleName("trialInformBox");
                    informBox.setSizeFull();
                    informBox.addComponent(informLbl);
                    informBox.setMargin(new MarginInfo(false, true, false, false));
                    informBox.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void layoutClick(LayoutClickEvent event) {
                            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
                        }
                    });
                    accountLayout.with(informBox).withAlign(informBox, Alignment.MIDDLE_LEFT);
                } else {
                    Label informLbl = new Label("", ContentMode.HTML);
                    informLbl.addStyleName("trialEndingNotification");
                    informLbl.setHeight("100%");
                    HorizontalLayout informBox = new HorizontalLayout();
                    informBox.addStyleName("trialInformBox");
                    informBox.setSizeFull();
                    informBox.setMargin(new MarginInfo(false, true, false, false));
                    informBox.addComponent(informLbl);
                    informBox.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                        private static final long serialVersionUID = 1L;

                        @Override
                        public void layoutClick(LayoutClickEvent event) {
                            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
                        }
                    });
                    accountLayout.with(informBox).withAlign(informBox, Alignment.MIDDLE_LEFT);

                    Duration dur = new Duration(new DateTime(billingAccount.getCreatedtime()), new DateTime());
                    int daysLeft = dur.toStandardDays().getDays();
                    if (daysLeft > 30) {
                        informLbl.setValue("<div class='informBlock'>Trial<br></div><div class='informBlock'>&gt;&gt;</div>");
//                        AppContext.getInstance().setIsValidAccount(false);
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
            Button buyPremiumBtn = new Button("Upgrade to Pro edition", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    UI.getCurrent().addWindow(new AdWindow());
                }
            });
            buyPremiumBtn.setIcon(FontAwesome.SHOPPING_CART);
            buyPremiumBtn.addStyleName("ad");
            accountLayout.addComponent(buyPremiumBtn);
        }

        LicenseResolver licenseResolver = AppContextUtil.getSpringBean(LicenseResolver.class);
        if (licenseResolver != null) {
            LicenseInfo licenseInfo = licenseResolver.getLicenseInfo();
            if (licenseInfo != null) {
                if (licenseInfo.isExpired()) {
                    Button buyPremiumBtn = new Button(AppContext.getMessage(LicenseI18nEnum.EXPIRE_NOTIFICATION), new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            UI.getCurrent().addWindow(new BuyPremiumSoftwareWindow());
                        }
                    });
                    buyPremiumBtn.setIcon(FontAwesome.SHOPPING_CART);
                    buyPremiumBtn.addStyleName("ad");
                    accountLayout.addComponent(buyPremiumBtn);
                } else if (licenseInfo.isTrial()) {
                    Duration dur = new Duration(new DateTime(), new DateTime(licenseInfo.getExpireDate()));
                    int days = dur.toStandardDays().getDays();
                    Button buyPremiumBtn = new Button(AppContext.getMessage(LicenseI18nEnum.TRIAL_NOTIFICATION, days), new ClickListener() {
                        @Override
                        public void buttonClick(ClickEvent event) {
                            UI.getCurrent().addWindow(new BuyPremiumSoftwareWindow());
                        }
                    });
                    buyPremiumBtn.setIcon(FontAwesome.SHOPPING_CART);
                    buyPremiumBtn.addStyleName("ad");
                    accountLayout.addComponent(buyPremiumBtn);
                }
            }
        }

        NotificationComponent notificationComponent = new NotificationComponent();
        accountLayout.addComponent(notificationComponent);

        if (StringUtils.isBlank(AppContext.getUser().getAvatarid())) {
            EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this,
                    new RequestUploadAvatarNotification()));
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

        Button myProfileBtn = new Button(AppContext.getMessage(AdminI18nEnum.VIEW_PROFILE), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
            }
        });
        myProfileBtn.setIcon(SettingAssetsManager.getAsset(SettingUIConstants.PROFILE));
        accountPopupContent.addOption(myProfileBtn);

        Button userMgtBtn = new Button(AppContext.getMessage(AdminI18nEnum.VIEW_USERS_AND_ROLES), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"}));
            }
        });
        userMgtBtn.setIcon(SettingAssetsManager.getAsset(SettingUIConstants.USERS));
        accountPopupContent.addOption(userMgtBtn);

        Button generalSettingBtn = new Button(AppContext.getMessage(AdminI18nEnum.VIEW_SETTING), new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "general"}));
            }
        });
        generalSettingBtn.setIcon(SettingAssetsManager.getAsset(SettingUIConstants.GENERAL_SETTING));
        accountPopupContent.addOption(generalSettingBtn);

        Button themeCustomizeBtn = new Button(AppContext.getMessage(AdminI18nEnum.VIEW_THEME), new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "theme"}));
            }
        });
        themeCustomizeBtn.setIcon(SettingAssetsManager.getAsset(SettingUIConstants.THEME_CUSTOMIZE));
        accountPopupContent.addOption(themeCustomizeBtn);


        if (!SiteConfiguration.isDemandEdition()) {
            Button setupBtn = new Button(AppContext.getMessage(AdminI18nEnum.VIEW_SETUP), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    accountMenu.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
                }
            });
            setupBtn.setIcon(FontAwesome.WRENCH);
            accountPopupContent.addOption(setupBtn);
        }

        accountPopupContent.addSeparator();

        Button helpBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_HELP));
        helpBtn.setIcon(FontAwesome.MORTAR_BOARD);
        ExternalResource helpRes = new ExternalResource("https://community.mycollab.com/meet-mycollab/");
        BrowserWindowOpener helpOpener = new BrowserWindowOpener(helpRes);
        helpOpener.extend(helpBtn);
        accountPopupContent.addOption(helpBtn);

        Button supportBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SUPPORT));
        supportBtn.setIcon(FontAwesome.LIFE_SAVER);
        ExternalResource supportRes = new ExternalResource("http://support.mycollab.com/");
        BrowserWindowOpener supportOpener = new BrowserWindowOpener(supportRes);
        supportOpener.extend(supportBtn);
        accountPopupContent.addOption(supportBtn);

        Button translateBtn = new Button(AppContext.getMessage(GenericI18Enum.ACTION_TRANSLATE));
        translateBtn.setIcon(FontAwesome.PENCIL);
        ExternalResource translateRes = new ExternalResource("https://community.mycollab.com/docs/developing-mycollab/translating/");
        BrowserWindowOpener translateOpener = new BrowserWindowOpener(translateRes);
        translateOpener.extend(translateBtn);
        accountPopupContent.addOption(translateBtn);

        if (!SiteConfiguration.isCommunityEdition()) {
            Button myAccountBtn = new Button(AppContext.getMessage(AdminI18nEnum.VIEW_BILLING), new Button.ClickListener() {
                private static final long serialVersionUID = 1L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    accountMenu.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
                }
            });
            myAccountBtn.setIcon(SettingAssetsManager.getAsset(SettingUIConstants.BILLING));
            accountPopupContent.addOption(myAccountBtn);
        }

        accountPopupContent.addSeparator();
        Button aboutBtn = new Button("About MyCollab", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                accountMenu.setPopupVisible(false);
                Window aboutWindow = ViewManager.getCacheComponent(AbstractAboutWindow.class);
                UI.getCurrent().addWindow(aboutWindow);
            }
        });
        aboutBtn.setIcon(FontAwesome.INFO_CIRCLE);
        accountPopupContent.addOption(aboutBtn);

        Button releaseNotesBtn = new Button("Release Notes");
        ExternalResource releaseNotesRes = new ExternalResource("https://community.mycollab.com/docs/hosting-mycollab-on-your-own-server/releases/");
        BrowserWindowOpener releaseNotesOpener = new BrowserWindowOpener(releaseNotesRes);
        releaseNotesOpener.extend(releaseNotesBtn);

        releaseNotesBtn.setIcon(FontAwesome.BULLHORN);
        accountPopupContent.addOption(releaseNotesBtn);

        Button signoutBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SIGNOUT), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null));
            }
        });
        signoutBtn.setIcon(FontAwesome.SIGN_OUT);
        accountPopupContent.addSeparator();
        accountPopupContent.addOption(signoutBtn);

        accountMenu.setContent(accountPopupContent);
        accountLayout.addComponent(accountMenu);
        return accountLayout;
    }
}
