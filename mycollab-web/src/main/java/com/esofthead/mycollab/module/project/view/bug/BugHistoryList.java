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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.MilestoneHistoryFieldFormat;
import com.esofthead.mycollab.module.project.ui.components.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
public class BugHistoryList extends HistoryLogComponent {

	public BugHistoryList() {
		super(ModuleNameConstants.PRJ, ProjectTypeConstants.BUG);
		this.addStyleName("activity-panel");
		this.setMargin(true);

		this.generateFieldDisplayHandler("description", "Description");
		this.generateFieldDisplayHandler("environment", "Environment");
		this.generateFieldDisplayHandler("summary", "Summary");
		this.generateFieldDisplayHandler("detail", "Detail");
		this.generateFieldDisplayHandler("status", "Status");
		this.generateFieldDisplayHandler("priority", "Priority");
		this.generateFieldDisplayHandler("severity", "Severity");
		this.generateFieldDisplayHandler("resolution", "Resolution");
		this.generateFieldDisplayHandler("estimateremaintime",
				"Estimated Remain Time");
		this.generateFieldDisplayHandler("duedate", "Due Date",
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("createdTime", "Created Time",
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("loguserFullName", "Logged by",
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("assignuser", LocalizationHelper
				.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("milestoneid",
				LocalizationHelper.getMessage(TaskI18nEnum.FORM_PHASE_FIELD),
				new MilestoneHistoryFieldFormat());
	}
}
