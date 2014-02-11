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

public class OpportunitySearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;

	private StringSearchField opportunityName;
	private StringSearchField accountName;
	private StringSearchField campaignName;
	private SetSearchField<String> assignUsers;
	private StringSearchField assignUserName;
	private NumberSearchField accountId;
	private NumberSearchField contactId;
	private StringSearchField nextStep;
	private SetSearchField<String> salesStages;
	private SetSearchField<String> leadSources;
	private NumberSearchField id;

	public StringSearchField getOpportunityName() {
		return opportunityName;
	}

	public void setOpportunityName(StringSearchField opportunityName) {
		this.opportunityName = opportunityName;
	}

	public StringSearchField getAccountName() {
		return accountName;
	}

	public void setAccountName(StringSearchField accountName) {
		this.accountName = accountName;
	}

	public StringSearchField getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(StringSearchField campaignName) {
		this.campaignName = campaignName;
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

	public StringSearchField getNextStep() {
		return nextStep;
	}

	public void setNextStep(StringSearchField nextStep) {
		this.nextStep = nextStep;
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
