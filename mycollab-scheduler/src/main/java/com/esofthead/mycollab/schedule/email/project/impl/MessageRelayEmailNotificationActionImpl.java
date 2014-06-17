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
package com.esofthead.mycollab.schedule.email.project.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.project.MessageRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MessageRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction<SimpleMessage> implements
		MessageRelayEmailNotificationAction {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ProjectService projectService;

	protected void setupMailHeaders(SimpleMessage message,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", message.getProjectName());
		currentProject.put(
				"webLink",
				ProjectLinkUtils.generateProjectFullLink(siteUrl,
						message.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = message.getTitle();
		String summaryLink = ProjectLinkUtils.generateMessagePreviewFullLink(
				siteUrl, message.getProjectid(), message.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "message");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			MailContext<SimpleMessage> context) {
		SimpleMessage message = messageService.findMessageById(
				context.getTypeid(), context.getSaccountid());

		if (message == null) {
			return null;
		}

		context.setWrappedBean(message);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(MessageI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
						message.getProjectName(),
						context.getChangeByUserFullName(),
						StringUtils.trim(message.getTitle(), 100)),
				context.templatePath("templates/email/project/itemCreatedNotifier.mt"));

		setupMailHeaders(message, context.getEmailNotification(),
				templateGenerator);

		templateGenerator.putVariable("message", message.getMessage());
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleMessage> context) {
		SimpleMessage message = messageService.findMessageById(
				context.getTypeid(), context.getSaccountid());

		if (message == null) {
			return null;
		}
		context.setWrappedBean(message);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(MessageI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
						message.getProjectName(),
						context.getChangeByUserFullName(),
						StringUtils.trim(message.getTitle(), 100)),
				context.templatePath("templates/email/project/itemCreatedNotifier.mt"));
		setupMailHeaders(message, context.getEmailNotification(),
				templateGenerator);

		templateGenerator.putVariable("message", message.getMessage());
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			MailContext<SimpleMessage> context) {
		SimpleMessage message = messageService.findMessageById(
				context.getTypeid(), context.getSaccountid());

		if (message == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(MessageI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
						message.getProjectName(),
						context.getChangeByUserFullName(),
						StringUtils.trim(message.getTitle(), 100)),
				context.templatePath("templates/email/project/itemCommentNotifier.mt"));
		setupMailHeaders(message, context.getEmailNotification(),
				templateGenerator);

		templateGenerator
				.putVariable("comment", context.getEmailNotification());

		return templateGenerator;
	}

}
