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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import java.util.Date;

import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.TimeLogComp;
import com.esofthead.mycollab.mobile.module.project.ui.TimeLogEditView;
import com.esofthead.mycollab.mobile.shell.events.ShellEvent;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class BugTimeLogComp extends TimeLogComp<SimpleBug> {

	private static final long serialVersionUID = -7045421785222291708L;

	@Override
	protected Double getTotalBillableHours(SimpleBug bean) {
		ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
		criteria.setProjectIds(new SetSearchField<Integer>(
				CurrentProjectVariables.getProjectId()));
		criteria.setType(new StringSearchField(ProjectTypeConstants.BUG));
		criteria.setTypeId(new NumberSearchField(bean.getId()));
		criteria.setIsBillable(new BooleanSearchField(true));
		return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
	}

	@Override
	protected Double getTotalNonBillableHours(SimpleBug bean) {
		ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
		criteria.setProjectIds(new SetSearchField<Integer>(
				CurrentProjectVariables.getProjectId()));
		criteria.setType(new StringSearchField(ProjectTypeConstants.BUG));
		criteria.setTypeId(new NumberSearchField(bean.getId()));
		criteria.setIsBillable(new BooleanSearchField(false));
		return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
	}

	@Override
	protected Double getRemainedHours(SimpleBug bean) {
		if (bean.getEstimateremaintime() != null) {
			return bean.getEstimateremaintime();
		}
		return 0d;
	}

	@Override
	protected boolean hasEditPermission() {
		return CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS);
	}

	@Override
	protected void showEditTimeView(SimpleBug bean) {
		EventBusFactory.getInstance().post(
				new ShellEvent.PushView(this, new BugTimeLogView(bean)));
	}

	private class BugTimeLogView extends TimeLogEditView<SimpleBug> {

		private static final long serialVersionUID = -482636222620345326L;

		protected BugTimeLogView(SimpleBug bean) {
			super(bean);
		}

		@Override
		protected void saveTimeInvest(double spentHours, boolean isBillable,
				Date forDate) {
			ItemTimeLogging item = new ItemTimeLogging();
			item.setLoguser(AppContext.getUsername());
			item.setLogvalue(spentHours);
			item.setTypeid(bean.getId());
			item.setType(ProjectTypeConstants.BUG);
			item.setSaccountid(AppContext.getAccountId());
			item.setProjectid(CurrentProjectVariables.getProjectId());
			item.setLogforday(forDate);
			item.setIsbillable(isBillable);

			itemTimeLoggingService.saveWithSession(item,
					AppContext.getUsername());
		}

		@Override
		protected void updateTimeRemain(double newValue) {
			BugService bugService = ApplicationContextUtil
					.getSpringBean(BugService.class);
			bean.setEstimateremaintime(newValue);
			bugService.updateWithSession(bean, AppContext.getUsername());
		}

		@Override
		protected ItemTimeLoggingSearchCriteria getItemSearchCriteria() {
			ItemTimeLoggingSearchCriteria searchCriteria = new ItemTimeLoggingSearchCriteria();
			searchCriteria.setProjectIds(new SetSearchField<Integer>(
					CurrentProjectVariables.getProjectId()));
			searchCriteria.setType(new StringSearchField(
					ProjectTypeConstants.BUG));
			searchCriteria.setTypeId(new NumberSearchField(bean.getId()));
			return searchCriteria;
		}

		@Override
		protected double getEstimateRemainTime() {
			if (bean.getEstimateremaintime() != null) {
				return bean.getEstimateremaintime();
			}
			return 0;
		}

		@Override
		protected boolean isEnableAdd() {
			return CurrentProjectVariables
					.canWrite(ProjectRolePermissionCollections.BUGS);
		}

	}
}
