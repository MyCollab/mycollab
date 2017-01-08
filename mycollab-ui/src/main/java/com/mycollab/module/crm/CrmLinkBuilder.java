/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm;

import com.mycollab.vaadin.MyCollabUI;

import static com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmLinkBuilder {

    public static String generateAccountPreviewLinkFull(Integer accountId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateAccountPreviewLink(accountId);
    }

    public static String generateCampaignPreviewLinkFull(Integer campaignId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateCallPreviewLink(campaignId);
    }

    public static String generateCasePreviewLinkFull(Integer caseId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateCasePreviewLink(caseId);
    }

    public static String generateContactPreviewLinkFull(Integer contactId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateContactPreviewLink(contactId);
    }

    public static String generateLeadPreviewLinkFull(Integer leadId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateLeadPreviewLink(leadId);
    }

    public static String generateOpportunityPreviewLinkFull(Integer opportunityId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateOpportunityPreviewLink(opportunityId);
    }

    public static String generateActivityPreviewLinkFull(String type, Integer typeId) {
        return CrmLinkGenerator.generateCrmItemFullLink(MyCollabUI.getSiteUrl(), type, typeId);
    }

    public static String generateMeetingPreviewLinkFull(Integer meetingId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateMeetingPreviewLink(meetingId);
    }

    public static String generateCallPreviewLinkFul(Integer callId) {
        return URL_PREFIX_PARAM + CrmLinkGenerator.generateCallPreviewLink(callId);
    }
}