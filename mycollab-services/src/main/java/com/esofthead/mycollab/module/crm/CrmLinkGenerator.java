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

import com.esofthead.mycollab.common.GenericLinkUtils;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmLinkGenerator {
	public static String generateAccountPreviewLink(Integer accountId) {
		return "crm/account/preview/"
				+ GenericLinkUtils.encodeParam(accountId);
	}

	public static String generateAccountPreviewFullLink(String siteUrl,
			Integer accountId) {
		return siteUrl + "#" + generateAccountPreviewLink(accountId);
	}

	public static String generateCampaignPreviewLink(Integer campaignId) {
		return "crm/campaign/preview/"
				+ GenericLinkUtils.encodeParam(campaignId);
	}

	public static String generateCampaignPreviewFullLink(String siteUrl,
			Integer campaignId) {
		return siteUrl + "#" + generateCampaignPreviewLink(campaignId);
	}

	public static String generateCasePreviewLink(Integer caseId) {
		return "crm/cases/preview/"
				+ GenericLinkUtils.encodeParam(caseId);
	}

	public static String generateCasePreviewFullLink(String siteUrl,
			Integer caseId) {
		return siteUrl + "#" + generateCasePreviewLink(caseId);
	}

	public static String generateContactPreviewLink(Integer contactId) {
		return "crm/contact/preview/"
				+ GenericLinkUtils.encodeParam(contactId);
	}

	public static String generateContactPreviewFullLink(String siteUrl,
			Integer contactId) {
		return siteUrl + "#" + generateContactPreviewLink(contactId);
	}

	public static String generateLeadPreviewLink(Integer leadId) {
		return "crm/lead/preview/"
				+ GenericLinkUtils.encodeParam(leadId);
	}

	public static String generateLeadPreviewFullLink(String siteUrl,
			Integer leadId) {
		return siteUrl + "#" + generateLeadPreviewLink(leadId);
	}

	public static String generateOpportunityPreviewLink(Integer opportunityId) {
		return "crm/opportunity/preview/"
				+ GenericLinkUtils.encodeParam(opportunityId);
	}

	public static String generateOpportunityPreviewFullLink(String siteUrl,
			Integer opportunityId) {
		return siteUrl + "#" + generateOpportunityPreviewLink(opportunityId);
	}

	public static String generateTaskPreviewLink(Integer taskId) {
		return "crm/activity/task/preview/"
				+ GenericLinkUtils.encodeParam(taskId);
	}

	public static String generateTaskPreviewFullLink(String siteUrl,
			Integer taskId) {
		return siteUrl + "#" + generateTaskPreviewLink(taskId);
	}

	public static String generateMeetingPreviewLink(Integer meetingId) {
		return "crm/activity/meeting/preview/"
				+ GenericLinkUtils.encodeParam(meetingId);
	}

	public static String generateMeetingPreviewFullLink(String siteUrl,
			Integer meetingId) {
		return siteUrl + "#" + generateMeetingPreviewLink(meetingId);
	}

	public static String generateCallPreviewLink(Integer callId) {
		return "crm/activity/call/preview/"
				+ GenericLinkUtils.encodeParam(callId);
	}

	public static String generateCallPreviewFullLink(String siteUrl,
			Integer callId) {
		return siteUrl + "#" + generateCallPreviewLink(callId);
	}

	public static String generateCrmItemLink(String type, int typeid) {
		String result = "";

		if (CrmTypeConstants.ACCOUNT.equals(type)) {
			result = generateAccountPreviewLink(typeid);
		} else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
			result = generateCampaignPreviewLink(typeid);
		} else if (CrmTypeConstants.CASE.equals(type)) {
			result = generateCasePreviewLink(typeid);
		} else if (CrmTypeConstants.CONTACT.equals(type)) {
			result = generateContactPreviewLink(typeid);
		} else if (CrmTypeConstants.LEAD.equals(type)) {
			result = generateLeadPreviewLink(typeid);
		} else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
			result = generateOpportunityPreviewLink(typeid);
		} else if (CrmTypeConstants.TASK.equals(type)) {
			result = generateTaskPreviewLink(typeid);
		} else if (CrmTypeConstants.MEETING.equals(type)) {
			result = generateMeetingPreviewLink(typeid);
		} else if (CrmTypeConstants.CALL.equals(type)) {
			result = generateCallPreviewLink(typeid);
		}
		return "#" + result;
	}

	public static String generateCrmItemFullLink(String siteUrl, String type,
			int typeId) {
		return siteUrl + generateCrmItemLink(type, typeId);
	}
}
