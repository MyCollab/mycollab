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
package com.esofthead.mycollab.module.crm.view;

import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CallI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.ContactI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum;
import com.esofthead.mycollab.utils.AuditLogShowHandler;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmActivityStreamGenerator {

	private static AuditLogShowHandler accountHandler = new AccountAuditLogShowHandler();
	private static AuditLogShowHandler contactHandler = new ContactAuditLogShowHandler();
	private static AuditLogShowHandler campaignHandler = new CampaignAuditLogShowHandler();
	private static AuditLogShowHandler leadHandler = new LeadAuditLogShowHandler();
	private static AuditLogShowHandler opportunityHandler = new OpportunityAuditLogShowHandler();
	private static AuditLogShowHandler caseHandler = new CaseAuditLogShowHandler();
	private static AuditLogShowHandler meetingHandler = new MeetingAuditLogShowHandler();
	private static AuditLogShowHandler taskHandler = new TaskAuditLogShowHandler();
	private static AuditLogShowHandler callHandler = new CallAuditLogShowHandler();

	private static AuditLogShowHandler defaultHandler = new AuditLogShowHandler();

	private static AuditLogShowHandler getShowHandler(String type) {
		if (CrmTypeConstants.ACCOUNT.equals(type)) {
			return accountHandler;
		} else if (CrmTypeConstants.CONTACT.equals(type)) {
			return contactHandler;
		} else if (CrmTypeConstants.CAMPAIGN.equals(type)) {
			return campaignHandler;
		} else if (CrmTypeConstants.LEAD.equals(type)) {
			return leadHandler;
		} else if (CrmTypeConstants.OPPORTUNITY.equals(type)) {
			return opportunityHandler;
		} else if (CrmTypeConstants.CASE.equals(type)) {
			return caseHandler;
		} else if (CrmTypeConstants.MEETING.equals(type)) {
			return meetingHandler;
		} else if (CrmTypeConstants.TASK.equals(type)) {
			return taskHandler;
		} else if (CrmTypeConstants.CALL.equals(type)) {
			return callHandler;
		} else {
			return defaultHandler;
		}
	}

	public static String generatorDetailChangeOfActivity(
			SimpleActivityStream activityStream) {

		if (activityStream.getAssoAuditLog() != null) {
			AuditLogShowHandler auditLogHandler = getShowHandler(activityStream
					.getType());
			return auditLogHandler.generateChangeSet(activityStream
					.getAssoAuditLog());
		} else {
			return "";
		}
	}

	private static class AccountAuditLogShowHandler extends AuditLogShowHandler {
		public AccountAuditLogShowHandler() {
			this.generateFieldDisplayHandler("accountname",
					AccountI18nEnum.FORM_ACCOUNT_NAME);
			this.generateFieldDisplayHandler("phoneoffice",
					CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD);
			this.generateFieldDisplayHandler("website",
					AccountI18nEnum.FORM_WEBSITE);
			this.generateFieldDisplayHandler("fax", AccountI18nEnum.FORM_FAX);
			this.generateFieldDisplayHandler("numemployees",
					AccountI18nEnum.FORM_EMPLOYEES);
			this.generateFieldDisplayHandler("alternatephone",
					AccountI18nEnum.FORM_OTHER_PHONE);
			this.generateFieldDisplayHandler("industry",
					AccountI18nEnum.FORM_INDUSTRY);
			this.generateFieldDisplayHandler("email",
					AccountI18nEnum.FORM_EMAIL);
			this.generateFieldDisplayHandler("type", AccountI18nEnum.FORM_TYPE);
			this.generateFieldDisplayHandler("ownership",
					AccountI18nEnum.FORM_OWNERSHIP);
			this.generateFieldDisplayHandler("annualrevenue",
					AccountI18nEnum.FORM_ANNUAL_REVENUE);
			this.generateFieldDisplayHandler("billingaddress",
					AccountI18nEnum.FORM_BILLING_ADDRESS);
			this.generateFieldDisplayHandler("shippingaddress",
					AccountI18nEnum.FORM_SHIPPING_ADDRESS);
			this.generateFieldDisplayHandler("city",
					AccountI18nEnum.FORM_BILLING_CITY);
			this.generateFieldDisplayHandler("shippingcity",
					AccountI18nEnum.FORM_SHIPPING_CITY);
			this.generateFieldDisplayHandler("state",
					AccountI18nEnum.FORM_BILLING_STATE);
			this.generateFieldDisplayHandler("shippingstate",
					AccountI18nEnum.FORM_SHIPPING_STATE);
			this.generateFieldDisplayHandler("postalcode",
					AccountI18nEnum.FORM_BILLING_POSTAL_CODE);
			this.generateFieldDisplayHandler("shippingpostalcode",
					AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
		}
	}

	private static class ContactAuditLogShowHandler extends AuditLogShowHandler {
		public ContactAuditLogShowHandler() {
			this.generateFieldDisplayHandler("firstname",
					ContactI18nEnum.FORM_FIRSTNAME);
			this.generateFieldDisplayHandler("lastname",
					ContactI18nEnum.FORM_LASTNAME);
			this.generateFieldDisplayHandler("accountId",
					ContactI18nEnum.FORM_ACCOUNTS);
			this.generateFieldDisplayHandler("title",
					ContactI18nEnum.FORM_TITLE);
			this.generateFieldDisplayHandler("department",
					ContactI18nEnum.FORM_DEPARTMENT);
			this.generateFieldDisplayHandler("email",
					ContactI18nEnum.FORM_EMAIL);
			this.generateFieldDisplayHandler("assistant",
					ContactI18nEnum.FORM_ASSISTANT);
			this.generateFieldDisplayHandler("assistantphone",
					ContactI18nEnum.FORM_ASSISTANT_PHONE);
			this.generateFieldDisplayHandler("leadsource",
					ContactI18nEnum.FORM_LEAD_SOURCE);
			this.generateFieldDisplayHandler("officephone",
					CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD);
			this.generateFieldDisplayHandler("mobile",
					ContactI18nEnum.FORM_MOBILE);
			this.generateFieldDisplayHandler("homephone",
					ContactI18nEnum.FORM_HOME_PHONE);
			this.generateFieldDisplayHandler("otherphone",
					ContactI18nEnum.FORM_OTHER_PHONE);
			this.generateFieldDisplayHandler("birthday",
					ContactI18nEnum.FORM_BIRTHDAY,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("iscallable",
					ContactI18nEnum.FORM_IS_CALLABLE);
			this.generateFieldDisplayHandler("assignuser",
					GenericI18Enum.FORM_ASSIGNEE);
			this.generateFieldDisplayHandler("primaddress",
					ContactI18nEnum.FORM_PRIMARY_ADDRESS);
			this.generateFieldDisplayHandler("primcity",
					ContactI18nEnum.FORM_PRIMARY_CITY);
			this.generateFieldDisplayHandler("primstate",
					ContactI18nEnum.FORM_PRIMARY_STATE);
			this.generateFieldDisplayHandler("primpostalcode",
					ContactI18nEnum.FORM_PRIMARY_POSTAL_CODE);
			this.generateFieldDisplayHandler("primcountry",
					ContactI18nEnum.FORM_PRIMARY_COUNTRY);
			this.generateFieldDisplayHandler("otheraddress",
					ContactI18nEnum.FORM_OTHER_ADDRESS);
			this.generateFieldDisplayHandler("othercity",
					ContactI18nEnum.FORM_OTHER_CITY);
			this.generateFieldDisplayHandler("otherstate",
					ContactI18nEnum.FORM_OTHER_STATE);
			this.generateFieldDisplayHandler("otherpostalcode",
					ContactI18nEnum.FORM_OTHER_POSTAL_CODE);
			this.generateFieldDisplayHandler("othercountry",
					ContactI18nEnum.FORM_OTHER_COUNTRY);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
		}
	}

	private static class CampaignAuditLogShowHandler extends
			AuditLogShowHandler {
		public CampaignAuditLogShowHandler() {
			this.generateFieldDisplayHandler("campaignname",
					CampaignI18nEnum.FORM_CAMPAIGN_NAME);
			this.generateFieldDisplayHandler("startdate",
					CampaignI18nEnum.FORM_START_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("enddate",
					CampaignI18nEnum.FORM_END_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status",
					CampaignI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("type", CampaignI18nEnum.FORM_TYPE);
			this.generateFieldDisplayHandler("currencyid",
					CampaignI18nEnum.FORM_CURRENCY,
					AuditLogShowHandler.CURRENCY_FIELD);
			this.generateFieldDisplayHandler("budget",
					CampaignI18nEnum.FORM_BUDGET);
			this.generateFieldDisplayHandler("expectedcost",
					CampaignI18nEnum.FORM_EXPECTED_COST);
			this.generateFieldDisplayHandler("actualcost",
					CampaignI18nEnum.FORM_ACTUAL_COST);
			this.generateFieldDisplayHandler("expectedrevenue",
					CampaignI18nEnum.FORM_EXPECTED_REVENUE);
			this.generateFieldDisplayHandler("assignuser",
					GenericI18Enum.FORM_ASSIGNEE);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
		}
	}

	private static class LeadAuditLogShowHandler extends AuditLogShowHandler {
		public LeadAuditLogShowHandler() {
			this.generateFieldDisplayHandler("prefixname",
					LeadI18nEnum.FORM_PREFIX);
			this.generateFieldDisplayHandler("firstname",
					LeadI18nEnum.FORM_FIRSTNAME);
			this.generateFieldDisplayHandler("lastname",
					LeadI18nEnum.FORM_LASTNAME);
			this.generateFieldDisplayHandler("title", LeadI18nEnum.FORM_TITLE);
			this.generateFieldDisplayHandler("department",
					LeadI18nEnum.FORM_DEPARTMENT);
			this.generateFieldDisplayHandler("accountname",
					LeadI18nEnum.FORM_ACCOUNT_NAME);
			this.generateFieldDisplayHandler("source",
					LeadI18nEnum.FORM_LEAD_SOURCE);
			this.generateFieldDisplayHandler("industry",
					LeadI18nEnum.FORM_INDUSTRY);
			this.generateFieldDisplayHandler("noemployees",
					LeadI18nEnum.FORM_NO_EMPLOYEES);
			this.generateFieldDisplayHandler("email", LeadI18nEnum.FORM_EMAIL);
			this.generateFieldDisplayHandler("officephone",
					LeadI18nEnum.FORM_OFFICE_PHONE);
			this.generateFieldDisplayHandler("mobile", LeadI18nEnum.FORM_MOBILE);
			this.generateFieldDisplayHandler("otherphone",
					LeadI18nEnum.FORM_OTHER_PHONE);
			this.generateFieldDisplayHandler("fax", LeadI18nEnum.FORM_FAX);
			this.generateFieldDisplayHandler("website",
					LeadI18nEnum.FORM_WEBSITE);
			this.generateFieldDisplayHandler("status", LeadI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("assignuser",
					GenericI18Enum.FORM_ASSIGNEE);
			this.generateFieldDisplayHandler("primaddress",
					LeadI18nEnum.FORM_PRIMARY_ADDRESS);
			this.generateFieldDisplayHandler("primcity",
					LeadI18nEnum.FORM_PRIMARY_CITY);
			this.generateFieldDisplayHandler("primstate",
					LeadI18nEnum.FORM_PRIMARY_STATE);
			this.generateFieldDisplayHandler("primpostalcode",
					LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE);
			this.generateFieldDisplayHandler("primcountry",
					LeadI18nEnum.FORM_PRIMARY_COUNTRY);
			this.generateFieldDisplayHandler("otheraddress",
					LeadI18nEnum.FORM_OTHER_ADDRESS);
			this.generateFieldDisplayHandler("othercity",
					LeadI18nEnum.FORM_OTHER_CITY);
			this.generateFieldDisplayHandler("otherstate",
					LeadI18nEnum.FORM_OTHER_STATE);
			this.generateFieldDisplayHandler("otherpostalcode",
					LeadI18nEnum.FORM_OTHER_POSTAL_CODE);
			this.generateFieldDisplayHandler("othercountry",
					LeadI18nEnum.FORM_OTHER_COUNTRY);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
		}
	}

	private static class OpportunityAuditLogShowHandler extends
			AuditLogShowHandler {
		public OpportunityAuditLogShowHandler() {
			this.generateFieldDisplayHandler("opportunityname",
					OpportunityI18nEnum.FORM_NAME);
			this.generateFieldDisplayHandler("currencyid",
					OpportunityI18nEnum.FORM_CURRENCY,
					AuditLogShowHandler.CURRENCY_FIELD);
			this.generateFieldDisplayHandler("amount",
					OpportunityI18nEnum.FORM_AMOUNT);
			this.generateFieldDisplayHandler("salesstage",
					OpportunityI18nEnum.FORM_SALE_STAGE);
			this.generateFieldDisplayHandler("probability",
					OpportunityI18nEnum.FORM_PROBABILITY);
			this.generateFieldDisplayHandler("nextstep",
					OpportunityI18nEnum.FORM_NEXT_STEP);
			this.generateFieldDisplayHandler("accountid",
					OpportunityI18nEnum.FORM_ACCOUNT_NAME);
			this.generateFieldDisplayHandler("expectedcloseddate",
					OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("opportunitytype",
					OpportunityI18nEnum.FORM_TYPE);
			this.generateFieldDisplayHandler("source",
					OpportunityI18nEnum.FORM_LEAD_SOURCE);
			this.generateFieldDisplayHandler("campaignid",
					OpportunityI18nEnum.FORM_CAMPAIGN_NAME);
			this.generateFieldDisplayHandler("assignuser",
					GenericI18Enum.FORM_ASSIGNEE);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
		}
	}

	private static class CaseAuditLogShowHandler extends AuditLogShowHandler {
		public CaseAuditLogShowHandler() {
			this.generateFieldDisplayHandler("priority",
					CaseI18nEnum.FORM_PRIORITY);
			this.generateFieldDisplayHandler("status", CaseI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("accountid",
					CaseI18nEnum.FORM_ACCOUNT);
			this.generateFieldDisplayHandler("phonenumber",
					CaseI18nEnum.FORM_PHONE);
			this.generateFieldDisplayHandler("origin", CaseI18nEnum.FORM_ORIGIN);
			this.generateFieldDisplayHandler("type", CaseI18nEnum.FORM_TYPE);
			this.generateFieldDisplayHandler("reason", CaseI18nEnum.FORM_REASON);
			this.generateFieldDisplayHandler("subject",
					CaseI18nEnum.FORM_SUBJECT);
			this.generateFieldDisplayHandler("email", CaseI18nEnum.FORM_EMAIL);
			this.generateFieldDisplayHandler("assignuser",
					GenericI18Enum.FORM_ASSIGNEE);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
			this.generateFieldDisplayHandler("resolution",
					CaseI18nEnum.FORM_RESOLUTION);
		}
	}

	private static class MeetingAuditLogShowHandler extends AuditLogShowHandler {
		public MeetingAuditLogShowHandler() {
			this.generateFieldDisplayHandler("subject",
					MeetingI18nEnum.FORM_SUBJECT);
			this.generateFieldDisplayHandler("status",
					MeetingI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("startdate",
					MeetingI18nEnum.FORM_START_DATE_TIME,
					AuditLogShowHandler.DATETIME_FIELD);
			this.generateFieldDisplayHandler("enddate",
					MeetingI18nEnum.FORM_END_DATE_TIME,
					AuditLogShowHandler.DATETIME_FIELD);
			this.generateFieldDisplayHandler("location",
					MeetingI18nEnum.FORM_LOCATION);
		}
	}

	private static class TaskAuditLogShowHandler extends AuditLogShowHandler {
		public TaskAuditLogShowHandler() {
			this.generateFieldDisplayHandler("subject",
					TaskI18nEnum.FORM_SUBJECT);
			this.generateFieldDisplayHandler("startdate",
					TaskI18nEnum.FORM_START_DATE,
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("duedate",
					TaskI18nEnum.FORM_DUE_DATE, AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status", TaskI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("assignuser",
					GenericI18Enum.FORM_ASSIGNEE);
			this.generateFieldDisplayHandler("priority",
					TaskI18nEnum.FORM_PRIORITY);
			this.generateFieldDisplayHandler("description",
					GenericI18Enum.FORM_DESCRIPTION);
		}
	}

	private static class CallAuditLogShowHandler extends AuditLogShowHandler {
		public CallAuditLogShowHandler() {
			this.generateFieldDisplayHandler("subject",
					CallI18nEnum.FORM_SUBJECT);
			this.generateFieldDisplayHandler("startdate",
					CallI18nEnum.FORM_START_DATE_TIME,
					AuditLogShowHandler.DATETIME_FIELD);
			this.generateFieldDisplayHandler("assignuser",
					GenericI18Enum.FORM_ASSIGNEE);
			this.generateFieldDisplayHandler("status", CallI18nEnum.FORM_STATUS);
			this.generateFieldDisplayHandler("purpose",
					CallI18nEnum.FORM_PURPOSE);
		}
	}
}
