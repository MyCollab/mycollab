package com.mycollab.module.crm

import com.mycollab.module.crm.i18n.OptionI18nEnum.*
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
object CrmDataTypeFactory {
    @JvmField
    val accountIndustryList = arrayOf(AccountIndustry.Apparel, AccountIndustry.Banking, AccountIndustry.Biotechnology, AccountIndustry.Chemicals, AccountIndustry.Communications, AccountIndustry.Construction, AccountIndustry.Consulting, AccountIndustry.Education, AccountIndustry.Electronics, AccountIndustry.Energy, AccountIndustry.Engineering, AccountIndustry.Entertainment, AccountIndustry.Environmental, AccountIndustry.Finance, AccountIndustry.Government, AccountIndustry.Healthcare, AccountIndustry.Hospitality, AccountIndustry.Insurance, AccountIndustry.Machinery, AccountIndustry.Manufactory, AccountIndustry.Media, AccountIndustry.Not_For_Profit, AccountIndustry.Retail, AccountIndustry.Shipping, AccountIndustry.Technology, AccountIndustry.Telecommunications, AccountIndustry.Other)

    private val ACCOUNT_TYPE_LIST = Arrays.asList(
            AccountType.Analyst, AccountType.Competitor, AccountType.Customer,
            AccountType.Integrator, AccountType.Investor, AccountType.Partner,
            AccountType.Press, AccountType.Prospect, AccountType.Reseller,
            AccountType.Other)

    @JvmField
    val leadSourceList = arrayOf(OpportunityLeadSource.Cold_Call, OpportunityLeadSource.Existing_Customer, OpportunityLeadSource.Self_Generated, OpportunityLeadSource.Employee, OpportunityLeadSource.Partner, OpportunityLeadSource.Public_Relations, OpportunityLeadSource.Direct_Email, OpportunityLeadSource.Conference, OpportunityLeadSource.Trade_Show, OpportunityLeadSource.Website, OpportunityLeadSource.Word_of_mouth, OpportunityLeadSource.Email, OpportunityLeadSource.Campaign, OpportunityLeadSource.Other)

    @JvmField
    val leadStatusList = arrayOf(LeadStatus.New, LeadStatus.Assigned, LeadStatus.In_Process, LeadStatus.Converted, LeadStatus.Recycled, LeadStatus.Dead)

    @JvmField
    val campaignStatusList = arrayOf(CampaignStatus.Planning, CampaignStatus.Active, CampaignStatus.Inactive, CampaignStatus.Completed, CampaignStatus.In_Queue)

    @JvmField
    val campaignTypeList = arrayOf(CampaignType.Conference, CampaignType.Webinar, CampaignType.Trade_Show, CampaignType.Public_Relations, CampaignType.Partners, CampaignType.Referral_Program, CampaignType.Advertisement, CampaignType.Banner_Ads, CampaignType.Direct_Email, CampaignType.Mail, CampaignType.Telemarketing, CampaignType.Other)

    @JvmField
    val opportunitySalesStageList = arrayOf(OpportunitySalesStage.Prospecting, OpportunitySalesStage.Qualification, OpportunitySalesStage.Need_Analysis, OpportunitySalesStage.Value_Proposition, OpportunitySalesStage.Perception_Analysis, OpportunitySalesStage.Proposal_Price_Quote, OpportunitySalesStage.Negotiation_Review, OpportunitySalesStage.Closed_Won, OpportunitySalesStage.Closed_Lost)

    @JvmField
    val opportunityContactRoleList = arrayOf(OpportunityContactRole.Primary_Decision_Marker, OpportunityContactRole.Evaluator, OpportunityContactRole.Influencer, OpportunityContactRole.Other)

    @JvmField
    val opportunityTypeList = arrayOf(OpportunityType.Existing_Business, OpportunityType.New_Business)

    @JvmField
    val casesStatusList = arrayOf(CaseStatus.New, CaseStatus.Assigned, CaseStatus.Closed, CaseStatus.Pending_Input, CaseStatus.Rejected, CaseStatus.Duplicate)

    @JvmField
    val casesPriorityList = arrayOf(CasePriority.High, CasePriority.Medium, CasePriority.Low)

    @JvmField
    val casesReason = arrayOf(CaseReason.User_did_not_attend_any_training, CaseReason.Complex_functionality, CaseReason.New_problem, CaseReason.Ambiguous_instruction)

    @JvmField
    val casesOrigin = arrayOf(CaseOrigin.Email, CaseOrigin.Phone, CaseOrigin.Web, CaseOrigin.Error_Log)

    @JvmField
    val casesType = arrayOf(CaseType.Problem, CaseType.Feature_Request, CaseType.Question)

    @JvmField
    val taskPriorities = arrayOf(TaskPriority.High, TaskPriority.Medium, TaskPriority.Low)

    @JvmField
    val taskStatuses = arrayOf(TaskStatus.Not_Started, TaskStatus.In_Progress, TaskStatus.Completed, TaskStatus.Pending_Input, TaskStatus.Deferred)

    @JvmField
    val accountTypeList = CrmDataTypeFactory.ACCOUNT_TYPE_LIST
}
