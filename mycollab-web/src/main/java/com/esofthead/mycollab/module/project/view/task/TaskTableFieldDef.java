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

import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TaskTableFieldDef {
	public static TableViewField id = new TableViewField("", "id",
			UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField taskname = new TableViewField(
			AppContext.getMessage(TaskI18nEnum.TABLE_TASK_NAME_HEADER),
			"taskname", UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField startdate = new TableViewField(
			AppContext.getMessage(TaskI18nEnum.TABLE_START_DATE_HEADER),
			"startdate", UIConstants.TABLE_DATE_WIDTH);

	public static TableViewField duedate = new TableViewField(
			AppContext.getMessage(TaskI18nEnum.TABLE_DUE_DATE_HEADER),
			"deadline", UIConstants.TABLE_DATE_WIDTH);

	public static TableViewField percentagecomplete = new TableViewField(
			AppContext
					.getMessage(TaskI18nEnum.TABLE_PER_COMPLETE_HEADER),
			"percentagecomplete", UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField assignee = new TableViewField("Assignee",
			"assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH);
}
