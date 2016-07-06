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
package com.mycollab.module.project.view

import com.mycollab.module.project.domain.{ProjectMember, SimpleProjectMember}
import com.mycollab.module.project.i18n.ProjectI18nEnum
import com.mycollab.vaadin.web.ui.UIConstants
import com.mycollab.common.TableViewField
import com.mycollab.module.project.i18n.{ProjectCommonI18nEnum, ProjectI18nEnum, ProjectMemberI18nEnum, TimeTrackingI18nEnum}

/**
  * @author MyCollab Ltd
  * @since 5.2.12
  */
object ProjectMemberTableFieldDef {
  val memberName = new TableViewField(ProjectMemberI18nEnum.FORM_USER, ProjectMember.Field.username.name(), UIConstants.TABLE_X_LABEL_WIDTH)
  val billingRate = new TableViewField(ProjectI18nEnum.FORM_BILLING_RATE, ProjectMember.Field.billingrate.name(), UIConstants.TABLE_S_LABEL_WIDTH)
  val overtimeRate = new TableViewField(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE, ProjectMember.Field.overtimebillingrate.name(),
    UIConstants.TABLE_S_LABEL_WIDTH)
  val roleName = new TableViewField(ProjectMemberI18nEnum.FORM_ROLE, ProjectMember.Field.projectroleid.name(), UIConstants.TABLE_X_LABEL_WIDTH)
  val projectName = new TableViewField(ProjectI18nEnum.SINGLE, SimpleProjectMember.Field.projectName.name(), UIConstants.TABLE_X_LABEL_WIDTH)
  val totalBillableLogTime = new TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, SimpleProjectMember.Field.totalBillableLogTime.name(),
    UIConstants.TABLE_X_LABEL_WIDTH)
  val totalNonBillableLogTime = new TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, SimpleProjectMember.Field.totalNonBillableLogTime.name(),
    UIConstants.TABLE_X_LABEL_WIDTH)
  val numOpenTasks = new TableViewField(ProjectCommonI18nEnum.OPT_OPEN_TASKS, SimpleProjectMember.Field.numOpenTasks.name(),
    UIConstants.TABLE_X_LABEL_WIDTH)
  val numOpenBugs = new TableViewField(ProjectCommonI18nEnum.OPT_OPEN_BUGS, SimpleProjectMember.Field.numOpenBugs.name(),
    UIConstants.TABLE_X_LABEL_WIDTH)
}
