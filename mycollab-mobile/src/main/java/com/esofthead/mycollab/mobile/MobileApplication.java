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
package com.esofthead.mycollab.mobile;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.IgnoreException;
import com.esofthead.mycollab.core.MyCollabVersion;
import com.esofthead.mycollab.core.SessionExpireException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.user.view.LoginPresenter;
import com.esofthead.mycollab.mobile.shell.ShellUrlResolver;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.shell.view.ShellController;
import com.esofthead.mycollab.mobile.ui.ConfirmDialog;
import com.esofthead.mycollab.module.billing.UsageExceedBillingPlanException;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.MyCollabUI;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.ThemeManager;
import com.esofthead.mycollab.vaadin.ui.service.GoogleAnalyticsService;
import com.vaadin.addon.touchkit.extensions.LocalStorage;
import com.vaadin.addon.touchkit.extensions.LocalStorageCallback;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
@Theme(MyCollabVersion.THEME_MOBILE_VERSION)
@Viewport("width=device-width, initial-scale=1")
@Widgetset("com.esofthead.mycollab.widgetset.MyCollabMobileWidgetSet")
public class MobileApplication extends MyCollabUI {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(MobileApplication.class);

    public static final String NAME_COOKIE = "mycollab";
    public static final ShellUrlResolver rootUrlResolver = new ShellUrlResolver();

