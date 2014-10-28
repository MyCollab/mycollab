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

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.NotificationType;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugPriority;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugResolution;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugSeverity;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectNotificationSettingService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.I18nFieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;

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

	private static final Logger LOG = LoggerFactory
			.getLogger(BugRelayEmailNotificationActionImpl.class);

	@Autowired
	private BugService bugService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectMemberService projectMemberService;

	@Autowired
	private ProjectNotificationSettingService projectNotificationService;

	private static final BugFieldNameMapper mapper = new BugFieldNameMapper();

	@Override
	protected void buildExtraTemplateVariables(MailContext<SimpleBug> context) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", bean.getProjectname());
		currentProject.put(
				"webLink",
				ProjectLinkGenerator.generateProjectFullLink(siteUrl,
						bean.getProjectid()));

		listOfTitles.add(currentProject);

		SimpleRelayEmailNotification emailNotification = context
				.getEmailNotification();

		HashMap<String, String> bugCode = new HashMap<String, String>();
		SimpleProject relatedProject = projectService.findById(
				bean.getProjectid(), emailNotification.getSaccountid());
		bugCode.put("displayName", "[" + relatedProject.getShortname() + "-"
				+ bean.getBugkey() + "]");
		bugCode.put(
				"webLink",
				ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl,
						bean.getBugkey(), bean.getProjectShortName()));

		listOfTitles.add(bugCode);

		String summary = bean.getSummary();
		String summaryLink = ProjectLinkGenerator.generateBugPreviewFullLink(
				siteUrl, bean.getBugkey(), bean.getProjectShortName());

		String avatarId = "";

		SimpleProjectMember projectMember = projectMemberService
				.findMemberByUsername(emailNotification.getChangeby(),
						bean.getProjectid(), emailNotification.getSaccountid());
		if (projectMember != null) {
			avatarId = projectMember.getMemberAvatarId();
		}
		Img userAvatar = new Img("", StorageManager.getAvatarLink(avatarId, 16));
		userAvatar.setWidth("16");
		userAvatar.setHeight("16");
		userAvatar.setStyle("display: inline-block; vertical-align: top;");

		String makeChangeUser = userAvatar.toString()
				+ emailNotification.getChangeByUserFullName();

		if (MonitorTypeConstants.CREATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					BugI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					BugI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					BugI18nEnum.MAIL_COMMENT_ITEM_HEADING, makeChangeUser));
		}

		contentGenerator.putVariable("titles", listOfTitles);
		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);

	}

	@Override
	protected SimpleBug getBeanInContext(MailContext<SimpleBug> context) {
		return bugService.findById(Integer.parseInt(context.getTypeid()),
				context.getSaccountid());
	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getSummary(), 100);
	}

	@Override
	protected String getCreateSubject(MailContext<SimpleBug> context) {
		return context.getMessage(BugI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
				bean.getProjectname(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getUpdateSubject(MailContext<SimpleBug> context) {
		return context.getMessage(BugI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
				bean.getProjectname(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getCommentSubject(MailContext<SimpleBug> context) {
		return context.getMessage(BugI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
				bean.getProjectname(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected List<SimpleUser> getListNotifyUsersWithFilter(
			ProjectRelayEmailNotification notification) {
		List<ProjectNotificationSetting> notificationSettings = projectNotificationService
				.findNotifications(notification.getProjectId(),
						notification.getSaccountid());

		List<SimpleUser> activeUsers = projectMemberService
				.getActiveUsersInProject(notification.getProjectId(),
						notification.getSaccountid());

		List<SimpleUser> inListUsers = notification.getNotifyUsers();

		if (notificationSettings != null) {
			for (ProjectNotificationSetting notificationSetting : notificationSettings) {
				if (NotificationType.None.name().equals(
						notificationSetting.getLevel())) {
					// remove users in list if he is already in list
					for (int i = inListUsers.size() - 1; i >= 0; i--) {
						SimpleUser inUser = inListUsers.get(i);
						if ((inUser.getUsername() != null)
								&& inUser.getUsername().equals(
										notificationSetting.getUsername())) {
							inListUsers.remove(i);
							break;
						}
					}
				} else if (NotificationType.Minimal.name().equals(
						notificationSetting.getLevel())) {
					boolean isAlreadyInList = false;
					for (SimpleUser user : inListUsers) {
						if ((user.getUsername() != null)
								&& user.getUsername().equals(
										notificationSetting.getUsername())) {
							isAlreadyInList = true;
							break;
						}
					}

					if (!isAlreadyInList) {
						SimpleBug bug = bugService.findById(
								Integer.parseInt(notification.getTypeid()),
								notification.getSaccountid());
						if (bug.getAssignuser() != null
								&& bug.getAssignuser().equals(
										notificationSetting.getUsername())) {
							for (SimpleUser user : activeUsers) {
								if ((user.getUsername()) != null
										&& user.getUsername().equals(
												notificationSetting
														.getUsername())) {
									inListUsers.add(user);
									break;
								}
							}
						}
					}

				} else if (NotificationType.Full.name().equals(
						notificationSetting.getLevel())) {
					boolean isAlreadyInList = false;
					for (SimpleUser user : inListUsers) {
						if ((user.getUsername() != null)
								&& user.getUsername().equals(
										notificationSetting.getUsername())) {
							isAlreadyInList = true;
							break;
						}
					}

					if (!isAlreadyInList) {
						for (SimpleUser user : activeUsers) {
							if ((user.getUsername() != null)
									&& user.getUsername().equals(
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

			put("status", new I18nFieldFormat("status",
					BugI18nEnum.FORM_STATUS, BugStatus.class));

			put("resolution", new I18nFieldFormat("resolution",
					BugI18nEnum.FORM_RESOLUTION, BugResolution.class));

			put("severity", new I18nFieldFormat("severity",
					BugI18nEnum.FORM_SEVERITY, BugSeverity.class));
			put("priority", new I18nFieldFormat("priority",
					BugI18nEnum.FORM_PRIORITY, BugPriority.class));

			put("duedate", new DateFieldFormat("duedate",
					BugI18nEnum.FORM_DUE_DATE));

			put("logby", new LogUserFieldFormat("logby",
					BugI18nEnum.FORM_LOG_BY));
		}
	}

	public static class MilestoneFieldFormat extends FieldFormat {

		public MilestoneFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();

			if (bug.getMilestoneid() == null || bug.getMilestoneName() == null) {
				return new Span().write();
			}
			String milestoneIconLink = ProjectResources
					.getResourceLink(ProjectTypeConstants.MILESTONE);
			Img img = TagBuilder.newImg("icon", milestoneIconLink);

			String milestoneLink = ProjectLinkGenerator
					.generateMilestonePreviewFullLink(context.getSiteUrl(),
							bug.getProjectid(), bug.getMilestoneid());
			A link = TagBuilder.newA(milestoneLink, bug.getMilestoneName());
			return TagBuilder.newLink(img, link).write();
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return new Span().write();
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

					String milestoneLink = ProjectLinkGenerator
							.generateMilestonePreviewFullLink(
									context.getSiteUrl(),
									milestone.getProjectid(), milestone.getId());
					A link = TagBuilder
							.newA(milestoneLink, milestone.getName());
					return TagBuilder.newLink(img, link).write();
				}
			} catch (Exception e) {
				LOG.error("Error", e);
			}

			return value;
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();
			if (bug.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						bug.getAssignUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(bug.getSaccountid()),
								bug.getAssignuser());
				A link = TagBuilder.newA(userLink, bug.getAssignuserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return new Span().write();
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return new Span().write();
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
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

		public LogUserFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleBug bug = (SimpleBug) context.getWrappedBean();
			if (bug.getLogby() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						bug.getLoguserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(bug.getSaccountid()),
								bug.getLogby());
				A link = TagBuilder.newA(userLink, bug.getLoguserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return new Span().write();
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return new Span().write();
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
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
