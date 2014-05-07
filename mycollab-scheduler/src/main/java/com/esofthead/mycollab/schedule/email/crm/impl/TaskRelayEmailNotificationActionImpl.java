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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.domain.SimpleTask;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.TaskService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.TaskRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TaskRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleTask> implements
		TaskRelayEmailNotificationAction {

	private static Logger log = LoggerFactory
			.getLogger(TaskRelayEmailNotificationActionImpl.class);

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private TaskService taskService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static final TaskFieldNameMapper mapper = new TaskFieldNameMapper();

	public TaskRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.TASK);
	}

	protected void setupMailHeaders(SimpleTask task,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = task.getSubject();
		String summaryLink = CrmLinkGenerator.generateTaskPreviewFullLink(
				siteUrl, task.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "task");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
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

			templateGenerator.putVariable("context",
					new MailContext<SimpleTask>(simpleTask, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);
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

		if (simpleTask == null) {
			return null;
		}
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
			templateGenerator.putVariable("context",
					new MailContext<SimpleTask>(simpleTask, user, siteUrl));
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

		if (simpleTask == null) {
			return null;
		}
		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the task \""
						+ StringUtils.trim(simpleTask.getSubject(), 100) + "\"",
				"templates/email/crm/itemAddNoteNotifier.mt");
		setupMailHeaders(simpleTask, emailNotification, templateGenerator);
		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public static class TaskFieldNameMapper extends ItemFieldMapper {

		public TaskFieldNameMapper() {
			put("subject", "Subject", true);

			put("status", "Status");
			put("startdate", new DateFieldFormat("startdate", "Start Date"));

			put("assignuser", new AssigneeFieldFormat("assignuser", "Assignee"));
			put("duedate", new DateFieldFormat("duedate", "Due Date"));

			put("contactid", new ContactFieldFormat("contactid", "Contact"));
			put("priority", "Priority");

			put("typeid", "Related To", true);
			put("description", "Description", true);
		}
	}

	public static class ContactFieldFormat extends FieldFormat {

		public ContactFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleTask task = (SimpleTask) context.getWrappedBean();
			String contactIconLink = CrmResources
					.getResourceLink(CrmTypeConstants.CONTACT);
			Img img = TagBuilder.newImg("icon", contactIconLink);

			String contactLink = CrmLinkGenerator
					.generateContactPreviewFullLink(context.getSiteUrl(),
							task.getContactid());
			A link = TagBuilder.newA(contactLink, task.getContactName());
			return TagBuilder.newLink(img, link).write();
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return "";
			}

			try {
				int contactId = Integer.parseInt(value);
				ContactService contactService = ApplicationContextUtil
						.getSpringBean(ContactService.class);
				SimpleContact contact = contactService.findById(contactId,
						context.getUser().getAccountId());

				if (contact != null) {
					String contactIconLink = CrmResources
							.getResourceLink(CrmTypeConstants.CONTACT);
					Img img = TagBuilder.newImg("icon", contactIconLink);
					String contactLink = CrmLinkGenerator
							.generateContactPreviewFullLink(
									context.getSiteUrl(), contact.getId());
					A link = TagBuilder.newA(contactLink,
							contact.getDisplayName());
					return TagBuilder.newLink(img, link).write();
				}
			} catch (Exception e) {
				log.error("Error", e);
			}

			return value;
		}

	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleTask task = (SimpleTask) context.getWrappedBean();

			String userAvatarLink = LinkUtils.getAvatarLink(
					task.getAssignUserAvatarId(), 16);

			Img img = TagBuilder.newImg("avatar", userAvatarLink);

			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(task.getSaccountid()),
					task.getAssignuser());
			A link = TagBuilder.newA(userLink, task.getAssignUserFullName());
			return TagBuilder.newLink(img, link).write();
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return "";
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = LinkUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = UserLinkUtils.generatePreviewFullUserLink(
						LinkUtils.getSiteUrl(user.getAccountId()),
						user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}
	}

}
