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

	@Override
	public String getSiteUrl() {

		return this.siteUrl;
	}

}
