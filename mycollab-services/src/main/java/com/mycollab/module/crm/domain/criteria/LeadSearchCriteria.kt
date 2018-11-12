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
import com.mycollab.module.crm.i18n.LeadI18nEnum
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class LeadSearchCriteria : SearchCriteria() {

    var leadName: StringSearchField? = null
    var assignUsers: SetSearchField<String>? = null
    var campaignId: NumberSearchField? = null
    var opportunityId: NumberSearchField? = null
    var id: NumberSearchField? = null
    var accountId: NumberSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField val p_leadContactName: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, GenericI18Enum.FORM_NAME,
                ConcatStringParam("contactname", "m_crm_lead", arrayOf("firstname", "lastname")))

        @JvmField val p_accountName: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ACCOUNT_NAME,
                StringParam("accountname", "m_crm_lead", "accountName"))

        @JvmField val p_website: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_WEBSITE,
                StringParam("website", "m_crm_lead", "website"))

        @JvmField val p_anyEmail: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ANY_EMAIL,
                CompositionStringParam("anyEmail", StringParam("", "m_crm_lead", "email")))

        @JvmField val p_anyPhone: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ANY_PHONE,
                CompositionStringParam("anyPhone", StringParam("", "m_crm_lead", "officePhone"),
                        StringParam("", "m_crm_lead", "homePhone"),
                        StringParam("", "m_crm_lead", "mobile"),
                        StringParam("", "m_crm_lead", "otherPhone"),
                        StringParam("", "m_crm_lead", "fax")))

        @JvmField val p_anyCity: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ANY_CITY,
                CompositionStringParam("anyCity",
                        StringParam("", "m_crm_lead", "primCity"),
                        StringParam("", "m_crm_lead", "otherCity")))

        @JvmField val p_billingCountry: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_PRIMARY_COUNTRY,
                StringListParam("billingCountry", "m_crm_lead", "primCountry", Arrays.asList(*CountryValueFactory.countries!!)))

        @JvmField val p_shippingCountry: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_OTHER_COUNTRY,
                StringListParam("shippingCountry", "m_crm_lead", "otherCountry",
                        Arrays.asList(*CountryValueFactory.countries!!)))

        @JvmField val p_statuses: Param = CacheParamMapper.register(CrmTypeConstants.LEAD, GenericI18Enum.FORM_STATUS,
                I18nStringListParam("status", "m_crm_lead", "status", listOf(*CrmDataTypeFactory.leadStatuses)))

        @JvmField val p_sources = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_LEAD_SOURCE,
                I18nStringListParam("source", "m_crm_lead", "source", listOf(*CrmDataTypeFactory.leadSources)))

        @JvmField val p_assignee: Param = CacheParamMapper.register<PropertyListParam<*>>(CrmTypeConstants.LEAD, GenericI18Enum.FORM_ASSIGNEE,
                PropertyListParam<String>("assignuser", "m_crm_lead", "assignUser"))
    }
}
