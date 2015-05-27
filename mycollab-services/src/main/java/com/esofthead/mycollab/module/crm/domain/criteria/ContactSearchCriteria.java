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
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.PropertyParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ContactSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static final Param p_name = new ConcatStringParam("contact-firstname",
			ContactI18nEnum.FORM_NAME, "m_crm_contact", new String[] {
					"firstname", "lastname" });

	public static final Param p_leadsource = new StringListParam(
			"contact-leadsource", ContactI18nEnum.FORM_LEAD_SOURCE,
			"m_crm_contact", "leadSource", Arrays.asList(CrmDataTypeFactory
					.getLeadSourceList()));

	public static final Param p_billingCountry = new StringListParam(
			"contact-billingCountry", ContactI18nEnum.FORM_PRIMARY_COUNTRY,
			"m_crm_contact", "primCountry", Arrays.asList(CountryValueFactory
					.getCountryList()));

	public static final Param p_shippingCountry = new StringListParam(
			"contact-shippingCountry", ContactI18nEnum.FORM_OTHER_COUNTRY,
			"m_crm_contact", "otherCountry", Arrays.asList(CountryValueFactory
					.getCountryList()));

	public static final Param p_anyPhone = new CompositionStringParam(
			"contact-anyPhone",
			ContactI18nEnum.FORM_ANY_PHONE,
			new StringParam[] {
					new StringParam("", null, "m_crm_contact", "officePhone"),
					new StringParam("", null, "m_crm_contact", "mobile"),
					new StringParam("", null, "m_crm_contact", "homePhone"),
					new StringParam("", null, "m_crm_contact", "otherPhone"),
					new StringParam("", null, "m_crm_contact", "fax"),
					new StringParam("", null, "m_crm_contact", "assistantPhone") });

	public static final Param p_anyEmail = new CompositionStringParam(
			"contact-anyEmail", ContactI18nEnum.FORM_ANY_EMAIL,
			new StringParam[] { new StringParam("", null, "m_crm_contact",
					"email") });

	public static final Param p_anyCity = new CompositionStringParam(
			"contact-anyCity", ContactI18nEnum.FORM_ANY_CITY,
			new StringParam[] {
					new StringParam("", null, "m_crm_contact", "primCity"),
					new StringParam("", null, "m_crm_contact", "otherCity") });

	public static final Param p_account = new PropertyParam("contact-account",
			ContactI18nEnum.FORM_ACCOUNTS, "m_crm_contact", "accountId");

	public static final Param p_assignee = new PropertyListParam(
			"contact-assignuser", GenericI18Enum.FORM_ASSIGNEE,
			"m_crm_contact", "assignUser");

	public static final Param p_createdtime = new DateParam("contact-createdtime",
			GenericI18Enum.FORM_CREATED_TIME, "m_crm_contact", "createdTime");

	public static final Param p_lastupdatedtime = new DateParam(
			"contact-lastupdatedtime", GenericI18Enum.FORM_LAST_UPDATED_TIME,
			"m_crm_contact", "lastUpdatedTime");

	private StringSearchField contactName;
	private SetSearchField<String> assignUsers;
	private NumberSearchField accountId;
	private StringSearchField firstname;
	private StringSearchField lastname;
	private StringSearchField anyEmail;
	private StringSearchField anyAddress;
	private StringSearchField anyState;
	private SetSearchField<String> countries;
	private StringSearchField anyPhone;
	private StringSearchField anyCity;
	private StringSearchField anyPostalCode;
	private SetSearchField<String> leadSources;
	private NumberSearchField id;
	private NumberSearchField campaignId;
	private NumberSearchField opportunityId;
	private NumberSearchField caseId;

	public StringSearchField getContactName() {
		return contactName;
	}

	public void setContactName(StringSearchField contactName) {
		this.contactName = contactName;
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

	public StringSearchField getFirstname() {
		return firstname;
	}

	public void setFirstname(StringSearchField firstname) {
		this.firstname = firstname;
	}

	public StringSearchField getLastname() {
		return lastname;
	}

	public void setLastname(StringSearchField lastname) {
		this.lastname = lastname;
	}

	public StringSearchField getAnyEmail() {
		return anyEmail;
	}

	public void setAnyEmail(StringSearchField anyEmail) {
		this.anyEmail = anyEmail;
	}

	public StringSearchField getAnyAddress() {
		return anyAddress;
	}

	public void setAnyAddress(StringSearchField anyAddress) {
		this.anyAddress = anyAddress;
	}

	public StringSearchField getAnyState() {
		return anyState;
	}

	public void setAnyState(StringSearchField anyState) {
		this.anyState = anyState;
	}

	public void setCountries(SetSearchField<String> countries) {
		this.countries = countries;
	}

	public SetSearchField<String> getCountries() {
		return countries;
	}

	public StringSearchField getAnyPhone() {
		return anyPhone;
	}

	public void setAnyPhone(StringSearchField anyPhone) {
		this.anyPhone = anyPhone;
	}

	public StringSearchField getAnyCity() {
		return anyCity;
	}

	public void setAnyCity(StringSearchField anyCity) {
		this.anyCity = anyCity;
	}

	public StringSearchField getAnyPostalCode() {
		return anyPostalCode;
	}

	public void setAnyPostalCode(StringSearchField anyPostalCode) {
		this.anyPostalCode = anyPostalCode;
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

	public NumberSearchField getCaseId() {
		return caseId;
	}

	public void setCaseId(NumberSearchField caseId) {
		this.caseId = caseId;
	}
}
