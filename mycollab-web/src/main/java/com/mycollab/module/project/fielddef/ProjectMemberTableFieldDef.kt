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

import com.mycollab.common.GridFieldMeta
import com.mycollab.module.project.domain.ProjectMember
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum
import com.mycollab.module.project.i18n.ProjectI18nEnum.*
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum.FORM_ROLE
import com.mycollab.module.project.i18n.ProjectMemberI18nEnum.FORM_USER
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectMemberTableFieldDef {
    @JvmField
    val memberName = GridFieldMeta(FORM_USER, ProjectMember.Field.username.name, WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val billingRate = GridFieldMeta(FORM_BILLING_RATE, ProjectMember.Field.billingrate.name, WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val overtimeRate = GridFieldMeta(FORM_OVERTIME_BILLING_RATE, ProjectMember.Field.overtimebillingrate.name,
            WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val roleName = GridFieldMeta(FORM_ROLE, ProjectMember.Field.projectroleid.name, WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val projectName = GridFieldMeta(SINGLE, SimpleProjectMember.Field.projectName.name, WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val totalBillableLogTime = GridFieldMeta(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, SimpleProjectMember.Field.totalBillableLogTime.name,
            WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val totalNonBillableLogTime = GridFieldMeta(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, SimpleProjectMember.Field.totalNonBillableLogTime.name,
            WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val numOpenTasks = GridFieldMeta(ProjectCommonI18nEnum.OPT_OPEN_TASKS, SimpleProjectMember.Field.numOpenTasks.name,
            WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val numOpenBugs = GridFieldMeta(ProjectCommonI18nEnum.OPT_OPEN_BUGS, SimpleProjectMember.Field.numOpenBugs.name,
            WebUIConstants.TABLE_X_LABEL_WIDTH)
}