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

import java.util.Arrays;

import com.esofthead.mycollab.common.CountryValueFactory;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.CompositionStringParam;
import com.esofthead.mycollab.core.db.query.ConcatStringParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_leadContactName = new ConcatStringParam(
			"lead-contactname", LeadI18nEnum.FORM_NAME, "m_crm_lead",
			new String[] { "firstname", "lastname" });

	public static Param p_accountName = new StringParam("lead-accountname",
			LeadI18nEnum.FORM_ACCOUNT_NAME, "m_crm_lead", "accountName");

	public static Param p_website = new StringParam("lead-accountname",
			LeadI18nEnum.FORM_WEBSITE, "m_crm_lead", "website");

	public static Param p_anyEmail = new CompositionStringParam(
			"lead-anyEmail",
			LeadI18nEnum.FORM_ANY_EMAIL,
			new StringParam[] { new StringParam("", null, "m_crm_lead", "email") });

	public static Param p_anyPhone = new CompositionStringParam(
			"lead-anyPhone", LeadI18nEnum.FORM_ANY_PHONE, new StringParam[] {
					new StringParam("", null, "m_crm_lead", "officePhone"),
					new StringParam("", null, "m_crm_lead", "homePhone"),
					new StringParam("", null, "m_crm_lead", "mobile"),
					new StringParam("", null, "m_crm_lead", "otherPhone"),
					new StringParam("", null, "m_crm_lead", "fax") });

	public static Param p_anyCity = new CompositionStringParam("lead-anyCity",
			LeadI18nEnum.FORM_ANY_CITY, new StringParam[] {
					new StringParam("", null, "m_crm_lead", "primCity"),
					new StringParam("", null, "m_crm_lead", "otherCity") });

	public static Param p_billingCountry = new StringListParam(
			"lead-billingCountry", LeadI18nEnum.FORM_PRIMARY_COUNTRY,
			"m_crm_lead", "primCountry", Arrays.asList(CountryValueFactory
					.getCountryList()));

	public static Param p_shippingCountry = new StringListParam(
			"lead-shippingCountry", LeadI18nEnum.FORM_OTHER_COUNTRY,
			"m_crm_lead", "otherCountry", Arrays.asList(CountryValueFactory
					.getCountryList()));

	public static Param p_statuses = new StringListParam("lead-status",
			LeadI18nEnum.FORM_STATUS, "m_crm_lead", "status",
			Arrays.asList(CrmDataTypeFactory.getLeadStatusList()));

	public static Param p_sources = new StringListParam("lead-source",
			LeadI18nEnum.FORM_LEAD_SOURCE, "m_crm_lead", "source",
			Arrays.asList(CrmDataTypeFactory.getLeadSourceList()));

	public static Param p_assignee = new PropertyListParam("lead-assignuser",
			GenericI18Enum.FORM_ASSIGNEE, "m_crm_lead", "assignUser");

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
