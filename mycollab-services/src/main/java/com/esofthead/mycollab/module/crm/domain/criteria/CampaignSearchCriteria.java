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

import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.RangeDateTimeSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CampaignSearchCriteria extends SearchCriteria {
	private static final long serialVersionUID = 1L;
	
	private StringSearchField campaignName;
	private StringSearchField assignUserName;
	private StringSearchField assignUser;
	private NumberSearchField leadId;
	private SetSearchField<String> statuses;
	private SetSearchField<String> types;
	private SetSearchField<String> assignUsers;
	private DateSearchField startDate;
	private DateSearchField endDate;
	private RangeDateTimeSearchField startDateRange;
	private RangeDateTimeSearchField endDateRange;
	private NumberSearchField id;

	public StringSearchField getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(StringSearchField campaignName) {
		this.campaignName = campaignName;
	}

	public StringSearchField getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(StringSearchField assignUser) {
		this.assignUser = assignUser;
	}

	public NumberSearchField getLeadId() {
		return leadId;
	}

	public void setLeadId(NumberSearchField leadId) {
		this.leadId = leadId;
	}

	public SetSearchField<String> getStatuses() {
		return statuses;
	}

	public void setStatuses(SetSearchField<String> statuses) {
		this.statuses = statuses;
	}

	public SetSearchField<String> getTypes() {
		return types;
	}

	public void setTypes(SetSearchField<String> types) {
		this.types = types;
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

	public DateSearchField getStartDate() {
		return startDate;
	}

	public void setStartDate(DateSearchField startDate) {
		this.startDate = startDate;
	}

	public DateSearchField getEndDate() {
		return endDate;
	}

	public void setEndDate(DateSearchField endDate) {
		this.endDate = endDate;
	}

	public RangeDateTimeSearchField getStartDateRange() {
		return startDateRange;
	}

	public void setStartDateRange(RangeDateTimeSearchField startDateRange) {
		this.startDateRange = startDateRange;
	}

	public RangeDateTimeSearchField getEndDateRange() {
		return endDateRange;
	}

	public void setEndDateRange(RangeDateTimeSearchField endDateRange) {
		this.endDateRange = endDateRange;
	}

	public void setId(NumberSearchField id) {
		this.id = id;
	}

	public NumberSearchField getId() {
		return id;
	}
}
