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

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;
	
	private StringSearchField campaignName;
	private StringSearchField referredBy;
	private StringSearchField leadName;
	private StringSearchField accountName;
	private SetSearchField<String> assignUsers;
	private StringSearchField assignUserName;
	private NumberSearchField campaignId;
	private NumberSearchField opportunityId;
	private StringSearchField firstname;
	private StringSearchField lastname;
	private StringSearchField anyEmail;
	private StringSearchField anyAddress;
	private StringSearchField anyCountry;
	private SetSearchField<String> sources;
	private StringSearchField anyPhone;
	private StringSearchField anyCity;
	private StringSearchField anyState;
	private SetSearchField<String> statuses;
	private NumberSearchField id;
	private NumberSearchField accountId;

	public StringSearchField getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(StringSearchField campaignName) {
		this.campaignName = campaignName;
	}

	public StringSearchField getReferredBy() {
		return referredBy;
	}

	public void setReferredBy(StringSearchField referredBy) {
		this.referredBy = referredBy;
	}

	public StringSearchField getLeadName() {
		return leadName;
	}

	public void setLeadName(StringSearchField leadName) {
		this.leadName = leadName;
	}

	public StringSearchField getAccountName() {
		return accountName;
	}

	public void setAccountName(StringSearchField accountName) {
		this.accountName = accountName;
	}

	public void setAssignUsers(SetSearchField<String> assignUsers) {
		this.assignUsers = assignUsers;
	}

	public SetSearchField<String> getAssignUsers() {
		return assignUsers;
	}

	public StringSearchField getAssignUserName() {
		return assignUserName;
	}

	public void setAssignUserName(StringSearchField assignUserName) {
		this.assignUserName = assignUserName;
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

	public StringSearchField getAnyCountry() {
		return anyCountry;
	}

	public void setAnyCountry(StringSearchField anyCountry) {
		this.anyCountry = anyCountry;
	}

	public void setSources(SetSearchField<String> sources) {
		this.sources = sources;
	}

	public SetSearchField<String> getSources() {
		return sources;
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

	public StringSearchField getAnyState() {
		return anyState;
	}

	public void setAnyState(StringSearchField anyState) {
		this.anyState = anyState;
	}

	public void setStatuses(SetSearchField<String> statuses) {
		this.statuses = statuses;
	}

	public SetSearchField<String> getStatuses() {
		return statuses;
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
