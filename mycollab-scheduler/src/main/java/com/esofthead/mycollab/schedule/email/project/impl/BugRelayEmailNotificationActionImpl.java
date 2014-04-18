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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSettingType;
import com.esofthead.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailItemLink;
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
	@Autowired
	private ProjectService projectService;

	private final BugFieldNameMapper mapper;

	public BugRelayEmailNotificationActionImpl() {
		mapper = new BugFieldNameMapper();
	}

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

			templateGenerator.putVariable("properties",
					getListOfProperties(bug, user));

			return templateGenerator;
		} else {
			return null;
		}

	}

	protected void setupMailHeaders(SimpleBug bug,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				bug.getProjectid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", bug.getProjectname());
		currentProject.put("webLink", linkGenerator.generateProjectFullLink());

		listOfTitles.add(currentProject);

		HashMap<String, String> bugCode = new HashMap<String, String>();
		SimpleProject relatedProject = projectService.findById(
				bug.getProjectid(), emailNotification.getSaccountid());
		bugCode.put("displayName", "[" + relatedProject.getShortname() + "-"
				+ bug.getBugkey() + "]");
		bugCode.put("webLink",
				linkGenerator.generateBugPreviewFullLink(bug.getId()));

		listOfTitles.add(bugCode);

		String summary = bug.getSummary();
		String summaryLink = ProjectLinkUtils.generateBugPreviewLink(
				bug.getProjectid(), bug.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "bug");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleBug bug, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				bug.getProjectid());

		listOfDisplayProperties.put(mapper.getFieldLabel("status"),
				Arrays.asList(new MailItemLink(null, bug.getStatus())));
		listOfDisplayProperties.put(mapper.getFieldLabel("priority"),
				Arrays.asList(new MailItemLink(null, bug.getPriority())));
		listOfDisplayProperties.put(mapper.getFieldLabel("severity"),
				Arrays.asList(new MailItemLink(null, bug.getSeverity())));
		listOfDisplayProperties.put(mapper.getFieldLabel("resolution"),
				Arrays.asList(new MailItemLink(null, bug.getResolution())));
		listOfDisplayProperties.put(mapper.getFieldLabel("status"),
				Arrays.asList(new MailItemLink(null, bug.getStatus())));
		if (bug.getDuedate() != null)
			listOfDisplayProperties.put(mapper.getFieldLabel("duedate"), Arrays
					.asList(new MailItemLink(null, DateTimeUtils
							.converToStringWithUserTimeZone(bug.getDuedate(),
									user.getTimezone()))));
		else {
			listOfDisplayProperties.put(mapper.getFieldLabel("duedate"), null);
		}
		if (bug.getMilestoneid() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("milestone"), Arrays
							.asList(new MailItemLink(linkGenerator
									.generateMilestonePreviewFullLink(bug
											.getMilestoneid()), bug
									.getMilestoneName())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("milestone"), null);
		}

		if (bug.getLogby() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("logby"), Arrays
					.asList(new MailItemLink(linkGenerator
							.generateUserPreviewFullLink(bug.getLogby()), bug
							.getLoguserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("logby"), null);
		}

		if (bug.getAssignuser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					Arrays.asList(new MailItemLink(linkGenerator
							.generateUserPreviewFullLink(bug.getAssignuser()),
							bug.getAssignuserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					null);
		}

		if (bug.getComponents() != null) {
			List<MailItemLink> lstBugComponent = new ArrayList<MailItemLink>();
			for (int i = 0; i < bug.getComponents().size(); i++) {
				com.esofthead.mycollab.module.tracker.domain.Component bugComponent = bug
						.getComponents().get(i);
				lstBugComponent.add(new MailItemLink(linkGenerator
						.generateBugComponentPreviewFullLink(bugComponent
								.getId()), bugComponent.getComponentname()));
			}
			listOfDisplayProperties.put("Components", lstBugComponent);
		} else {
			listOfDisplayProperties.put("Components", null);
		}

		if (bug.getComponents() != null) {
			List<MailItemLink> lstBugVersion = new ArrayList<MailItemLink>();
			for (int i = 0; i < bug.getAffectedVersions().size(); i++) {
				Version bugVersion = bug.getAffectedVersions().get(i);
				lstBugVersion.add(new MailItemLink(linkGenerator
						.generateBugVersionPreviewFullLink(bugVersion.getId()),
						bugVersion.getVersionname()));
			}
			listOfDisplayProperties.put("Versions", lstBugVersion);
		} else {
			listOfDisplayProperties.put("Versions", null);
		}

		return listOfDisplayProperties;
	}

	@Override
	public TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleBug bug = bugService.findById(emailNotification.getTypeid(),
				emailNotification.getSaccountid());

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
			fieldNameMap.put("logby", "Logged By");
			fieldNameMap.put("milestoneid", "Milestone");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}
}
