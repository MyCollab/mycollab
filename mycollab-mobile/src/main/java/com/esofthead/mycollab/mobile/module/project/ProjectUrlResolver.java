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
package com.esofthead.mycollab.mobile.module.project;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.module.project.view.message.MessageUrlResolver;
import com.esofthead.mycollab.mobile.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.mobile.shell.ModuleHelper;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.UrlResolver;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class ProjectUrlResolver extends UrlResolver {

	public UrlResolver build() {
		this.addSubResolver("dashboard", new DashboardUrlResolver());
		this.addSubResolver("message", new MessageUrlResolver());
		return this;
	}

	@Override
	public void handle(String... params) {
		if (!ModuleHelper.isCurrentProjectModule()) {
			EventBusFactory.getInstance().post(
					new ShellEvent.GotoProjectModule(this, null));
		} else {
			super.handle(params);
		}
	}

	@Override
	protected void defaultPageErrorHandler() {
		EventBusFactory.getInstance().post(
				new ShellEvent.GotoProjectModule(this, null));
	}

	@Override
	protected void handlePage(String... params) {
		super.handlePage(params);
		EventBusFactory.getInstance().post(
				new ProjectEvent.GotoProjectList(this, null));
	}

	public static class DashboardUrlResolver extends ProjectUrlResolver {

		@Override
		protected void handlePage(String... params) {
			if (params == null || params.length == 0) {
				EventBusFactory.getInstance().post(
						new ShellEvent.GotoProjectModule(this, null));
			} else {
				String decodeUrl = UrlEncodeDecoder.decode(params[0]);
				int projectId = Integer.parseInt(decodeUrl);
				PageActionChain chain = new PageActionChain(
						new ProjectScreenData.Goto(projectId));
				EventBusFactory.getInstance().post(
						new ProjectEvent.GotoMyProject(this, chain));
			}
		}
	}

}
