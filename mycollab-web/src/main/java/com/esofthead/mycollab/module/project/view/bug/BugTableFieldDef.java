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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public interface BugTableFieldDef {

	public static TableViewField selected = new TableViewField(null,
			"selected", UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField action = new TableViewField(null, "id",
			UIConstants.TABLE_CONTROL_WIDTH);

	public static TableViewField summary = new TableViewField(
			BugI18nEnum.FORM_SUMMARY, "summary",
			UIConstants.TABLE_EX_LABEL_WIDTH);

	public static TableViewField description = new TableViewField(
			BugI18nEnum.FORM_DESCRIPTION, "description",
			UIConstants.TABLE_EX_LABEL_WIDTH);

	public static TableViewField environment = new TableViewField(
			BugI18nEnum.FORM_ENVIRONMENT, "environment",
			UIConstants.TABLE_EX_LABEL_WIDTH);

	public static TableViewField status = new TableViewField(
			BugI18nEnum.FORM_STATUS, "status", UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField severity = new TableViewField(
			BugI18nEnum.FORM_SEVERITY, "severity",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField duedate = new TableViewField(
			BugI18nEnum.FORM_DUE_DATE, "duedate", UIConstants.TABLE_DATE_WIDTH);

	public static TableViewField logBy = new TableViewField(
			BugI18nEnum.FORM_LOG_BY, "loguserFullName",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField priority = new TableViewField(
			BugI18nEnum.FORM_PRIORITY, "priority",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField resolution = new TableViewField(
			BugI18nEnum.FORM_RESOLUTION, "resolution",
			UIConstants.TABLE_M_LABEL_WIDTH);

	public static TableViewField createdTime = new TableViewField(
			BugI18nEnum.FORM_CREATED_TIME, "createdtime",
			UIConstants.TABLE_DATE_TIME_WIDTH);

	public static TableViewField assignUser = new TableViewField(
			GenericI18Enum.FORM_ASSIGNEE_FIELD, "assignuserFullName",
			UIConstants.TABLE_X_LABEL_WIDTH);
}
