/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.data;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmLinkBuilder {

    public static String generateAccountPreviewLinkFull(Integer accountId) {
        if (accountId == null) {
            return "";
        }
        return CrmLinkGenerator.generateAccountPreviewFullLink(AppContext.getSiteUrl(), accountId);
    }

    public static String generateCampaignPreviewLinkFull(Integer campaignId) {
        if (campaignId == null) {
            return "";
        }
        return CrmLinkGenerator.generateCampaignPreviewFullLink(AppContext.getSiteUrl(), campaignId);
    }

    public static String generateCasePreviewLinkFull(Integer caseId) {
        if (caseId == null) {
            return "";
        }
        return CrmLinkGenerator.generateCasePreviewFullLink(AppContext.getSiteUrl(), caseId);
    }

    public static String generateContactPreviewLinkFull(Integer contactId) {
        if (contactId == null) {
            return "";
        }
        return CrmLinkGenerator.generateContactPreviewFullLink(
                AppContext.getSiteUrl(), contactId);
    }

    public static String generateLeadPreviewLinkFull(Integer leadId) {
        if (leadId == null) {
            return "";
        }
        return AppContext.getSiteUrl() + GenericLinkUtils.URL_PREFIX_PARAM + CrmLinkGenerator.generateLeadPreviewLink(leadId);
    }

    public static String generateOpportunityPreviewLinkFull(
            Integer opportunityId) {
        if (opportunityId == null) {
            return "";
        }
        return CrmLinkGenerator.generateOpportunityPreviewFullLink(AppContext.getSiteUrl(), opportunityId);
    }

    public static String generateActivityPreviewLinkFull(String type, Integer typeId) {
        if (type == null || typeId == null) {
            return "";
        }

        return CrmLinkGenerator.generateCrmItemFullLink(AppContext.getSiteUrl(), type, typeId);

    }

    public static String generateMeetingPreviewLinkFull(Integer meetingId) {
        if (meetingId == null) {
            return "";
        }
        return CrmLinkGenerator.generateMeetingPreviewFullLink(AppContext.getSiteUrl(), meetingId);
    }

    public static String generateCallPreviewLinkFul(Integer callId) {
        if (callId == null) {
            return "";
        }
        return CrmLinkGenerator.generateCallPreviewFullLink(AppContext.getSiteUrl(), callId);
    }
}