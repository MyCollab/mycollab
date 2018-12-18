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