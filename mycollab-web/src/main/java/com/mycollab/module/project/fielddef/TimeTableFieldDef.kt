package com.mycollab.module.project.fielddef

import com.mycollab.common.TableViewField
import com.mycollab.module.project.i18n.ProjectI18nEnum
import com.mycollab.module.project.i18n.TimeTrackingI18nEnum
import com.mycollab.module.user.accountsettings.localization.UserI18nEnum
import com.mycollab.vaadin.web.ui.WebUIConstants

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TimeTableFieldDef {
    @JvmField
    var id = TableViewField(null, "id", 60)

    @JvmField
    val summary = TableViewField(TimeTrackingI18nEnum.FORM_SUMMARY, "name", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val logUser = TableViewField(UserI18nEnum.SINGLE, "logUserFullName", WebUIConstants.TABLE_X_LABEL_WIDTH)

    @JvmField
    val logValue = TableViewField(TimeTrackingI18nEnum.LOG_VALUE, "logvalue", WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val billable = TableViewField(TimeTrackingI18nEnum.FORM_IS_BILLABLE, "isbillable", WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val overtime = TableViewField(TimeTrackingI18nEnum.FORM_IS_OVERTIME, "isovertime", WebUIConstants.TABLE_S_LABEL_WIDTH)

    @JvmField
    val logForDate = TableViewField(TimeTrackingI18nEnum.LOG_FOR_DATE, "logforday", WebUIConstants.TABLE_DATE_TIME_WIDTH)

    @JvmField
    val project = TableViewField(ProjectI18nEnum.SINGLE, "projectName", WebUIConstants.TABLE_X_LABEL_WIDTH)
}