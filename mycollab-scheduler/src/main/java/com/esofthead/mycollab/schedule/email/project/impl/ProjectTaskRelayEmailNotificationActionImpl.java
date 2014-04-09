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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingType;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class ProjectTaskRelayEmailNotificationActionImpl extends
		SendMailToFollowersAction implements
		ProjectTaskRelayEmailNotificationAction {

	@Autowired
	private ProjectTaskService projectTaskService;
	@Autowired
	private AuditLogService auditLogService;

	private final ProjectFieldNameMapper mapper;

	public ProjectTaskRelayEmailNotificationActionImpl() {
		mapper = new ProjectFieldNameMapper();
	}

	@Override
	public TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int taskId = emailNotification.getTypeid();
		SimpleTask task = projectTaskService.findById(taskId,
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(task.getTaskname(), 100);
		ScheduleUserTimeZoneUtils.formatDateTimeZone(task, user.getTimezone(),
				new String[] { "startdate", "enddate", "deadline",
						"actualstartdate", "actualenddate" });

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$task.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has created the task \"" + subject + "\"",
				"templates/email/project/taskCreatedNotifier.mt");
		templateGenerator.putVariable("task", task);
		templateGenerator.putVariable("hyperLinks", createHyperLinks(task));
		return templateGenerator;
	}

	private Map<String, String> createHyperLinks(SimpleTask task) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				task.getProjectid());
		hyperLinks.put("taskUrl",
				linkGenerator.generateTaskPreviewFullLink(task.getId()));
		hyperLinks.put("shortTaskUrl",
				StringUtils.trim(task.getTaskname(), 100));
		hyperLinks.put("projectUrl", linkGenerator.generateProjectFullLink());
		hyperLinks
				.put("assignUserUrl", linkGenerator
						.generateUserPreviewFullLink(task.getAssignuser()));
		hyperLinks.put("taskListUrl", linkGenerator
				.generateTaskGroupPreviewFullLink(task.getTasklistid()));
		return hyperLinks;
	}

	@Override
	public TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int taskId = emailNotification.getTypeid();
		SimpleTask task = projectTaskService.findById(taskId,
				emailNotification.getSaccountid());
		if (task == null) {
			return null;
		}

		ScheduleUserTimeZoneUtils.formatDateTimeZone(task, user.getTimezone(),
				new String[] { "startdate", "enddate", "deadline",
						"actualstartdate", "actualenddate" });
		String subject = StringUtils.trim(task.getTaskname(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$task.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has updated the task \"" + subject + "\"",
				"templates/email/project/taskUpdatedNotifier.mt");
		templateGenerator.putVariable("task", task);
		templateGenerator.putVariable("hyperLinks", createHyperLinks(task));

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());

			ScheduleUserTimeZoneUtils.formatDate(auditLog, user.getTimezone(),
					new String[] { "startdate", "enddate", "deadline",
							"actualstartdate", "actualenddate" });

			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("mapper", mapper);
		}
		templateGenerator.putVariable(
				"lstComment",
				getListComment(task.getSaccountid(), ProjectTypeConstants.TASK,
						task.getId()));

		return templateGenerator;
	}

	@Override
	public TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int taskId = emailNotification.getTypeid();
		SimpleTask task = projectTaskService.findById(taskId,
				emailNotification.getSaccountid());
		if (task == null) {
			return null;
		}
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				task.getProjectid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$task.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has commented on the task \""
						+ StringUtils.trim(task.getTaskname(), 100) + "\"",
				"templates/email/project/taskCommentNotifier.mt");
		templateGenerator.putVariable("task", task);
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", linkGenerator
				.generateUserPreviewFullLink(emailNotification.getChangeby()));
		templateGenerator.putVariable("hyperLinks", createHyperLinks(task));

		return templateGenerator;
	}

	public class ProjectFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ProjectFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("taskname", "Task Name");
			fieldNameMap.put("startdate", "Start Date");
			fieldNameMap.put("enddate", "End Date");
			fieldNameMap.put("actualstartdate", "Actual Start Date");
			fieldNameMap.put("actualenddate", "Actual End Date");
			fieldNameMap.put("assignUserFullName", "Assignee");
			fieldNameMap.put("percentagecomplete", "Complete (%)");
			fieldNameMap.put("notes", "Notes");
			fieldNameMap.put("priority", "Priority");
			fieldNameMap.put("deadline", "Deadline");
			fieldNameMap.put("taskListName", "Task Group");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

	@Override
	protected List<SimpleUser> getListNotififyUserWithFilter(
			ProjectRelayEmailNotification notification) {
		List<ProjectNotificationSetting> notificationSettings = projectNotificationService
				.findNotifications(
						((ProjectRelayEmailNotification) notification)
								.getProjectId(), notification.getSaccountid());

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
						SimpleTask task = projectTaskService.findById(
								notification.getTypeid(),
								notification.getSaccountid());
						if (task.getAssignuser() != null
								&& task.getAssignuser().equals(
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

}
