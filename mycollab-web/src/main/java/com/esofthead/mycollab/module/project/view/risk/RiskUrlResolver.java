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
package com.esofthead.mycollab.module.project.view.risk;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.domain.criteria.RiskSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.RiskScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class RiskUrlResolver extends ProjectUrlResolver {
	public RiskUrlResolver() {
		this.addSubResolver("list", new ListUrlResolver());
		this.addSubResolver("preview", new PreviewUrlResolver());
		this.addSubResolver("add", new AddUrlResolver());
		this.addSubResolver("edit", new EditUrlResolver());
	}

	private static class ListUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int projectId = new UrlTokenizer(params[0]).getInt();

			RiskSearchCriteria riskSearchCriteria = new RiskSearchCriteria();
			riskSearchCriteria.setProjectId(new NumberSearchField(projectId));

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new RiskScreenData.Search(riskSearchCriteria));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class PreviewUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			UrlTokenizer token = new UrlTokenizer(params[0]);

			int projectId = token.getInt();
			int riskId = token.getInt();
			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new RiskScreenData.Read(riskId));
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
					new RiskScreenData.Add(new Risk()));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class EditUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			UrlTokenizer token = new UrlTokenizer(params[0]);

			int projectId = token.getInt();
			int riskId = token.getInt();

			RiskService riskService = ApplicationContextUtil
					.getSpringBean(RiskService.class);
			SimpleRisk risk = riskService.findById(riskId,
					AppContext.getAccountId());
			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new RiskScreenData.Edit(risk));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}
}
