package com.mycollab.module.crm.domain.criteria

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.query.*
import com.mycollab.module.crm.CrmDataTypeFactory
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.i18n.OpportunityI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum

import java.util.Arrays

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class OpportunitySearchCriteria : SearchCriteria() {

    var opportunityName: StringSearchField? = null
    var assignUsers: SetSearchField<String>? = null
    var accountId: NumberSearchField? = null
    var contactId: NumberSearchField? = null
    var campaignId: NumberSearchField? = null
    var salesStages: SetSearchField<String>? = null
    var leadSources: SetSearchField<String>? = null
    var id: NumberSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField val p_opportunityName: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                GenericI18Enum.FORM_NAME, StringParam("name", "m_crm_opportunity", "opportunityName"))

        @JvmField val p_account: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                OpportunityI18nEnum.FORM_ACCOUNT_NAME, PropertyParam("account", "m_crm_opportunity", "accountid"))

        @JvmField val p_campaign: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                OpportunityI18nEnum.FORM_CAMPAIGN_NAME, PropertyParam("campaign", "m_crm_opportunity", "campaignid"))

        @JvmField val p_nextStep: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                OpportunityI18nEnum.FORM_NEXT_STEP, StringParam("nextstep", "m_crm_opportunity", "nextStep"))

        @JvmField val p_saleStage: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                OpportunityI18nEnum.FORM_SALE_STAGE, I18nStringListParam("saleStage", "m_crm_opportunity", "salesStage",
                listOf(*CrmDataTypeFactory.opportunitySalesStageList)))

        @JvmField val p_leadSource = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                OpportunityI18nEnum.FORM_LEAD_SOURCE, I18nStringListParam("leadSource", "m_crm_opportunity", "source",
                Arrays.asList<OptionI18nEnum.OpportunityLeadSource>(*CrmDataTypeFactory.leadSourceList)))

        @JvmField val p_type: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY, GenericI18Enum.FORM_TYPE,
                I18nStringListParam("type", "m_crm_opportunity", "type", listOf(*CrmDataTypeFactory.opportunityTypeList)))

        @JvmField val p_assignee: Param = CacheParamMapper.register<PropertyListParam<*>>(CrmTypeConstants.OPPORTUNITY, GenericI18Enum.FORM_ASSIGNEE,
                PropertyListParam<String>("assignee", "m_crm_opportunity", "assignUser"))

        @JvmField val p_expectedcloseddate: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE, DateParam("expectedcloseddate", "m_crm_opportunity",
                "expectedClosedDate"))

        @JvmField val p_createdtime: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createdtime", "m_crm_opportunity", "createdTime"))

        @JvmField val p_lastupdatedtime: Param = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
                GenericI18Enum.FORM_LAST_UPDATED_TIME, DateParam("lastUpdatedTime", "m_crm_opportunity",
                "lastUpdatedTime"))
    }
}
