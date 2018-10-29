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
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.project.domain.Milestone
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MilestoneTableFieldDef {
    @JvmField
    val id = GridFieldMeta(GenericI18Enum.FORM_PROGRESS, Milestone.Field.id.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val milestoneName = GridFieldMeta(GenericI18Enum.FORM_NAME, Milestone.Field.name.name, WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val status = GridFieldMeta(GenericI18Enum.FORM_STATUS, Milestone.Field.status.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val startDate = GridFieldMeta(GenericI18Enum.FORM_START_DATE, Milestone.Field.startdate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val endDate = GridFieldMeta(GenericI18Enum.FORM_END_DATE, Milestone.Field.enddate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val assignee = GridFieldMeta(GenericI18Enum.FORM_ASSIGNEE, SimpleMilestone.Field.ownerFullName.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val billableHours = GridFieldMeta(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "totalBillableHours", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val nonBillableHours = GridFieldMeta(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "totalNonBillableHours", WebUIConstants.TABLE_X_LABEL_WIDTH)
}