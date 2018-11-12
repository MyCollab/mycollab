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

import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchCriteria
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.db.query.*
import com.mycollab.module.crm.CrmDataTypeFactory
import com.mycollab.module.crm.CrmTypeConstants

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class CampaignSearchCriteria : SearchCriteria() {

    var campaignName: StringSearchField? = null
    var assignUser: StringSearchField? = null
    var leadId: NumberSearchField? = null
    var statuses: SetSearchField<String>? = null
    var types: SetSearchField<String>? = null
    var assignUsers: SetSearchField<String>? = null
    var id: NumberSearchField? = null

    companion object {
        private val serialVersionUID = 1L

        @JvmField val p_campaignName: Param = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_NAME,
                StringParam("name", "m_crm_campaign", "campaignName"))

        @JvmField val p_startDate: Param = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_START_DATE,
                DateParam("startdate", "m_crm_campaign", "startDate"))

        @JvmField val p_endDate: Param = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_END_DATE,
                DateParam("enddate", "m_crm_campaign", "endDate"))

        @JvmField val p_createdtime: Param = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_CREATED_TIME,
                DateParam("createdtime", "m_crm_campaign", "createdTime"))

        @JvmField val p_lastUpdatedTime: Param = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN,
                GenericI18Enum.FORM_LAST_UPDATED_TIME, DateParam("lastUpdatedTime", "m_crm_campaign", "lastUpdatedTime"))

        @JvmField val p_types: Param = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_TYPE,
                I18nStringListParam("type", "m_crm_campaign", "type", listOf(*CrmDataTypeFactory.campaignTypes)))

        @JvmField val p_statuses: Param = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_STATUS,
                I18nStringListParam("status", "m_crm_campaign", "status", listOf(*CrmDataTypeFactory.campaignStatuses)))

        @JvmField val p_assignee: Param = CacheParamMapper.register<PropertyListParam<*>>(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_ASSIGNEE,
                PropertyListParam<String>("assignuser", "m_crm_campaign", "assignUser"))
    }
}
