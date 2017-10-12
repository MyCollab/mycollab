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
package com.mycollab.module.crm.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.OptionI18nEnum.TaskPriority
import com.mycollab.module.crm.i18n.OptionI18nEnum.TaskStatus
import com.mycollab.module.crm.i18n.TaskI18nEnum
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class AssignmentFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("subject", TaskI18nEnum.FORM_SUBJECT)
        generateFieldDisplayHandler("startdate", GenericI18Enum.FORM_START_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("duedate", GenericI18Enum.FORM_DUE_DATE, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, I18nHistoryFieldFormat(TaskStatus::class.java))
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, UserHistoryFieldFormat())
        generateFieldDisplayHandler("priority", TaskI18nEnum.FORM_PRIORITY, I18nHistoryFieldFormat(TaskPriority::class.java))
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION)
        generateFieldDisplayHandler("contactName", TaskI18nEnum.FORM_CONTACT)
    }

    companion object {
        private val _instance = AssignmentFieldFormatter()

        fun instance(): AssignmentFieldFormatter {
            return _instance
        }
    }
}
