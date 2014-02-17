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
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingType;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class BugRelayEmailNotificationActionImpl extends
		SendMailToFollowersAction implements BugRelayEmailNotificationAction {
	@Autowired
	private BugService bugService;
	@Autowired
	private AuditLogService auditLogService;

	private final BugFieldNameMapper mapper;

	public BugRelayEmailNotificationActionImpl() {
		mapper = new BugFieldNameMapper();
	}

	private Map<String, String> constructHyperLinks(SimpleBug bug) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				bug.getProjectid());

		hyperLinks.put("bugUrl",
				linkGenerator.generateBugPreviewFullLink(bug.getId()));
		hyperLinks.put("shortBugUrl",
				StringUtils.subString(bug.getSummary(), 150));
		hyperLinks.put("projectUrl", linkGenerator.generateProjectFullLink());
		if (bug.getLogby() != null) {
			hyperLinks.put("loggedUserUrl",
					linkGenerator.generateUserPreviewFullLink(bug.getLogby()));
		}
		if (bug.getAssignuser() != null) {
			hyperLinks.put("assignUserUrl", linkGenerator
					.generateUserPreviewFullLink(bug.getAssignuser()));
		}

		if (bug.getMilestoneid() != null) {
			hyperLinks.put("milestoneUrl", linkGenerator
					.generateMilestonePreviewFullLink(bug.getMilestoneid()));
		}
		return hyperLinks;
	}

	private void constructBugComponentVersionLink(SimpleBug bug,
			TemplateGenerator templateGenerator) {
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				bug.getProjectid());
		if (bug.getComponents() != null) {
			List<BugLinkMapper> lstBugComponent = new ArrayList<BugRelayEmailNotificationActionImpl.BugLinkMapper>();
			for (int i = 0; i < bug.getComponents().size(); i++) {
				com.esofthead.mycollab.module.tracker.domain.Component bugComponent = bug
						.getComponents().get(i);
				lstBugComponent.add(new BugLinkMapper(linkGenerator
						.generateBugComponentPreviewFullLink(bugComponent
								.getId()), bugComponent.getComponentname()));
			}
			templateGenerator.putVariable("lstBugComponent", lstBugComponent);
		}

		if (bug.getComponents() != null) {
			List<BugLinkMapper> lstBugVersion = new ArrayList<BugRelayEmailNotificationActionImpl.BugLinkMapper>();
			for (int i = 0; i < bug.getAffectedVersions().size(); i++) {
				Version bugVersion = bug.getAffectedVersions().get(i);
				lstBugVersion.add(new BugLinkMapper(linkGenerator
						.generateBugVersionPreviewFullLink(bugVersion.getId()),
						bugVersion.getVersionname()));
			}
			templateGenerator.putVariable("lstBugVersion", lstBugVersion);
		}
	}

	@Override
	public TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int bugId = emailNotification.getTypeid();
		SimpleBug bug = bugService.findById(bugId,
				emailNotification.getSaccountid());
		if (bug != null) {
			String subject = bug.getSummary();

			TemplateGenerator templateGenerator = new TemplateGenerator(
					"[$bug.projectname]: Bug \"" + subject
							+ "\" has been created",
					"templates/email/project/bugCreatedNotifier.mt");

			ScheduleUserTimeZoneUtils.formatDateTimeZone(bug,
					user.getTimezone(), new String[] { "duedate" });
			templateGenerator.putVariable("bug", bug);
			templateGenerator.putVariable("hyperLinks",
					constructHyperLinks(bug));
			constructBugComponentVersionLink(bug, templateGenerator);
			return templateGenerator;
		} else {
			return null;
		}

	}

	@Override
	public TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleBug bug = bugService.findById(emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.subString(bug.getSummary(), 150);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$bug.projectname]: Bug \"" + subject
						+ "...\" has been updated",
				"templates/email/project/bugUpdatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(bug, user.getTimezone(),
				new String[] { "duedate" });
		templateGenerator.putVariable("bug", bug);
		templateGenerator.putVariable("hyperLinks", constructHyperLinks(bug));

		constructBugComponentVersionLink(bug, templateGenerator);
		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());

			ScheduleUserTimeZoneUtils.formatDate(auditLog, user.getTimezone(),
					new String[] { "duedate" });
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("mapper", mapper);
		}

		templateGenerator.putVariable(
				"lstComment",
				getListComment(bug.getSaccountid(),
						MonitorTypeConstants.PRJ_BUG, bug.getId()));

		return templateGenerator;
	}

	@Override
	public TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int bugId = emailNotification.getTypeid();
		SimpleBug bug = bugService.findById(bugId,
				emailNotification.getSaccountid());
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				bug.getProjectid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$bug.projectname]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has commented on "
						+ StringUtils.subString(bug.getSummary(), 100) + "\"",
				"templates/email/project/bugCommentNotifier.mt");
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", linkGenerator
				.generateUserPreviewFullLink(emailNotification.getChangeby()));
		templateGenerator.putVariable("bug", bug);
		templateGenerator.putVariable("hyperLinks", constructHyperLinks(bug));

		return templateGenerator;
	}

	public class BugFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		BugFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("summary", "Bug Summary");
			fieldNameMap.put("description", "Description");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("assignuser", "Assigned to");
			fieldNameMap.put("resolution", "Resolution");
			fieldNameMap.put("severity", "Serverity");
			fieldNameMap.put("environment", "Environment");
			fieldNameMap.put("priority", "Priority");
			fieldNameMap.put("duedate", "Due Date");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

	public class BugLinkMapper {
		private String link;
		private String displayname;

		public BugLinkMapper(String link, String displayname) {
			this.link = link;
			this.displayname = displayname;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public String getDisplayname() {
			return displayname;
		}

		public void setDisplayname(String displayname) {
			this.displayname = displayname;
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
}
