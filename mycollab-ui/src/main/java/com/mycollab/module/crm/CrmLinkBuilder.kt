/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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