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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingType;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectNotificationSettingService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class SendMailToAllMembersAction<B> implements
		SendingRelayEmailNotificationAction {

	@Autowired
	private ExtMailService extMailService;

	@Autowired
	private ProjectMemberService projectMemberService;

	@Autowired
	private ProjectNotificationSettingService projectNotificationService;

	protected String siteUrl;

	protected List<SimpleUser> getNotifyUsers(
			ProjectRelayEmailNotification notification) {
		List<SimpleUser> usersInProject = projectMemberService
				.getActiveUsersInProject(
						((ProjectRelayEmailNotification) notification)
								.getProjectId(), notification.getSaccountid());

		List<ProjectNotificationSetting> notificationSettings = projectNotificationService
				.findNotifications(notification.getProjectId(),
						notification.getSaccountid());
		if (notificationSettings != null && notificationSettings.size() > 0) {
			for (ProjectNotificationSetting setting : notificationSettings) {
				if (ProjectNotificationSettingType.NONE.equals(setting
						.getLevel())
						|| ProjectNotificationSettingType.MINIMAL
								.equals(setting.getLevel())) {
					for (SimpleUser user : usersInProject) {
						if (user.getUsername().equals(setting.getUsername())) {
							usersInProject.remove(user);
							break;
						}
					}
				}
			}
		}
		return usersInProject;
	}

	@Override
	public void sendNotificationForCreateAction(
			SimpleRelayEmailNotification notification) {
		List<SimpleUser> notifiers = getNotifyUsers((ProjectRelayEmailNotification) notification);
		if ((notifiers != null) && !notifiers.isEmpty()) {
			onInitAction(notification);
			for (SimpleUser user : notifiers) {
				MailContext<B> context = new MailContext<B>(notification, user,
						siteUrl);
				TemplateGenerator templateGenerator = templateGeneratorForCreateAction(context);
				if (templateGenerator != null) {

					templateGenerator.putVariable("userName",
							user.getDisplayName());

					MailRecipientField userMail = new MailRecipientField(
							user.getEmail(), user.getUsername());
					List<MailRecipientField> lst = new ArrayList<MailRecipientField>();
					lst.add(userMail);

					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", lst, null, null,
							templateGenerator.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);
				}
			}
		}
	}

	@Override
	public void sendNotificationForUpdateAction(
			SimpleRelayEmailNotification notification) {
		List<SimpleUser> notifiers = getNotifyUsers((ProjectRelayEmailNotification) notification);
		if ((notifiers != null) && !notifiers.isEmpty()) {
			onInitAction(notification);
			for (SimpleUser user : notifiers) {
				MailContext<B> context = new MailContext<B>(notification, user,
						siteUrl);
				TemplateGenerator templateGenerator = templateGeneratorForUpdateAction(context);
				if (templateGenerator != null) {

					templateGenerator.putVariable("userName",
							user.getDisplayName());

					MailRecipientField userMail = new MailRecipientField(
							user.getEmail(), user.getUsername());
					List<MailRecipientField> lst = new ArrayList<MailRecipientField>();
					lst.add(userMail);

					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", lst, null, null,
							templateGenerator.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);
				}
			}
		}

	}

	@Override
	public void sendNotificationForCommentAction(
			SimpleRelayEmailNotification notification) {
		List<SimpleUser> notifiers = getNotifyUsers((ProjectRelayEmailNotification) notification);
		if ((notifiers != null) && !notifiers.isEmpty()) {
			onInitAction(notification);

			for (SimpleUser user : notifiers) {
				MailContext<B> context = new MailContext<B>(notification, user,
						siteUrl);
				TemplateGenerator templateGenerator = templateGeneratorForCommentAction(context);
				templateGenerator
						.putVariable("userName", user.getDisplayName());

				MailRecipientField userMail = new MailRecipientField(
						user.getEmail(), user.getUsername());
				List<MailRecipientField> lst = new ArrayList<MailRecipientField>();
				lst.add(userMail);

				extMailService.sendHTMLMail("noreply@mycollab.com", "MyCollab",
						lst, null, null,
						templateGenerator.generateSubjectContent(),
						templateGenerator.generateBodyContent(), null);
			}
		}
	}

	private void onInitAction(SimpleRelayEmailNotification notification) {
		siteUrl = MailUtils.getSiteUrl(notification.getSaccountid());
	}

	protected abstract TemplateGenerator templateGeneratorForCreateAction(
			MailContext<B> context);

	protected abstract TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<B> context);

	protected abstract TemplateGenerator templateGeneratorForCommentAction(
			MailContext<B> context);
}
