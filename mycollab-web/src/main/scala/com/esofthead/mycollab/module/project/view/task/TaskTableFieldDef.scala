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
package com.esofthead.mycollab.module.project.view.task

import com.esofthead.mycollab.common.TableViewField
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.module.project.i18n.{TimeTrackingI18nEnum, TaskI18nEnum}
import com.esofthead.mycollab.vaadin.web.ui.UIConstants

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
object TaskTableFieldDef {
    val id: TableViewField = new TableViewField(null, "id", UIConstants.TABLE_CONTROL_WIDTH)
    val taskname: TableViewField = new TableViewField(TaskI18nEnum.FORM_TASK_NAME, "taskname", UIConstants.TABLE_X_LABEL_WIDTH)
    val status: TableViewField = new TableViewField(TaskI18nEnum.FORM_STATUS, "status", UIConstants.TABLE_X_LABEL_WIDTH)
    val startdate: TableViewField = new TableViewField(TaskI18nEnum.FORM_START_DATE, "startdate", UIConstants.TABLE_DATE_WIDTH)
    val duedate: TableViewField = new TableViewField(TaskI18nEnum.FORM_DEADLINE, "deadline", UIConstants.TABLE_DATE_WIDTH)
    val percentagecomplete: TableViewField = new TableViewField(TaskI18nEnum.FORM_PERCENTAGE_COMPLETE, "percentagecomplete", UIConstants.TABLE_S_LABEL_WIDTH)
    val assignee: TableViewField = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignUserFullName", UIConstants.TABLE_X_LABEL_WIDTH)
    val billableHours: TableViewField = new TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "billableHours",
        UIConstants.TABLE_M_LABEL_WIDTH)
    val nonBillableHours: TableViewField = new TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "nonBillableHours",
        UIConstants.TABLE_M_LABEL_WIDTH)
}
