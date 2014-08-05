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

import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.TimeLogComp;
import com.esofthead.mycollab.module.project.ui.components.TimeLogEditWindow;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class BugTimeLogSheet extends TimeLogComp<SimpleBug> {

	@Override
	protected boolean hasEditPermission() {
		return CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS);
	}

	@Override
	protected Double getTotalBillableHours(SimpleBug bean) {
		ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		criteria.setType(new StringSearchField(ProjectTypeConstants.BUG));
		criteria.setTypeId(new NumberSearchField(bean.getId()));
		criteria.setIsBillable(new BooleanSearchField(true));
		return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
	}

	@Override
	protected Double getTotalNonBillableHours(SimpleBug bean) {
		ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
		criteria.setProjectId(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
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
	protected void showEditTimeWindow(SimpleBug bean) {
		UI.getCurrent().addWindow(new BugTimeLogEditWindow(bean));

	}

	private class BugTimeLogEditWindow extends TimeLogEditWindow<SimpleBug> {
		public BugTimeLogEditWindow(SimpleBug bean) {
			super(bean);
			this.setModal(true);
			this.setCaption(AppContext
					.getMessage(TimeTrackingI18nEnum.DIALOG_LOG_TIME_ENTRY_TITLE));
			this.setModal(true);
			this.addCloseListener(new CloseListener() {

				@Override
				public void windowClose(CloseEvent e) {
					BugTimeLogSheet.this
							.displayTime(BugTimeLogEditWindow.this.bean);
				}
			});
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

			itemTimeLoggingService.saveWithSession(item,
					AppContext.getUsername());

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
