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

import com.hp.gagawa.java.elements.Div
import com.hp.gagawa.java.elements.Table
import com.hp.gagawa.java.elements.Td
import com.hp.gagawa.java.elements.Tr
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.StringUtils.trimHtmlTags
import com.mycollab.html.TooltipBuilder
import com.mycollab.html.TooltipBuilder.Companion.buildCellLink
import com.mycollab.html.TooltipBuilder.Companion.buildCellName
import com.mycollab.html.TooltipBuilder.Companion.buildCellValue
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.crm.domain.*
import com.mycollab.module.crm.i18n.*
import com.mycollab.module.file.StorageUtils
import com.mycollab.module.user.AccountLinkGenerator
import org.slf4j.LoggerFactory
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CrmTooltipGenerator {
    private val LOG = LoggerFactory.getLogger(CrmTooltipGenerator::class.java)

    private fun generateTooltipNull(locale: Locale): String {
        val div = Div()
        val table = Table()
        table.style = "padding-left:10px;  color: #5a5a5a; font-size:11px;"

        val trRow1 = Tr()
        trRow1.appendChild(Td().setStyle(
                "vertical-align: top; text-align: left;").appendText(
                LocalizationHelper.getMessage(locale, GenericI18Enum.TOOLTIP_NO_ITEM_EXISTED)))

        table.appendChild(trRow1)
        div.appendChild(table)

        return div.write()
    }

    private fun getStringBaseNullCondition(value: Any?): String {
        return value?.toString() ?: ""
    }

    @JvmStatic
    fun generateToolTipAccount(locale: Locale, account: SimpleAccount?, siteURL: String): String? {
        if (account == null) {
            return generateTooltipNull(locale)
        }

        try {
            val tooltipBuilder = TooltipBuilder()
            tooltipBuilder.appendTitle(account.accountname)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale,
                    AccountI18nEnum.FORM_WEBSITE) + ": ")

            val cell12 = buildCellLink(getStringBaseNullCondition(account.website), account.website)

            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, AccountI18nEnum.FORM_OFFICE_PHONE))
            val cell14 = buildCellValue(account.phoneoffice)

            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipBuilder.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, AccountI18nEnum.FORM_EMPLOYEES))

            val cell22 = buildCellValue(getStringBaseNullCondition(account.numemployees))

            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL))

            val emailLink = if (account.email != null) String.format("mailto: %s", account.email) else ""
            val cell24 = buildCellLink(emailLink, account.email)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipBuilder.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))

            val userLink = if (account.assignuser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURL, account.assignuser) else ""
            val userAvatarLink = StorageUtils.getAvatarPath(account.assignUserAvatarId, 16)
            val cell32 = buildCellLink(userLink, userAvatarLink, account.assignUserFullName)

            val cell33 = buildCellName(LocalizationHelper.getMessage(locale,
                    AccountI18nEnum.FORM_ANNUAL_REVENUE))

            val cell34 = buildCellValue(account.annualrevenue)
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipBuilder.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell42 = buildCellValue(trimHtmlTags(account.description))
            cell42.setAttribute("colspan", "3")
            trRow4.appendChild(cell41, cell42)
            tooltipBuilder.appendRow(trRow4)

            return tooltipBuilder.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate Account tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipContact(locale: Locale, dateFormat: String, contact: SimpleContact?, siteURL: String,
                               userTimeZone: TimeZone): String? {
        if (contact == null) {
            return generateTooltipNull(locale)
        }

        try {
            val tooltipBuilder = TooltipBuilder()
            tooltipBuilder.appendTitle(contact.contactName)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_FIRSTNAME))
            val cell12 = buildCellValue(contact.firstname)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_OFFICE_PHONE))
            val cell14 = buildCellValue(contact.officephone)
            trRow1.appendChild(cell11, cell12, cell13, cell14)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_LASTNAME))
            val cell22 = buildCellValue(contact.lastname)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_MOBILE))
            val cell24 = buildCellValue(contact.mobile)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipBuilder.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL))

            val contactEmailLink = if (contact.email != null) String.format("mailto:%s", contact.email) else ""
            val cell32 = buildCellLink(contactEmailLink, contact.email)

            val cell33 = buildCellName(LocalizationHelper.getMessage(locale,
                    ContactI18nEnum.FORM_BIRTHDAY))
            val birthday = DateTimeUtils.convertToStringWithUserTimeZone(contact.birthday, dateFormat, locale, userTimeZone)
            val cell34 = buildCellValue(birthday)

            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipBuilder.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_DEPARTMENT))
            val cell42 = buildCellValue(contact.department)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (contact.assignuser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURL, contact.assignuser) else ""
            val assignAvatarLink = StorageUtils.getAvatarPath(contact.assignUserAvatarId, 16)
            val cell44 = buildCellLink(assignUserLink, assignAvatarLink, contact.assignUserFullName)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipBuilder.appendRow(trRow4)

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_PRIMARY_ADDRESS))
            val cell52 = buildCellValue(contact.primaddress)
            val cell53 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_OTHER_ADDRESS))
            val cell54 = buildCellValue(contact.otheraddress)
            trRow5.appendChild(cell51, cell52, cell53, cell54)
            tooltipBuilder.appendRow(trRow5)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell62 = buildCellValue(trimHtmlTags(contact.description))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipBuilder.appendRow(trRow6)

            return tooltipBuilder.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate Contact tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateTooltipCampaign(locale: Locale, dateFormat: String, campaign: SimpleCampaign?, siteURl: String, userTimeZone: TimeZone): String? {
        if (campaign == null)
            return generateTooltipNull(locale)

        try {
            val tooltipBuilder = TooltipBuilder()
            tooltipBuilder.appendTitle(campaign.campaignname)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE))
            val startDate = DateTimeUtils.convertToStringWithUserTimeZone(campaign.startdate, dateFormat, locale, userTimeZone)
            val cell12 = buildCellValue(startDate)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell14 = buildCellValue(campaign.status)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipBuilder.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE))
            val endDate = DateTimeUtils.convertToStringWithUserTimeZone(campaign.enddate, dateFormat, locale, userTimeZone)
            val cell22 = buildCellValue(endDate)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_TYPE))
            val cell24 = buildCellValue(campaign.type)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipBuilder.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CURRENCY))
            val currency = if (campaign.currencyid != null) campaign.currencyid else ""
            val cell32 = buildCellValue(currency)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))

            val assignUserLink = if (campaign.assignuser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURl, campaign.assignuser) else ""
            val assignUserAvatarLink = StorageUtils.getAvatarPath(campaign.assignUserAvatarId, 16)
            val cell34 = buildCellLink(assignUserLink, assignUserAvatarLink, campaign.assignUserFullName)
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipBuilder.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_EXPECTED_COST))
            val cell42 = buildCellValue(campaign.expectedcost)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_BUDGET))
            val cell44 = buildCellValue(campaign.budget)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipBuilder.appendRow(trRow4)

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_EXPECTED_REVENUE))
            val cell52 = buildCellValue(campaign.expectedrevenue)
            val cell53 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_ACTUAL_COST))
            val cell54 = buildCellValue(campaign.actualcost)
            trRow5.appendChild(cell51, cell52, cell53, cell54)
            tooltipBuilder.appendRow(trRow5)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell62 = buildCellValue(trimHtmlTags(campaign.description))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipBuilder.appendRow(trRow6)

            return tooltipBuilder.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate Camapgin tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateTooltipLead(locale: Locale, lead: SimpleLead?, siteURl: String, userTimeZone: TimeZone): String? {
        if (lead == null)
            return generateTooltipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(lead.leadName)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_FIRSTNAME))
            val cell12 = buildCellValue(lead.firstname)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL))
            val emailLink = if (lead.email != null) "mailto:" + lead.email else ""
            val cell14 = buildCellLink(emailLink, lead.email)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_LASTNAME))
            val cell22 = buildCellValue(lead.lastname)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_OFFICE_PHONE))
            val cell24 = buildCellValue(lead.officephone)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_TITLE))
            val cell32 = buildCellValue(lead.title)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_MOBILE))
            val cell34 = buildCellValue(lead.mobile)
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_DEPARTMENT))
            val cell42 = buildCellValue(lead.department)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_FAX))
            val cell44 = buildCellValue(lead.fax)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipManager.appendRow(trRow4)

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_ACCOUNT_NAME))
            val cell52 = buildCellValue(lead.accountname)
            val cell53 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_WEBSITE))
            val cell54 = buildCellLink(getStringBaseNullCondition(lead.website), lead.website)
            trRow5.appendChild(cell51, cell52, cell53, cell54)
            tooltipManager.appendRow(trRow5)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_LEAD_SOURCE))
            val cell62 = buildCellValue(lead.leadsourcedesc)
            val cell63 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))

            val assignUserLink = if (lead.assignuser != null)
                AccountLinkGenerator
                        .generatePreviewFullUserLink(siteURl, lead.assignuser)
            else
                ""
            val assignUserAvatarLink = StorageUtils.getAvatarPath(lead.assignUserAvatarId, 16)
            val cell64 = buildCellLink(assignUserLink, assignUserAvatarLink, lead.assignUserFullName)
            trRow6.appendChild(cell61, cell62, cell63, cell64)
            tooltipManager.appendRow(trRow6)

            val trRow7 = Tr()
            val cell71 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_PRIMARY_ADDRESS))
            val cell72 = buildCellValue(lead.primaddress)
            val cell73 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_OTHER_ADDRESS))
            val cell74 = buildCellValue(lead.otheraddress)
            trRow7.appendChild(cell71, cell72, cell73, cell74)

            val trRow8 = Tr()
            val cell81 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE))
            val cell82 = buildCellValue(lead.primpostalcode)
            val cell83 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_OTHER_POSTAL_CODE))
            val cell84 = buildCellValue(lead.otherpostalcode)
            trRow8.appendChild(cell81, cell82, cell83, cell84)
            tooltipManager.appendRow(trRow8)

            val trRow9 = Tr()
            val cell91 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell92 = buildCellValue(trimHtmlTags(lead.description))
            cell92.setAttribute("colspan", "3")
            trRow9.appendChild(cell91, cell92)
            tooltipManager.appendRow(trRow9)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate Lead tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateTooltipOpportunity(locale: Locale, dateFormat: String, opportunity: SimpleOpportunity?, siteURl: String, userTimeZone: TimeZone): String? {
        if (opportunity == null)
            return generateTooltipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(opportunity.opportunityname)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CURRENCY))
            val currency = if (opportunity.currencyid != null) opportunity.currencyid else ""
            val cell12 = buildCellValue(currency)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_ACCOUNT_NAME))
            val accountLink = if (opportunity.accountid != null) CrmLinkGenerator.generateAccountPreviewFullLink(siteURl, opportunity.accountid) else ""
            val cell14 = buildCellLink(accountLink, opportunity.accountName)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_AMOUNT))
            val cell22 = buildCellValue(opportunity.amount)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE))
            val expectedClosedDate = DateTimeUtils.convertToStringWithUserTimeZone(
                    opportunity.expectedcloseddate, dateFormat, locale, userTimeZone)
            val cell24 = buildCellValue(expectedClosedDate)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_SALE_STAGE))
            val cell32 = buildCellValue(opportunity.salesstage)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_LEAD_SOURCE))
            val cell34 = buildCellValue(opportunity.source)
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_PROBABILITY))
            val cell42 = buildCellValue(opportunity.probability)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_CAMPAIGN_NAME))
            val campaignLink = if (opportunity.campaignid != null) CrmLinkGenerator.generateCampaignPreviewFullLink(siteURl, opportunity.campaignid) else ""
            val cell44 = buildCellLink(campaignLink, opportunity.campaignName)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipManager.appendRow(trRow4)

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_NEXT_STEP))
            val cell52 = buildCellValue(opportunity.nextstep)
            val cell53 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (opportunity.assignuser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURl, opportunity.assignuser) else ""
            val assignUserAvatarLink = StorageUtils.getAvatarPath(opportunity.assignUserAvatarId, 16)
            val cell54 = buildCellLink(assignUserLink, assignUserAvatarLink, opportunity.assignUserFullName)
            trRow5.appendChild(cell51, cell52, cell53, cell54)
            tooltipManager.appendRow(trRow5)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell62 = buildCellValue(trimHtmlTags(opportunity.description))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipManager.appendRow(trRow6)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate Opportunity tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateTooltipCases(locale: Locale, cases: SimpleCase?, siteURL: String, userTimeZone: TimeZone): String? {
        if (cases == null)
            return generateTooltipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(cases.subject)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_PRIORITY))
            val cell12 = buildCellValue(cases.priority)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_TYPE))
            val cell14 = buildCellValue(cases.type)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell22 = buildCellValue(cases.status)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_REASON))
            val cell24 = buildCellValue(cases.reason)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_ACCOUNT))
            val accountLink = if (cases.accountid != null) CrmLinkGenerator.generateAccountPreviewFullLink(siteURL, cases.accountid) else ""
            val cell32 = buildCellLink(accountLink, cases.accountName)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL))
            val emailLink = if (cases.email != null) String.format("mailto:%s", cases.email) else ""
            val cell34 = buildCellLink(emailLink, cases.email)
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_PHONE))
            val cell42 = buildCellValue(cases.phonenumber)
            val cell43 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (cases.assignuser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURL, cases.assignuser) else ""
            val assignUserAvatarLink = StorageUtils.getAvatarPath(cases.assignUserAvatarId, 16)
            val cell44 = buildCellLink(assignUserLink, assignUserAvatarLink, cases.assignUserFullName)
            trRow4.appendChild(cell41, cell42, cell43, cell44)
            tooltipManager.appendRow(trRow4)

            val trRow5 = Tr()
            val cell51 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell52 = buildCellValue(trimHtmlTags(cases.description))
            cell52.setAttribute("colspan", "3")
            trRow5.appendChild(cell51, cell52)
            tooltipManager.appendRow(trRow5)

            val trRow6 = Tr()
            val cell61 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_RESOLUTION))
            val cell62 = buildCellValue(trimHtmlTags(cases.resolution))
            cell62.setAttribute("colspan", "3")
            trRow6.appendChild(cell61, cell62)
            tooltipManager.appendRow(trRow6)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate Case tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipMeeting(locale: Locale, dateTimeFormat: String, meeting: SimpleMeeting?, siteUrl: String, userTimeZone: TimeZone): String? {
        if (meeting == null)
            return generateTooltipNull(locale)
        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(meeting.subject)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, MeetingI18nEnum.FORM_START_DATE_TIME))
            val startDateTime = DateTimeUtils.convertToStringWithUserTimeZone(meeting.startdate, dateTimeFormat, locale, userTimeZone)
            val cell12 = buildCellValue(startDateTime)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell14 = buildCellValue(meeting.status)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, MeetingI18nEnum.FORM_END_DATE_TIME))
            val endDateTime = DateTimeUtils.convertToStringWithUserTimeZone(meeting.enddate, dateTimeFormat, locale, userTimeZone)
            val cell22 = buildCellValue(endDateTime)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, MeetingI18nEnum.FORM_LOCATION))
            val cell24 = buildCellValue(meeting.location)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell32 = buildCellValue(trimHtmlTags(meeting.description))
            cell32.setAttribute("colspan", "3")
            trRow3.appendChild(cell31, cell32)
            tooltipManager.appendRow(trRow3)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate CRM Meeting servlert tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipCall(locale: Locale, dateFormat: String, call: SimpleCall?, siteURL: String, userTimeZone: TimeZone): String? {
        if (call == null)
            return generateTooltipNull(locale)
        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(call.subject)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, CallI18nEnum.FORM_START_DATE_TIME))
            val datetime = DateTimeUtils.convertToStringWithUserTimeZone(call.startdate, dateFormat, locale, userTimeZone)
            val cell12 = buildCellValue(datetime)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell14 = buildCellValue(call.status)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DURATION))
            val cell22 = buildCellValue(call.durationinseconds)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, CallI18nEnum.FORM_PURPOSE))
            val cell24 = buildCellValue(call.purpose)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell32 = buildCellValue(trimHtmlTags(call.description))
            cell32.setAttribute("colspan", "3")
            trRow3.appendChild(cell31, cell32)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, CallI18nEnum.FORM_RESULT))
            val cell42 = buildCellValue(trimHtmlTags(call.result))
            cell42.setAttribute("colspan", "3")
            trRow4.appendChild(cell41, cell42)
            tooltipManager.appendRow(trRow4)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate CRM Call servlert tooltip servlet", e)
            return null
        }

    }

    @JvmStatic
    fun generateToolTipCrmTask(locale: Locale, dateFormat: String, task: SimpleCrmTask?, siteURL: String,
                               userTimeZone: TimeZone): String? {
        if (task == null)
            return generateTooltipNull(locale)

        try {
            val tooltipManager = TooltipBuilder()
            tooltipManager.appendTitle(task.subject)

            val trRow1 = Tr()
            val cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE))
            val startDate = DateTimeUtils.convertToStringWithUserTimeZone(task.startdate, dateFormat, locale, userTimeZone)
            val cell12 = buildCellValue(startDate)
            val cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS))
            val cell14 = buildCellValue(task.status)
            trRow1.appendChild(cell11, cell12, cell13, cell14)
            tooltipManager.appendRow(trRow1)

            val trRow2 = Tr()
            val cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE))
            val dueDate = DateTimeUtils.convertToStringWithUserTimeZone(task.duedate, dateFormat, locale, userTimeZone)
            val cell22 = buildCellValue(dueDate)
            val cell23 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_CONTACT))
            val contactLink = if (task.contactid != null) CrmLinkGenerator.generateContactPreviewFullLink(siteURL, task.contactid) else ""
            val cell24 = buildCellLink(contactLink, task.contactName)
            trRow2.appendChild(cell21, cell22, cell23, cell24)
            tooltipManager.appendRow(trRow2)

            val trRow3 = Tr()
            val cell31 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_PRIORITY))
            val cell32 = buildCellValue(task.priority)
            val cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE))
            val assignUserLink = if (task.assignuser != null) AccountLinkGenerator.generatePreviewFullUserLink(siteURL, task.assignuser) else ""
            val assignUserAvatarLink = StorageUtils.getAvatarPath(task.assignUserAvatarId, 16)
            val cell34 = buildCellLink(assignUserLink, assignUserAvatarLink, task.assignUserFullName)
            trRow3.appendChild(cell31, cell32, cell33, cell34)
            tooltipManager.appendRow(trRow3)

            val trRow4 = Tr()
            val cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION))
            val cell42 = buildCellValue(trimHtmlTags(task.description))
            cell42.setAttribute("colspan", "3")
            trRow4.appendChild(cell41, cell42)
            tooltipManager.appendRow(trRow4)

            return tooltipManager.create().write()
        } catch (e: Exception) {
            LOG.error("Error while generate CRM Task tooltip servlet", e)
            return null
        }
    }
}
