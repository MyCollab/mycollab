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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.DeploymentMode;
import com.esofthead.mycollab.mobile.module.crm.view.CrmModuleController;
import com.esofthead.mycollab.mobile.module.user.view.LoginPresenter;
import com.esofthead.mycollab.mobile.module.user.view.LoginView;
import com.esofthead.mycollab.mobile.shell.ShellController;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
@Theme("mycollab-mobile")
@Widgetset("com.esofthead.mycollab.widgetset.MyCollabMobileWidgetSet")
public class MobileApplication extends UI {
	private static final long serialVersionUID = 1L;

	private static final String CURRENT_APP = "mobileApp";

	private static Logger log = LoggerFactory
			.getLogger(MobileApplication.class);

	/**
	 * Context of current logged in user
	 */
	private AppContext currentContext;

	private String initialSubDomain = "1";
	private String initialUrl = "";

	public static MobileApplication getInstance() {
		return (MobileApplication) VaadinSession.getCurrent().getAttribute(
				CURRENT_APP);
	}

	@Override
	protected void init(VaadinRequest request) {
		log.debug("Init mycollab mobile application {}", this.toString());

		initialUrl = this.getPage().getUriFragment();
		VaadinSession.getCurrent().setAttribute(CURRENT_APP, this);
		currentContext = new AppContext(this);
		postSetupApp(request);
		try {
			currentContext.initDomain(initialSubDomain);
		} catch (Exception e) {
			// TODO: show content notice user there is no existing domain
			return;
		}

		final LoginPresenter presenter = PresenterResolver
				.getPresenter(LoginPresenter.class);
		LoginView loginView = presenter.initView();

		MobileNavigationManager manager = new MobileNavigationManager();
		setContent(manager);
		manager.navigateTo(loginView.getWidget());

		registerControllers(manager);

	}

	private void postSetupApp(VaadinRequest request) {
		VaadinServletRequest servletRequest = (VaadinServletRequest) request;
		if (SiteConfiguration.getDeploymentMode() == DeploymentMode.SITE) {
			initialSubDomain = servletRequest.getServerName().split("\\.")[0];
		} else {
			initialSubDomain = servletRequest.getServerName();
		}
	}

	private void registerControllers(MobileNavigationManager manager) {
		ControllerRegistry.addController(new ShellController(manager));
		ControllerRegistry.addController(new CrmModuleController(manager));
	}

	public AppContext getSessionData() {
		return currentContext;
	}

	public String getInitialUrl() {
		return initialUrl;
	}

	@Override
	public void close() {
		super.close();
		log.debug("Application is closed. Clean all resources");
		currentContext = null;
		VaadinSession.getCurrent().close();
	}

}
