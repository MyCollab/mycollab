package com.mycollab.module.crm.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.CallI18nEnum
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class CallFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("subject", CallI18nEnum.FORM_SUBJECT)
        generateFieldDisplayHandler("startdate", CallI18nEnum.FORM_START_DATE_TIME, FieldGroupFormatter.DATE_FIELD)
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, UserHistoryFieldFormat())
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS)
        generateFieldDisplayHandler("purpose", CallI18nEnum.FORM_PURPOSE)
    }

    companion object {
        private val _instance = CallFieldFormatter()

        fun instance(): CallFieldFormatter {
            return _instance
        }
    }
}
