package com.mycollab.module.project.fielddef

import com.mycollab.common.TableViewField
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
    val id = TableViewField(GenericI18Enum.FORM_PROGRESS, Milestone.Field.id.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val milestoneName = TableViewField(GenericI18Enum.FORM_NAME, Milestone.Field.name.name, WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val status = TableViewField(GenericI18Enum.FORM_STATUS, Milestone.Field.status.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val startDate = TableViewField(GenericI18Enum.FORM_START_DATE, Milestone.Field.startdate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val endDate = TableViewField(GenericI18Enum.FORM_END_DATE, Milestone.Field.enddate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val assignee = TableViewField(GenericI18Enum.FORM_ASSIGNEE, SimpleMilestone.Field.ownerFullName.name, WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val billableHours = TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "totalBillableHours", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val nonBillableHours = TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "totalNonBillableHours", WebUIConstants.TABLE_X_LABEL_WIDTH)
}