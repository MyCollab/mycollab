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
import com.esofthead.mycollab.common.ui.components.notification.*;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.MyCollabVersion;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.jetty.ServerInstance;
import com.esofthead.mycollab.module.billing.AccountStatusConstants;
import com.esofthead.mycollab.module.billing.service.BillingService;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.esofthead.mycollab.module.user.domain.BillingPlan;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.module.user.ui.SettingAssetsManager;
import com.esofthead.mycollab.module.user.ui.SettingUIConstants;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.view.components.AboutWindow;
import com.esofthead.mycollab.shell.view.components.UpgradeConfirmWindow;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.support.domain.TestimonialForm;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.ModuleHelper;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.esofthead.mycollab.web.IDesktopModule;
import com.google.gson.Gson;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Text;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.teemu.VaadinIcons;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
@ViewComponent
public final class MainViewImpl extends AbstractPageView implements MainView {
    private static final long serialVersionUID = 1L;

    private static Logger LOG = LoggerFactory.getLogger(MainViewImpl.class);

    private CustomLayout headerLayout;
    private CssLayout bodyLayout;

    public MainViewImpl() {
        this.setSizeFull();
        ControllerRegistry.addController(new MainViewController(this));
        ThemeManager.loadUserTheme(AppContext.getAccountId());
    }

    @Override
    public void display() {
        this.removeAllComponents();
        bodyLayout = new CssLayout();
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

        ComponentContainer widget = module.getWidget();
        bodyLayout.addComponent(widget);

        MHorizontalLayout serviceMenu = module.buildMenu();
        if (serviceMenu != null) {
            headerLayout.addComponent(serviceMenu, "serviceMenu");
        }
    }

    private CustomLayout createFooter() {
        CustomLayout footer = CustomLayoutExt.createLayout("footer");
        footer.setStyleName("footer");
        footer.setWidth("100%");
        footer.setHeightUndefined();

        Link companyLink = new Link("MyCollab", new ExternalResource("https://www.mycollab.com"));
        companyLink.setTargetName("_blank");

        footer.addComponent(companyLink, "company-url");

        Calendar currentCal = Calendar.getInstance();
        Label currentYear = new Label(String.valueOf(currentCal.get(Calendar.YEAR)));
        currentYear.setSizeUndefined();
        footer.addComponent(currentYear, "current-year");

        MHorizontalLayout footerRight = new MHorizontalLayout();

        Link blogLink = new Link("Blog", new ExternalResource("https://www.mycollab.com/blog"));
        blogLink.setIcon(FontAwesome.RSS);
        blogLink.setTargetName("_blank");

        Link sendFeedback = new Link("Feedback", new ExternalResource("http://support.mycollab.com"));
        sendFeedback.setIcon(FontAwesome.REPLY_ALL);
        sendFeedback.setTargetName("_blank");

        if (SiteConfiguration.getDeploymentMode() == DeploymentMode.standalone) {
            Link rateUsLink = new Link("Rate us!", new ExternalResource("http://sourceforge.net/projects/mycollab/reviews/new"));
            rateUsLink.setTargetName("_blank");
            rateUsLink.setIcon(FontAwesome.THUMBS_O_UP);
            footerRight.with(rateUsLink);
        }

        Link tweetUs = new Link("Tweet", new ExternalResource("https://twitter.com/intent/tweet?text=I am using MyCollab to manage all project activities, accounts and it works great @mycollabdotcom &source=webclient"));
        tweetUs.setTargetName("_blank");
        tweetUs.setIcon(FontAwesome.TWITTER);
        footerRight.with(tweetUs, blogLink, sendFeedback);
        footer.addComponent(footerRight, "footer-right");
        return footer;
    }

