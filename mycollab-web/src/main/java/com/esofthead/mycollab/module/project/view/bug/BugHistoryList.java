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
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.localization.BugI18nEnum;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.module.project.ui.components.MilestoneHistoryFieldFormat;
import com.esofthead.mycollab.module.project.ui.components.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.AppContext;
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

		this.generateFieldDisplayHandler("description",
				AppContext.getMessage(BugI18nEnum.FORM_DESCRIPTION));
		this.generateFieldDisplayHandler("environment",
				AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT));
		this.generateFieldDisplayHandler("summary",
				AppContext.getMessage(BugI18nEnum.FORM_SUMMARY));
		this.generateFieldDisplayHandler("detail", "Detail");
		this.generateFieldDisplayHandler("status",
				AppContext.getMessage(BugI18nEnum.FORM_STATUS));
		this.generateFieldDisplayHandler("priority",
				AppContext.getMessage(BugI18nEnum.FORM_PRIORITY));
		this.generateFieldDisplayHandler("severity",
				AppContext.getMessage(BugI18nEnum.FORM_SEVERITY));
		this.generateFieldDisplayHandler("resolution",
				AppContext.getMessage(BugI18nEnum.FORM_RESOLUTION));
		this.generateFieldDisplayHandler("estimateremaintime",
				AppContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE));
		this.generateFieldDisplayHandler("duedate",
				AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE),
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("createdTime",
				AppContext.getMessage(BugI18nEnum.FORM_CREATED_TIME),
				HistoryLogComponent.DATE_FIELD);
		this.generateFieldDisplayHandler("loguserFullName",
				AppContext.getMessage(BugI18nEnum.FORM_LOG_BY),
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("assignuser",
				AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
				new ProjectMemberHistoryFieldFormat());
		this.generateFieldDisplayHandler("milestoneid",
				AppContext.getMessage(TaskI18nEnum.FORM_PHASE_FIELD),
				new MilestoneHistoryFieldFormat());
	}
}
