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
package com.mycollab.module.project.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.module.project.i18n.{ProjectI18nEnum, TimeTrackingI18nEnum}
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
  * @author MyCollab Ltd
  * @since 5.2.8
  */
object TimeTableFieldDef {
  var id = new TableViewField(null, "id", 60)
  val summary = new TableViewField(TimeTrackingI18nEnum.FORM_SUMMARY, "name", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val logUser = new TableViewField(UserI18nEnum.SINGLE, "logUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val logValue = new TableViewField(TimeTrackingI18nEnum.LOG_VALUE, "logvalue", WebUIConstants.TABLE_S_LABEL_WIDTH)
  val billable = new TableViewField(TimeTrackingI18nEnum.FORM_IS_BILLABLE, "isbillable", WebUIConstants.TABLE_S_LABEL_WIDTH)
  val overtime = new TableViewField(TimeTrackingI18nEnum.FORM_IS_OVERTIME, "isovertime", WebUIConstants.TABLE_S_LABEL_WIDTH)
  val logForDate = new TableViewField(TimeTrackingI18nEnum.LOG_FOR_DATE, "logforday", WebUIConstants.TABLE_DATE_TIME_WIDTH)
  val project = new TableViewField(ProjectI18nEnum.SINGLE, "projectName", WebUIConstants.TABLE_X_LABEL_WIDTH)
}
