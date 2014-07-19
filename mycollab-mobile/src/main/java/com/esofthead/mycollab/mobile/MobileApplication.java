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

import static com.esofthead.mycollab.common.MyCollabSession.CURRENT_APP;
import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.user.events.UserEvent;
import com.esofthead.mycollab.mobile.shell.ShellController;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.MyCollabUI;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.vaadin.addon.touchkit.extensions.LocalStorage;
import com.vaadin.addon.touchkit.extensions.LocalStorageCallback;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager.NavigationEvent.Direction;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
@Theme("mycollab-mobile")
@Widgetset("com.esofthead.mycollab.widgetset.MyCollabMobileWidgetSet")
public class MobileApplication extends MyCollabUI {
	private static final long serialVersionUID = 1L;

	public static final String LOGIN_DATA = "m_login";

	private static Logger log = LoggerFactory
			.getLogger(MobileApplication.class);

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

		final NavigationManager manager = new NavigationManager();
		manager.addNavigationListener(new NavigationManager.NavigationListener() {
			private static final long serialVersionUID = -2317588983851761998L;

			@Override
			public void navigate(NavigationEvent event) {
				NavigationManager currentNavigator = (NavigationManager) event
						.getSource();
				if (event.getDirection() == Direction.BACK) {
					Component nextComponent = currentNavigator
							.getNextComponent();
					if (nextComponent instanceof NavigationView) {
						((NavigationView) nextComponent)
								.setPreviousComponent(null);
					}
					currentNavigator.removeComponent(nextComponent);
					currentNavigator.getState().setNextComponent(null);
				}
			}
		});
		setContent(manager);

		registerControllers(manager);
		checkLocalData();
	}

	private void checkLocalData() {
		LocalStorage.detectValue(LOGIN_DATA, new LocalStorageCallback() {
			private static final long serialVersionUID = 3217947479690600476L;

			@Override
			public void onSuccess(String value) {
				if (value != null) {
					String[] loginParams = value.split("\\$");
					try {
						EventBusFactory.getInstance().post(
								new UserEvent.PlainLogin(this, new String[] {
										loginParams[0],
										PasswordEncryptHelper
												.decryptText(loginParams[1]),
										String.valueOf(false) }));
					} catch (MyCollabException exception) {
						EventBusFactory.getInstance().post(
								new ShellEvent.GotoLoginView(this, null));
					}
				} else {
					EventBusFactory.getInstance().post(
							new ShellEvent.GotoLoginView(this, null));
				}
			}

			@Override
			public void onFailure(FailureEvent error) {
				EventBusFactory.getInstance().post(
						new ShellEvent.GotoLoginView(this, null));
			}
		});
	}

	private void registerControllers(NavigationManager manager) {
		ControllerRegistry.addController(new ShellController(manager));
	}
}
