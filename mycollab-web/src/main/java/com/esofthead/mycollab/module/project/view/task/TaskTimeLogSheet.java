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
package com.esofthead.mycollab.module.project.view.task;

import java.util.GregorianCalendar;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.ui.components.TimeLogCompOld;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskTimeLogSheet extends TimeLogCompOld<SimpleTask> {
	private static final long serialVersionUID = 1L;

	protected TaskTimeLogSheet(SimpleTask bean) {
		super(bean);
	}

	@Override
	protected void saveTimeInvest() {
		ItemTimeLogging item = new ItemTimeLogging();
		item.setLoguser(AppContext.getUsername());
		item.setLogvalue(getInvestValue());
		item.setTypeid(bean.getId());
		item.setType(ProjectTypeConstants.TASK);
		item.setSaccountid(AppContext.getAccountId());
		item.setProjectid(CurrentProjectVariables.getProjectId());
		item.setLogforday(new GregorianCalendar().getTime());
		item.setIsbillable(isBillableHours());

		itemTimeLoggingService.saveWithSession(item, AppContext.getUsername());
	}

	@Override
	protected void updateTimeRemain() {
		ProjectTaskService bugService = ApplicationContextUtil
				.getSpringBean(ProjectTaskService.class);
		bean.setRemainestimate(getUpdateRemainTime());
		bugService.updateWithSession(bean, AppContext.getUsername());
	}

	@Override
	protected ItemTimeLoggingSearchCriteria getItemSearchCriteria() {
		ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
		searchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		searchCriteria
				.setType(new StringSearchField(ProjectTypeConstants.TASK));
		searchCriteria.setTypeId(new NumberSearchField(bean.getId()));
		return searchCriteria;
	}

	@Override
	protected double getEstimateRemainTime() {
		if (bean.getRemainestimate() != null) {
			return bean.getRemainestimate();
		}
		return -1;
	}

	@Override
	protected boolean isEnableAdd() {
		return CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS);
	}

}
