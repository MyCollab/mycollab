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
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunitySearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_opportunityName = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            GenericI18Enum.FORM_NAME, new StringParam("name", "m_crm_opportunity", "opportunityName"));

    public static final Param p_account = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            OpportunityI18nEnum.FORM_ACCOUNT_NAME, new PropertyParam("account", "m_crm_opportunity", "accountid"));

    public static final Param p_campaign = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            OpportunityI18nEnum.FORM_CAMPAIGN_NAME, new PropertyParam("campaign", "m_crm_opportunity", "campaignid"));

    public static final Param p_nextStep = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            OpportunityI18nEnum.FORM_NEXT_STEP, new StringParam("nextstep", "m_crm_opportunity", "nextStep"));

    public static final Param p_saleStage = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            OpportunityI18nEnum.FORM_SALE_STAGE, new StringListParam("saleStage", "m_crm_opportunity", "salesStage",
                    Arrays.asList(CrmDataTypeFactory.getOpportunitySalesStageList())));

    public static final Param p_leadSource = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            OpportunityI18nEnum.FORM_LEAD_SOURCE, new StringListParam("leadSource", "m_crm_opportunity", "source",
                    Arrays.asList(CrmDataTypeFactory.getLeadSourceList())));

    public static final Param p_type = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY, GenericI18Enum.FORM_TYPE,
            new StringListParam("type", "m_crm_opportunity", "type", Arrays.asList(CrmDataTypeFactory.getOpportunityTypeList())));

    public static final Param p_assignee = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam("assignee", "m_crm_opportunity", "assignUser"));

    public static final Param p_expectedcloseddate = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE, new DateParam("expectedcloseddate", "m_crm_opportunity",
                    "expectedClosedDate"));

    public static final Param p_createdtime = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY, GenericI18Enum.FORM_CREATED_TIME,
            new DateParam("createdtime", "m_crm_opportunity", "createdTime"));

    public static final Param p_lastupdatedtime = CacheParamMapper.register(CrmTypeConstants.OPPORTUNITY,
            GenericI18Enum.FORM_LAST_UPDATED_TIME, new DateParam("lastUpdatedTime", "m_crm_opportunity",
                    "lastUpdatedTime"));

    private StringSearchField opportunityName;
    private SetSearchField<String> assignUsers;
    private NumberSearchField accountId;
    private NumberSearchField contactId;
    private NumberSearchField campaignId;
    private SetSearchField<String> salesStages;
    private SetSearchField<String> leadSources;
    private NumberSearchField id;

    public StringSearchField getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(StringSearchField opportunityName) {
        this.opportunityName = opportunityName;
    }

    public void setAssignUsers(SetSearchField<String> assignUsers) {
        this.assignUsers = assignUsers;
    }

    public SetSearchField<String> getAssignUsers() {
        return assignUsers;
    }

    public NumberSearchField getAccountId() {
        return accountId;
    }

    public void setAccountId(NumberSearchField accountId) {
        this.accountId = accountId;
    }

    public NumberSearchField getContactId() {
        return contactId;
    }

    public void setContactId(NumberSearchField contactId) {
        this.contactId = contactId;
    }

    public NumberSearchField getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(NumberSearchField campaignId) {
        this.campaignId = campaignId;
    }

    public void setSalesStages(SetSearchField<String> salesStages) {
        this.salesStages = salesStages;
    }

    public SetSearchField<String> getSalesStages() {
        return salesStages;
    }

    public void setLeadSources(SetSearchField<String> leadSources) {
        this.leadSources = leadSources;
    }

    public SetSearchField<String> getLeadSources() {
        return leadSources;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }
}
