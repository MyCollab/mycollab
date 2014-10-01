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
package com.esofthead.mycollab.module.project.view.time;

import com.esofthead.mycollab.common.UrlTokenizer;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TimeTrackingScreenData;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TimeUrlResolver extends ProjectUrlResolver {
	public TimeUrlResolver() {
		this.addSubResolver("list", new ListUrlResolver());
	}

	private static class ListUrlResolver extends ProjectUrlResolver {
		@Override
		protected void handlePage(String... params) {
			int projectId = new UrlTokenizer(params[0]).getInt();

			ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
			searchCriteria
					.setProjectIds(new SetSearchField<Integer>(projectId));
			searchCriteria.setRangeDate(ItemTimeLoggingSearchCriteria
					.getCurrentRangeDateOfWeekSearchField());

			PageActionChain chain = new PageActionChain(
					new ProjectScreenData.Goto(projectId),
					new TimeTrackingScreenData.Search(searchCriteria));
			EventBusFactory.getInstance().post(
					new ProjectEvent.GotoMyProject(this, chain));
		}
	}
}
