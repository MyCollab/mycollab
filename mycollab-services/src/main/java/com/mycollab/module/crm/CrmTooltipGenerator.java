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
package com.mycollab.module.crm;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Tr;
import com.mycollab.common.TooltipBuilder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.crm.domain.*;
import com.mycollab.module.crm.i18n.*;
import com.mycollab.module.user.AccountLinkGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.TimeZone;

import static com.mycollab.common.TooltipBuilder.*;
import static com.mycollab.core.utils.StringUtils.trimHtmlTags;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmTooltipGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(CrmTooltipGenerator.class);

    private static String generateTolltipNull(Locale locale) {
        Div div = new Div();
        Table table = new Table();
        table.setStyle("padding-left:10px;  color: #5a5a5a; font-size:11px;");

        Tr trRow1 = new Tr();
        trRow1.appendChild(new Td().setStyle(
                "vertical-align: top; text-align: left;").appendText(
                LocalizationHelper.getMessage(locale, GenericI18Enum.TOOLTIP_NO_ITEM_EXISTED)));

        table.appendChild(trRow1);
        div.appendChild(table);

        return div.write();
    }

    private static String getStringBaseNullCondition(Object value) {
        return (value != null) ? value.toString() : "";
    }

    public static String generateToolTipAccount(Locale locale, SimpleAccount account, String siteURL) {
        if (account == null) {
            return generateTolltipNull(locale);
        }

        try {
            TooltipBuilder tooltipBuilder = new TooltipBuilder();
            tooltipBuilder.appendTitle(account.getAccountname());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale,
                    AccountI18nEnum.FORM_WEBSITE) + ": ");

            Td cell12 = buildCellLink(
                    getStringBaseNullCondition(account.getWebsite()),
                    account.getWebsite());

            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, AccountI18nEnum.FORM_OFFICE_PHONE));
            Td cell14 = buildCellValue(account.getPhoneoffice());

            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipBuilder.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, AccountI18nEnum.FORM_EMPLOYEES));

            Td cell22 = buildCellValue(getStringBaseNullCondition(account
                    .getNumemployees()));

            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL));

            String emailLink = (account.getEmail() != null) ? String.format(
                    "mailto: %s", account.getEmail()) : "";
            Td cell24 = buildCellLink(emailLink, account.getEmail());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipBuilder.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));

            String userLink = (account.getAssignuser() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL, account.getAssignuser()) : "";
            String userAvatarLink = StorageFactory.getAvatarPath(account.getAssignUserAvatarId(), 16);
            Td cell32 = buildCellLink(userLink, userAvatarLink, account.getAssignUserFullName());

            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
                    AccountI18nEnum.FORM_ANNUAL_REVENUE));

            Td cell34 = buildCellValue(account.getAnnualrevenue());
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipBuilder.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell42 = buildCellValue(trimHtmlTags(account.getDescription()));
            cell42.setAttribute("colspan", "3");
            trRow4.appendChild(cell41, cell42);
            tooltipBuilder.appendRow(trRow4);

            return tooltipBuilder.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate Account tooltip servlet", e);
            return null;
        }
    }

    public static String generateToolTipContact(Locale locale, String dateFormat, SimpleContact contact, String siteURL,
                                                TimeZone userTimeZone) {
        if (contact == null) {
            return generateTolltipNull(locale);
        }

        try {
            TooltipBuilder tooltipBuilder = new TooltipBuilder();
            tooltipBuilder.appendTitle(contact.getContactName());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_FIRSTNAME));
            Td cell12 = buildCellValue(contact.getFirstname());
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_OFFICE_PHONE));
            Td cell14 = buildCellValue(contact.getOfficephone());
            trRow1.appendChild(cell11, cell12, cell13, cell14);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_LASTNAME));
            Td cell22 = buildCellValue(contact.getLastname());
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_MOBILE));
            Td cell24 = buildCellValue(contact.getMobile());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipBuilder.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL));

            String contactEmailLink = (contact.getEmail() != null) ? String.format("mailto:%s", contact.getEmail()) : "";
            Td cell32 = buildCellLink(contactEmailLink, contact.getEmail());

            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale,
                    ContactI18nEnum.FORM_BIRTHDAY));
            String birthday = DateTimeUtils.convertToStringWithUserTimeZone(contact.getBirthday(), dateFormat, userTimeZone);
            Td cell34 = buildCellValue(birthday);

            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipBuilder.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_DEPARTMENT));
            Td cell42 = buildCellValue(contact.getDepartment());
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (contact.getAssignuser() != null) ? AccountLinkGenerator
                    .generatePreviewFullUserLink(siteURL,
                            contact.getAssignuser()) : "";
            String assignAvatarLink = StorageFactory.getAvatarPath(
                    contact.getAssignUserAvatarId(), 16);
            Td cell44 = buildCellLink(assignUserLink, assignAvatarLink, contact.getAssignUserFullName());
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipBuilder.appendRow(trRow4);

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_PRIMARY_ADDRESS));
            Td cell52 = buildCellValue(contact.getPrimaddress());
            Td cell53 = buildCellName(LocalizationHelper.getMessage(locale, ContactI18nEnum.FORM_OTHER_ADDRESS));
            Td cell54 = buildCellValue(contact.getOtheraddress());
            trRow5.appendChild(cell51, cell52, cell53, cell54);
            tooltipBuilder.appendRow(trRow5);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell62 = buildCellValue(trimHtmlTags(contact.getDescription()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipBuilder.appendRow(trRow6);

            return tooltipBuilder.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate Contact tooltip servlet", e);
            return null;
        }
    }

    public static String generateTooltipCampaign(Locale locale, String dateFormat, SimpleCampaign campaign, String siteURl, TimeZone userTimeZone) {
        if (campaign == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipBuilder = new TooltipBuilder();
            tooltipBuilder.appendTitle(campaign.getCampaignname());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE));
            String startDate = DateTimeUtils.convertToStringWithUserTimeZone(
                    campaign.getStartdate(), dateFormat, userTimeZone);
            Td cell12 = buildCellValue(startDate);
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell14 = buildCellValue(campaign.getStatus());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipBuilder.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_END_DATE));
            String endDate = DateTimeUtils.convertToStringWithUserTimeZone(campaign.getEnddate(), dateFormat, userTimeZone);
            Td cell22 = buildCellValue(endDate);
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_TYPE));
            Td cell24 = buildCellValue(campaign.getType());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipBuilder.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CURRENCY));
            String currency = (campaign.getCurrencyid() != null) ? campaign.getCurrencyid() : "";
            Td cell32 = buildCellValue(currency);
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));

            String assignUserLink = (campaign.getAssignuser() != null) ? AccountLinkGenerator
                    .generatePreviewFullUserLink(siteURl, campaign.getAssignuser()) : "";
            String assignUserAvatarLink = StorageFactory.getAvatarPath(campaign.getAssignUserAvatarId(), 16);
            Td cell34 = buildCellLink(assignUserLink, assignUserAvatarLink, campaign.getAssignUserFullName());
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipBuilder.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_EXPECTED_COST));
            Td cell42 = buildCellValue(campaign.getExpectedcost());
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_BUDGET));
            Td cell44 = buildCellValue(campaign.getBudget());
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipBuilder.appendRow(trRow4);

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_EXPECTED_REVENUE));
            Td cell52 = buildCellValue(campaign.getExpectedrevenue());
            Td cell53 = buildCellName(LocalizationHelper.getMessage(locale, CampaignI18nEnum.FORM_ACTUAL_COST));
            Td cell54 = buildCellValue(campaign.getActualcost());
            trRow5.appendChild(cell51, cell52, cell53, cell54);
            tooltipBuilder.appendRow(trRow5);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell62 = buildCellValue(trimHtmlTags(campaign.getDescription()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipBuilder.appendRow(trRow6);

            return tooltipBuilder.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate Camapgin tooltip servlet", e);
            return null;
        }
    }

    public static String generateTooltipLead(Locale locale, SimpleLead lead, String siteURl, TimeZone userTimeZone) {
        if (lead == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(lead.getLeadName());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_FIRSTNAME));
            Td cell12 = buildCellValue(lead.getFirstname());
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL));
            String emailLink = (lead.getEmail() != null) ? "mailto:" + lead.getEmail() : "";
            Td cell14 = buildCellLink(emailLink, lead.getEmail());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_LASTNAME));
            Td cell22 = buildCellValue(lead.getLastname());
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_OFFICE_PHONE));
            Td cell24 = buildCellValue(lead.getOfficephone());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_TITLE));
            Td cell32 = buildCellValue(lead.getTitle());
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_MOBILE));
            Td cell34 = buildCellValue(lead.getMobile());
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_DEPARTMENT));
            Td cell42 = buildCellValue(lead.getDepartment());
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_FAX));
            Td cell44 = buildCellValue(lead.getFax());
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipManager.appendRow(trRow4);

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_ACCOUNT_NAME));
            Td cell52 = buildCellValue(lead.getAccountname());
            Td cell53 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_WEBSITE));
            Td cell54 = buildCellLink(getStringBaseNullCondition(lead.getWebsite()), lead.getWebsite());
            trRow5.appendChild(cell51, cell52, cell53, cell54);
            tooltipManager.appendRow(trRow5);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_LEAD_SOURCE));
            Td cell62 = buildCellValue(lead.getLeadsourcedesc());
            Td cell63 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));

            String assignUserLink = (lead.getAssignuser() != null) ? AccountLinkGenerator
                    .generatePreviewFullUserLink(siteURl, lead.getAssignuser())
                    : "";
            String assignUserAvatarLink = StorageFactory.getAvatarPath(lead.getAssignUserAvatarId(), 16);
            Td cell64 = buildCellLink(assignUserLink, assignUserAvatarLink, lead.getAssignUserFullName());
            trRow6.appendChild(cell61, cell62, cell63, cell64);
            tooltipManager.appendRow(trRow6);

            Tr trRow7 = new Tr();
            Td cell71 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_PRIMARY_ADDRESS));
            Td cell72 = buildCellValue(lead.getPrimaddress());
            Td cell73 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_OTHER_ADDRESS));
            Td cell74 = buildCellValue(lead.getOtheraddress());
            trRow7.appendChild(cell71, cell72, cell73, cell74);

            Tr trRow8 = new Tr();
            Td cell81 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE));
            Td cell82 = buildCellValue(lead.getPrimpostalcode());
            Td cell83 = buildCellName(LocalizationHelper.getMessage(locale, LeadI18nEnum.FORM_OTHER_POSTAL_CODE));
            Td cell84 = buildCellValue(lead.getOtherpostalcode());
            trRow8.appendChild(cell81, cell82, cell83, cell84);
            tooltipManager.appendRow(trRow8);

            Tr trRow9 = new Tr();
            Td cell91 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell92 = buildCellValue(trimHtmlTags(lead.getDescription()));
            cell92.setAttribute("colspan", "3");
            trRow9.appendChild(cell91, cell92);
            tooltipManager.appendRow(trRow9);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate Lead tooltip servlet", e);
            return null;
        }
    }

    public static String generateTooltipOpportunity(Locale locale, String dateFormat, SimpleOpportunity opportunity, String siteURl, TimeZone userTimeZone) {
        if (opportunity == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(opportunity.getOpportunityname());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_CURRENCY));
            String currency = (opportunity.getCurrencyid() != null) ? opportunity.getCurrencyid() : "";
            Td cell12 = buildCellValue(currency);
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_ACCOUNT_NAME));
            String accountLink = (opportunity.getAccountid() != null) ? CrmLinkGenerator
                    .generateAccountPreviewFullLink(siteURl, opportunity.getAccountid()) : "";
            Td cell14 = buildCellLink(accountLink, opportunity.getAccountName());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_AMOUNT));
            Td cell22 = buildCellValue(opportunity.getAmount());
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE));
            String expectedClosedDate = DateTimeUtils.convertToStringWithUserTimeZone(
                    opportunity.getExpectedcloseddate(), dateFormat, userTimeZone);
            Td cell24 = buildCellValue(expectedClosedDate);
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_SALE_STAGE));
            Td cell32 = buildCellValue(opportunity.getSalesstage());
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_LEAD_SOURCE));
            Td cell34 = buildCellValue(opportunity.getSource());
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_PROBABILITY));
            Td cell42 = buildCellValue(opportunity.getProbability());
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_CAMPAIGN_NAME));
            String campaignLink = (opportunity.getCampaignid() != null) ? CrmLinkGenerator
                    .generateCampaignPreviewFullLink(siteURl, opportunity.getCampaignid()) : "";
            Td cell44 = buildCellLink(campaignLink, opportunity.getCampaignName());
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipManager.appendRow(trRow4);

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, OpportunityI18nEnum.FORM_NEXT_STEP));
            Td cell52 = buildCellValue(opportunity.getNextstep());
            Td cell53 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (opportunity.getAssignuser() != null) ? AccountLinkGenerator
                    .generatePreviewFullUserLink(siteURl, opportunity.getAssignuser()) : "";
            String assignUserAvatarLink = StorageFactory.getAvatarPath(opportunity.getAssignUserAvatarId(), 16);
            Td cell54 = buildCellLink(assignUserLink, assignUserAvatarLink,
                    opportunity.getAssignUserFullName());
            trRow5.appendChild(cell51, cell52, cell53, cell54);
            tooltipManager.appendRow(trRow5);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell62 = buildCellValue(trimHtmlTags(opportunity.getDescription()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipManager.appendRow(trRow6);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate Opportunity tooltip servlet", e);
            return null;
        }
    }

    public static String generateTooltipCases(Locale locale, SimpleCase cases, String siteURL, TimeZone userTimeZone) {
        if (cases == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(cases.getSubject());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_PRIORITY));
            Td cell12 = buildCellValue(cases.getPriority());
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_TYPE));
            Td cell14 = buildCellValue(cases.getType());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell22 = buildCellValue(cases.getStatus());
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_REASON));
            Td cell24 = buildCellValue(cases.getReason());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_ACCOUNT));
            String accountLink = (cases.getAccountid() != null) ? CrmLinkGenerator
                    .generateAccountPreviewFullLink(siteURL, cases.getAccountid()) : "";
            Td cell32 = buildCellLink(accountLink, cases.getAccountName());
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_EMAIL));
            String emailLink = (cases.getEmail() != null) ? String.format(
                    "mailto:%s", cases.getEmail()) : "";
            Td cell34 = buildCellLink(emailLink, cases.getEmail());
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_PHONE));
            Td cell42 = buildCellValue(cases.getPhonenumber());
            Td cell43 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (cases.getAssignuser() != null) ? AccountLinkGenerator
                    .generatePreviewFullUserLink(siteURL, cases.getAssignuser()) : "";
            String assignUserAvatarLink = StorageFactory.getAvatarPath(cases.getAssignUserAvatarId(), 16);
            Td cell44 = buildCellLink(assignUserLink, assignUserAvatarLink, cases.getAssignUserFullName());
            trRow4.appendChild(cell41, cell42, cell43, cell44);
            tooltipManager.appendRow(trRow4);

            Tr trRow5 = new Tr();
            Td cell51 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell52 = buildCellValue(trimHtmlTags(cases.getDescription()));
            cell52.setAttribute("colspan", "3");
            trRow5.appendChild(cell51, cell52);
            tooltipManager.appendRow(trRow5);

            Tr trRow6 = new Tr();
            Td cell61 = buildCellName(LocalizationHelper.getMessage(locale, CaseI18nEnum.FORM_RESOLUTION));
            Td cell62 = buildCellValue(trimHtmlTags(cases.getResolution()));
            cell62.setAttribute("colspan", "3");
            trRow6.appendChild(cell61, cell62);
            tooltipManager.appendRow(trRow6);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate Case tooltip servlet", e);
            return null;
        }
    }

    public static String generateToolTipMeeting(Locale locale, String dateTimeFormat, SimpleMeeting meeting, String siteUrl, TimeZone userTimeZone) {
        if (meeting == null)
            return generateTolltipNull(locale);
        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(meeting.getSubject());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, MeetingI18nEnum.FORM_START_DATE_TIME));
            String startDateTime = DateTimeUtils.convertToStringWithUserTimeZone(meeting.getStartdate(),
                    dateTimeFormat, userTimeZone);
            Td cell12 = buildCellValue(startDateTime);
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell14 = buildCellValue(meeting.getStatus());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, MeetingI18nEnum.FORM_END_DATE_TIME));
            String endDateTime = DateTimeUtils.convertToStringWithUserTimeZone(
                    meeting.getEnddate(), dateTimeFormat, userTimeZone);
            Td cell22 = buildCellValue(endDateTime);
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, MeetingI18nEnum.FORM_LOCATION));
            Td cell24 = buildCellValue(meeting.getLocation());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell32 = buildCellValue(trimHtmlTags(meeting.getDescription()));
            cell32.setAttribute("colspan", "3");
            trRow3.appendChild(cell31, cell32);
            tooltipManager.appendRow(trRow3);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate CRM Meeting servlert tooltip servlet", e);
            return null;
        }
    }

    public static String generateToolTipCall(Locale locale, String dateFormat, SimpleCall call, String siteURL, TimeZone userTimeZone) {
        if (call == null)
            return generateTolltipNull(locale);
        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(call.getSubject());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, CallI18nEnum.FORM_START_DATE_TIME));
            String datetime = DateTimeUtils.convertToStringWithUserTimeZone(
                    call.getStartdate(), dateFormat, userTimeZone);
            Td cell12 = buildCellValue(datetime);
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell14 = buildCellValue(call.getStatus());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DURATION));
            Td cell22 = buildCellValue(call.getDurationinseconds());
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, CallI18nEnum.FORM_PURPOSE));
            Td cell24 = buildCellValue(call.getPurpose());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell32 = buildCellValue(trimHtmlTags(call.getDescription()));
            cell32.setAttribute("colspan", "3");
            trRow3.appendChild(cell31, cell32);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale,
                    CallI18nEnum.FORM_RESULT));
            Td cell42 = buildCellValue(trimHtmlTags(call.getResult()));
            cell42.setAttribute("colspan", "3");
            trRow4.appendChild(cell41, cell42);
            tooltipManager.appendRow(trRow4);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate CRM Call servlert tooltip servlet", e);
            return null;
        }
    }

    public static String generateToolTipCrmTask(Locale locale, String dateFormat, SimpleTask task, String siteURL,
                                                TimeZone userTimeZone) {
        if (task == null)
            return generateTolltipNull(locale);

        try {
            TooltipBuilder tooltipManager = new TooltipBuilder();
            tooltipManager.appendTitle(task.getSubject());

            Tr trRow1 = new Tr();
            Td cell11 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_START_DATE));
            String startDate = DateTimeUtils.convertToStringWithUserTimeZone(task.getStartdate(), dateFormat, userTimeZone);
            Td cell12 = buildCellValue(startDate);
            Td cell13 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_STATUS));
            Td cell14 = buildCellValue(task.getStatus());
            trRow1.appendChild(cell11, cell12, cell13, cell14);
            tooltipManager.appendRow(trRow1);

            Tr trRow2 = new Tr();
            Td cell21 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DUE_DATE));
            String dueDate = DateTimeUtils.convertToStringWithUserTimeZone(task.getDuedate(), dateFormat, userTimeZone);
            Td cell22 = buildCellValue(dueDate);
            Td cell23 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_CONTACT));
            String contactLink = (task.getContactid() != null) ? CrmLinkGenerator.generateContactPreviewFullLink(siteURL, task.getContactid()) : "";
            Td cell24 = buildCellLink(contactLink, task.getContactName());
            trRow2.appendChild(cell21, cell22, cell23, cell24);
            tooltipManager.appendRow(trRow2);

            Tr trRow3 = new Tr();
            Td cell31 = buildCellName(LocalizationHelper.getMessage(locale, TaskI18nEnum.FORM_PRIORITY));
            Td cell32 = buildCellValue(task.getPriority());
            Td cell33 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_ASSIGNEE));
            String assignUserLink = (task.getAssignuser() != null) ? AccountLinkGenerator.generatePreviewFullUserLink(siteURL, task.getAssignuser()) : "";
            String assignUserAvatarLink = StorageFactory.getAvatarPath(task.getAssignUserAvatarId(), 16);
            Td cell34 = buildCellLink(assignUserLink, assignUserAvatarLink, task.getAssignUserFullName());
            trRow3.appendChild(cell31, cell32, cell33, cell34);
            tooltipManager.appendRow(trRow3);

            Tr trRow4 = new Tr();
            Td cell41 = buildCellName(LocalizationHelper.getMessage(locale, GenericI18Enum.FORM_DESCRIPTION));
            Td cell42 = buildCellValue(trimHtmlTags(task.getDescription()));
            cell42.setAttribute("colspan", "3");
            trRow4.appendChild(cell41, cell42);
            tooltipManager.appendRow(trRow4);

            return tooltipManager.create().write();
        } catch (Exception e) {
            LOG.error("Error while generate CRM Task tooltip servlet", e);
            return null;
        }
    }
}
