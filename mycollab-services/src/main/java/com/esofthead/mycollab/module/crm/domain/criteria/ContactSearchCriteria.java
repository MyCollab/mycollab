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
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.CompositionStringParam;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.db.query.PropertyListParam;
import com.esofthead.mycollab.core.db.query.StringListParam;
import com.esofthead.mycollab.core.db.query.StringParam;
import com.esofthead.mycollab.module.crm.CrmDataTypeFactory;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ContactSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_firstname = new StringParam("contact-firstname",
			"First Name", "m_crm_contact", "firstname");

	public static Param p_lastname = new StringParam("contact-lastname",
			"Last Name", "m_crm_contact", "lastname");

	public static Param p_leadsource = new StringListParam(
			"contact-leadsource", "Lead Source", "m_crm_contact", "leadSource",
			Arrays.asList(CrmDataTypeFactory.getLeadSourceList()));

	public static Param p_billingCountry = new StringListParam(
			"contact-billingCountry", "Billing Country", "m_crm_contact",
			"primCountry", Arrays.asList(CountryValueFactory.getCountryList()));

	public static Param p_shippingCountry = new StringListParam(
			"contact-shippingCountry", "Shipping Country", "m_crm_contact",
			"otherCountry", Arrays.asList(CountryValueFactory.getCountryList()));

	public static Param p_anyPhone = new CompositionStringParam(
			"contact-anyPhone",
			"Any Phone",
			new StringParam[] {
					new StringParam("", "", "m_crm_contact", "officePhone"),
					new StringParam("", "", "m_crm_contact", "mobile"),
					new StringParam("", "", "m_crm_contact", "homePhone"),
					new StringParam("", "", "m_crm_contact", "otherPhone"),
					new StringParam("", "", "m_crm_contact", "fax"),
					new StringParam("", "", "m_crm_contact", "assistantPhone") });

	public static Param p_anyEmail = new CompositionStringParam(
			"contact-anyEmail", "Any Email",
			new StringParam[] { new StringParam("", "", "m_crm_contact",
					"email") });

	public static Param p_anyCity = new CompositionStringParam(
			"contact-anyCity", "Any City", new StringParam[] {
					new StringParam("", "", "m_crm_contact", "primCity"),
					new StringParam("", "", "m_crm_contact", "otherCity") });

	public static Param p_assignee = new PropertyListParam(
			"contact-assignuser", "Assignee", "m_crm_contact", "assignUser");

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
