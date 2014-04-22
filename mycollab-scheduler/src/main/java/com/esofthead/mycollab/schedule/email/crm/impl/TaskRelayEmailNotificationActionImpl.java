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
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.TaskService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.crm.TaskRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class TaskRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleTask> implements
		TaskRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private TaskService taskService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private final TaskFieldNameMapper mapper;

	public TaskRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.TASK);
		mapper = new TaskFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleTask task,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(task.getSaccountid()));

		String summary = task.getSubject();
		String summaryLink = crmLinkGenerator.generateTaskPreviewFullLink(task
				.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "task");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleTask task, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(task.getSaccountid()));

		if (task.getStatus() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("status"),
					Arrays.asList(new MailItemLink(null, task.getStatus())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("status"), null);
		}

		if (task.getStartdate() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("startdate"),
					Arrays.asList(new MailItemLink(null, DateTimeUtils
							.converToStringWithUserTimeZone(
									task.getStartdate(), user.getTimezone()))));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("startdate"), null);
		}

		if (task.getTypeid() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("typeid"), Arrays
					.asList(generateRelatedItem(task.getType(),
							task.getTypeid(), task.getSaccountid(),
							crmLinkGenerator)));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("typeid"), null);
		}

		if (task.getDuedate() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("duedate"), Arrays
					.asList(new MailItemLink(null, DateTimeUtils
							.converToStringWithUserTimeZone(task.getDuedate(),
									user.getTimezone()))));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("duedate"), null);
		}

		if (task.getContactid() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("contactid"),
					Arrays.asList(new MailItemLink(
							crmLinkGenerator
									.generateContactPreviewFullLink(task
											.getContactid()), task
									.getContactName())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("contactid"), null);
		}

		if (task.getPriority() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("priority"),
					Arrays.asList(new MailItemLink(null, task.getPriority())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("priority"), null);
		}

		if (task.getAssignuser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					Arrays.asList(new MailItemLink(crmLinkGenerator
							.generateUserPreviewFullLink(task.getAssignuser()),
							task.getAssignUserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					null);
		}

		if (task.getDescription() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("description"), Arrays
							.asList(new MailItemLink(null, task
									.getDescription())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					null);
		}

		return listOfDisplayProperties;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleTask simpleTask = taskService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleTask != null) {
			String subject = StringUtils.trim(simpleTask.getSubject(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has created the task \"" + subject + "\"",
					"templates/email/crm/itemCreatedNotifier.mt");
			setupMailHeaders(simpleTask, emailNotification, templateGenerator);

			templateGenerator.putVariable("properties",
					getListOfProperties(simpleTask, user));
			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleTask simpleTask = taskService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(simpleTask.getSubject(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the task \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");
		setupMailHeaders(simpleTask, emailNotification, templateGenerator);

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
		SimpleTask simpleTask = taskService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the task \""
						+ StringUtils.trim(simpleTask.getSubject(), 100) + "\"",
				"templates/email/crm/itemAddNoteNotifier.mt");
		setupMailHeaders(simpleTask, emailNotification, templateGenerator);
		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class TaskFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		TaskFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("subject", "Subject");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("startdate", "Start Date");
			fieldNameMap.put("typeid", "Related To");
			fieldNameMap.put("duedate", "Due Date");
			fieldNameMap.put("contactid", "Contact");
			fieldNameMap.put("priority", "Priority");
			fieldNameMap.put("assignuser", "Assignee");
			fieldNameMap.put("description", "Description");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

}
