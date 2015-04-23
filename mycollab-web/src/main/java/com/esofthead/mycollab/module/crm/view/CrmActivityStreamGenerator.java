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

import static com.esofthead.mycollab.module.crm.view.account.AccountHistoryLogList.accountFormatter;
import static com.esofthead.mycollab.module.crm.view.activity.AssignmentHistoryLogList.assignmentFormatter;
import static com.esofthead.mycollab.module.crm.view.activity.CallHistoryLogList.callFormatter;
import static com.esofthead.mycollab.module.crm.view.activity.MeetingHistoryLogList.meetingFormatter;
import static com.esofthead.mycollab.module.crm.view.campaign.CampaignHistoryLogList.campaignFormatter;
import static com.esofthead.mycollab.module.crm.view.cases.CaseHistoryLogList.caseFormatter;
import static com.esofthead.mycollab.module.crm.view.contact.ContactHistoryLogList.contactFormatter;
import static com.esofthead.mycollab.module.crm.view.lead.LeadHistoryLogList.leadFormatter;
import static com.esofthead.mycollab.module.crm.view.opportunity.OpportunityHistoryLogList.opportunityFormatter;

import java.util.HashMap;
import java.util.Map;

import com.esofthead.mycollab.common.domain.SimpleActivityStream;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.utils.AuditLogPrinter;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CrmActivityStreamGenerator {

	private static final Map<String, AuditLogPrinter> auditPrinters;
	static {
		auditPrinters = new HashMap<>();
		auditPrinters.put(CrmTypeConstants.ACCOUNT, new AuditLogPrinter(
				accountFormatter));
		auditPrinters.put(CrmTypeConstants.CONTACT, new AuditLogPrinter(
				contactFormatter));
		auditPrinters.put(CrmTypeConstants.CAMPAIGN, new AuditLogPrinter(
				campaignFormatter));
		auditPrinters.put(CrmTypeConstants.LEAD, new AuditLogPrinter(
				leadFormatter));
		auditPrinters.put(CrmTypeConstants.OPPORTUNITY, new AuditLogPrinter(
				opportunityFormatter));
		auditPrinters.put(CrmTypeConstants.CASE, new AuditLogPrinter(
				caseFormatter));
		auditPrinters.put(CrmTypeConstants.MEETING, new AuditLogPrinter(
				meetingFormatter));
		auditPrinters.put(CrmTypeConstants.TASK, new AuditLogPrinter(
				assignmentFormatter));
		auditPrinters.put(CrmTypeConstants.CALL, new AuditLogPrinter(
				callFormatter));
	}

	public static String generatorDetailChangeOfActivity(
			SimpleActivityStream activityStream) {

		if (activityStream.getAssoAuditLog() != null) {
			AuditLogPrinter auditLogHandler = auditPrinters.get(activityStream
					.getType());
			if (auditLogHandler != null) {
				return auditLogHandler.generateChangeSet(activityStream
						.getAssoAuditLog());
			}

		}

		return "";
	}
}
