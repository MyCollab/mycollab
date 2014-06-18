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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingType;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
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
public class BugRelayEmailNotificationActionImpl extends
		SendMailToFollowersAction<SimpleBug> implements
		BugRelayEmailNotificationAction {
	private static Logger log = LoggerFactory
			.getLogger(BugRelayEmailNotificationActionImpl.class);

	@Autowired
	private BugService bugService;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ProjectService projectService;

	private static final BugFieldNameMapper mapper = new BugFieldNameMapper();

	@Override
	public TemplateGenerator templateGeneratorForCreateAction(
			MailContext<SimpleBug> context) {
		SimpleBug bug = bugService.findById(context.getTypeid(),
				context.getSaccountid());
		if (bug != null) {
			context.setWrappedBean(bug);
			String subject = StringUtils.trim(bug.getSummary(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(BugI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
							bug.getProjectname(),
							context.getChangeByUserFullName(), subject),
					context.templatePath("templates/email/project/itemCreatedNotifier.mt"));

			setupMailHeaders(bug, context.getEmailNotification(),
					templateGenerator);

			templateGenerator.putVariable("context", context);
			templateGenerator.putVariable("mapper", mapper);

			return templateGenerator;
		} else {
			return null;
		}

	}

	protected void setupMailHeaders(SimpleBug bug,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", bug.getProjectname());
		currentProject.put(
				"webLink",
				ProjectLinkUtils.generateProjectFullLink(siteUrl,
						bug.getProjectid()));

		listOfTitles.add(currentProject);

		HashMap<String, String> bugCode = new HashMap<String, String>();
		SimpleProject relatedProject = projectService.findById(
				bug.getProjectid(), emailNotification.getSaccountid());
		bugCode.put("displayName", "[" + relatedProject.getShortname() + "-"
				+ bug.getBugkey() + "]");
		bugCode.put(
				"webLink",
				ProjectLinkUtils.generateBugPreviewFullLink(siteUrl,
						bug.getProjectid(), bug.getId()));

		listOfTitles.add(bugCode);

		String summary = bug.getSummary();
		String summaryLink = ProjectLinkUtils.generateBugPreviewFullLink(
				siteUrl, bug.getProjectid(), bug.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "bug");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	public TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleBug> context) {
		SimpleBug bug = bugService.findById(context.getTypeid(),
				context.getSaccountid());
		if (bug == null) {
			return null;
		}
		context.setWrappedBean(bug);
		String subject = StringUtils.trim(bug.getSummary(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(BugI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
						bug.getProjectname(),
						context.getChangeByUserFullName(), subject),
				context.templatePath("templates/email/project/itemUpdatedNotifier.mt"));

		setupMailHeaders(bug, context.getEmailNotification(), templateGenerator);

		if (context.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					context.getTypeid(), context.getSaccountid());
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("context", context);
			templateGenerator.putVariable("mapper", mapper);
		}

		return templateGenerator;
	}

	@Override
	public TemplateGenerator templateGeneratorForCommentAction(
			MailContext<SimpleBug> context) {
		SimpleBug bug = bugService.findById(context.getTypeid(),
				context.getSaccountid());
		if (bug == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(BugI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
						bug.getProjectname(),
						context.getChangeByUserFullName(),
						StringUtils.trim(bug.getSummary(), 100)),
				context.templatePath("templates/email/project/itemCommentNotifier.mt"));

		setupMailHeaders(bug, context.getEmailNotification(), templateGenerator);

		templateGenerator
				.putVariable("comment", context.getEmailNotification());

		return templateGenerator;
	}

	@Override
	protected List<SimpleUser> getListNotifyUsersWithFilter(
			ProjectRelayEmailNotification notification) {
		List<ProjectNotificationSetting> notificationSettings = projectNotificationService
				.findNotifications(notification.getProjectId(),
						notification.getSaccountid());

		ProjectMemberService projectService = ApplicationContextUtil
				.getSpringBean(ProjectMemberService.class);

		List<SimpleUser> activeUsers = projectService.getActiveUsersInProject(
				notification.getProjectId(), notification.getSaccountid());

		List<SimpleUser> inListUsers = notification.getNotifyUsers();

		if (notificationSettings != null && notificationSettings.size() > 0) {
			for (ProjectNotificationSetting notificationSetting : notificationSettings) {
				if (ProjectNotificationSettingType.NONE
						.equals(notificationSetting.getLevel())) {
					// remove users in list if he is already in list
					for (SimpleUser user : inListUsers) {
						if ((user.getUsername() != null && user.getUsername()
								.equals(notificationSetting.getUsername()))
								|| user.getEmail().equals(
										notificationSetting.getUsername())) {
							inListUsers.remove(user);
							break;
						}
					}
				} else if (ProjectNotificationSettingType.MINIMAL
						.equals(notificationSetting.getLevel())) {
					boolean isAlreadyInList = false;
					for (SimpleUser user : inListUsers) {
						if ((user.getUsername() != null && user.getUsername()
								.equals(notificationSetting.getUsername()))
								|| user.getEmail().equals(
										notificationSetting.getUsername())) {
							isAlreadyInList = true;
							break;
						}
					}

					if (!isAlreadyInList) {
						SimpleBug bug = bugService.findById(
								notification.getTypeid(),
								notification.getSaccountid());
						if (bug.getAssignuser() != null
								&& bug.getAssignuser().equals(
										notificationSetting.getUsername())) {
							for (SimpleUser user : activeUsers) {
								if ((user.getUsername() != null && user
										.getUsername().equals(
												notificationSetting
														.getUsername()))
										|| user.getEmail().equals(
												notificationSetting
														.getUsername())) {
									inListUsers.add(user);
									break;
								}
							}
						}
					}

				} else if (ProjectNotificationSettingType.FULL
						.equals(notificationSetting.getLevel())) {
					boolean isAlreadyInList = false;
					for (SimpleUser user : inListUsers) {
						if ((user.getUsername() != null && user.getUsername()
								.equals(notificationSetting.getUsername()))
								|| user.getEmail().equals(
										notificationSetting.getUsername())) {
							isAlreadyInList = true;
							break;
						}
					}

					if (!isAlreadyInList) {
						for (SimpleUser user : activeUsers) {
							if ((user.getUsername() != null && user
									.getUsername().equals(
											notificationSetting.getUsername()))
									|| user.getEmail().equals(
											notificationSetting.getUsername())) {
								inListUsers.add(user);
								break;
							}
						}
					}
				}
			}
		}

		return inListUsers;
	}

	public static class BugFieldNameMapper extends ItemFieldMapper {

		public BugFieldNameMapper() {
			put("summary", BugI18nEnum.FORM_SUMMARY, true);

			put("environment", BugI18nEnum.FORM_ENVIRONMENT, true);

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

			put("assignuser", new AssigneeFieldFormat("assignuser",
					GenericI18Enum.FORM_ASSIGNEE));
			put("milestoneid", new MilestoneFieldFormat("milestoneid",
					BugI18nEnum.FORM_PHASE));

			put("status", BugI18nEnum.FORM_STATUS);
			put("resolution", BugI18nEnum.FORM_RESOLUTION);

			put("severity", BugI18nEnum.FORM_SEVERITY);
			put("priority", BugI18nEnum.FORM_PRIORITY);

			put("duedate", new DateFieldFormat("duedate",
					BugI18nEnum.FORM_DUE_DATE));
			put("logby", new LogUserFieldFormat("logby",
					BugI18nEnum.FORM_LOG_BY));
		}
	}

	public static class MilestoneFieldFormat extends FieldFormat {

		public MilestoneFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();

			if (bug.getMilestoneid() == null || bug.getMilestoneName() == null) {
				return "";
			}
			String milestoneIconLink = ProjectResources
					.getResourceLink(ProjectTypeConstants.MILESTONE);
			Img img = TagBuilder.newImg("icon", milestoneIconLink);

			String milestoneLink = ProjectLinkUtils
					.generateMilestonePreviewFullLink(context.getSiteUrl(),
							bug.getProjectid(), bug.getMilestoneid());
			A link = TagBuilder.newA(milestoneLink, bug.getMilestoneName());
			return TagBuilder.newLink(img, link).write();
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return "";
			}

			try {
				int milestoneId = Integer.parseInt(value);
				MilestoneService milestoneService = ApplicationContextUtil
						.getSpringBean(MilestoneService.class);
				SimpleMilestone milestone = milestoneService.findById(
						milestoneId, context.getUser().getAccountId());

				if (milestone != null) {
					String milestoneIconLink = ProjectResources
							.getResourceLink(ProjectTypeConstants.MILESTONE);
					Img img = TagBuilder.newImg("icon", milestoneIconLink);

					String milestoneLink = ProjectLinkUtils
							.generateMilestonePreviewFullLink(
									context.getSiteUrl(),
									milestone.getProjectid(), milestone.getId());
					A link = TagBuilder
							.newA(milestoneLink, milestone.getName());
					return TagBuilder.newLink(img, link).write();
				}
			} catch (Exception e) {
				log.error("Error", e);
			}

			return value;
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();
			if (bug.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						bug.getAssignUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(bug.getSaccountid()),
						bug.getAssignuser());
				A link = TagBuilder.newA(userLink, bug.getAssignuserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return "";
			}
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
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(user.getAccountId()),
						user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}
	}

	public static class LogUserFieldFormat extends FieldFormat {

		public LogUserFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();
			if (bug.getLogby() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						bug.getLoguserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(bug.getSaccountid()),
						bug.getLogby());
				A link = TagBuilder.newA(userLink, bug.getLoguserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return "";
			}
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
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(user.getAccountId()),
						user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}

	}
}
