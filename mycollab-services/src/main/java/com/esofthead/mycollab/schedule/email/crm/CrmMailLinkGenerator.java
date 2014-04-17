package com.esofthead.mycollab.schedule.email.crm;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;

public class CrmMailLinkGenerator extends GenericLinkUtils {

	private final String siteUrl;

	public CrmMailLinkGenerator(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String generateAccountPreviewFullLink(Integer accountId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateAccountPreviewLink(accountId);
	}

	public String generateCallPreviewFullLink(Integer callId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateCallPreviewLink(callId);
	}

	public String generateCasePreviewFullLink(Integer caseId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateCasePreviewLink(caseId);
	}

	public String generateCampainPreviewFullLilnk(Integer campaignId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateCampaignPreviewLink(campaignId);
	}

	public String generateContactPreviewFullLink(Integer contactId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateContactPreviewLink(contactId);
	}

	public String generateLeadPreviewFullLink(Integer leadId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateLeadPreviewLink(leadId);
	}

	public String generateMeetingPreviewFullLink(Integer meetingId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateMeetingPreviewLink(meetingId);
	}

	public String generateOpportunityPreviewFullLink(Integer opportunityId) {
		return siteUrl
				+ GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator
						.generateOpportunityPreviewLink(opportunityId);
	}

	public String generateTaskPreviewFullLink(Integer taskId) {
		return siteUrl + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateTaskPreviewLink(taskId);
	}

	@Override
	public String getSiteUrl() {

		return this.siteUrl;
	}

}
