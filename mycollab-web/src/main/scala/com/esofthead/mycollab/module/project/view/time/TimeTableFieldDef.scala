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
package com.esofthead.mycollab.module.project.view.time

import com.esofthead.mycollab.common.TableViewField
import com.esofthead.mycollab.module.project.i18n.{ProjectI18nEnum, TimeTrackingI18nEnum}
import com.esofthead.mycollab.vaadin.web.ui.UIConstants

/**
  * @author MyCollab Ltd
  * @since 5.2.8
  */
object TimeTableFieldDef {
  var id = new TableViewField(null, "id", 60)
  val summary = new TableViewField(TimeTrackingI18nEnum.FORM_SUMMARY, "summary", UIConstants.TABLE_X_LABEL_WIDTH)
  val logUser = new TableViewField(TimeTrackingI18nEnum.LOG_USER, "logUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
  val logValue = new TableViewField(TimeTrackingI18nEnum.LOG_VALUE, "logvalue", UIConstants.TABLE_S_LABEL_WIDTH)
  val billable = new TableViewField(TimeTrackingI18nEnum.FORM_IS_BILLABLE, "isbillable", UIConstants.TABLE_S_LABEL_WIDTH)
  val overtime = new TableViewField(TimeTrackingI18nEnum.FORM_IS_OVERTIME, "isovertime", UIConstants.TABLE_S_LABEL_WIDTH)
  val logForDate = new TableViewField(TimeTrackingI18nEnum.LOG_FOR_DATE, "logforday", UIConstants.TABLE_DATE_TIME_WIDTH)
  val project = new TableViewField(ProjectI18nEnum.SINGLE, "projectName", UIConstants.TABLE_X_LABEL_WIDTH)
}
