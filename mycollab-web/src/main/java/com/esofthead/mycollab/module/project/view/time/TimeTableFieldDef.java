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
package com.esofthead.mycollab.module.project.view.time;

import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class TimeTableFieldDef {
	public static TableViewField id = new TableViewField(null, "id", 60);

	public static TableViewField summary = new TableViewField(
			TimeTrackingI18nEnum.FORM_SUMMARY, "summary",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField logUser = new TableViewField(
			TimeTrackingI18nEnum.LOG_USER, "logUserFullName",
			UIConstants.TABLE_X_LABEL_WIDTH);

	public static TableViewField logValue = new TableViewField(
			TimeTrackingI18nEnum.LOG_VALUE, "logvalue",
			UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField billable = new TableViewField(
			TimeTrackingI18nEnum.FORM_IS_BILLABLE, "isbillable",
			UIConstants.TABLE_S_LABEL_WIDTH);

	public static TableViewField logForDate = new TableViewField(
			TimeTrackingI18nEnum.LOG_FOR_DATE, "logforday",
			UIConstants.TABLE_DATE_TIME_WIDTH);

	public static TableViewField project = new TableViewField(
			TimeTrackingI18nEnum.FORM_PROJECT, "projectName",
			UIConstants.TABLE_X_LABEL_WIDTH);
}
