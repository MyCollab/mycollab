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
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.project.i18n.{BugI18nEnum, MilestoneI18nEnum, TimeTrackingI18nEnum}
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object BugTableFieldDef {
  val action = new TableViewField(null, "id", WebUIConstants.TABLE_CONTROL_WIDTH)
  val summary = new TableViewField(BugI18nEnum.FORM_SUMMARY, "name", WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val description = new TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val environment = new TableViewField(BugI18nEnum.FORM_ENVIRONMENT, "environment", WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val status = new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_M_LABEL_WIDTH)
  val severity = new TableViewField(BugI18nEnum.FORM_SEVERITY, "severity", WebUIConstants.TABLE_M_LABEL_WIDTH)
  val startDate = new TableViewField(GenericI18Enum.FORM_START_DATE, BugWithBLOBs.Field.startdate.name(), WebUIConstants.TABLE_DATE_WIDTH)
  val endDate = new TableViewField(GenericI18Enum.FORM_END_DATE, BugWithBLOBs.Field.enddate.name(), WebUIConstants.TABLE_DATE_WIDTH)
  val dueDate = new TableViewField(GenericI18Enum.FORM_DUE_DATE, "duedate", WebUIConstants.TABLE_DATE_WIDTH)
  val logBy = new TableViewField(BugI18nEnum.FORM_LOG_BY, "loguserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val priority = new TableViewField(GenericI18Enum.FORM_PRIORITY, "priority", WebUIConstants.TABLE_S_LABEL_WIDTH)
  val resolution = new TableViewField(BugI18nEnum.FORM_RESOLUTION, "resolution", WebUIConstants.TABLE_M_LABEL_WIDTH)
  val createdTime = new TableViewField(GenericI18Enum.FORM_CREATED_TIME, "createdtime", WebUIConstants.TABLE_DATE_TIME_WIDTH)
  val assignUser = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignuserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val milestoneName = new TableViewField(MilestoneI18nEnum.SINGLE, "milestoneName", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val billableHours = new TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "billableHours", WebUIConstants.TABLE_M_LABEL_WIDTH)
  val nonBillableHours = new TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "nonBillableHours", WebUIConstants.TABLE_M_LABEL_WIDTH)
}
