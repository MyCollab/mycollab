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
import com.mycollab.module.crm.i18n.ContactI18nEnum

import java.util.Arrays

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ContactSearchCriteria : SearchCriteria() {

    var contactName: StringSearchField? = null
    var assignUsers: SetSearchField<String>? = null
    var accountId: NumberSearchField? = null
    var firstname: StringSearchField? = null
    var lastname: StringSearchField? = null
    var anyEmail: StringSearchField? = null
    var anyAddress: StringSearchField? = null
    var anyState: StringSearchField? = null
    var countries: SetSearchField<String>? = null
    var anyPhone: StringSearchField? = null
    var anyCity: StringSearchField? = null
    var anyPostalCode: StringSearchField? = null
    var leadSources: SetSearchField<String>? = null
    var id: NumberSearchField? = null
    var campaignId: NumberSearchField? = null
    var opportunityId: NumberSearchField? = null
    var caseId: NumberSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField val p_name: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_NAME,
                ConcatStringParam("firstname", "m_crm_contact", arrayOf("firstname", "lastname")))

        @JvmField val p_leadsource = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_LEAD_SOURCE,
                I18nStringListParam("leadsource", "m_crm_contact", "leadSource",
                        listOf(*CrmDataTypeFactory.leadSourceList)))

        @JvmField val p_billingCountry: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_PRIMARY_COUNTRY,
                StringListParam("billingCountry", "m_crm_contact", "primCountry",
                        Arrays.asList(*CountryValueFactory.countryList!!)))

        @JvmField val p_shippingCountry: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_OTHER_COUNTRY,
                StringListParam("shippingCountry", "m_crm_contact", "otherCountry",
                        Arrays.asList(*CountryValueFactory.countryList!!)))

        @JvmField val p_anyPhone: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ANY_PHONE,
                CompositionStringParam("anyPhone",
                        StringParam("", "m_crm_contact", "officePhone"),
                        StringParam("", "m_crm_contact", "mobile"),
                        StringParam("", "m_crm_contact", "homePhone"),
                        StringParam("", "m_crm_contact", "otherPhone"),
                        StringParam("", "m_crm_contact", "fax"),
                        StringParam("", "m_crm_contact", "assistantPhone")))

        @JvmField val p_anyEmail: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ANY_EMAIL,
                CompositionStringParam("anyEmail", StringParam("", "m_crm_contact", "email")))

        @JvmField val p_anyCity: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ANY_CITY,
                CompositionStringParam("anyCity",
                        StringParam("", "m_crm_contact", "primCity"),
                        StringParam("", "m_crm_contact", "otherCity")))

        @JvmField val p_account: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, ContactI18nEnum.FORM_ACCOUNTS,
                PropertyParam("account", "m_crm_contact", "accountId"))

        @JvmField val p_assignee: Param = CacheParamMapper.register<PropertyListParam<*>>(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_ASSIGNEE,
                PropertyListParam<String>("assignuser", "m_crm_contact", "assignUser"))

        @JvmField val p_createdtime: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createdtime", "m_crm_contact", "createdTime"))

        @JvmField val p_lastupdatedtime: Param = CacheParamMapper.register(CrmTypeConstants.CONTACT, GenericI18Enum.FORM_LAST_UPDATED_TIME,
                DateParam("lastupdatedtime", "m_crm_contact", "lastUpdatedTime"))
    }
}
