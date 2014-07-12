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

import static com.esofthead.mycollab.common.MyCollabSession.CURRENT_APP;

import java.util.Collection;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.common.SessionIdGenerator;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.core.SecurityException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.core.arguments.GroupIdProvider;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.billing.SubDomainNotExistException;
import com.esofthead.mycollab.module.billing.UsageExceedBillingPlanException;
import com.esofthead.mycollab.module.user.view.LoginPresenter;
import com.esofthead.mycollab.module.user.view.LoginView;
import com.esofthead.mycollab.shell.ShellController;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.view.FragmentNavigator;
import com.esofthead.mycollab.shell.view.MainWindowContainer;
import com.esofthead.mycollab.shell.view.NoSubDomainExistedWindow;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Theme("mycollab")
@Widgetset("com.esofthead.mycollab.widgetset.MyCollabWidgetSet")
public class DesktopApplication extends UI {

	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory
			.getLogger(DesktopApplication.class);

	private MainWindowContainer mainWindowContainer;

	/**
	 * Context of current logged in user
	 */
	private AppContext currentContext;

	private String initialSubDomain = "1";
	private String initialUrl = "";

	public static final String NAME_COOKIE = "mycollab";

	static {
		GroupIdProvider.registerAccountIdProvider(new GroupIdProvider() {

			@Override
			public Integer getGroupId() {
				return AppContext.getAccountId();
			}
		});

		SessionIdGenerator.registerSessionIdGenerator(new SessionIdGenerator() {

			@Override
			public String getSessionIdApp() {
				return UI.getCurrent().toString();
			}
		});
	}

	public static DesktopApplication getInstance() {
		return (DesktopApplication) MyCollabSession.getVariable(CURRENT_APP);
	}

