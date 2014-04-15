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
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.project.MessageRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class MessageRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		MessageRelayEmailNotificationAction {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ProjectService projectService;

	protected void setupMailHeaders(SimpleMessage message,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				message.getProjectid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", message.getProjectName());
		currentProject.put("webLink", linkGenerator.generateProjectFullLink());

		listOfTitles.add(currentProject);

		String summary = message.getTitle();
		String summaryLink = ProjectLinkUtils.generateMessagePreviewLink(
				message.getProjectid(), message.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "message");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {

		int messageId = emailNotification.getTypeid();

		SimpleMessage message = messageService.findMessageById(messageId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ message.getProjectName() + "]: "
				+ message.getFullPostedUserName() + " sent a message \""
				+ StringUtils.trim(message.getTitle(), 100) + "\"",
				"templates/email/project/itemCreatedNotifier.mt");

		setupMailHeaders(message, emailNotification, templateGenerator);

		templateGenerator.putVariable("message", message.getMessage());
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int messageId = emailNotification.getTypeid();
		SimpleMessage message = messageService.findMessageById(messageId,
				emailNotification.getSaccountid());
		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ message.getProjectName() + "]: "
				+ message.getFullPostedUserName()
				+ " updated content of the message \""
				+ StringUtils.trim(message.getTitle(), 100) + "\"",
				"templates/email/project/itemCreatedNotifier.mt");
		setupMailHeaders(message, emailNotification, templateGenerator);

		templateGenerator.putVariable("message", message.getMessage());
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int messageId = emailNotification.getTypeid();
		SimpleMessage message = messageService.findMessageById(messageId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ message.getProjectName() + "]: "
				+ message.getFullPostedUserName() + " has commented on \""
				+ StringUtils.trim(message.getTitle(), 100) + "\"",
				"templates/email/project/itemCommentNotifier.mt");
		setupMailHeaders(message, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

}
