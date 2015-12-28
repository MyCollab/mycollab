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
package com.esofthead.mycollab.web;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.*;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.SubDomainNotExistException;
import com.esofthead.mycollab.module.billing.UsageExceedBillingPlanException;
import com.esofthead.mycollab.module.user.dao.UserAccountMapper;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.UserAccount;
import com.esofthead.mycollab.module.user.domain.UserAccountExample;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.module.user.view.LoginPresenter;
import com.esofthead.mycollab.module.user.view.LoginView;
import com.esofthead.mycollab.shell.ShellController;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.view.MainWindowContainer;
import com.esofthead.mycollab.shell.view.ShellUrlResolver;
import com.esofthead.mycollab.shell.view.components.NoSubDomainExistedWindow;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.AsyncInvoker;
import com.esofthead.mycollab.vaadin.MyCollabUI;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.ui.service.GoogleAnalyticsService;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.UncategorizedSQLException;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.util.BrowserCookie;

import java.util.Collection;
import java.util.GregorianCalendar;

import static com.esofthead.mycollab.core.utils.ExceptionUtils.getExceptionType;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Theme(MyCollabVersion.THEME_VERSION)
@Widgetset("com.esofthead.mycollab.widgetset.MyCollabWidgetSet")
@PreserveOnRefresh
public class DesktopApplication extends MyCollabUI {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(DesktopApplication.class);
    public static final String NAME_COOKIE = "mycollab";
    public static final ShellUrlResolver rootUrlResolver = new ShellUrlResolver();

    private MainWindowContainer mainWindowContainer;

