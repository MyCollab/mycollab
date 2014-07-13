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
package com.esofthead.mycollab.module.project.view.standup;

import java.util.Date;
import java.util.GregorianCalendar;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.service.StandupReportService;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.StandupScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class StandupUrlResolver extends ProjectUrlResolver {
	public StandupUrlResolver() {
		this.addSubResolver("list", new ListUrlResolver());
		this.addSubResolver("add", new PreviewUrlResolver());
	}

	private static class ListUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			String[] tokens = decodeUrl.split("/");
			int projectId = Integer.parseInt(tokens[0]);

			StandupReportSearchCriteria standupSearchCriteria = new StandupReportSearchCriteria();
			standupSearchCriteria
					.setProjectId(new NumberSearchField(projectId));

			if (tokens.length > 1) {
				Date date = AppContext.parseDate(tokens[1]);
				standupSearchCriteria.setOnDate(new DateSearchField(
						SearchField.AND, date));
			} else {
				standupSearchCriteria.setOnDate(new DateSearchField(
						SearchField.AND, new GregorianCalendar().getTime()));
			}

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new StandupScreenData.Search(standupSearchCriteria));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}

	private static class PreviewUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			String decodeUrl = UrlEncodeDecoder.decode(params[0]);
			String[] tokens = decodeUrl.split("/");

			int projectId = Integer.parseInt(tokens[0]);
			Date onDate = AppContext.parseDate(tokens[2]);

			StandupReportService reportService = ApplicationContextUtil
					.getSpringBean(StandupReportService.class);
			SimpleStandupReport report = reportService
					.findStandupReportByDateUser(projectId,
							AppContext.getUsername(), onDate,
							AppContext.getAccountId());
			if (report == null) {
				report = new SimpleStandupReport();
			}

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new StandupScreenData.Add(report));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}
}
