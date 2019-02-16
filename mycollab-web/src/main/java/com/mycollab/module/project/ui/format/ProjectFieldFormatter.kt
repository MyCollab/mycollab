package com.mycollab.module.project.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.project.i18n.ProjectI18nEnum
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
class ProjectFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, FieldGroupFormatter.TRIM_HTMLS)
        generateFieldDisplayHandler("planstartdate", GenericI18Enum.FORM_START_DATE, FieldGroupFormatter.DATE_FIELD)
        generateFieldDisplayHandler("planenddate", GenericI18Enum.FORM_END_DATE, FieldGroupFormatter.DATE_FIELD)
        generateFieldDisplayHandler("istemplate", ProjectI18nEnum.FORM_TEMPLATE)
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS)
        generateFieldDisplayHandler("shortname", ProjectI18nEnum.FORM_SHORT_NAME)
        generateFieldDisplayHandler("homepage", ProjectI18nEnum.FORM_HOME_PAGE)
        generateFieldDisplayHandler("currencyid", GenericI18Enum.FORM_CURRENCY, FieldGroupFormatter.CURRENCY_FIELD)
        generateFieldDisplayHandler("defaultbillingrate", ProjectI18nEnum.FORM_BILLING_RATE)
        generateFieldDisplayHandler("defaultovertimebillingrate", ProjectI18nEnum.FORM_OVERTIME_BILLING_RATE)
    }

    companion object {
        private val _instance = ProjectFieldFormatter()

        fun instance(): ProjectFieldFormatter = _instance
    }
}