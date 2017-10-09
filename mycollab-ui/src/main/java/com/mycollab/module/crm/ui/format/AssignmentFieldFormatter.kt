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
