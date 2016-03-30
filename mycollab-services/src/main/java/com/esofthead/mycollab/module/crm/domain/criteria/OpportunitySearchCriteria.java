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
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunitySearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_opportunityName = new StringParam("opportunity-name",
            OpportunityI18nEnum.FORM_NAME, "m_crm_opportunity",
            "opportunityName");

    public static final Param p_account = new PropertyParam("opportunity-account",
            OpportunityI18nEnum.FORM_ACCOUNT_NAME, "m_crm_opportunity",
            "accountid");

    public static final Param p_campaign = new PropertyParam("opportunity-campaign",
            OpportunityI18nEnum.FORM_CAMPAIGN_NAME, "m_crm_opportunity",
            "campaignid");

    public static final Param p_nextStep = new StringParam("opportunity-nextstep",
            OpportunityI18nEnum.FORM_NEXT_STEP, "m_crm_opportunity", "nextStep");

    public static final Param p_saleStage = new StringListParam(
            "opportunity-saleStage", OpportunityI18nEnum.FORM_SALE_STAGE,
            "m_crm_opportunity", "salesStage", Arrays.asList(CrmDataTypeFactory
            .getOpportunitySalesStageList()));

    public static final Param p_leadSource = new StringListParam(
            "opportunity-leadSource", OpportunityI18nEnum.FORM_LEAD_SOURCE,
            "m_crm_opportunity", "source", Arrays.asList(CrmDataTypeFactory
            .getLeadSourceList()));

    public static final Param p_type = new StringListParam("opportunity-type",
            OpportunityI18nEnum.FORM_TYPE, "m_crm_opportunity", "type",
            Arrays.asList(CrmDataTypeFactory.getOpportunityTypeList()));

    public static final Param p_assignee = new PropertyListParam(
            "opportunity-assignee", GenericI18Enum.FORM_ASSIGNEE,
            "m_crm_opportunity", "assignUser");

    public static final Param p_expectedcloseddate = new DateParam(
            "opportunity-expectedcloseddate",
            OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE, "m_crm_opportunity",
            "expectedClosedDate");

    public static final Param p_createdtime = new DateParam(
            "opportunity-createdtime", GenericI18Enum.FORM_CREATED_TIME,
            "m_crm_opportunity", "createdTime");

    public static final Param p_lastupdatedtime = new DateParam(
            "opportunity-lastUpdatedTime",
            GenericI18Enum.FORM_LAST_UPDATED_TIME, "m_crm_opportunity",
            "lastUpdatedTime");

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
