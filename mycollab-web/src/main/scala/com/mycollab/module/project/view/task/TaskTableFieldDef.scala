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
package com.mycollab.module.project.view.task

import com.mycollab.module.project.i18n.TaskI18nEnum
import com.mycollab.vaadin.web.ui.UIConstants
import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.project.i18n.{TaskI18nEnum, TimeTrackingI18nEnum}

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
object TaskTableFieldDef {
  val id = new TableViewField(null, "id", UIConstants.TABLE_CONTROL_WIDTH)
  val taskname = new TableViewField(GenericI18Enum.FORM_NAME, "taskname", UIConstants.TABLE_X_LABEL_WIDTH)
  val note = new TableViewField(TaskI18nEnum.FORM_NOTES, "notes", UIConstants.TABLE_EX_LABEL_WIDTH)
  val originalEstimate = new TableViewField(TaskI18nEnum.FORM_ORIGINAL_ESTIMATE, "originalestimate", UIConstants.TABLE_M_LABEL_WIDTH)
  val remainEstimate = new TableViewField(TaskI18nEnum.FORM_REMAIN_ESTIMATE, "remainestimate", UIConstants.TABLE_M_LABEL_WIDTH)
  val isEstimate = new TableViewField(TaskI18nEnum.FORM_IS_ESTIMATED, "isestimated", UIConstants.TABLE_M_LABEL_WIDTH)
  val status = new TableViewField(GenericI18Enum.FORM_STATUS, "status", UIConstants.TABLE_X_LABEL_WIDTH)
  val priority = new TableViewField(TaskI18nEnum.FORM_PRIORITY, "priority", UIConstants.TABLE_M_LABEL_WIDTH)
  val startdate = new TableViewField(GenericI18Enum.FORM_START_DATE, "startdate", UIConstants.TABLE_DATE_WIDTH)
  val enddate = new TableViewField(GenericI18Enum.FORM_END_DATE, "enddate", UIConstants.TABLE_DATE_WIDTH)
  val duedate = new TableViewField(GenericI18Enum.FORM_DUE_DATE, "deadline", UIConstants.TABLE_DATE_WIDTH)
  val percentagecomplete = new TableViewField(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE, "percentagecomplete", UIConstants.TABLE_S_LABEL_WIDTH)
  val logUser = new TableViewField(TaskI18nEnum.FORM_LOG_BY, "logByFullName", UIConstants.TABLE_X_LABEL_WIDTH)
  val assignee = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
  val milestoneName = new TableViewField(TaskI18nEnum.FORM_PHASE, "milestoneName", UIConstants.TABLE_X_LABEL_WIDTH)
  val billableHours = new TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "billableHours", UIConstants.TABLE_M_LABEL_WIDTH)
  val nonBillableHours = new TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "nonBillableHours", UIConstants.TABLE_M_LABEL_WIDTH)
}
