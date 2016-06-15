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
package com.esofthead.mycollab.module.project.view.milestone

import com.esofthead.mycollab.common.TableViewField
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.module.project.domain.{Milestone, SimpleMilestone}
import com.esofthead.mycollab.module.project.i18n.{MilestoneI18nEnum, TimeTrackingI18nEnum}
import com.esofthead.mycollab.vaadin.web.ui.UIConstants

/**
  * @author MyCollab Ltd
  * @since 5.2.11
  */
object MilestoneTableFieldDef {
  val id = new TableViewField(GenericI18Enum.FORM_PROGRESS, Milestone.Field.id.name(), UIConstants.TABLE_M_LABEL_WIDTH)
  val milestoneName = new TableViewField(GenericI18Enum.FORM_NAME, Milestone.Field.name.name(), UIConstants.TABLE_X_LABEL_WIDTH)
  val status = new TableViewField(GenericI18Enum.FORM_STATUS, Milestone.Field.status.name(), UIConstants.TABLE_M_LABEL_WIDTH)
  val startDate = new TableViewField(GenericI18Enum.FORM_START_DATE, Milestone.Field.startdate.name(), UIConstants.TABLE_DATE_WIDTH)
  val endDate = new TableViewField(GenericI18Enum.FORM_END_DATE, Milestone.Field.enddate.name(), UIConstants.TABLE_DATE_WIDTH)
  val assignee = new TableViewField(GenericI18Enum.FORM_ASSIGNEE, SimpleMilestone.Field.ownerFullName.name(), UIConstants.TABLE_M_LABEL_WIDTH)
  val billableHours = new TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "totalBillableHours", UIConstants.TABLE_X_LABEL_WIDTH)
  val nonBillableHours = new TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "totalNonBillableHours", UIConstants.TABLE_X_LABEL_WIDTH)
}
