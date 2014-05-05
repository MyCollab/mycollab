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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.TaskGroupHistoryFieldFormat;
import com.esofthead.mycollab.module.user.ui.components.UserHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class TaskHistoryList extends HistoryLogComponent {
	private static final long serialVersionUID = 1L;

	public TaskHistoryList() {
		super(ModuleNameConstants.PRJ, ProjectTypeConstants.TASK);
		this.addStyleName("activity-panel");

		this.generateFieldDisplayHandler("taskname", "Task Name");
		this.generateFieldDisplayHandler("startdate", "Start Date",
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("enddate", "End Date",
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("actualstartdate",
				"Actual Start Date", HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("actualenddate", "Actual End Date",
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("deadline", "Deadline",
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("isestimated", "Is Estimated");
		this.generateFieldDisplayHandler("assignuser", AppContext
				.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new UserHistoryFieldFormat());
		this.generateFieldDisplayHandler("tasklistid", AppContext
				.getMessage(TaskI18nEnum.FORM_TASKGROUP_FIELD),
				new TaskGroupHistoryFieldFormat());
		this.generateFieldDisplayHandler("percentagecomplete", "Complete(%)");
		this.generateFieldDisplayHandler("notes", "Notes");
	}

}
