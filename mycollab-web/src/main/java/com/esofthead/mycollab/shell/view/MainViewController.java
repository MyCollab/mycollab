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

import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.module.crm.view.CrmModulePresenter;
import com.esofthead.mycollab.module.crm.view.CrmModuleScreenData;
import com.esofthead.mycollab.module.file.view.FileModuleScreenData;
import com.esofthead.mycollab.module.file.view.IFileModulePresenter;
import com.esofthead.mycollab.module.project.view.ProjectModulePresenter;
import com.esofthead.mycollab.module.project.view.parameters.ProjectModuleScreenData;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountModulePresenter;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountModuleScreenData;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.mvp.AbstractController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class MainViewController extends AbstractController {
	private static final long serialVersionUID = 1L;

	private MainView container;

	public MainViewController(MainView view) {
		this.container = view;
		bind();
	}

	private void bind() {
		this.register(new ApplicationEventListener<ShellEvent.GotoCrmModule>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ShellEvent.GotoCrmModule event) {
				CrmModulePresenter crmModulePresenter = PresenterResolver
						.getPresenter(CrmModulePresenter.class);
				CrmModuleScreenData.GotoModule screenData = new CrmModuleScreenData.GotoModule(
						(String[]) event.getData());
				crmModulePresenter.go(container, screenData);
			}
		});

		this.register(new ApplicationEventListener<ShellEvent.GotoProjectModule>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ShellEvent.GotoProjectModule event) {
				ProjectModulePresenter prjPresenter = PresenterResolver
						.getPresenter(ProjectModulePresenter.class);
				ProjectModuleScreenData.GotoModule screenData = new ProjectModuleScreenData.GotoModule(
						(String[]) event.getData());
				prjPresenter.go(container, screenData);
			}
		});

		this.register(new ApplicationEventListener<ShellEvent.GotoUserAccountModule>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ShellEvent.GotoUserAccountModule event) {
				AccountModulePresenter presenter = PresenterResolver
						.getPresenter(AccountModulePresenter.class);
				presenter.go(container, new AccountModuleScreenData.GotoModule(
						(String[]) event.getData()));
			}
		});

		this.register(new ApplicationEventListener<ShellEvent.GotoFileModule>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ShellEvent.GotoFileModule event) {
				IFileModulePresenter fileModulePresenter = PresenterResolver
						.getPresenter(IFileModulePresenter.class);
				FileModuleScreenData.GotoModule screenData = new FileModuleScreenData.GotoModule(
						(String[]) event.getData());
				fileModulePresenter.go(container, screenData);
			}
		});
	}
}
