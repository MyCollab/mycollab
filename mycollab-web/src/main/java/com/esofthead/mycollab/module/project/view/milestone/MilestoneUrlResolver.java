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
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.MilestoneScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class MilestoneUrlResolver extends ProjectUrlResolver {
	public MilestoneUrlResolver() {
		this.addSubResolver("list", new ListUrlResolver());
		this.addSubResolver("add", new AddUrlResolver());
		this.addSubResolver("edit", new EditUrlResolver());
		this.addSubResolver("preview", new PreviewUrlResolver());
	}

	private static class ListUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int projectId = new UrlTokenizer(params[0]).getInt();

			MilestoneSearchCriteria milestoneSearchCriteria = new MilestoneSearchCriteria();
			milestoneSearchCriteria.setProjectId(new NumberSearchField(
					projectId));

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new MilestoneScreenData.Search(milestoneSearchCriteria));
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
					new MilestoneScreenData.Add(new Milestone()));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class EditUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			UrlTokenizer token = new UrlTokenizer(params[0]);

			int projectId = token.getInt();
			int milestoneid = token.getInt();

			MilestoneService milestoneService = ApplicationContextUtil
					.getSpringBean(MilestoneService.class);
			SimpleMilestone milestone = milestoneService.findById(milestoneid,
					AppContext.getAccountId());
			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new MilestoneScreenData.Edit(milestone));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class PreviewUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			UrlTokenizer token = new UrlTokenizer(params[0]);

			int projectId = token.getInt();
			int milestoneid = token.getInt();
			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new MilestoneScreenData.Read(milestoneid));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}
}
