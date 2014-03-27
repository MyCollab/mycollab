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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
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

	private Map<String, String> constructHyperLinks(SimpleMessage message) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				message.getProjectid());
		hyperLinks.put("messageUrl",
				linkGenerator.generateMessagePreviewFullLink(message.getId()));
		hyperLinks.put("shortMessageUrl",
				StringUtils.trim(message.getTitle(), 100));
		hyperLinks.put("projectUrl", linkGenerator.generateProjectFullLink());
		hyperLinks.put("createdUserUrl", linkGenerator
				.generateUserPreviewFullLink(message.getPosteduser()));

		return hyperLinks;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int messageId = emailNotification.getTypeid();
		SimpleMessage message = messageService.findMessageById(messageId,
				emailNotification.getSaccountid());
		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$message.projectName]: $message.fullPostedUserName sent a message \""
						+ StringUtils.trim(message.getTitle(), 100)
						+ "\"",
				"templates/email/project/messageCreatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(message,
				user.getTimezone(), new String[] { "posteddate" });
		templateGenerator.putVariable("message", message);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(message));
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int messageId = emailNotification.getTypeid();
		SimpleMessage message = messageService.findMessageById(messageId,
				emailNotification.getSaccountid());
		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$message.projectName]: $message.fullPostedUserName updated content of the message \""
						+ StringUtils.trim(message.getTitle(), 100)
						+ "\"",
				"templates/email/project/messageUpdatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(message,
				user.getTimezone(), new String[] { "posteddate" });
		templateGenerator.putVariable("message", message);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(message));
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int messageId = emailNotification.getTypeid();
		SimpleMessage message = messageService.findMessageById(messageId,
				emailNotification.getSaccountid());
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				message.getProjectid());
		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$message.projectName]: $!message.fullPostedUserName has commented on \""
						+ StringUtils.trim(message.getTitle(), 100)
						+ "\"",
				"templates/email/project/messageCommentNotifier.mt");
		templateGenerator.putVariable("message", message);
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", linkGenerator
				.generateUserPreviewFullLink(emailNotification.getChangeby()));
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(message));
		return templateGenerator;
	}

}