    @Override
    protected void init(VaadinRequest request) {
        GoogleAnalyticsService googleAnalyticsService = ApplicationContextUtil.getSpringBean(GoogleAnalyticsService.class);
        googleAnalyticsService.registerUI(this);

        VaadinSession.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Throwable e = event.getThrowable();
                IgnoreException ignoreException = (IgnoreException) getExceptionType(e, IgnoreException.class);
                if (ignoreException != null) {
                    return;
                }

                SessionExpireException sessionExpireException = (SessionExpireException) getExceptionType(e, SessionExpireException.class);
                if (sessionExpireException != null) {
                    Page.getCurrent().getJavaScript().execute("window.location.reload();");
                    return;
                }

                UserInvalidInputException invalidException = (UserInvalidInputException) getExceptionType(
                        e, UserInvalidInputException.class);
                if (invalidException != null) {
                    NotificationUtil.showWarningNotification(AppContext.getMessage(GenericI18Enum.ERROR_USER_INPUT_MESSAGE,
                            invalidException.getMessage()));
                } else {
                    UsageExceedBillingPlanException usageBillingException = (UsageExceedBillingPlanException) getExceptionType(
                            e, UsageExceedBillingPlanException.class);
                    if (usageBillingException != null) {
                        if (AppContext.isAdmin()) {
                            ConfirmDialog.show(UI.getCurrent(),
                                    AppContext.getMessage(GenericI18Enum.EXCEED_BILLING_PLAN_MSG_FOR_ADMIN),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                                    AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                                    new ConfirmDialog.CloseListener() {
                                        private static final long serialVersionUID = 1L;

                                        @Override
                                        public void onClose(ConfirmDialog dialog) {
                                            if (dialog.isConfirmed()) {
                                                Collection<Window> windowsList = UI.getCurrent().getWindows();
                                                for (Window window : windowsList) {
                                                    window.close();
                                                }
                                                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
                                            }
                                        }
                                    });

                        } else {
                            NotificationUtil.showErrorNotification(AppContext.getMessage(GenericI18Enum.EXCEED_BILLING_PLAN_MSG_FOR_USER));
                        }
                    } else {
                        LOG.error("Error", e);
                        NotificationUtil.showErrorNotification(AppContext.getMessage(GenericI18Enum.ERROR_USER_NOTICE_INFORMATION_MESSAGE));
                    }
                }

            }
        });

        initialUrl = this.getPage().getUriFragment();
        currentContext = new AppContext();
        postSetupApp(request);
        try {
            currentContext.initDomain(initialSubDomain);
        } catch (Exception e) {
            // TODO: show content notice user there is no existing domain
            return;
        }

        this.getLoadingIndicatorConfiguration().setFirstDelay(0);
        this.getLoadingIndicatorConfiguration().setSecondDelay(300);
        this.getLoadingIndicatorConfiguration().setThirdDelay(500);

        final NavigationManager manager = new NavigationManager();
        setContent(manager);

        registerControllers(manager);
        ThemeManager.loadMobileTheme(AppContext.getAccountId());

        getPage().addUriFragmentChangedListener(new UriFragmentChangedListener() {
            private static final long serialVersionUID = -6410955178515535406L;

            @Override
            public void uriFragmentChanged(UriFragmentChangedEvent event) {
                setInitialUrl(event.getUriFragment());
                enter(event.getUriFragment());
            }
        });
        detectAutoLogin();
    }

    private void detectAutoLogin() {
        LocalStorage.detectValue(NAME_COOKIE, new LocalStorageCallback() {
            @Override
            public void onSuccess(String value) {
                if (StringUtils.isNotBlank(value)) {
                    String[] loginParams = value.split("\\$");
                    doLogin(loginParams[0], PasswordEncryptHelper.decryptText(loginParams[1]), false);
                } else {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoLoginView(this));
                }
            }

            @Override
            public void onFailure(FailureEvent failureEvent) {
                EventBusFactory.getInstance().post(new ShellEvent.GotoLoginView(this));
            }
        });
    }

    private void enter(String uriFragement) {
        try {
            rootUrlResolver.navigateByFragement(uriFragement);
        } catch (Exception e) {
            LOG.error("Error", e);
        }
    }

    private void registerControllers(NavigationManager manager) {
        ControllerRegistry.addController(new ShellController(manager));
    }

    public void doLogin(String username, String password, boolean isRememberPassword) {
        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        SimpleUser user = userService.authentication(username, password, AppContext.getSubDomain(), false);

        if (isRememberPassword) {
            rememberPassword(username, password);
        }

        BillingAccountService billingAccountService = ApplicationContextUtil.getSpringBean(BillingAccountService.class);

        SimpleBillingAccount billingAccount = billingAccountService.getBillingAccountById(AppContext.getAccountId());
        LOG.debug(String.format("Get billing account successfully: %s", BeanUtility.printBeanObj(billingAccount)));
        AppContext.getInstance().setSessionVariables(user, billingAccount);

        UserAccountMapper userAccountMapper = ApplicationContextUtil.getSpringBean(UserAccountMapper.class);
        UserAccount userAccount = new UserAccount();
        userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
        UserAccountExample ex = new UserAccountExample();
        ex.createCriteria().andAccountidEqualTo(billingAccount.getId()).andUsernameEqualTo(user.getUsername());
        userAccountMapper.updateByExampleSelective(userAccount, ex);
        EventBusFactory.getInstance().post(new ShellEvent.GotoMainPage(this, null));
    }

    private void rememberPassword(String username, String password) {
        String storeVal = username + "$" + PasswordEncryptHelper.encryptText(password);
        LocalStorage.get().put(NAME_COOKIE, storeVal);
    }

    private static Throwable getExceptionType(Throwable e, Class<? extends Throwable> exceptionType) {
        if (exceptionType.isAssignableFrom(e.getClass())) {
            return e;
        } else if (e.getCause() != null) {
            return getExceptionType(e.getCause(), exceptionType);
        } else {
            return null;
        }
    }

    public void redirectToLoginView() {
        clearSession();
        AppContext.addFragment("", "Login Page");
        // clear cookie remember username/password if any
        this.unsetRememberPassword();

        NavigationManager navigationManager = (NavigationManager) this.getContent();
        navigationManager.getViewStack().empty();
        LoginPresenter presenter = PresenterResolver.getPresenter(LoginPresenter.class);
        presenter.go(navigationManager, null);
    }

    private void clearSession() {
        if (currentContext != null) {
            currentContext.clearSessionVariables();
            initialUrl = "";
        }
    }

    private void unsetRememberPassword() {
        LocalStorage.get().put(NAME_COOKIE, "");
    }
}
