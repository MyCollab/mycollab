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

import com.mycollab.common.GenericLinkUtils
import com.mycollab.common.GenericLinkUtils.URL_PREFIX_PARAM

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CrmLinkGenerator {
    @JvmStatic
    fun generateAccountPreviewLink(accountId: Int?): String =
            "${URL_PREFIX_PARAM}crm/account/preview/${GenericLinkUtils.encodeParam(accountId)}"

    @JvmStatic
    fun generateAccountPreviewFullLink(siteUrl: String, accountId: Int?): String =
            "$siteUrl${generateAccountPreviewLink(accountId)}"

    @JvmStatic
    fun generateCampaignPreviewLink(campaignId: Int?): String =
            "${URL_PREFIX_PARAM}crm/campaign/preview/" + GenericLinkUtils.encodeParam(campaignId)

    @JvmStatic
    fun generateCampaignPreviewFullLink(siteUrl: String, campaignId: Int?): String =
            "$siteUrl${generateCampaignPreviewLink(campaignId)}"

    @JvmStatic
    fun generateCasePreviewLink(caseId: Int?): String =
            "${URL_PREFIX_PARAM}crm/cases/preview/${GenericLinkUtils.encodeParam(caseId)}"

    @JvmStatic
    fun generateCasePreviewFullLink(siteUrl: String, caseId: Int?): String {
        return "$siteUrl${generateCasePreviewLink(caseId)}"
    }

    @JvmStatic
    fun generateContactPreviewLink(contactId: Int?): String =
            "${URL_PREFIX_PARAM}crm/contact/preview/${GenericLinkUtils.encodeParam(contactId)}"

    @JvmStatic
    fun generateContactPreviewFullLink(siteUrl: String, contactId: Int?): String {
        return "$siteUrl${generateContactPreviewLink(contactId)}"
    }

    @JvmStatic
    fun generateLeadPreviewLink(leadId: Int?): String =
            "${URL_PREFIX_PARAM}crm/lead/preview/${GenericLinkUtils.encodeParam(leadId)}"

    @JvmStatic
    fun generateLeadPreviewFullLink(siteUrl: String, leadId: Int?): String =
            "$siteUrl${generateLeadPreviewLink(leadId)}"

    @JvmStatic
    fun generateOpportunityPreviewLink(opportunityId: Int?): String =
            "${URL_PREFIX_PARAM}crm/opportunity/preview/${GenericLinkUtils.encodeParam(opportunityId)}"

    @JvmStatic
    fun generateOpportunityPreviewFullLink(siteUrl: String, opportunityId: Int?): String =
            "$siteUrl${generateOpportunityPreviewLink(opportunityId)}"

    @JvmStatic
    fun generateTaskPreviewLink(taskId: Int?): String =
            "${URL_PREFIX_PARAM}crm/activity/task/preview/${GenericLinkUtils.encodeParam(taskId)}"

    @JvmStatic
    fun generateTaskPreviewFullLink(siteUrl: String, taskId: Int?): String =
            "$siteUrl${generateTaskPreviewLink(taskId)}"

    @JvmStatic
    fun generateMeetingPreviewLink(meetingId: Int?): String =
            "${URL_PREFIX_PARAM}crm/activity/meeting/preview/${GenericLinkUtils.encodeParam(meetingId)}"

    @JvmStatic
    fun generateMeetingPreviewFullLink(siteUrl: String, meetingId: Int?): String =
            "$siteUrl${generateMeetingPreviewLink(meetingId)}"

    @JvmStatic
    fun generateCallPreviewLink(callId: Int?): String =
            "${URL_PREFIX_PARAM}crm/activity/call/preview/${GenericLinkUtils.encodeParam(callId)}"

    @JvmStatic
    fun generateCallPreviewFullLink(siteUrl: String, callId: Int?): String =
            "$siteUrl${generateCallPreviewLink(callId)}"

    @JvmStatic
    fun generateCrmItemLink(type: String, typeId: Int?): String = when (type) {
        CrmTypeConstants.ACCOUNT -> generateAccountPreviewLink(typeId)
        CrmTypeConstants.CAMPAIGN -> generateCampaignPreviewLink(typeId)
        CrmTypeConstants.CASE -> generateCasePreviewLink(typeId)
        CrmTypeConstants.CONTACT -> generateContactPreviewLink(typeId)
        CrmTypeConstants.LEAD -> generateLeadPreviewLink(typeId)
        CrmTypeConstants.OPPORTUNITY -> generateOpportunityPreviewLink(typeId)
        CrmTypeConstants.TASK -> generateTaskPreviewLink(typeId)
        CrmTypeConstants.MEETING -> generateMeetingPreviewLink(typeId)
        CrmTypeConstants.CALL -> generateCallPreviewLink(typeId)
        else -> ""
    }

    fun generateCrmItemFullLink(siteUrl: String, type: String, typeId: Int?): String =
            "$siteUrl${generateCrmItemLink(type, typeId)}"
}
