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
package com.mycollab.module.crm.domain.criteria;

import com.mycollab.common.CountryValueFactory;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.db.query.*;
import com.mycollab.module.crm.CrmDataTypeFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.i18n.LeadI18nEnum;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadSearchCriteria extends SearchCriteria {
    private static final long serialVersionUID = 1L;

    public static final Param p_leadContactName = CacheParamMapper.register(CrmTypeConstants.LEAD, GenericI18Enum.FORM_NAME,
            new ConcatStringParam("contactname", "m_crm_lead", new String[]{"firstname", "lastname"}));

    public static final Param p_accountName = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ACCOUNT_NAME,
            new StringParam("accountname", "m_crm_lead", "accountName"));

    public static final Param p_website = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_WEBSITE,
            new StringParam("website", "m_crm_lead", "website"));

    public static final Param p_anyEmail = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ANY_EMAIL,
            new CompositionStringParam("anyEmail", new StringParam("", "m_crm_lead", "email")));

    public static final Param p_anyPhone = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ANY_PHONE,
            new CompositionStringParam("anyPhone", new StringParam("", "m_crm_lead", "officePhone"),
                    new StringParam("", "m_crm_lead", "homePhone"),
                    new StringParam("", "m_crm_lead", "mobile"),
                    new StringParam("", "m_crm_lead", "otherPhone"),
                    new StringParam("", "m_crm_lead", "fax")));

    public static final Param p_anyCity = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_ANY_CITY,
            new CompositionStringParam("anyCity",
                    new StringParam("", "m_crm_lead", "primCity"),
                    new StringParam("", "m_crm_lead", "otherCity")));

    public static final Param p_billingCountry = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_PRIMARY_COUNTRY,
            new StringListParam("billingCountry", "m_crm_lead", "primCountry", Arrays.asList(CountryValueFactory.getCountryList())));

    public static final Param p_shippingCountry = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_OTHER_COUNTRY,
            new StringListParam("shippingCountry", "m_crm_lead", "otherCountry",
                    Arrays.asList(CountryValueFactory.getCountryList())));

    public static final Param p_statuses = CacheParamMapper.register(CrmTypeConstants.LEAD, GenericI18Enum.FORM_STATUS,
            new I18nStringListParam("status", "m_crm_lead", "status", Arrays.asList(CrmDataTypeFactory.getLeadStatusList())));

    public static final I18nStringListParam p_sources = CacheParamMapper.register(CrmTypeConstants.LEAD, LeadI18nEnum.FORM_LEAD_SOURCE,
            new I18nStringListParam("source", "m_crm_lead", "source", Arrays.asList(CrmDataTypeFactory.getLeadSourceList())));

    public static final Param p_assignee = CacheParamMapper.register(CrmTypeConstants.LEAD, GenericI18Enum.FORM_ASSIGNEE,
            new PropertyListParam("assignuser", "m_crm_lead", "assignUser"));

    private StringSearchField leadName;
    private SetSearchField<String> assignUsers;
    private NumberSearchField campaignId;
    private NumberSearchField opportunityId;
    private NumberSearchField id;
    private NumberSearchField accountId;

    public StringSearchField getLeadName() {
        return leadName;
    }

    public void setLeadName(StringSearchField leadName) {
        this.leadName = leadName;
    }

    public void setAssignUsers(SetSearchField<String> assignUsers) {
        this.assignUsers = assignUsers;
    }

    public SetSearchField<String> getAssignUsers() {
        return assignUsers;
    }

    public NumberSearchField getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(NumberSearchField campaignId) {
        this.campaignId = campaignId;
    }

    public NumberSearchField getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(NumberSearchField opportunityId) {
        this.opportunityId = opportunityId;
    }

    public void setId(NumberSearchField id) {
        this.id = id;
    }

    public NumberSearchField getId() {
        return id;
    }

    public NumberSearchField getAccountId() {
        return accountId;
    }

    public void setAccountId(NumberSearchField accountId) {
        this.accountId = accountId;
    }
}
