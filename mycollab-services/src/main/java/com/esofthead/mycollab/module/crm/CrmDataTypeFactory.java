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
package com.esofthead.mycollab.module.crm;

import java.util.Arrays;
import java.util.List;

import com.esofthead.mycollab.module.crm.i18n.OptionI18nEnum.AccountType;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmDataTypeFactory {
	private static String[] ACCOUNT_INDUSTRY_LIST = new String[] { "Apparel",
			"Banking", "Biotechnology", "Chemicals", "Communications",
			"Construction", "Consulting", "Education", "Electronics", "Energy",
			"Engineering", "Entertainment", "Environmental", "Finance",
			"Goverment", "Healthcare", "Hospitality", "Insurance", "Machinery",
			"Manufactory", "Media", "Not For Profit", "Recreation", "Retail",
			"Shipping", "Technology", "Telecommunications", "Utilities",
			"Other" };

	private static final List<AccountType> ACCOUNT_TYPE_LIST = Arrays.asList(
			AccountType.Analyst, AccountType.Competitor, AccountType.Customer,
			AccountType.Integrator, AccountType.Investor, AccountType.Partner,
			AccountType.Press, AccountType.Prospect, AccountType.Reseller,
			AccountType.Other);

	private static String[] LEAD_SOURCE_LIST = new String[] { "Cold Call",
			"Existing Customer", "Self Generated", "Employee", "Partner",
			"Public Relations", "Direct Email", "Conference", "Trade Show",
			"Website", "Word of mouth", "Email", "Campaign", "Other" };

	private static String[] LEAD_STATUS_LIST = new String[] { "New",
			"Assigned", "In Process", "Converted", "Recycled", "Dead" };

	private static String[] CAMPAIGN_STATUS_LIST = new String[] { "Planning",
			"Active", "Inactive", "Complete", "In Queue", "Sending" };

	private static String[] CAMPAIGN_TYPE_LIST = new String[] { "Conference",
			"Webinar", "Trade Show", "Public Relations", "Partners",
			"Referral Program", "Advertisement", "Banner Ads", "Direct Email",
			"Mail", "Telemarketing", "Others" };

	private static String[] OPPORTUNITY_SALES_STAGE_LIST = new String[] {
			"Prospecting", "Qualification", "Need Analysis",
			"Value Proposition", "Id. Decision Markers", "Perception Analysis",
			"Proposal/Price Quote", "Negotiation/Review", "Closed Won",
			"Closed Lost" };

	private static String[] OPPORTUNITY_CONTACT_ROLE_LIST = new String[] {
			"Primary Decision Marker", "Evaluator", "Influencer", "Other" };

	private static String[] OPPORTUNITY_TYPE_LIST = new String[] {
			"Existing Business", "New Business" };

	private static String[] CASES_STATUS_LIST = new String[] { "New",
			"Assigned", "Closed", "Pending Input", "Rejected", "Duplicate" };

	private static String[] CASES_PRIORITY_LIST = new String[] { "High",
			"Medium", "Low" };

	private static String[] CASES_REASON_LIST = new String[] {
			"User did not attend any training", "Complex functionality",
			"New problem", "Ambigous instruction" };

	private static String[] CASES_ORIGIN_LIST = new String[] { "Email",
			"Phone", "Web", "Error Log" };

	private static String[] CASES_TYPE_LIST = new String[] { "Problem",
			"Feature Request", "Question" };

	private static String[] TASK_PRIORITY_LIST = new String[] { "High",
			"Medium", "Low" };

	private static String[] TASK_STATUS_LIST = new String[] { "Not Started",
			"In Progress", "Completed", "Pending Input", "Deferred" };

	public static String[] getAccountIndustryList() {
		return ACCOUNT_INDUSTRY_LIST;
	}

	public static List<? extends Enum<?>> getAccountTypeList() {
		return ACCOUNT_TYPE_LIST;
	}

	public static String[] getLeadSourceList() {
		return LEAD_SOURCE_LIST;
	}

	public static String[] getLeadStatusList() {
		return LEAD_STATUS_LIST;
	}

	public static String[] getCampaignStatusList() {
		return CAMPAIGN_STATUS_LIST;
	}

	public static String[] getCampaignTypeList() {
		return CAMPAIGN_TYPE_LIST;
	}

	public static String[] getOpportunitySalesStageList() {
		return OPPORTUNITY_SALES_STAGE_LIST;
	}

	public static String[] getOpportunityContactRoleList() {
		return OPPORTUNITY_CONTACT_ROLE_LIST;
	}

	public static String[] getOpportunityTypeList() {
		return OPPORTUNITY_TYPE_LIST;
	}

	public static String[] getCasesStatusList() {
		return CASES_STATUS_LIST;
	}

	public static String[] getCasesPriorityList() {
		return CASES_PRIORITY_LIST;
	}

	public static String[] getCasesReason() {
		return CASES_REASON_LIST;
	}

	public static String[] getCasesOrigin() {
		return CASES_ORIGIN_LIST;
	}

	public static String[] getCasesType() {
		return CASES_TYPE_LIST;
	}

	public static String[] getTaskPriorities() {
		return TASK_PRIORITY_LIST;
	}

	public static String[] getTaskStatuses() {
		return TASK_STATUS_LIST;
	}
}
