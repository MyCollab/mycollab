package com.mycollab.module.crm.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.MeetingI18nEnum
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class MeetingFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("subject", MeetingI18nEnum.FORM_SUBJECT)
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS)
        generateFieldDisplayHandler("startdate", MeetingI18nEnum.FORM_START_DATE_TIME, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("enddate", MeetingI18nEnum.FORM_END_DATE_TIME, FieldGroupFormatter.DATETIME_FIELD)
        generateFieldDisplayHandler("location", MeetingI18nEnum.FORM_LOCATION)
    }

    companion object {
        private val _instance = MeetingFieldFormatter()

        fun instance(): MeetingFieldFormatter {
            return _instance
        }
    }
}