    private CustomLayout createTopMenu() {
        headerLayout = CustomLayoutExt.createLayout("topNavigation");
        headerLayout.setStyleName("topNavigation");
        headerLayout.setHeight("40px");
        headerLayout.setWidth("100%");

        final PopupButton modulePopup = new PopupButton("");
        modulePopup.setDirection(Alignment.BOTTOM_LEFT);
        modulePopup.setIcon(AccountAssetsResolver.createLogoResource(AppContext.getBillingAccount().getLogopath(), 150));
        OptionPopupContent modulePopupContent = new OptionPopupContent().withWidth("160px");
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

        headerLayout.addComponent(modulePopup, "mainLogo");

        MHorizontalLayout accountLayout = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false, false));
        accountLayout.setHeight("40px");
        accountLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        if (SiteConfiguration.getDeploymentMode() == DeploymentMode.site) {
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

                    Date createdTime = billingAccount.getCreatedtime();
                    long timeDeviation = System.currentTimeMillis() - createdTime.getTime();
                    int daysLeft = (int) Math.floor(timeDeviation / (double) (1000 * 60 * 60 * 24));
                    if (daysLeft > 30) {
                        BillingService billingService = ApplicationContextUtil.getSpringBean(BillingService.class);
                        BillingPlan freeBillingPlan = billingService.getFreeBillingPlan();
//                        billingAccount.setBillingPlan(freeBillingPlan);
                        informLbl.setValue("<div class='informBlock'>TRIAL ENDING<br>"
                                + " 0 DAYS LEFT</div><div class='informBlock'>&gt;&gt;</div>");
                    } else {
                        informLbl.setValue(String.format("<div class='informBlock'>TRIAL ENDING<br>%d DAYS LEFT</div><div class='informBlock'>&gt;&gt;</div>", 30 - daysLeft));
                    }
                }
            }
        }

        Label accountNameLabel = new Label(AppContext.getSubDomain());
        accountNameLabel.addStyleName("subdomain");
        accountLayout.addComponent(accountNameLabel);

        NotificationButton notificationButton = new NotificationButton();
        accountLayout.addComponent(notificationButton);
        if (AppContext.getUser().getTimezone() == null) {
            EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this, new TimezoneNotification()));
        }

        if (StringUtils.isBlank(AppContext.getUser().getAvatarid())) {
            EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this,
                    new RequestUploadAvatarNotification()));
        }

        if ("admin@mycollab.com".equals(AppContext.getUsername())) {
            EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this,
                    new ChangeDefaultUsernameNotification()));
        }

        if (SiteConfiguration.getDeploymentMode() == DeploymentMode.standalone) {
            UI.getCurrent().access(new Runnable() {
                @Override
                public void run() {
                    try {
                        RestTemplate restTemplate = new RestTemplate();
                        String result = restTemplate.getForObject("https://api.mycollab.com/api/checkupdate?version=" +
                                MyCollabVersion.getVersion(), String.class);
                        Gson gson = new Gson();
                        Properties props = gson.fromJson(result, Properties.class);
                        String version = props.getProperty("version");
                        if (MyCollabVersion.isEditionNewer(version)) {
                            if (AppContext.isAdmin() && StringUtils.isNotBlank(props.getProperty("autoDownload"))) {
                                UI.getCurrent().addWindow(new UpgradeConfirmWindow(props));
                                UI.getCurrent().push();
                            } else {
                                EventBusFactory.getInstance().post(new ShellEvent.NewNotification(this,
                                        new NewUpdateNotification(props)));
                            }
                        }
                    } catch (Exception e) {
                        LOG.error("Error when call remote api", e);
                    }
                }
            });


            ExtMailService mailService = ApplicationContextUtil.getSpringBean(ExtMailService.class);
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

        OptionPopupContent accountPopupContent = new OptionPopupContent().withWidth("160px");

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

        Button generalSettingBtn = new Button("General", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "general"}));
            }
        });
        generalSettingBtn.setIcon(SettingAssetsManager.getAsset(SettingUIConstants.GENERAL_SETTING));
        accountPopupContent.addOption(generalSettingBtn);

        Button themeCustomizeBtn = new Button("Make Theme", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                accountMenu.setPopupVisible(false);
                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setting", "theme"}));
            }
        });
        themeCustomizeBtn.setIcon(SettingAssetsManager.getAsset(SettingUIConstants.THEME_CUSTOMIZE));
        accountPopupContent.addOption(themeCustomizeBtn);


        if (SiteConfiguration.getDeploymentMode() == DeploymentMode.standalone) {
            Button setupBtn = new Button(AppContext.getMessage(AdminI18nEnum.VIEW_SETUP), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    accountMenu.setPopupVisible(false);
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"setup"}));
                }
            });
            setupBtn.setIcon(FontAwesome.WRENCH);
            accountPopupContent.addOption(setupBtn);

            Button aboutBtn = new Button("About", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    accountMenu.setPopupVisible(false);
                    ServerInstance.getInstance().preUpgrade();
                    UI.getCurrent().addWindow(new AboutWindow());
                }
            });
            aboutBtn.setIcon(FontAwesome.INFO_CIRCLE);
            accountPopupContent.addOption(aboutBtn);
        }

        accountPopupContent.addSeparator();
        Button supportBtn = new Button("Support");
        supportBtn.setIcon(FontAwesome.LIFE_SAVER);
        ExternalResource supportRes = new ExternalResource("http://support.mycollab.com/");
        BrowserWindowOpener supportOpener = new BrowserWindowOpener(supportRes);
        supportOpener.extend(supportBtn);
        accountPopupContent.addOption(supportBtn);

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

        headerLayout.addComponent(accountLayout, "accountMenu");
        return headerLayout;
    }

    private static class AdRequestWindow extends Window {
        AdRequestWindow(final SimpleUser user) {
            super("Need help!");
            this.setModal(true);
            this.setResizable(false);
            this.setWidth("600px");

            MVerticalLayout content = new MVerticalLayout();

            Label message = new Label("Our development team has spent more than <b>65 man years development " +
                    "effort</b> (the real number) to give this product free to you. " +
                    "If you like our app, please be sure to spread it to the world. It just takes you few minutes for this kindness action. <b>Your help will encourage us to continue develop MyCollab" +
                    " for free </b> and others have a chance to use an useful app as well", ContentMode.HTML);

            MVerticalLayout shareControls = new MVerticalLayout();
            Label rateSourceforge = new Label(new Div().appendChild(new Text(FontAwesome.THUMBS_O_UP.getHtml()), DivLessFormatter.EMPTY_SPACE(), new A("http://sourceforge.net/projects/mycollab/reviews/new", "_blank")
                    .appendText("Rate us on Sourceforge")).setStyle("color:#006dac").write(), ContentMode.HTML);

            Label tweetUs = new Label(new Div().appendChild(new Text(FontAwesome.TWITTER.getHtml()), DivLessFormatter.EMPTY_SPACE(),
                    new A("https://twitter.com/intent/tweet?text=Im using MyCollab to manage all project activities, accounts and it works great @mycollabdotcom&source=webclient", "_blank")
                            .appendText("Share on Twitter")).setStyle("color:#006dac").write(), ContentMode.HTML);

            Label linkedIn = new Label(new Div().appendChild(new Text(FontAwesome.LINKEDIN_SQUARE.getHtml()), DivLessFormatter.EMPTY_SPACE(),
                    new A("https://www.linkedin.com/cws/share?url=https%3A%2F%2Fwww.mycollab.com&original_referer=https%3A%2F%2Fwww.mycollab.com&token=&isFramed=false&lang=en_US", "_blank")
                            .appendText("Share on LinkedIn")).setStyle("color:#006dac").write(), ContentMode.HTML);

            Button testimonialBtn = new Button("Write a testimonial (We will bring it on our website)", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    AdRequestWindow.this.close();
                    turnOffAdd(user);
                    UI.getCurrent().addWindow(new TestimonialWindow());
                }
            });
            testimonialBtn.setStyleName(UIConstants.THEME_LINK);
            testimonialBtn.setIcon(FontAwesome.KEYBOARD_O);

            shareControls.with(rateSourceforge, tweetUs, linkedIn, testimonialBtn);

            MHorizontalLayout btnControls = new MHorizontalLayout();
            Button ignoreBtn = new Button("Ignore", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    AdRequestWindow.this.close();
                    turnOffAdd(user);
                }
            });
            ignoreBtn.addStyleName(UIConstants.THEME_GRAY_LINK);

            Button loveBtn = new Button("I did", new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    AdRequestWindow.this.close();
                    NotificationUtil.showNotification("We appreciate your kindness action", "Thank you for your time");
                    turnOffAdd(user);
                }
            });
            loveBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
            loveBtn.setIcon(FontAwesome.HEART);

            btnControls.with(loveBtn, ignoreBtn);

            content.with(message, shareControls, btnControls).withAlign(btnControls, Alignment.MIDDLE_RIGHT);
            this.setContent(content);
        }

        private void turnOffAdd(SimpleUser user) {
            user.setRequestad(false);
            UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
            userService.updateSelectiveWithSession(user, AppContext.getUsername());
        }
    }

    private static class TestimonialWindow extends Window {
        TestimonialWindow() {
            super("Thank you! We appreciate your help!");
            this.setModal(true);
            this.setResizable(false);
            this.setWidth("900px");

            MVerticalLayout content = new MVerticalLayout().withMargin(false);

            final TestimonialForm entity = new TestimonialForm();
            final AdvancedEditBeanForm<TestimonialForm> editForm = new AdvancedEditBeanForm<>();
            editForm.setFormLayoutFactory(new IFormLayoutFactory() {
                GridFormLayoutHelper gridFormLayoutHelper;

                @Override
                public ComponentContainer getLayout() {
                    gridFormLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(2, 4);
                    return gridFormLayoutHelper.getLayout();
                }

                @Override
                public void attachField(Object propertyId, Field<?> field) {
                    if ("displayname".equals(propertyId)) {
                        gridFormLayoutHelper.addComponent(field, "Name", 0, 0, 2, "100%");
                    } else if ("company".equals(propertyId)) {
                        gridFormLayoutHelper.addComponent(field, "Company", 0, 1);
                    } else if ("jobrole".equals(propertyId)) {
                        gridFormLayoutHelper.addComponent(field, "Job Role", 1, 1);
                    } else if ("website".equals(propertyId)) {
                        gridFormLayoutHelper.addComponent(field, "Website", 0, 2);
                    } else if ("email".equals(propertyId)) {
                        gridFormLayoutHelper.addComponent(field, "Email", 1, 2);
                    } else if ("testimonial".equals(propertyId)) {
                        gridFormLayoutHelper.addComponent(field, "Testimonial", 0, 3, 2, "100%");
                    }
                }
            });
            editForm.setBeanFormFieldFactory(new AbstractBeanFieldGroupEditFieldFactory<TestimonialForm>(editForm) {
                @Override
                protected Field<?> onCreateField(Object propertyId) {
                    if ("testimonial".equals(propertyId)) {
                        return new TextArea();
                    }
                    return null;
                }
            });
            editForm.setBean(entity);
            content.addComponent(editForm);

            MHorizontalLayout buttonControls = new MHorizontalLayout().withMargin(true);

            Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    TestimonialWindow.this.close();
                }
            });
            cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);

            Button submitBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SUBMIT), new ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    if (editForm.validateForm()) {
                        TestimonialWindow.this.close();
                        NotificationUtil.showNotification("We appreciate your kindness action", "Thank you for your time");
                        try {
                            RestTemplate restTemplate = new RestTemplate();
                            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
                            MultiValueMap<String, Object> values = new LinkedMultiValueMap<>();
                            values.add("company", entity.getCompany());
                            values.add("displayname", entity.getDisplayname());
                            values.add("email", entity.getEmail());
                            values.add("jobrole", entity.getJobrole());
                            values.add("testimonial", entity.getTestimonial());
                            values.add("website", entity.getWebsite());
                            restTemplate.postForObject("https://api.mycollab.com/api/testimonial", values, String.class);
                        } catch (Exception e) {
                            LOG.error("Error when call remote api", e);
                        }
                    }
                }
            });
            submitBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
            submitBtn.setIcon(FontAwesome.MAIL_FORWARD);

            buttonControls.with(cancelBtn, submitBtn);

            content.with(buttonControls).withAlign(buttonControls, Alignment.MIDDLE_RIGHT);
            this.setContent(content);
        }
    }
}