	@Override
	protected void init(VaadinRequest request) {
		log.debug("Init mycollab application {} associate with session {}",
				this.toString(), VaadinSession.getCurrent());
		log.debug("Register default error handler");

		VaadinSession.getCurrent().setErrorHandler(new DefaultErrorHandler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				Throwable e = event.getThrowable();
				UserInvalidInputException invalidException = (UserInvalidInputException) getExceptionType(
						e, UserInvalidInputException.class);
				if (invalidException != null) {
					NotificationUtil.showWarningNotification(AppContext
							.getMessage(
									GenericI18Enum.ERROR_USER_INPUT_MESSAGE,
									invalidException.getMessage()));
				} else {
					SecurityException securityException = (SecurityException) getExceptionType(
							e, SecurityException.class);
					if (securityException != null) {
						NotificationUtil.showMessagePermissionAlert();
					} else {
						UsageExceedBillingPlanException usageBillingException = (UsageExceedBillingPlanException) getExceptionType(
								e, UsageExceedBillingPlanException.class);
						if (usageBillingException != null) {
							if (AppContext.isAdmin()) {
								ConfirmDialogExt.show(
										UI.getCurrent(),
										AppContext
												.getMessage(
														GenericI18Enum.WINDOW_ATTENTION_TITLE,
														SiteConfiguration
																.getSiteName()),
										AppContext
												.getMessage(GenericI18Enum.EXCEED_BILLING_PLAN_MSG_FOR_ADMIN),
										AppContext
												.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
										AppContext
												.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
										new ConfirmDialog.Listener() {
											private static final long serialVersionUID = 1L;

											@Override
											public void onClose(
													ConfirmDialog dialog) {
												if (dialog.isConfirmed()) {
													Collection<Window> windowsList = UI
															.getCurrent()
															.getWindows();
													for (Window window : windowsList) {
														window.close();
													}
													EventBus.getInstance()
															.fireEvent(
																	new ShellEvent.GotoUserAccountModule(
																			this,
																			new String[] { "billing" }));
												}
											}
										});

							} else {
								NotificationUtil.showErrorNotification(AppContext
										.getMessage(GenericI18Enum.EXCEED_BILLING_PLAN_MSG_FOR_USER));
							}
						} else {
							log.error("Error", e);
							NotificationUtil.showErrorNotification(AppContext
									.getMessage(GenericI18Enum.ERROR_USER_NOTICE_INFORMATION_MESSAGE));
						}

					}
				}

			}
		});

		initialUrl = this.getPage().getUriFragment();
		MyCollabSession.putVariable(CURRENT_APP, this);
		currentContext = new AppContext(this);
		postSetupApp(request);
		try {
			currentContext.initDomain(initialSubDomain);
		} catch (SubDomainNotExistException e) {
			this.setContent(new NoSubDomainExistedWindow(initialSubDomain));
			return;
		}

		mainWindowContainer = new MainWindowContainer();
		this.setContent(mainWindowContainer);

		getPage().addUriFragmentChangedListener(
				new UriFragmentChangedListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void uriFragmentChanged(UriFragmentChangedEvent event) {
						enter(event.getUriFragment());
					}
				});
	}

	private void enter(String uriFragement) {
		FragmentNavigator.navigateByFragement(uriFragement);
	}

	private void postSetupApp(VaadinRequest request) {
		VaadinServletRequest servletRequest = (VaadinServletRequest) request;
		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.SITE) {
			initialSubDomain = servletRequest.getServerName().split("\\.")[0];
		} else {
			initialSubDomain = servletRequest.getServerName();
		}
	}

	public AppContext getSessionData() {
		return currentContext;
	}

	public String getInitialUrl() {
		return initialUrl;
	}

	@Override
	public void close() {
		log.debug("Application is closed. Clean all resources");
		currentContext.clearSession();
		initialUrl = "";
		currentContext = null;
		super.close();
	}

	private void clearSession() {
		if (currentContext != null) {
			currentContext.clearSession();
			initialUrl = "";
			ViewManager.clearViewCaches();
			PresenterResolver.clearCaches();
			EventBus.getInstance().clear();
			ControllerRegistry.reset();
		}
	}

	public void redirectToLoginView() {
		clearSession();

		AppContext.addFragment("", "Login Page");
		// clear cookie remember username/password if any
		DesktopApplication.getInstance().unsetRememberPassword();

		ControllerRegistry.addController(new ShellController(
				mainWindowContainer));
		LoginPresenter presenter = PresenterResolver
				.getPresenter(LoginPresenter.class);
		LoginView loginView = presenter.initView();

		mainWindowContainer.setStyleName("loginView");

		if (loginView.getParent() == null
				|| loginView.getParent() == mainWindowContainer) {
			((MainWindowContainer) mainWindowContainer).setAutoLogin(false);
			((MainWindowContainer) mainWindowContainer).setContent(loginView);
		} else {
			presenter.go(mainWindowContainer, null);
		}
	}

	public void rememberPassword(String username, String password) {

		Cookie cookie = getCookieByName(DesktopApplication.NAME_COOKIE);
		String storeVal = username + "$"
				+ PasswordEncryptHelper.encyptText(password);
		if (cookie == null) {
			cookie = new Cookie(DesktopApplication.NAME_COOKIE, storeVal);
		} else {
			cookie.setValue(storeVal);
		}
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 7);

		Page.getCurrent()
				.getJavaScript()
				.execute(
						String.format(
								"document.cookie = '%s=%s; expires=%s;';",
								"mycollab", storeVal, 60 * 60 * 24 * 7 + ""));
	}

	public void unsetRememberPassword() {
		Cookie cookie = getCookieByName(DesktopApplication.NAME_COOKIE);

		if (cookie != null) {
			cookie.setValue("");
			cookie.setMaxAge(0);
			Page.getCurrent()
					.getJavaScript()
					.execute(
							String.format(
									"document.cookie = '%s=%s; expires=%s;';",
									"mycollab", "", "0"));
		}
	}

	public Cookie getCookieByName(String name) {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

		// Iterate to find cookie by its name
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}

		return null;
	}

	private static Throwable getExceptionType(Throwable e,
			Class<? extends Throwable> exceptionType) {
		if (exceptionType.isAssignableFrom(e.getClass())) {
			return e;
		} else if (e.getCause() != null) {
			return getExceptionType(e.getCause(), exceptionType);
		} else {
			return null;
		}
	}
}
