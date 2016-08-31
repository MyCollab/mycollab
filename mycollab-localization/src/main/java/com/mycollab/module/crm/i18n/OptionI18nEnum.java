/**
 * This file is part of mycollab-localization.
 *
 * mycollab-localization is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-localization is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-localization.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * @author MyCollab Ltd.
 * @since 4.3.0
 */
public class OptionI18nEnum {

    @BaseName("crm-accounttype")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum AccountType {
        Analyst,
        Competitor,
        Customer,
        Integrator,
        Investor,
        Partner,
        Press,
        Prospect,
        Reseller,
        Other
    }

    @BaseName("crm-accountindustry")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum AccountIndustry {
        Apparel,
        Banking,
        Biotechnology,
        Chemicals,
        Communications,
        Construction,
        Consulting,
        Education,
        Electronics,
        Energy,
        Engineering,
        Entertainment,
        Environmental,
        Finance,
        Government,
        Healthcare,
        Hospitality,
        Insurance,
        Machinery,
        Manufactory,
        Media,
        Not_For_Profit,
        Retail,
        Shipping,
        Technology,
        Telecommunications,
        Other
    }

    @BaseName("crm-opportunitysalesstage")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum OpportunitySalesStage {
        Prospecting,
        Qualification,
        Need_Analysis,
        Value_Proposition,
        Perception_Analysis,
        Proposal_Price_Quote,
        Negotiation_Review,
        Closed_Won,
        Closed_Lost
    }

    @BaseName("crm-opportunityleadsource")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum OpportunityLeadSource {
        Cold_Call,
        Existing_Customer,
        Self_Generated,
        Employee,
        Partner,
        Public_Relations,
        Direct_Email,
        Conference,
        Trade_Show,
        Website,
        Word_of_mouth,
        Email,
        Campaign,
        Other
    }

    @BaseName("crm-opportunitytype")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum OpportunityType {
        Existing_Business,
        New_Business
    }

    @BaseName("crm-opportunitycontactrole")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum OpportunityContactRole {
        Primary_Decision_Marker,
        Evaluator,
        Influencer,
        Other
    }

    @BaseName("crm-leadstatus")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum LeadStatus {
        New,
        Assigned,
        In_Process,
        Converted,
        Recycled,
        Dead
    }

    @BaseName("crm-campaigntype")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CampaignType {
        Conference,
        Webinar,
        Trade_Show,
        Public_Relations,
        Partners,
        Referral_Program,
        Advertisement,
        Banner_Ads,
        Direct_Email,
        Mail,
        Telemarketing,
        Other
    }

    @BaseName("crm-campaignstatus")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CampaignStatus {
        Planning,
        Active,
        Inactive,
        Completed,
        In_Queue
    }

    @BaseName("crm-casestatus")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CaseStatus {
        New,
        Assigned,
        Closed,
        Pending_Input,
        Rejected,
        Duplicate
    }

    @BaseName("crm-casepriority")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CasePriority {
        High,
        Medium,
        Low
    }

    @BaseName("crm-caseorigin")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CaseOrigin {
        Email,
        Phone,
        Web,
        Error_Log
    }

    @BaseName("crm-casereason")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CaseReason {
        User_did_not_attend_any_training,
        Complex_functionality,
        New_problem,
        Ambiguous_instruction
    }

    @BaseName("crm-casetype")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum CaseType {
        Problem,
        Feature_Request,
        Question
    }

    @BaseName("crm-taskpriority")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum TaskPriority {
        High,
        Medium,
        Low
    }

    @BaseName("crm-taskstatus")
    @LocaleData(value = {@Locale("en-US")}, defaultCharset = "UTF-8")
    public enum TaskStatus {
        Not_Started,
        In_Progress,
        Completed,
        Pending_Input,
        Deferred
    }
}
