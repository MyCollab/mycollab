package com.mycollab.module.crm

import com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM
import com.mycollab.vaadin.AppUI

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CrmLinkBuilder {

    @JvmStatic fun generateAccountPreviewLinkFull(accountId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateAccountPreviewLink(accountId)

    @JvmStatic fun generateCampaignPreviewLinkFull(campaignId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateCampaignPreviewLink(campaignId)

    @JvmStatic fun generateCasePreviewLinkFull(caseId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateCasePreviewLink(caseId)

    @JvmStatic fun generateContactPreviewLinkFull(contactId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateContactPreviewLink(contactId)

    @JvmStatic fun generateLeadPreviewLinkFull(leadId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateLeadPreviewLink(leadId)

    @JvmStatic fun generateOpportunityPreviewLinkFull(opportunityId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateOpportunityPreviewLink(opportunityId)

    @JvmStatic fun generateActivityPreviewLinkFull(type: String, typeId: Int?): String =
            CrmLinkGenerator.generateCrmItemFullLink(AppUI.siteUrl, type, typeId)

    @JvmStatic fun generateMeetingPreviewLinkFull(meetingId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateMeetingPreviewLink(meetingId)

    @JvmStatic fun generateCallPreviewLinkFul(callId: Int?): String =
            URL_PREFIX_PARAM + CrmLinkGenerator.generateCallPreviewLink(callId)
}