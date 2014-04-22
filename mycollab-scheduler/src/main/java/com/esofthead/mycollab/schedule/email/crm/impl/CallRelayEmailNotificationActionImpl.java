/**
 * This file is part of mycollab-scheduler.
 *
 * mycollab-scheduler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-scheduler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-scheduler.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.schedule.email.crm.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.service.CallService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.crm.CallRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class CallRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleCall> implements
		CallRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private CallService callService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private final CallFieldNameMapper mapper;

	public CallRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.CALL);
		mapper = new CallFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleCall call,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(call.getSaccountid()));

		String summary = call.getSubject();
		String summaryLink = crmLinkGenerator.generateCallPreviewFullLink(call
				.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "call");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleCall call, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(call.getSaccountid()));

		if (call.getStatus() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("status"),
					Arrays.asList(new MailItemLink(null, call.getStatus())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("status"), null);
		}

		if (call.getStartdate() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("startdate"),
					Arrays.asList(new MailItemLink(null, DateTimeUtils
							.converToStringWithUserTimeZone(
									call.getStartdate(), user.getTimezone()))));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("startdate"), null);
		}

		if (call.getTypeid() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("typeid"), Arrays
					.asList(generateRelatedItem(call.getType(),
							call.getTypeid(), call.getSaccountid(),
							crmLinkGenerator)));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("typeid"), null);
		}

		if (call.getDurationinseconds() != null) {
			listOfDisplayProperties.put(mapper
					.getFieldLabel("durationinseconds"), Arrays
					.asList(new MailItemLink(null, call.getDurationinseconds()
							.toString())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("durationinseconds"), null);
		}

		if (call.getPurpose() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("purpose"),
					Arrays.asList(new MailItemLink(null, call.getPurpose())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("purpose"), null);
		}

		if (call.getAssignuser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					Arrays.asList(new MailItemLink(crmLinkGenerator
							.generateUserPreviewFullLink(call.getAssignuser()),
							call.getAssignUserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					null);
		}

		if (call.getDescription() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("description"), Arrays
							.asList(new MailItemLink(null, call
									.getDescription())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					null);
		}

		if (call.getResult() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("result"), Arrays
					.asList(new MailItemLink(null, call.getDescription())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("result"), null);
		}

		return listOfDisplayProperties;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCall simpleCall = callService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleCall != null) {
			String subject = StringUtils.trim(simpleCall.getSubject(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ "has created the call \"" + subject + "\"",
					"templates/email/crm/itemCreatedNotifier.mt");
			setupMailHeaders(simpleCall, emailNotification, templateGenerator);

			templateGenerator.putVariable("properties",
					getListOfProperties(simpleCall, user));

			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCall simpleCall = callService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(simpleCall.getSubject(), 150);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the call \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");

		setupMailHeaders(simpleCall, emailNotification, templateGenerator);

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());

			templateGenerator.putVariable("historyLog", auditLog);

			templateGenerator.putVariable("mapper", mapper);
		}
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCall simpleCall = callService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the call \""
						+ StringUtils.trim(simpleCall.getSubject(), 100) + "\"",
				"templates/email/crm/itemAddNoteNotifier.mt");
		setupMailHeaders(simpleCall, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class CallFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		CallFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("subject", "Subject");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("startdate", "Start Date & Time");
			fieldNameMap.put("typeid", "Related to");
			fieldNameMap.put("durationinseconds", "Duration");
			fieldNameMap.put("purpose", "Purpose");
			fieldNameMap.put("assignuser", "Assignee");
			fieldNameMap.put("description", "Description");
			fieldNameMap.put("result", "Result");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

}
