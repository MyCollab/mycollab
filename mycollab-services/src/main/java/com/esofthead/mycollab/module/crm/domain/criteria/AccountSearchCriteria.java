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
import com.esofthead.mycollab.core.db.query.DateParam;
import com.esofthead.mycollab.core.db.query.I18nStringListParam;
import com.esofthead.mycollab.core.db.query.NumberParam;
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
public class AccountSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	public static Param p_accountName = new StringParam("account-name",
			"Account Name", "m_crm_account", "accountName");

	public static Param p_website = new StringParam("account-website",
			"Website", "m_crm_account", "website");

	public static Param p_numemployees = new NumberParam("account-employees",
			"Employees", "m_crm_account", "numemployees");

	public static Param p_assignee = new PropertyListParam(
			"account-assignuser", "Assignee", "m_crm_account", "assignUser");

	public static Param p_createdtime = new DateParam("account-createdtime",
			"Created Time", "m_crm_account", "createdTime");

	public static Param p_lastupdatedtime = new DateParam(
			"account-lastupdatedtime", "Last Updated Time", "m_crm_account",
			"lastUpdatedTime");

	public static Param p_anyCity = new CompositionStringParam(
			"account-anyCity", "Any City", new StringParam[] {
					new StringParam("", "", "m_crm_account", "city"),
					new StringParam("", "", "m_crm_account", "shippingCity") });

	public static Param p_anyPhone = new CompositionStringParam(
			"account-anyPhone", "Any Phone", new StringParam[] {
					new StringParam("", "", "m_crm_account", "alternatePhone"),
					new StringParam("", "", "m_crm_account", "phoneOffice") });

	public static Param p_industries = new StringListParam("account-industry",
			"Industry", "m_crm_account", "industry",
			Arrays.asList(CrmDataTypeFactory.getAccountIndustryList()));

	public static Param p_types = new I18nStringListParam("account-type",
			"Type", "m_crm_account", "type",
			CrmDataTypeFactory.getAccountTypeList());

	public static Param p_billingCountry = new StringListParam(
			"account-billingCountry", "Billing Country", "m_crm_account",
			"billingCountry", Arrays.asList(CountryValueFactory
					.getCountryList()));

	public static Param p_shippingCountry = new StringListParam(
			"account-shippingCountry", "Shipping Country", "m_crm_account",
			"shippingCountry", Arrays.asList(CountryValueFactory
					.getCountryList()));

	private StringSearchField accountname;
	private StringSearchField assignUser;
	private StringSearchField website;
	private SetSearchField<String> types;
	private SetSearchField<String> industries;
	private SetSearchField<String> assignUsers;
	private StringSearchField anyCity;
	private StringSearchField anyPhone;
	private StringSearchField anyAddress;
	private StringSearchField anyMail;
	private NumberSearchField id;
	private NumberSearchField campaignId;

	public StringSearchField getAnyMail() {
		return anyMail;
	}

	public void setAnyMail(StringSearchField anyMail) {
		this.anyMail = anyMail;
	}

	public StringSearchField getAnyAddress() {
		return anyAddress;
	}

	public void setAnyAddress(StringSearchField anyAddress) {
		this.anyAddress = anyAddress;
	}

	public StringSearchField getAccountname() {
		return accountname;
	}

	public void setAccountname(StringSearchField accountname) {
		this.accountname = accountname;
	}

	public StringSearchField getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(StringSearchField assignUser) {
		this.assignUser = assignUser;
	}

	public void setAnyCity(StringSearchField anyCity) {
		this.anyCity = anyCity;
	}

	public StringSearchField getAnyCity() {
		return anyCity;
	}

	public StringSearchField getWebsite() {
		return website;
	}

	public void setWebsite(StringSearchField website) {
		this.website = website;
	}

	public SetSearchField<String> getTypes() {
		return types;
	}

	public void setTypes(SetSearchField<String> types) {
		this.types = types;
	}

	public SetSearchField<String> getIndustries() {
		return industries;
	}

	public void setIndustries(SetSearchField<String> industries) {
		this.industries = industries;
	}

	public SetSearchField<String> getAssignUsers() {
		return assignUsers;
	}

	public void setAssignUsers(SetSearchField<String> assignUsers) {
		this.assignUsers = assignUsers;
	}

	public StringSearchField getAnyPhone() {
		return anyPhone;
	}

	public void setAnyPhone(StringSearchField anyPhone) {
		this.anyPhone = anyPhone;
	}

	public NumberSearchField getId() {
		return id;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public NumberSearchField getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(NumberSearchField campaignId) {
		this.campaignId = campaignId;
	}
}
