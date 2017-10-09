package com.mycollab.module.crm.ui

import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.i18n.*
import com.mycollab.vaadin.UserUIContext

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object CrmLocalizationTypeMap {
    @JvmStatic fun getType(key: String): String {
        return when (key) {
            CrmTypeConstants.ACCOUNT -> UserUIContext.getMessage(AccountI18nEnum.SINGLE)
            CrmTypeConstants.CALL -> UserUIContext.getMessage(CallI18nEnum.SINGLE)
            CrmTypeConstants.CAMPAIGN -> UserUIContext.getMessage(CampaignI18nEnum.SINGLE)
            CrmTypeConstants.CASE -> UserUIContext.getMessage(CaseI18nEnum.SINGLE)
            CrmTypeConstants.CONTACT -> UserUIContext.getMessage(ContactI18nEnum.SINGLE)
            CrmTypeConstants.LEAD -> UserUIContext.getMessage(LeadI18nEnum.SINGLE)
            CrmTypeConstants.MEETING -> UserUIContext.getMessage(MeetingI18nEnum.SINGLE)
            CrmTypeConstants.OPPORTUNITY -> UserUIContext.getMessage(OpportunityI18nEnum.SINGLE)
            CrmTypeConstants.TASK -> UserUIContext.getMessage(TaskI18nEnum.SINGLE)
            else -> ""
        }
    }
}