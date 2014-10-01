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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.VersionScreenData;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class VersionUrlResolver extends ProjectUrlResolver {
	public VersionUrlResolver() {
		this.addSubResolver("list", new ListUrlResolver());
		this.addSubResolver("add", new AddUrlResolver());
		this.addSubResolver("edit", new EditUrlResolver());
		this.addSubResolver("preview", new PreviewUrlResolver());
	}

	private static class ListUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int projectId = new UrlTokenizer(params[0]).getInt();

			VersionSearchCriteria versionSearchCriteria = new VersionSearchCriteria();
			versionSearchCriteria
					.setProjectId(new NumberSearchField(projectId));

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new VersionScreenData.Search(versionSearchCriteria));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class PreviewUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			UrlTokenizer token = new UrlTokenizer(params[0]);

			int projectId = token.getInt();
			int versionId = token.getInt();
			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new VersionScreenData.Read(versionId));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class EditUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			UrlTokenizer token = new UrlTokenizer(params[0]);

			int projectId = token.getInt();
			int versionId = token.getInt();

			VersionService versionService = ApplicationContextUtil
					.getSpringBean(VersionService.class);
			SimpleVersion version = versionService.findById(versionId,
					AppContext.getAccountId());
			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new VersionScreenData.Edit(version));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class AddUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int projectId = new UrlTokenizer(params[0]).getInt();

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new VersionScreenData.Add(new Version()));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}
}
