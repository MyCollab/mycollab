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

import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.view.ForgotPasswordPresenter;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.shell.events.ShellEvent.GotoMainPage;
import com.esofthead.mycollab.shell.events.ShellEvent.LogOut;
import com.esofthead.mycollab.shell.view.MainView;
import com.esofthead.mycollab.shell.view.MainViewPresenter;
import com.esofthead.mycollab.shell.view.MainWindowContainer;
import com.esofthead.mycollab.vaadin.MyCollabUI;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.web.DesktopApplication;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ShellController implements IController {

	private static final long serialVersionUID = 1L;

	private final MainWindowContainer container;
	private EventBus eventBus;

	public ShellController(MainWindowContainer container) {
		this.container = container;
		this.eventBus = EventBusFactory.getInstance();
		bind();
	}

	private void bind() {
		eventBus.register(new ApplicationEventListener<ShellEvent.GotoMainPage>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
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

		eventBus.register(new ApplicationEventListener<ShellEvent.LogOut>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(LogOut event) {
				((DesktopApplication) MyCollabUI.getInstance())
						.redirectToLoginView();
			}
		});

		eventBus.register(new ApplicationEventListener<ShellEvent.GotoForgotPasswordPage>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ShellEvent.GotoForgotPasswordPage event) {
				ForgotPasswordPresenter presenter = PresenterResolver
						.getPresenter(ForgotPasswordPresenter.class);
				presenter.go(container, null);
			}

		});
	}
}
