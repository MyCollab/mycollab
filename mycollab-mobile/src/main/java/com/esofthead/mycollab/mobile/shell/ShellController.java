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
package com.esofthead.mycollab.mobile.shell;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.CrmEvent;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent.GotoMainPage;
import com.esofthead.mycollab.mobile.shell.ui.MainViewPresenter;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.vaadin.addon.touchkit.ui.NavigationManager;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class ShellController implements IController {
	private static final long serialVersionUID = 1L;

	final private NavigationManager mainNav;

	public ShellController(NavigationManager navigationManager) {
		this.mainNav = navigationManager;
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
						MainViewPresenter presenter = PresenterResolver
								.getPresenter(MainViewPresenter.class);
						presenter.go(mainNav, null);
					}

				});
		EventBus.getInstance().addListener(
				new ApplicationEventListener<ShellEvent.GotoCrmModule>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return ShellEvent.GotoCrmModule.class;
					}

					@Override
					public void handle(ShellEvent.GotoCrmModule event) {
						EventBus.getInstance().fireEvent(
								new CrmEvent.GotoHome(this, event.getData()));
					}
				});
	}
}
