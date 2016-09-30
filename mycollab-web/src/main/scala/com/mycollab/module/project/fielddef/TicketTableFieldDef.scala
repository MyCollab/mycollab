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
import com.mycollab.module.project.i18n.{MilestoneI18nEnum, TaskI18nEnum, TimeTrackingI18nEnum}
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
  * @author MyCollab Ltd
  * @since 5.4.3
  */
object TicketTableFieldDef {
  val name = new TableViewField(GenericI18Enum.FORM_NAME, "name", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val description = new TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val status = new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val priority = new TableViewField(GenericI18Enum.FORM_PRIORITY, "priority", WebUIConstants.TABLE_M_LABEL_WIDTH)
  val startdate = new TableViewField(GenericI18Enum.FORM_START_DATE, "startDate", WebUIConstants.TABLE_DATE_WIDTH)
  val enddate = new TableViewField(GenericI18Enum.FORM_END_DATE, "endDate", WebUIConstants.TABLE_DATE_WIDTH)
  val duedate = new TableViewField(GenericI18Enum.FORM_DUE_DATE, "dueDate", WebUIConstants.TABLE_DATE_WIDTH)
  val logUser = new TableViewField(TaskI18nEnum.FORM_LOG_BY, "createdUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val assignee = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val milestoneName = new TableViewField(MilestoneI18nEnum.SINGLE, "milestoneName", WebUIConstants.TABLE_X_LABEL_WIDTH)
  val billableHours = new TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "billableHours", WebUIConstants.TABLE_M_LABEL_WIDTH)
  val nonBillableHours = new TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "nonBillableHours", WebUIConstants.TABLE_M_LABEL_WIDTH)
}
