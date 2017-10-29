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
package com.mycollab.module.project.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.module.project.i18n.BugI18nEnum
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.*
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class BugFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, FieldGroupFormatter.TRIM_HTMLS)
        generateFieldDisplayHandler("environment", BugI18nEnum.FORM_ENVIRONMENT, FieldGroupFormatter.TRIM_HTMLS)
        generateFieldDisplayHandler("name", BugI18nEnum.FORM_SUMMARY)
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, I18nHistoryFieldFormat(StatusI18nEnum::class.java))
        generateFieldDisplayHandler("priority", GenericI18Enum.FORM_PRIORITY, I18nHistoryFieldFormat(Priority::class.java))
        generateFieldDisplayHandler("severity", BugI18nEnum.FORM_SEVERITY, I18nHistoryFieldFormat(BugSeverity::class.java))
        generateFieldDisplayHandler("resolution", BugI18nEnum.FORM_RESOLUTION, I18nHistoryFieldFormat(BugResolution::class.java))
        generateFieldDisplayHandler(BugWithBLOBs.Field.remainestimate.name, BugI18nEnum.FORM_REMAIN_ESTIMATE)
        generateFieldDisplayHandler(BugWithBLOBs.Field.originalestimate.name, BugI18nEnum.FORM_ORIGINAL_ESTIMATE)
        generateFieldDisplayHandler("startdate", GenericI18Enum.FORM_START_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("enddate", GenericI18Enum.FORM_END_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("duedate", GenericI18Enum.FORM_DUE_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("createdTime", GenericI18Enum.FORM_CREATED_TIME, FieldGroupFormatter.PRETTY_DATE_TIME_FIELD)
        generateFieldDisplayHandler("loguserFullName", BugI18nEnum.FORM_LOG_BY, ProjectMemberHistoryFieldFormat())
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, ProjectMemberHistoryFieldFormat())
        generateFieldDisplayHandler("milestoneid", MilestoneI18nEnum.SINGLE, MilestoneHistoryFieldFormat())
    }

    companion object {
        private val _instance = BugFieldFormatter()

        fun instance(): BugFieldFormatter = _instance
    }
}
