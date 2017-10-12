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
import com.mycollab.module.crm.i18n.OpportunityI18nEnum
import com.mycollab.module.user.ui.format.UserHistoryFieldFormat
import com.mycollab.vaadin.ui.formatter.FieldGroupFormatter
import com.mycollab.vaadin.ui.formatter.I18nHistoryFieldFormat

import com.mycollab.module.crm.i18n.OptionI18nEnum.*

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
class OpportunityFieldFormatter private constructor() : FieldGroupFormatter() {

    init {
        generateFieldDisplayHandler("opportunityname", GenericI18Enum.FORM_NAME)
        generateFieldDisplayHandler("currencyid", GenericI18Enum.FORM_CURRENCY, FieldGroupFormatter.CURRENCY_FIELD)
        generateFieldDisplayHandler("amount", OpportunityI18nEnum.FORM_AMOUNT)
        generateFieldDisplayHandler("salesstage", OpportunityI18nEnum.FORM_SALE_STAGE, I18nHistoryFieldFormat(OpportunitySalesStage::class.java))
        generateFieldDisplayHandler("probability", OpportunityI18nEnum.FORM_PROBABILITY)
        generateFieldDisplayHandler("nextstep", OpportunityI18nEnum.FORM_NEXT_STEP)
        generateFieldDisplayHandler("accountid", OpportunityI18nEnum.FORM_ACCOUNT_NAME, AccountHistoryFieldFormat())
        generateFieldDisplayHandler("expectedcloseddate", OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE, FieldGroupFormatter.PRETTY_DATE_FIELD)
        generateFieldDisplayHandler("opportunitytype", GenericI18Enum.FORM_TYPE, I18nHistoryFieldFormat(OpportunityType::class.java))
        generateFieldDisplayHandler("source", OpportunityI18nEnum.FORM_LEAD_SOURCE, I18nHistoryFieldFormat(OpportunityLeadSource::class.java))
        generateFieldDisplayHandler("campaignid", OpportunityI18nEnum.FORM_CAMPAIGN_NAME, CampaignHistoryFieldFormat())
        generateFieldDisplayHandler("assignuser", GenericI18Enum.FORM_ASSIGNEE, UserHistoryFieldFormat())
        generateFieldDisplayHandler("description", GenericI18Enum.FORM_DESCRIPTION, FieldGroupFormatter.TRIM_HTMLS)
    }

    companion object {
        private val _instance = OpportunityFieldFormatter()

        fun instance(): OpportunityFieldFormatter {
            return _instance
        }
    }
}
