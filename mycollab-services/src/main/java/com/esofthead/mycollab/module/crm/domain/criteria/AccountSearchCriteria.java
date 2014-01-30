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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;

public class AccountSearchCriteria extends SearchCriteria {

	private StringSearchField accountname;
	private StringSearchField assignUser;
	private StringSearchField assignUserName;
	private StringSearchField anyCity;
	private StringSearchField website;
	private SetSearchField<String> types;
	private SetSearchField<String> industries;
	private SetSearchField<String> assignUsers;
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

	public StringSearchField getAssignUserName() {
		return assignUserName;
	}

	public void setAssignUserName(StringSearchField assignUserName) {
		this.assignUserName = assignUserName;
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
