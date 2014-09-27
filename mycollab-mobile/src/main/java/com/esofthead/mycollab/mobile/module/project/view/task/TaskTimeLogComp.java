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
package com.esofthead.mycollab.mobile.module.project.view.task;

import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.ui.TimeLogComp;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.0
 */
public class TaskTimeLogComp extends TimeLogComp<SimpleTask> {

	private static final long serialVersionUID = 8006444639083945910L;

	@Override
	protected Double getTotalBillableHours(SimpleTask bean) {
		ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
		criteria.setProjectIds(new SetSearchField<Integer>(
				CurrentProjectVariables.getProjectId()));
		criteria.setType(new StringSearchField(ProjectTypeConstants.TASK));
		criteria.setTypeId(new NumberSearchField(bean.getId()));
		criteria.setIsBillable(new BooleanSearchField(true));
		return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
	}

	@Override
	protected Double getTotalNonBillableHours(SimpleTask bean) {
		ItemTimeLoggingSearchCriteria criteria = new ItemTimeLoggingSearchCriteria();
		criteria.setProjectIds(new SetSearchField<Integer>(
				CurrentProjectVariables.getProjectId()));
		criteria.setType(new StringSearchField(ProjectTypeConstants.TASK));
		criteria.setTypeId(new NumberSearchField(bean.getId()));
		criteria.setIsBillable(new BooleanSearchField(false));
		return itemTimeLoggingService.getTotalHoursByCriteria(criteria);
	}

	@Override
	protected Double getRemainedHours(SimpleTask bean) {
		if (bean.getRemainestimate() != null) {
			return bean.getRemainestimate();
		}
		return 0d;
	}

	@Override
	protected boolean hasEditPermission() {
		return CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.TASKS);
	}

	@Override
	protected void showEditTimeView(SimpleTask bean) {
		// TODO Auto-generated method stub

	}

}
