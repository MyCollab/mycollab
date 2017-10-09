package com.mycollab.module.project.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.project.i18n.BugI18nEnum
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object BugTableFieldDef {
    @JvmField
    val action = TableViewField(null, "id", WebUIConstants.TABLE_CONTROL_WIDTH)

    @JvmField
    val summary = TableViewField(BugI18nEnum.FORM_SUMMARY, "name", WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val description = TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val environment = TableViewField(BugI18nEnum.FORM_ENVIRONMENT, "environment", WebUIConstants.TABLE_EX_LABEL_WIDTH)

    @JvmField
    val status = TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val severity = TableViewField(BugI18nEnum.FORM_SEVERITY, "severity", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val startDate = TableViewField(GenericI18Enum.FORM_START_DATE, BugWithBLOBs.Field.startdate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val endDate = TableViewField(GenericI18Enum.FORM_END_DATE, BugWithBLOBs.Field.enddate.name, WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val dueDate = TableViewField(GenericI18Enum.FORM_DUE_DATE, "duedate", WebUIConstants.TABLE_DATE_WIDTH)

    @JvmField
    val logBy = TableViewField(BugI18nEnum.FORM_LOG_BY, "loguserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val priority = TableViewField(GenericI18Enum.FORM_PRIORITY, "priority", WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val resolution = TableViewField(BugI18nEnum.FORM_RESOLUTION, "resolution", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val createdTime = TableViewField(GenericI18Enum.FORM_CREATED_TIME, "createdtime", WebUIConstants.TABLE_DATE_TIME_WIDTH)

    @JvmField
    val assignUser = TableViewField(GenericI18Enum.FORM_ASSIGNEE, "assignuserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val milestoneName = TableViewField(MilestoneI18nEnum.SINGLE, "milestoneName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val billableHours = TableViewField(TimeTrackingI18nEnum.OPT_BILLABLE_HOURS, "billableHours", WebUIConstants.TABLE_M_LABEL_WIDTH)

    @JvmField
    val nonBillableHours = TableViewField(TimeTrackingI18nEnum.OPT_NON_BILLABLE_HOURS, "nonBillableHours", WebUIConstants.TABLE_M_LABEL_WIDTH)
}