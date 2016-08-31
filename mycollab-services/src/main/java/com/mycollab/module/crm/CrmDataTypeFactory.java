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

import com.mycollab.module.crm.i18n.OptionI18nEnum.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CrmDataTypeFactory {
    private static AccountIndustry[] ACCOUNT_INDUSTRY_LIST = new AccountIndustry[]{AccountIndustry.Apparel,
            AccountIndustry.Banking, AccountIndustry.Biotechnology, AccountIndustry.Chemicals, AccountIndustry.Communications,
            AccountIndustry.Construction, AccountIndustry.Consulting, AccountIndustry.Education, AccountIndustry.Electronics,
            AccountIndustry.Energy, AccountIndustry.Engineering, AccountIndustry.Entertainment, AccountIndustry.Environmental,
            AccountIndustry.Finance, AccountIndustry.Government, AccountIndustry.Healthcare, AccountIndustry.Hospitality,
            AccountIndustry.Insurance, AccountIndustry.Machinery, AccountIndustry.Manufactory, AccountIndustry.Media,
            AccountIndustry.Not_For_Profit, AccountIndustry.Retail, AccountIndustry.Shipping, AccountIndustry.Technology,
            AccountIndustry.Telecommunications, AccountIndustry.Other};

    private static final List<AccountType> ACCOUNT_TYPE_LIST = Arrays.asList(
            AccountType.Analyst, AccountType.Competitor, AccountType.Customer,
            AccountType.Integrator, AccountType.Investor, AccountType.Partner,
            AccountType.Press, AccountType.Prospect, AccountType.Reseller,
            AccountType.Other);

    private static OpportunityLeadSource[] LEAD_SOURCE_LIST = new OpportunityLeadSource[]{OpportunityLeadSource.Cold_Call,
            OpportunityLeadSource.Existing_Customer, OpportunityLeadSource.Self_Generated, OpportunityLeadSource.Employee,
            OpportunityLeadSource.Partner, OpportunityLeadSource.Public_Relations, OpportunityLeadSource.Direct_Email,
            OpportunityLeadSource.Conference, OpportunityLeadSource.Trade_Show, OpportunityLeadSource.Website,
            OpportunityLeadSource.Word_of_mouth, OpportunityLeadSource.Email, OpportunityLeadSource.Campaign,
            OpportunityLeadSource.Other};

    private static LeadStatus[] LEAD_STATUS_LIST = new LeadStatus[]{LeadStatus.New,
            LeadStatus.Assigned, LeadStatus.In_Process, LeadStatus.Converted, LeadStatus.Recycled, LeadStatus.Dead};

    private static CampaignStatus[] CAMPAIGN_STATUS_LIST = new CampaignStatus[]{CampaignStatus.Planning,
            CampaignStatus.Active, CampaignStatus.Inactive, CampaignStatus.Completed, CampaignStatus.In_Queue};

    private static CampaignType[] CAMPAIGN_TYPE_LIST = new CampaignType[]{CampaignType.Conference,
            CampaignType.Webinar, CampaignType.Trade_Show, CampaignType.Public_Relations, CampaignType.Partners,
            CampaignType.Referral_Program, CampaignType.Advertisement, CampaignType.Banner_Ads, CampaignType.Direct_Email,
            CampaignType.Mail, CampaignType.Telemarketing, CampaignType.Other};

    private static OpportunitySalesStage[] OPPORTUNITY_SALES_STAGE_LIST = new OpportunitySalesStage[]{
            OpportunitySalesStage.Prospecting, OpportunitySalesStage.Qualification, OpportunitySalesStage.Need_Analysis,
            OpportunitySalesStage.Value_Proposition, OpportunitySalesStage.Perception_Analysis,
            OpportunitySalesStage.Proposal_Price_Quote, OpportunitySalesStage.Negotiation_Review,
            OpportunitySalesStage.Closed_Won, OpportunitySalesStage.Closed_Lost};

    private static OpportunityContactRole[] OPPORTUNITY_CONTACT_ROLE_LIST = new OpportunityContactRole[]{
            OpportunityContactRole.Primary_Decision_Marker, OpportunityContactRole.Evaluator,
            OpportunityContactRole.Influencer, OpportunityContactRole.Other};

    private static OpportunityType[] OPPORTUNITY_TYPE_LIST = new OpportunityType[]{
            OpportunityType.Existing_Business, OpportunityType.New_Business};

    private static CaseStatus[] CASES_STATUS_LIST = new CaseStatus[]{CaseStatus.New,
            CaseStatus.Assigned, CaseStatus.Closed, CaseStatus.Pending_Input, CaseStatus.Rejected, CaseStatus.Duplicate};

    private static CasePriority[] CASES_PRIORITY_LIST = new CasePriority[]{CasePriority.High,
            CasePriority.Medium, CasePriority.Low};

    private static CaseReason[] CASES_REASON_LIST = new CaseReason[]{
            CaseReason.User_did_not_attend_any_training, CaseReason.Complex_functionality,
            CaseReason.New_problem, CaseReason.Ambiguous_instruction};

    private static CaseOrigin[] CASES_ORIGIN_LIST = new CaseOrigin[]{CaseOrigin.Email,
            CaseOrigin.Phone, CaseOrigin.Web, CaseOrigin.Error_Log};

    private static CaseType[] CASES_TYPE_LIST = new CaseType[]{CaseType.Problem,
            CaseType.Feature_Request, CaseType.Question};

    private static TaskPriority[] TASK_PRIORITY_LIST = new TaskPriority[]{TaskPriority.High,
            TaskPriority.Medium, TaskPriority.Low};

    private static TaskStatus[] TASK_STATUS_LIST = new TaskStatus[]{TaskStatus.Not_Started,
            TaskStatus.In_Progress, TaskStatus.Completed, TaskStatus.Pending_Input, TaskStatus.Deferred};

    public static AccountIndustry[] getAccountIndustryList() {
        return ACCOUNT_INDUSTRY_LIST;
    }

    public static List<? extends Enum<?>> getAccountTypeList() {
        return ACCOUNT_TYPE_LIST;
    }

    public static OpportunityLeadSource[] getLeadSourceList() {
        return LEAD_SOURCE_LIST;
    }

    public static LeadStatus[] getLeadStatusList() {
        return LEAD_STATUS_LIST;
    }

    public static CampaignStatus[] getCampaignStatusList() {
        return CAMPAIGN_STATUS_LIST;
    }

    public static CampaignType[] getCampaignTypeList() {
        return CAMPAIGN_TYPE_LIST;
    }

    public static OpportunitySalesStage[] getOpportunitySalesStageList() {
        return OPPORTUNITY_SALES_STAGE_LIST;
    }

    public static OpportunityContactRole[] getOpportunityContactRoleList() {
        return OPPORTUNITY_CONTACT_ROLE_LIST;
    }

    public static OpportunityType[] getOpportunityTypeList() {
        return OPPORTUNITY_TYPE_LIST;
    }

    public static CaseStatus[] getCasesStatusList() {
        return CASES_STATUS_LIST;
    }

    public static CasePriority[] getCasesPriorityList() {
        return CASES_PRIORITY_LIST;
    }

    public static CaseReason[] getCasesReason() {
        return CASES_REASON_LIST;
    }

    public static CaseOrigin[] getCasesOrigin() {
        return CASES_ORIGIN_LIST;
    }

    public static CaseType[] getCasesType() {
        return CASES_TYPE_LIST;
    }

    public static TaskPriority[] getTaskPriorities() {
        return TASK_PRIORITY_LIST;
    }

    public static TaskStatus[] getTaskStatuses() {
        return TASK_STATUS_LIST;
    }
}
