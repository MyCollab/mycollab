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

import com.mycollab.module.project.domain.Project
import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.project.i18n.ProjectI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
  * @author MyCollab Ltd
  * @since 5.2.12
  */
object ProjectTableFieldDef {
  val selected = new TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH);
  val projectName = new TableViewField(GenericI18Enum.FORM_NAME, Project.Field.name.name(), WebUIConstants.TABLE_X_LABEL_WIDTH)
  val lead = new TableViewField(ProjectI18nEnum.FORM_LEADER, Project.Field.lead.name(), WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val client = new TableViewField(ProjectI18nEnum.FORM_ACCOUNT_NAME, Project.Field.accountid.name(), WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val createdDate = new TableViewField(GenericI18Enum.FORM_CREATED_TIME, Project.Field.createdtime.name(), WebUIConstants.TABLE_DATE_WIDTH)
  val homePage = new TableViewField(ProjectI18nEnum.FORM_HOME_PAGE, Project.Field.homepage.name(), WebUIConstants.TABLE_EX_LABEL_WIDTH)
  val status = new TableViewField(GenericI18Enum.FORM_STATUS, Project.Field.projectstatus.name(), WebUIConstants.TABLE_S_LABEL_WIDTH)
  val startDate = new TableViewField(GenericI18Enum.FORM_START_DATE, Project.Field.planstartdate.name(), WebUIConstants.TABLE_DATE_WIDTH)
  val endDate = new TableViewField(GenericI18Enum.FORM_END_DATE, Project.Field.planenddate.name(), WebUIConstants.TABLE_DATE_WIDTH)
  val rate = new TableViewField(ProjectI18nEnum.FORM_BILLING_RATE, Project.Field.defaultbillingrate.name(), WebUIConstants.TABLE_M_LABEL_WIDTH)
  val overtimeRate = new TableViewField(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE, Project.Field.defaultovertimebillingrate.name(), WebUIConstants.TABLE_M_LABEL_WIDTH)
  val budget = new TableViewField(ProjectI18nEnum.FORM_TARGET_BUDGET, Project.Field.targetbudget.name(), WebUIConstants.TABLE_M_LABEL_WIDTH)
  val actualBudget = new TableViewField(ProjectI18nEnum.FORM_ACTUAL_BUDGET, Project.Field.targetbudget.name(), WebUIConstants
    .TABLE_M_LABEL_WIDTH)
}