    @Override
    protected void init(final VaadinRequest request) {
        GoogleAnalyticsService googleAnalyticsService = ApplicationContextUtil.getSpringBean(GoogleAnalyticsService.class);
        googleAnalyticsService.registerUI(this);

        LOG.debug("Register default error handler");

        if (SiteConfiguration.getPullMethod() == SiteConfiguration.PullMethod.push) {
            getPushConfiguration().setPushMode(PushMode.MANUAL);
        }

        VaadinSession.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Throwable e = event.getThrowable();
                handleException(request.getHeader("user-agent"), e);
            }
        });

        initialUrl = this.getPage().getUriFragment();
        currentContext = new AppContext();
        postSetupApp(request);

        try {
            currentContext.initDomain(initialSubDomain);
        } catch (SubDomainNotExistException e) {
            this.setContent(new NoSubDomainExistedWindow(initialSubDomain));
            return;
        }

        EventBusFactory.getInstance().register(new ShellErrorHandler());

        mainWindowContainer = new MainWindowContainer();
        this.setContent(mainWindowContainer);

        getPage().addUriFragmentChangedListener(new UriFragmentChangedListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void uriFragmentChanged(UriFragmentChangedEvent event) {
                enter(event.getUriFragment());
            }
        });

        String userAgent = request.getHeader("user-agent");
        if (isInNotSupportedBrowserList(userAgent.toLowerCase())) {
            NotificationUtil.showWarningNotification("Your browser is out of date. Some features of MyCollab will not" +
                    " behave correctly. You should upgrade to the newer browser.");
        }
    }

    private boolean isInNotSupportedBrowserList(String userAgent) {
        return (userAgent.indexOf("msie 6.0") != -1) || (userAgent.indexOf("msie 6.1") != -1)
                || userAgent.indexOf("msie 7.0") != -1 || userAgent.indexOf("msie 8.0") != -1 || userAgent.indexOf("msie 9.0") != -1;
    }

    private static Class[] systemExceptions = new Class[]{UncategorizedSQLException.class};

    private void handleException(String userAgent, Throwable e) {
        IgnoreException ignoreException = getExceptionType(e, IgnoreException.class);
        if (ignoreException != null) {
            return;
        }

        SessionExpireException sessionExpireException = getExceptionType(e, SessionExpireException.class);
        if (sessionExpireException != null) {
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
            return;
        }

        UsageExceedBillingPlanException usageBillingException = getExceptionType(e, UsageExceedBillingPlanException.class);
        if (usageBillingException != null) {
            if (AppContext.isAdmin()) {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.WINDOW_ATTENTION_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.EXCEED_BILLING_PLAN_MSG_FOR_ADMIN),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
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
            return;
        }

        UserInvalidInputException invalidException = getExceptionType(e, UserInvalidInputException.class);
        if (invalidException != null) {
            NotificationUtil.showWarningNotification(AppContext.getMessage(
                    GenericI18Enum.ERROR_USER_INPUT_MESSAGE, invalidException.getMessage()));
            return;
        }

        UnsupportedFeatureException unsupportedException = getExceptionType(e, UnsupportedFeatureException.class);
        if (unsupportedException != null) {
            NotificationUtil.showFeatureNotPresentInSubscription();
            return;
        }

        ResourceNotFoundException resourceNotFoundException = getExceptionType(e, ResourceNotFoundException.class);
        if (resourceNotFoundException != null) {
            NotificationUtil.showWarningNotification("Can not found resource.");
            LOG.error("404", resourceNotFoundException);
            return;
        }

        SecureAccessException secureAccessException = getExceptionType(e, SecureAccessException.class);
        if (secureAccessException != null) {
            NotificationUtil.showWarningNotification("You can not access the specific resource");
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"preview"}));
            return;
        }

        for (Class systemEx : systemExceptions) {
            Exception ex = (Exception) getExceptionType(e, systemEx);
            if (ex != null) {
                ConfirmDialog dialog = ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.WINDOW_ERROR_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.ERROR_USER_SYSTEM_ERROR, ex.getMessage()),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        new ConfirmDialog.Listener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void onClose(ConfirmDialog dialog) {

                            }
                        });
                Button okBtn = dialog.getOkButton();
                BrowserWindowOpener opener = new BrowserWindowOpener("http://support.mycollab.com");
                opener.extend(okBtn);
                return;
            }
        }

        LOG.error("Error " + userAgent, e);
        ConfirmDialog dialog = ConfirmDialogExt.show(UI.getCurrent(),
                AppContext.getMessage(GenericI18Enum.WINDOW_ERROR_TITLE, AppContext.getSiteName()),
                AppContext.getMessage(GenericI18Enum.ERROR_USER_NOTICE_INFORMATION_MESSAGE),
                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                new ConfirmDialog.Listener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClose(ConfirmDialog dialog) {

                    }
                });
        Button okBtn = dialog.getOkButton();
        BrowserWindowOpener opener = new BrowserWindowOpener("http://support.mycollab.com");
        opener.extend(okBtn);
    }

    private void enter(String uriFragement) {
        rootUrlResolver.navigateByFragement(uriFragement);
    }

    private void clearSession() {
        if (currentContext != null) {
            currentContext.clearSessionVariables();
            initialUrl = "";
        }
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

    public void redirectToLoginView() {
        clearSession();

        AppContext.addFragment("", "Login Page");
        // clear cookie remember username/password if any
        this.unsetRememberPassword();

        ControllerRegistry.addController(new ShellController(mainWindowContainer));
        LoginPresenter presenter = PresenterResolver.getPresenter(LoginPresenter.class);
        LoginView loginView = presenter.getView();

        mainWindowContainer.setStyleName("loginView");

        if (loginView.getParent() == null || loginView.getParent() == mainWindowContainer) {
            mainWindowContainer.setAutoLogin(false);
            mainWindowContainer.setContent(loginView);
        } else {
            presenter.go(mainWindowContainer, null);
        }
    }

    public void rememberPassword(String username, String password) {
        String storeVal = username + "$" + PasswordEncryptHelper.encryptText(password);
        BrowserCookie.setCookie(DesktopApplication.NAME_COOKIE, storeVal);
    }

    public void unsetRememberPassword() {
        BrowserCookie.setCookie(DesktopApplication.NAME_COOKIE, "");
    }

    private class ShellErrorHandler {
        @Subscribe
        public void handle(ShellEvent.NotifyErrorEvent event) {
            final Throwable e = (Throwable) event.getData();
            AsyncInvoker.access(new AsyncInvoker.PageCommand() {
                @Override
                public void run() {
                    handleException("", e);
                }
            });
        }
    }
}
