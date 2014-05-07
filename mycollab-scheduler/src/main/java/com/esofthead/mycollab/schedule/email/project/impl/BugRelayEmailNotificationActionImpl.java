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
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingType;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
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
		SendMailToFollowersAction implements BugRelayEmailNotificationAction {
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
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int bugId = emailNotification.getTypeid();
		SimpleBug bug = bugService.findById(bugId,
				emailNotification.getSaccountid());
		if (bug != null) {
			String subject = StringUtils.trim(bug.getSummary(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator("["
					+ bug.getProjectname() + "]: "
					+ emailNotification.getChangeByUserFullName()
					+ " has created the bug \"" + subject + "\"",
					"templates/email/project/itemCreatedNotifier.mt");

			setupMailHeaders(bug, emailNotification, templateGenerator);

			templateGenerator.putVariable("context",
					new MailContext<SimpleBug>(bug, user, siteUrl));
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
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleBug bug = bugService.findById(emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (bug == null) {
			return null;
		}

		String subject = StringUtils.trim(bug.getSummary(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ bug.getProjectname() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has updated the bug \"" + subject + "\"",
				"templates/email/project/itemUpdatedNotifier.mt");

		setupMailHeaders(bug, emailNotification, templateGenerator);

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("context",
					new MailContext<SimpleBug>(bug, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);
		}

		return templateGenerator;
	}

	@Override
	public TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int bugId = emailNotification.getTypeid();
		SimpleBug bug = bugService.findById(bugId,
				emailNotification.getSaccountid());
		if (bug == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ bug.getProjectname() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has commented on the bug \""
				+ StringUtils.trim(bug.getSummary(), 100) + "\"",
				"templates/email/project/itemCommentNotifier.mt");

		setupMailHeaders(bug, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	@Override
	protected List<SimpleUser> getListNotififyUserWithFilter(
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
			put("summary", "Bug Summary", true);

			put("environment", "Environment", true);

			put("description", "Description", true);

			put("assignuser", new AssigneeFieldFormat("assignuser", "Assignee"));
			put("milestoneid", new MilestoneFieldFormat("milestoneid",
					"Milestone"));

			put("status", "Status");
			put("resolution", "Resolution");

			put("severity", "Serverity");
			put("priority", "Priority");

			put("duedate", new DateFieldFormat("duedate", "Due Date"));
			put("logby", new LogUserFieldFormat("logby", "Logged By"));
		}
	}

	public static class MilestoneFieldFormat extends FieldFormat {

		public MilestoneFieldFormat(String fieldName, String displayName) {
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

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();
			String userAvatarLink = LinkUtils.getAvatarLink(
					bug.getAssignUserAvatarId(), 16);
			Img img = TagBuilder.newImg("avatar", userAvatarLink);

			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(bug.getSaccountid()),
					bug.getAssignuser());
			A link = TagBuilder.newA(userLink, bug.getAssignuserFullName());
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

	public static class LogUserFieldFormat extends FieldFormat {

		public LogUserFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();
			String userAvatarLink = LinkUtils.getAvatarLink(
					bug.getLoguserAvatarId(), 16);
			Img img = TagBuilder.newImg("avatar", userAvatarLink);

			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(bug.getSaccountid()), bug.getLogby());
			A link = TagBuilder.newA(userLink, bug.getLoguserFullName());
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
