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
package com.esofthead.mycollab.shell;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.user.view.ForgotPasswordPresenter;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.events.ShellEvent.GotoMainPage;
import com.esofthead.mycollab.shell.events.ShellEvent.LogOut;
import com.esofthead.mycollab.shell.view.MainView;
import com.esofthead.mycollab.shell.view.MainViewPresenter;
import com.esofthead.mycollab.shell.view.MainWindowContainer;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.web.DesktopApplication;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ShellController implements IController {

	private static final long serialVersionUID = 1L;

	private final MainWindowContainer container;

	public ShellController(MainWindowContainer container) {
		this.container = container;
		bind();
	}

	private void bind() {
		EventBus.getInstance().addListener(
				new ApplicationEventListener<ShellEvent.GotoMainPage>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ShellEvent.GotoMainPage.class;
					}

					@Override
					public void handle(GotoMainPage event) {
						MainViewPresenter mainViewPresenter = PresenterResolver
								.getPresenter(MainViewPresenter.class);
						MainView mainView = mainViewPresenter.initView();
						((MainWindowContainer) container).setContent(mainView);

						container.setStyleName("mainView");

						mainViewPresenter.go(container, null);
					}
				});

		EventBus.getInstance().addListener(
				new ApplicationEventListener<ShellEvent.LogOut>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ShellEvent.LogOut.class;
					}

					@Override
					public void handle(LogOut event) {
						DesktopApplication.getInstance().redirectToLoginView();
					}
				});

		EventBus.getInstance()
				.addListener(
						new ApplicationEventListener<ShellEvent.GotoForgotPasswordPage>() {
							private static final long serialVersionUID = 1L;

							@Override
							public Class<? extends ApplicationEvent> getEventType() {
								return ShellEvent.GotoForgotPasswordPage.class;
							}

							@Override
							public void handle(
									ShellEvent.GotoForgotPasswordPage event) {
								ForgotPasswordPresenter presenter = PresenterResolver
										.getPresenter(ForgotPasswordPresenter.class);
								presenter.go(container, null);
							}

						});
	}
}
