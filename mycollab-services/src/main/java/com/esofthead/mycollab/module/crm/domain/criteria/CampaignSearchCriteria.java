/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.domain.criteria;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.*;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_campaignName = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_NAME,
            new StringParam("name", "m_crm_campaign", "campaignName"));

    public static final Param p_startDate = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_START_DATE,
            new DateParam("startdate", "m_crm_campaign", "startDate"));

    public static final Param p_endDate = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_END_DATE,
            new DateParam("enddate", "m_crm_campaign", "endDate"));

    public static final Param p_createdtime = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdtime", "m_crm_campaign", "createdTime"));

    public static final Param p_lastUpdatedTime = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN,
            GenericI18Enum.FORM_LAST_UPDATED_TIME, new DateParam("lastUpdatedTime", "m_crm_campaign", "lastUpdatedTime"));

    public static final Param p_types = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_TYPE,
            new StringListParam("type", "m_crm_campaign", "type", Arrays.asList(CrmDataTypeFactory.getCampaignTypeList())));

    public static final Param p_statuses = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_STATUS,
            new StringListParam("status", "m_crm_campaign", "status", Arrays.asList(CrmDataTypeFactory.getCampaignStatusList())));

    public static final Param p_assignee = CacheParamMapper.register(CrmTypeConstants.CAMPAIGN, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam("assignuser", "m_crm_campaign", "assignUser"));

    private StringSearchField campaignName;
    private StringSearchField assignUser;
    private NumberSearchField leadId;
    private SetSearchField<String> statuses;
    private SetSearchField<String> types;
    private SetSearchField<String> assignUsers;
    private NumberSearchField id;

    public StringSearchField getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(StringSearchField campaignName) {
        this.campaignName = campaignName;
    }

    public StringSearchField getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(StringSearchField assignUser) {
        this.assignUser = assignUser;
    }

    public NumberSearchField getLeadId() {
        return leadId;
    }

    public void setLeadId(NumberSearchField leadId) {
        this.leadId = leadId;
    }

    public SetSearchField<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(SetSearchField<String> statuses) {
        this.statuses = statuses;
    }

    public SetSearchField<String> getTypes() {
        return types;
    }

    public void setTypes(SetSearchField<String> types) {
        this.types = types;
    }

    public SetSearchField<String> getAssignUsers() {
        return assignUsers;
    }

    public void setAssignUsers(SetSearchField<String> assignUsers) {
        this.assignUsers = assignUsers;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }
}
