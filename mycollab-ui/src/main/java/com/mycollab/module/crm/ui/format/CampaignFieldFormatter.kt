package com.mycollab.module.crm.ui.format

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.module.crm.i18n.CampaignI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum.CampaignStatus
import com.mycollab.module.crm.i18n.OptionI18nEnum.CampaignType
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class CampaignFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("campaignname", GenericI18Enum.FORM_NAME)
        generateFieldDisplayHandler("startdate", GenericI18Enum.FORM_START_DATE, FieldGroupFormatter.DATE_FIELD)
        generateFieldDisplayHandler("enddate", GenericI18Enum.FORM_END_DATE, FieldGroupFormatter.DATE_FIELD)
        generateFieldDisplayHandler("status", GenericI18Enum.FORM_STATUS, I18nHistoryFieldFormat(CampaignStatus::class.java))
        generateFieldDisplayHandler("type", GenericI18Enum.FORM_TYPE, I18nHistoryFieldFormat(CampaignType::class.java))
        generateFieldDisplayHandler("currencyid", GenericI18Enum.FORM_CURRENCY, FieldGroupFormatter.CURRENCY_FIELD)
        generateFieldDisplayHandler("budget", CampaignI18nEnum.FORM_BUDGET)
        generateFieldDisplayHandler("expectedcost", CampaignI18nEnum.FORM_EXPECTED_COST)
        generateFieldDisplayHandler("actualcost", CampaignI18nEnum.FORM_ACTUAL_COST)
        generateFieldDisplayHandler("expectedrevenue", CampaignI18nEnum.FORM_EXPECTED_REVENUE)
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, UserHistoryFieldFormat())
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, FieldGroupFormatter.TRIM_HTMLS)
    }

    companion object {
        private val _instance = CampaignFieldFormatter()

        fun instance(): CampaignFieldFormatter {
            return _instance
        }
    }
}
