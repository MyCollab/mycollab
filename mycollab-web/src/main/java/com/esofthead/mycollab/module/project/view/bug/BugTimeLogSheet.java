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

import java.util.GregorianCalendar;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.ui.components.CompTimeLogSheet;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class BugTimeLogSheet extends CompTimeLogSheet<SimpleBug> {

	protected BugTimeLogSheet(SimpleBug bean) {
		super(bean);
	}

	@Override
	protected void saveTimeInvest() {
		ItemTimeLogging item = new ItemTimeLogging();
		item.setLoguser(AppContext.getUsername());
		item.setLogvalue(getInvestValue());
		item.setTypeid(bean.getId());
		item.setType(ProjectTypeConstants.BUG);
		item.setSaccountid(AppContext.getAccountId());
		item.setProjectid(CurrentProjectVariables.getProjectId());
		item.setLogforday(new GregorianCalendar().getTime());
		item.setIsbillable(isBillableHours());

		itemTimeLoggingService.saveWithSession(item, AppContext.getUsername());

	}

	@Override
	protected void updateTimeRemain() {
		BugService bugService = ApplicationContextUtil
				.getSpringBean(BugService.class);
		bean.setEstimateremaintime(getUpdateRemainTime());
		bugService.updateWithSession(bean, AppContext.getUsername());
	}

	@Override
	protected ItemTimeLoggingSearchCriteria getItemSearchCriteria() {
		ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
		searchCriteria.setProjectId(new NumberSearchField(
				CurrentProjectVariables.getProjectId()));
		searchCriteria.setType(new StringSearchField(ProjectTypeConstants.BUG));
		searchCriteria.setTypeId(new NumberSearchField(bean.getId()));
		return searchCriteria;
	}

	@Override
	protected double getEstimateRemainTime() {
		if (bean.getEstimateremaintime() != null) {
			return bean.getEstimateremaintime();
		}
		return -1;
	}

	@Override
	protected boolean isEnableAdd() {
		return CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS);
	}

}
