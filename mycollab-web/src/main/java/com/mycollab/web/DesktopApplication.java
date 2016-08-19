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
package com.mycollab.web;

import com.google.common.eventbus.Subscribe;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.EnDecryptHelper;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.core.*;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.billing.SubDomainNotExistException;
import com.mycollab.module.billing.UsageExceedBillingPlanException;
import com.mycollab.module.user.dao.UserAccountMapper;
import com.mycollab.module.user.domain.SimpleBillingAccount;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.domain.UserAccount;
import com.mycollab.module.user.domain.UserAccountExample;
import com.mycollab.module.user.service.BillingAccountService;
import com.mycollab.module.user.service.UserService;
import com.mycollab.shell.ShellController;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.shell.view.LoginPresenter;
import com.mycollab.shell.view.LoginView;
import com.mycollab.shell.view.MainWindowContainer;
import com.mycollab.shell.view.ShellUrlResolver;
import com.mycollab.shell.view.components.NoSubDomainExistedWindow;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.AsyncInvoker;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.service.BroadcastReceiverService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.*;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.UncategorizedSQLException;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.util.BrowserCookie;

import java.util.*;

import static com.mycollab.core.utils.ExceptionUtils.getExceptionType;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@Theme(MyCollabVersion.THEME_VERSION)
@Widgetset("com.mycollab.widgetset.MyCollabWidgetSet")
public class DesktopApplication extends MyCollabUI {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(DesktopApplication.class);

    public static final String ACCOUNT_COOKIE = "mycollab";
    public static final String TEMP_ACCOUNT_COOKIE = "temp_account_mycollab";

    private static List<String> ipLists = new ArrayList<>();

    private MainWindowContainer mainWindowContainer;
    private BroadcastReceiverService broadcastReceiverService;

