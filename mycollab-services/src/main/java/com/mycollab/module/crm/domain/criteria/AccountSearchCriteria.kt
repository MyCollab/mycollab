package com.mycollab.module.crm.domain.criteria

import com.mycollab.common.CountryValueFactory
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.query.*
import com.mycollab.module.crm.CrmDataTypeFactory
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.i18n.AccountI18nEnum

import java.util.Arrays

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class AccountSearchCriteria : SearchCriteria() {

    var accountname: StringSearchField? = null
    var assignUser: StringSearchField? = null
    var website: StringSearchField? = null
    var types: SetSearchField<String>? = null
    var industries: SetSearchField<String>? = null
    var assignUsers: SetSearchField<String>? = null
    var anyCity: StringSearchField? = null
    var anyPhone: StringSearchField? = null
    var anyAddress: StringSearchField? = null
    var anyMail: StringSearchField? = null
    var id: NumberSearchField? = null
    var campaignId: NumberSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField val p_accountName = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                AccountI18nEnum.FORM_ACCOUNT_NAME, StringParam("name", "m_crm_account", "accountName"))

        @JvmField val p_website = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, AccountI18nEnum.FORM_WEBSITE,
                StringParam("website", "m_crm_account", "website"))

        @JvmField val p_numemployees = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                AccountI18nEnum.FORM_EMPLOYEES, NumberParam("employees", "m_crm_account", "numemployees"))

        @JvmField val p_assignee: PropertyListParam<*> = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, GenericI18Enum
                .FORM_ASSIGNEE, PropertyListParam<String>("assignuser", "m_crm_account", "assignUser"))

        @JvmField val p_createdtime = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                GenericI18Enum.FORM_CREATED_TIME, DateParam("createdtime", "m_crm_account", "createdTime"))

        @JvmField val p_lastupdatedtime = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                GenericI18Enum.FORM_LAST_UPDATED_TIME, DateParam("lastupdatedtime", "m_crm_account", "lastUpdatedTime"))

        @JvmField val p_anyCity = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                AccountI18nEnum.FORM_ANY_CITY, CompositionStringParam("anyCity",
                StringParam("", "m_crm_account", "city"),
                StringParam("", "m_crm_account", "shippingCity")))

        @JvmField val p_anyPhone = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, AccountI18nEnum.FORM_ANY_PHONE,
                CompositionStringParam("anyPhone",
                        StringParam("", "m_crm_account", "alternatePhone"),
                        StringParam("", "m_crm_account", "phoneOffice")))

        @JvmField val p_industries = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                AccountI18nEnum.FORM_INDUSTRY, I18nStringListParam("industry", "m_crm_account", "industry",
                listOf(*CrmDataTypeFactory.accountIndustryList)))

        @JvmField val p_types = CacheParamMapper.register(CrmTypeConstants.ACCOUNT, GenericI18Enum.FORM_TYPE,
                I18nStringListParam("type", "m_crm_account", "type",
                        CrmDataTypeFactory.accountTypeList))

        @JvmField val p_billingCountry: Param = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                AccountI18nEnum.FORM_BILLING_COUNTRY, StringListParam("billingCountry", "m_crm_account", "billingCountry",
                Arrays.asList(*CountryValueFactory.countryList!!)))

        @JvmField val p_shippingCountry: Param = CacheParamMapper.register(CrmTypeConstants.ACCOUNT,
                AccountI18nEnum.FORM_SHIPPING_COUNTRY, StringListParam("shippingCountry", "m_crm_account", "shippingCountry",
                Arrays.asList(*CountryValueFactory.countryList!!)))
    }
}
