/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.project.domain.Project
import com.mycollab.module.project.i18n.ProjectI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectTableFieldDef {
    @JvmField
    val selected = TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH);

    @JvmField
    val projectName = TableViewField(GenericI18Enum.FORM_NAME, Project.Field.name.name, WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val lead = TableViewField(ProjectI18nEnum.FORM_LEADER, Project.Field.memlead.name, WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val client = TableViewField(ProjectI18nEnum.FORM_ACCOUNT_NAME, Project.Field.accountid.name, WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val createdDate = TableViewField(GenericI18Enum.FORM_CREATED_TIME, Project.Field.createdtime.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val homePage = TableViewField(ProjectI18nEnum.FORM_HOME_PAGE, Project.Field.homepage.name, WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val status = TableViewField(GenericI18Enum.FORM_STATUS, Project.Field.projectstatus.name, WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val startDate = TableViewField(GenericI18Enum.FORM_START_DATE, Project.Field.planstartdate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val endDate = TableViewField(GenericI18Enum.FORM_END_DATE, Project.Field.planenddate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val rate = TableViewField(ProjectI18nEnum.FORM_BILLING_RATE, Project.Field.defaultbillingrate.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val overtimeRate = TableViewField(ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE, Project.Field.defaultovertimebillingrate.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val budget = TableViewField(ProjectI18nEnum.FORM_TARGET_BUDGET, Project.Field.targetbudget.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val actualBudget = TableViewField(ProjectI18nEnum.FORM_ACTUAL_BUDGET, Project.Field.targetbudget.name, WebUIConstants.TABLE_M_LABEL_WIDTH)
}