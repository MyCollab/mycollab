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
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingType;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.TaskPriority;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectNotificationSettingService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.I18nFieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
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
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectTaskRelayEmailNotificationActionImpl extends
		SendMailToFollowersAction<SimpleTask> implements
		ProjectTaskRelayEmailNotificationAction {

	private static Logger log = LoggerFactory
			.getLogger(ProjectTaskRelayEmailNotificationActionImpl.class);

	@Autowired
	private ProjectTaskService projectTaskService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectMemberService projectMemberService;

	@Autowired
	private ProjectNotificationSettingService projectNotificationService;

	private static final TaskFieldNameMapper mapper = new TaskFieldNameMapper();

	@Override
	protected void buildExtraTemplateVariables(MailContext<SimpleTask> context) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", bean.getProjectName());
		currentProject.put(
				"webLink",
				ProjectLinkGenerator.generateProjectFullLink(siteUrl,
						bean.getProjectid()));

		listOfTitles.add(currentProject);

		SimpleRelayEmailNotification emailNotification = context
				.getEmailNotification();

		HashMap<String, String> taskCode = new HashMap<String, String>();
		SimpleProject relatedProject = projectService.findById(
				bean.getProjectid(), emailNotification.getSaccountid());
		taskCode.put("displayName", "[" + relatedProject.getShortname() + "-"
				+ bean.getTaskkey() + "]");
		taskCode.put(
				"webLink",
				ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl,
						bean.getProjectid(), bean.getId()));

		listOfTitles.add(taskCode);

		String summary = bean.getTaskname();
		String summaryLink = ProjectLinkGenerator.generateTaskPreviewFullLink(
				siteUrl, bean.getProjectid(), bean.getId());

		String avatarId = "";

		SimpleProjectMember projectMember = projectMemberService
				.findMemberByUsername(emailNotification.getChangeby(),
						bean.getProjectid(), emailNotification.getSaccountid());
		if (projectMember != null) {
			avatarId = projectMember.getMemberAvatarId();
		}
		Img userAvatar = new Img("", SiteConfiguration.getAvatarLink(avatarId,
				16));
		userAvatar.setWidth("16");
		userAvatar.setHeight("16");
		userAvatar.setStyle("display: inline-block; vertical-align: top;");

		String makeChangeUser = userAvatar.toString()
				+ emailNotification.getChangeByUserFullName();

		if (MonitorTypeConstants.CREATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					TaskI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					TaskI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					TaskI18nEnum.MAIL_COMMENT_ITEM_HEADING, makeChangeUser));
		}

		contentGenerator.putVariable("titles", listOfTitles);
		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);

	}

	@Override
	protected SimpleTask getBeanInContext(MailContext<SimpleTask> context) {
		return projectTaskService.findById(context.getTypeid(),
				context.getSaccountid());
	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getTaskname(), 100);
	}

	@Override
	protected String getCreateSubject(MailContext<SimpleTask> context) {
		return context.getMessage(TaskI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getUpdateSubject(MailContext<SimpleTask> context) {
		return context.getMessage(TaskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getCommentSubject(MailContext<SimpleTask> context) {
		return context.getMessage(TaskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
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

		if (notificationSettings != null && notificationSettings.size() > 0) {
			for (ProjectNotificationSetting notificationSetting : notificationSettings) {
				if (ProjectNotificationSettingType.NONE
						.equals(notificationSetting.getLevel())) {
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
				} else if (ProjectNotificationSettingType.MINIMAL
						.equals(notificationSetting.getLevel())) {
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
						SimpleTask task = projectTaskService.findById(
								notification.getTypeid(),
								notification.getSaccountid());
						if (task.getAssignuser() != null
								&& task.getAssignuser().equals(
										notificationSetting.getUsername())) {
							for (SimpleUser user : activeUsers) {
								if ((user.getUsername() != null)
										&& user.getUsername().equals(
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

	public static class TaskFieldNameMapper extends ItemFieldMapper {

		public TaskFieldNameMapper() {

			put("taskname", TaskI18nEnum.FORM_TASK_NAME, true);

			put("startdate", new DateFieldFormat("startdate",
					TaskI18nEnum.FORM_START_DATE));
			put("enddate", new DateFieldFormat("enddate",
					TaskI18nEnum.FORM_END_DATE));

			put("actualstartdate", new DateFieldFormat("actualstartdate",
					TaskI18nEnum.FORM_ACTUAL_START_DATE));
			put("actualenddate", new DateFieldFormat("actualenddate",
					TaskI18nEnum.FORM_ACTUAL_END_DATE));

			put("deadline", new DateFieldFormat("deadline",
					TaskI18nEnum.FORM_DEADLINE));
			put("percentagecomplete", TaskI18nEnum.FORM_PERCENTAGE_COMPLETE);

			put("priority", new I18nFieldFormat("priority",
					TaskI18nEnum.FORM_PRIORITY, TaskPriority.class));
			put("assignuser", new AssigneeFieldFormat("assignuser",
					GenericI18Enum.FORM_ASSIGNEE));

			put("tasklistid", new TaskGroupFieldFormat("tasklistid",
					TaskI18nEnum.FORM_TASKGROUP));
			put("notes", TaskI18nEnum.FORM_NOTES);
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleTask task = (SimpleTask) context.getWrappedBean();
			if (task.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						task.getAssignUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(task.getSaccountid()),
								task.getAssignuser());
				A link = TagBuilder
						.newA(userLink, task.getAssignUserFullName());
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

	public static class TaskGroupFieldFormat extends FieldFormat {

		public TaskGroupFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleTask task = (SimpleTask) context.getWrappedBean();
			if (task.getTasklistid() != null) {
				String taskgroupIconLink = ProjectResources
						.getResourceLink(ProjectTypeConstants.TASK_LIST);
				Img img = TagBuilder.newImg("icon", taskgroupIconLink);

				String tasklistlink = ProjectLinkGenerator
						.generateTaskGroupPreviewFullLink(context.getSiteUrl(),
								task.getProjectid(), task.getTasklistid());
				A link = TagBuilder.newA(tasklistlink, task.getTaskListName());
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

			try {
				int taskgroupId = Integer.parseInt(value);
				ProjectTaskListService tasklistService = ApplicationContextUtil
						.getSpringBean(ProjectTaskListService.class);
				SimpleTaskList taskgroup = tasklistService.findById(
						taskgroupId, context.getUser().getAccountId());

				if (taskgroup != null) {
					String taskgroupIconLink = ProjectResources
							.getResourceLink(ProjectTypeConstants.TASK_LIST);
					Img img = TagBuilder.newImg("icon", taskgroupIconLink);

					String tasklistlink = ProjectLinkGenerator
							.generateTaskPreviewFullLink(context.getSiteUrl(),
									taskgroup.getProjectid(), taskgroup.getId());
					A link = TagBuilder.newA(tasklistlink, taskgroup.getName());
					return TagBuilder.newLink(img, link).write();
				}
			} catch (Exception e) {
				log.error("Error", e);
			}

			return value;
		}

	}
}
