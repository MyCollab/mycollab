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
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.utils.AuditLogShowHandler;
import com.esofthead.mycollab.vaadin.AppContext;

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
			this.generateFieldDisplayHandler("accountname", "Account Name");
			this.generateFieldDisplayHandler("phoneoffice", AppContext
					.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD));
			this.generateFieldDisplayHandler("website", "Website");
			this.generateFieldDisplayHandler("fax", "Fax");
			this.generateFieldDisplayHandler("numemployees", "Employees");
			this.generateFieldDisplayHandler("alternatephone", "Other Phone");
			this.generateFieldDisplayHandler("industry", "Industry");
			this.generateFieldDisplayHandler("email", "Email");
			this.generateFieldDisplayHandler("type", "Type");
			this.generateFieldDisplayHandler("ownership", "Ownership");
			this.generateFieldDisplayHandler("annualrevenue", "Annual Revenue");
			this.generateFieldDisplayHandler("billingaddress",
					"Billing Address");
			this.generateFieldDisplayHandler("shippingaddress",
					"Shipping Address");
			this.generateFieldDisplayHandler("city", "Billing City");
			this.generateFieldDisplayHandler("shippingcity", "Shipping City");
			this.generateFieldDisplayHandler("state", "Billing State");
			this.generateFieldDisplayHandler("shippingstate", "Shipping State");
			this.generateFieldDisplayHandler("postalcode", "Postal Code");
			this.generateFieldDisplayHandler("shippingpostalcode",
					"Shipping Postal Code");
			this.generateFieldDisplayHandler("description", "Description");
		}
	}

	private static class ContactAuditLogShowHandler extends AuditLogShowHandler {
		public ContactAuditLogShowHandler() {
			this.generateFieldDisplayHandler("firstname", "First Name");
			this.generateFieldDisplayHandler("lastname", "Last Name");
			this.generateFieldDisplayHandler("accountId", "Account");
			this.generateFieldDisplayHandler("title", "Title");
			this.generateFieldDisplayHandler("department", "Department");
			this.generateFieldDisplayHandler("email", "Email");
			this.generateFieldDisplayHandler("assistant", "Assistant");
			this.generateFieldDisplayHandler("assistantphone",
					"Assistant Phone");
			this.generateFieldDisplayHandler("leadsource", "Leade Source");
			this.generateFieldDisplayHandler("officephone", AppContext
					.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD));
			this.generateFieldDisplayHandler("mobile", "Mobile");
			this.generateFieldDisplayHandler("homephone", "Home Phone");
			this.generateFieldDisplayHandler("otherphone", "Other Phone");
			this.generateFieldDisplayHandler("birthday", "Birthday",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("iscallable", "Callable");
			this.generateFieldDisplayHandler("assignuser", AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE));
			this.generateFieldDisplayHandler("primaddress", "Address");
			this.generateFieldDisplayHandler("primcity", "City");
			this.generateFieldDisplayHandler("primstate", "State");
			this.generateFieldDisplayHandler("primpostalcode", "Postal Code");
			this.generateFieldDisplayHandler("primcountry", "Country");
			this.generateFieldDisplayHandler("otheraddress", "Other Address");
			this.generateFieldDisplayHandler("othercity", "Other City");
			this.generateFieldDisplayHandler("otherstate", "Other State");
			this.generateFieldDisplayHandler("otherpostalcode",
					"Other Postal Code");
			this.generateFieldDisplayHandler("othercountry", "Other Country");
			this.generateFieldDisplayHandler("description", "Description");
		}
	}

	private static class CampaignAuditLogShowHandler extends
			AuditLogShowHandler {
		public CampaignAuditLogShowHandler() {
			this.generateFieldDisplayHandler("campaignname", "Name");
			this.generateFieldDisplayHandler("startdate", "Start Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("enddate", "End Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("type", "Type");
			this.generateFieldDisplayHandler("currencyid", "Currency",
					AuditLogShowHandler.CURRENCY_FIELD);
			this.generateFieldDisplayHandler("budget", "Budget");
			this.generateFieldDisplayHandler("expectedcost", "Expected Cost");
			this.generateFieldDisplayHandler("actualcost", "Actual Cost");
			this.generateFieldDisplayHandler("expectedrevenue",
					"Expected Revenue");
			this.generateFieldDisplayHandler("assignuser", AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE));
			this.generateFieldDisplayHandler("description", "Description");
		}
	}

	private static class LeadAuditLogShowHandler extends AuditLogShowHandler {
		public LeadAuditLogShowHandler() {
			this.generateFieldDisplayHandler("prefixname", "Prefix name");
			this.generateFieldDisplayHandler("firstname", "First name");
			this.generateFieldDisplayHandler("lastname", "Last Name");
			this.generateFieldDisplayHandler("title", "Title");
			this.generateFieldDisplayHandler("department", "Department");
			this.generateFieldDisplayHandler("accountname", "Account Name");
			this.generateFieldDisplayHandler("source", "Lead Source");
			this.generateFieldDisplayHandler("industry", "Industry");
			this.generateFieldDisplayHandler("noemployees", "No of Employees");
			this.generateFieldDisplayHandler("email", "Email");
			this.generateFieldDisplayHandler("officephone", "Office Phone");
			this.generateFieldDisplayHandler("mobile", "Mobile");
			this.generateFieldDisplayHandler("otherphone", "Other Phone");
			this.generateFieldDisplayHandler("fax", "Fax");
			this.generateFieldDisplayHandler("website", "Web Site");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("assignuser", AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE));
			this.generateFieldDisplayHandler("primaddress", "Address");
			this.generateFieldDisplayHandler("primcity", "City");
			this.generateFieldDisplayHandler("primstate", "State");
			this.generateFieldDisplayHandler("primpostalcode", "Postal Code");
			this.generateFieldDisplayHandler("primcountry", "Country");
			this.generateFieldDisplayHandler("otheraddress", "Other Address");
			this.generateFieldDisplayHandler("othercity", "Other City");
			this.generateFieldDisplayHandler("otherstate", "Other State");
			this.generateFieldDisplayHandler("otherpostalcode",
					"Other Postal Code");
			this.generateFieldDisplayHandler("othercountry", "Other Country");
			this.generateFieldDisplayHandler("description", "Description");
		}
	}

	private static class OpportunityAuditLogShowHandler extends
			AuditLogShowHandler {
		public OpportunityAuditLogShowHandler() {
			this.generateFieldDisplayHandler("opportunityname",
					"Opportunity Name");
			this.generateFieldDisplayHandler("currencyid", "Currency",
					AuditLogShowHandler.CURRENCY_FIELD);
			this.generateFieldDisplayHandler("amount", "Amount");
			this.generateFieldDisplayHandler("salesstage", "Sales Stage");
			this.generateFieldDisplayHandler("probability", "Probability (%)");
			this.generateFieldDisplayHandler("nextstep", "Next Step");
			this.generateFieldDisplayHandler("accountid", "Account Name");
			this.generateFieldDisplayHandler("expectedcloseddate",
					"Expected Close Date", AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("opportunitytype", "Type");
			this.generateFieldDisplayHandler("source", "Lead Source");
			this.generateFieldDisplayHandler("campaignid", "Campaign");
			this.generateFieldDisplayHandler("assignuser", AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE));
			this.generateFieldDisplayHandler("description", "Description");
		}
	}

	private static class CaseAuditLogShowHandler extends AuditLogShowHandler {
		public CaseAuditLogShowHandler() {
			this.generateFieldDisplayHandler("priority", "Priority");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("accountid", "Account Name");
			this.generateFieldDisplayHandler("phonenumber", "Phone Number");
			this.generateFieldDisplayHandler("origin", "Origin");
			this.generateFieldDisplayHandler("type", "Type");
			this.generateFieldDisplayHandler("reason", "Reason");
			this.generateFieldDisplayHandler("subject", "Subject");
			this.generateFieldDisplayHandler("email", "Email");
			this.generateFieldDisplayHandler("assignuser", AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE));
			this.generateFieldDisplayHandler("description", "Description");
			this.generateFieldDisplayHandler("resolution", "Resolution");
		}
	}

	private static class MeetingAuditLogShowHandler extends AuditLogShowHandler {
		public MeetingAuditLogShowHandler() {
			this.generateFieldDisplayHandler("subject", "Subject");
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("type", "Type");
			this.generateFieldDisplayHandler("startdate", "Start Date",
					AuditLogShowHandler.DATETIME_FIELD);
			this.generateFieldDisplayHandler("enddate", "End Date",
					AuditLogShowHandler.DATETIME_FIELD);
			this.generateFieldDisplayHandler("location", "Location");
		}
	}

	private static class TaskAuditLogShowHandler extends AuditLogShowHandler {
		public TaskAuditLogShowHandler() {
			this.generateFieldDisplayHandler("subject", "Subject");
			this.generateFieldDisplayHandler("startdate", "Start Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("duedate", "Due Date",
					AuditLogShowHandler.DATE_FIELD);
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("assignuser", AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE));
			this.generateFieldDisplayHandler("priority", "Priority");
			this.generateFieldDisplayHandler("description", "Description");
		}
	}

	private static class CallAuditLogShowHandler extends AuditLogShowHandler {
		public CallAuditLogShowHandler() {
			this.generateFieldDisplayHandler("subject", "Subject");
			this.generateFieldDisplayHandler("startdate", "Start Date",
					AuditLogShowHandler.DATETIME_FIELD);
			this.generateFieldDisplayHandler("assignuser", AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE));
			this.generateFieldDisplayHandler("status", "Status");
			this.generateFieldDisplayHandler("purpose", "Purpose");
		}
	}
}