    @Override
    protected void init(final VaadinRequest request) {
        broadcastReceiverService = AppContextUtil.getSpringBean(BroadcastReceiverService.class);
        if (SiteConfiguration.getPullMethod() == SiteConfiguration.PullMethod.push) {
            getPushConfiguration().setPushMode(PushMode.MANUAL);
        }

        VaadinSession.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                Throwable e = event.getThrowable();
                handleException(request, e);
            }
        });

        setCurrentFragmentUrl(this.getPage().getUriFragment());
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

        getPage().setTitle("MyCollab - Online project management");

        getPage().addUriFragmentChangedListener(uriFragmentChangedEvent -> enter(uriFragmentChangedEvent.getUriFragment()));

        String userAgent = request.getHeader("user-agent");
        if (isInNotSupportedBrowserList(userAgent.toLowerCase())) {
            NotificationUtil.showWarningNotification("Your browser is out of date. Some features of MyCollab will not" +
                    " behave correctly. You should upgrade to the newer browser.");
        }
    }

    @Override
    protected void refresh(VaadinRequest request) {
        EventBusFactory.getInstance().post(new ShellEvent.RefreshPage(this));
    }

    private boolean isInNotSupportedBrowserList(String userAgent) {
        return (userAgent.contains("msie 6.0")) || (userAgent.contains("msie 6.1"))
                || userAgent.contains("msie 7.0") || userAgent.contains("msie 8.0") || userAgent.contains("msie 9.0");
    }

    private static Class[] systemExceptions = new Class[]{UncategorizedSQLException.class, MyBatisSystemException.class};

    private String printRequest(VaadinRequest request) {
        StringBuilder requestInfo = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String attr = headerNames.nextElement();
            requestInfo.append(attr + ": " + request.getHeader(attr)).append('\n');
        }
        requestInfo.append("Subdomain: " + initialSubDomain).append('\n');
        requestInfo.append("Remote address: " + request.getRemoteAddr()).append('\n');
        requestInfo.append("Path info: " + request.getPathInfo()).append('\n');
        requestInfo.append("Remote host: " + request.getRemoteHost()).append('\n');
        return requestInfo.toString();
    }

    private void handleException(VaadinRequest request, Throwable e) {
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
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                Collection<Window> windowsList = UI.getCurrent().getWindows();
                                for (Window window : windowsList) {
                                    window.close();
                                }
                                EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
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
                ConfirmDialog dialog = ConfirmDialogExt.show(DesktopApplication.this,
                        AppContext.getMessage(GenericI18Enum.WINDOW_ERROR_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.ERROR_USER_SYSTEM_ERROR, ex.getMessage()),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                        });
                Button okBtn = dialog.getOkButton();
                BrowserWindowOpener opener = new BrowserWindowOpener("http://support.mycollab.com");
                opener.extend(okBtn);
                return;
            }
        }

        IllegalStateException asyncNotSupport = getExceptionType(e, IllegalStateException.class);
        if (asyncNotSupport != null && asyncNotSupport.getMessage().contains("!asyncSupported")) {
            ConfirmDialog dialog = ConfirmDialogExt.show(DesktopApplication.this,
                    AppContext.getMessage(GenericI18Enum.WINDOW_ERROR_TITLE, AppContext.getSiteName()),
                    "Your network does not support websocket! Please contact your network administrator to solve it",
                    AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                    AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                    confirmDialog -> {
                    });
            Button okBtn = dialog.getOkButton();
            BrowserWindowOpener opener = new BrowserWindowOpener("http://support.mycollab.com");
            opener.extend(okBtn);
            if (request != null) {
                String remoteAddress = request.getRemoteHost();
                if (remoteAddress != null) {
                    if (!ipLists.contains(remoteAddress)) {
                        LOG.error("Async not supported: " + printRequest(request));
                        ipLists.add(remoteAddress);
                    }
                }
            }
            return;
        }

        LOG.error("Error ", e);
        ConfirmDialog dialog = ConfirmDialogExt.show(DesktopApplication.this,
                AppContext.getMessage(GenericI18Enum.WINDOW_ERROR_TITLE, AppContext.getSiteName()),
                AppContext.getMessage(GenericI18Enum.ERROR_USER_NOTICE_INFORMATION_MESSAGE),
                AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                confirmDialog -> {
                });
        Button okBtn = dialog.getOkButton();
        BrowserWindowOpener opener = new BrowserWindowOpener("http://support.mycollab.com");
        opener.extend(okBtn);
    }

    private void enter(String newFragmentUrl) {
        ShellUrlResolver.ROOT().resolveFragment(newFragmentUrl);
    }

    private void clearSession() {
        if (currentContext != null) {
            currentContext.clearSessionVariables();
            setCurrentFragmentUrl("");
        }
        Broadcaster.unregister(broadcastReceiverService);
    }

    @Override
    public void detach() {
        Broadcaster.unregister(broadcastReceiverService);
        super.detach();
    }

    public void doLogin(String username, String password, boolean isRememberPassword) {
        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        SimpleUser user = userService.authentication(username, password, AppContext.getSubDomain(), false);

        if (isRememberPassword) {
            rememberAccount(username, password);
        } else {
            rememberTempAccount(username, password);
        }

        afterDoLogin(user);
    }

    public void afterDoLogin(SimpleUser user) {
        BillingAccountService billingAccountService = AppContextUtil.getSpringBean(BillingAccountService.class);

        SimpleBillingAccount billingAccount = billingAccountService.getBillingAccountById(AppContext.getAccountId());
        LOG.info(String.format("Get billing account successfully - Pricing: %s, User: %s - %s", "" + billingAccount.getBillingPlan().getPricing(),
                user.getUsername(), user.getDisplayName()));
        AppContext.getInstance().setSessionVariables(user, billingAccount);

        UserAccountMapper userAccountMapper = AppContextUtil.getSpringBean(UserAccountMapper.class);
        UserAccount userAccount = new UserAccount();
        userAccount.setLastaccessedtime(new GregorianCalendar().getTime());
        UserAccountExample ex = new UserAccountExample();
        ex.createCriteria().andAccountidEqualTo(billingAccount.getId()).andUsernameEqualTo(user.getUsername());
        userAccountMapper.updateByExampleSelective(userAccount, ex);
        EventBusFactory.getInstance().post(new ShellEvent.GotoMainPage(this, null));
        broadcastReceiverService.registerApp(this);
        Broadcaster.register(broadcastReceiverService);
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
            mainWindowContainer.setContent(loginView);
        } else {
            presenter.go(mainWindowContainer, null);
        }
    }

    private void rememberAccount(String username, String password) {
        String storeVal = username + "$" + EnDecryptHelper.encryptText(password);
        BrowserCookie.setCookie(ACCOUNT_COOKIE, storeVal);
    }

    private void rememberTempAccount(String username, String password) {
        String storeVal = username + "$" + EnDecryptHelper.encryptText(password);
        String setCookieVal = String.format("var now = new Date(); now.setTime(now.getTime() + 1 * 1800 * 1000); " +
                "document.cookie = \"%s=%s; expires=\" + now.toUTCString() + \"; path=/\";", TEMP_ACCOUNT_COOKIE, storeVal);
        JavaScript.getCurrent().execute(setCookieVal);
    }

    private void unsetRememberPassword() {
        BrowserCookie.setCookie(ACCOUNT_COOKIE, "");
        BrowserCookie.setCookie(TEMP_ACCOUNT_COOKIE, "");
    }

    public AppContext getAssociateContext() {
        return (AppContext) getAttribute("context");
    }

    public void reloadPage() {
        getUI().getPage().getJavaScript().execute("window.location.reload();");
    }

    private class ShellErrorHandler {
        @Subscribe
        public void handle(ShellEvent.NotifyErrorEvent event) {
            final Throwable e = (Throwable) event.getData();
            AsyncInvoker.access(getUI(), new AsyncInvoker.PageCommand() {
                @Override
                public void run() {
                    handleException(null, e);
                }
            });
        }
    }
}
