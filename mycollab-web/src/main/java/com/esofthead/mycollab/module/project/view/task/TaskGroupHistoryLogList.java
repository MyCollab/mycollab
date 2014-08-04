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
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.ui.format.MilestoneHistoryFieldFormat;
import com.esofthead.mycollab.module.project.ui.format.ProjectMemberHistoryFieldFormat;
import com.esofthead.mycollab.utils.FieldGroupFomatter;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class TaskGroupHistoryLogList extends HistoryLogComponent {
	private static final long serialVersionUID = 1L;

	public static final FieldGroupFomatter tasklistFormatter;

	static {
		tasklistFormatter = new FieldGroupFomatter();

		tasklistFormatter.generateFieldDisplayHandler("name",
				TaskGroupI18nEnum.FORM_NAME_FIELD);
		tasklistFormatter.generateFieldDisplayHandler("description",
				TaskGroupI18nEnum.FORM_DESCRIPTION_FIELD);
		tasklistFormatter.generateFieldDisplayHandler("owner",
				GenericI18Enum.FORM_ASSIGNEE,
				new ProjectMemberHistoryFieldFormat());
		tasklistFormatter.generateFieldDisplayHandler("milestoneid",
				TaskGroupI18nEnum.FORM_PHASE_FIELD,
				new MilestoneHistoryFieldFormat());
		tasklistFormatter.generateFieldDisplayHandler("percentageComplete",
				TaskGroupI18nEnum.FORM_PROGRESS_FIELD);
		tasklistFormatter.generateFieldDisplayHandler("numOpenTasks",
				TaskGroupI18nEnum.FORM_OPEN_TASKS_FIELD);
	}

	public TaskGroupHistoryLogList() {
		super(ModuleNameConstants.PRJ, ProjectTypeConstants.TASK_LIST);
		this.addStyleName("activity-panel");
	}

	@Override
	protected FieldGroupFomatter buildFormatter() {
		return tasklistFormatter;
	}
}
