package com.esofthead.mycollab.module.crm.data;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.vaadin.AppContext;

public class CrmLinkBuilder {
	
	public static String generateAccountPreviewLinkFull(Integer accountId) {
		if (accountId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateAccountPreviewLink(accountId);
	}
	
	public static String generateCampaignPreviewLinkFull(Integer campaignId) {
		if (campaignId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateCampaignPreviewLink(campaignId);
	}
	
	public static String generateCasePreviewLinkFull(Integer caseId) {
		if (caseId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateCasePreviewLink(caseId);
	}
	
	public static String generateContactPreviewLinkFull(Integer contactId) {
		if (contactId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateContactPreviewLink(contactId);
	}
	
	public static String generateLeadPreviewLinkFull(Integer leadId) {
		if (leadId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateLeadPreviewLink(leadId);
	}
	
	public static String generateOpportunityPreviewLinkFull(Integer opportunityId) {
		if (opportunityId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateOpportunityPreviewLink(opportunityId);
	}
	
	public static String generateActivityPreviewLinkFull(String type, Integer typeId) {
		if (type == null || typeId == null) {
			return "";
		}
		
			return AppContext.getSiteUrl() 
				+ CrmLinkGenerator.generateCrmItemLink(type,typeId);
		
	}

	public static String generateMeetingPreviewLinkFull(Integer meetingId) {
		if (meetingId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateMeetingPreviewLink(meetingId);
	}
	
	public static String generateCallPreviewLinkFul(Integer callId) {
		if (callId == null) {
			return "";
		}
		return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM
				+ CrmLinkGenerator.generateCallPreviewLink(callId);
	}

}
