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
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class ProjectMilestoneRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		ProjectMilestoneRelayEmailNotificationAction {
	@Autowired
	private MilestoneService milestoneService;

	@Autowired
	private AuditLogService auditLogService;

	@Autowired
	private ProjectService projectService;

	private final ProjectFieldNameMapper mapper;

	public ProjectMilestoneRelayEmailNotificationActionImpl() {
		mapper = new ProjectFieldNameMapper();
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int milestoneId = emailNotification.getTypeid();
		SimpleMilestone milestone = milestoneService.findById(milestoneId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has created the phase \""
						+ StringUtils.trim(milestone.getName(), 100)
						+ "\"",
				"templates/email/project/phaseCreatedNotifier.mt");

		ScheduleUserTimeZoneUtils.formatDateTimeZone(milestone,
				user.getTimezone(), new String[] { "startdate", "enddate" });
		templateGenerator.putVariable("milestone", milestone);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(milestone, emailNotification));

		return templateGenerator;
	}

	private Map<String, String> createHyperLinks(SimpleMilestone milestone,
			SimpleRelayEmailNotification emailNotification) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				milestone.getProjectid());
		hyperLinks.put("milestoneURL", linkGenerator
				.generateMilestonePreviewFullLink(milestone.getId()));
		hyperLinks.put("projectUrl", linkGenerator.generateProjectFullLink());
		hyperLinks.put("ownerUserUrl", linkGenerator
				.generateUserPreviewFullLink(milestone.getOwnerFullName()));

		SimpleProject project = projectService.findById(
				milestone.getProjectid(), emailNotification.getSaccountid());
		if (project != null) {
			hyperLinks.put("projectName", project.getName());
		}

		return hyperLinks;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int milestoneId = emailNotification.getTypeid();
		SimpleMilestone milestone = milestoneService.findById(milestoneId,
				emailNotification.getSaccountid());
		if (milestone == null) {
			return null;
		}

		String subject = StringUtils.trim(milestone.getName(), 100);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has updated the phase \"" + subject + "\"",
				"templates/email/project/phaseUpdateNotifier.mt");

		ScheduleUserTimeZoneUtils.formatDateTimeZone(milestone,
				user.getTimezone(), new String[] { "startdate", "enddate" });
		templateGenerator.putVariable("milestone", milestone);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(milestone, emailNotification));

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			ScheduleUserTimeZoneUtils.formatDate(auditLog, user.getTimezone(),
					new String[] { "startdate", "enddate" });
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("mapper", mapper);
		}

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int milestoneId = emailNotification.getTypeid();
		SimpleMilestone milestone = milestoneService.findById(milestoneId,
				emailNotification.getSaccountid());
		if (milestone == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ "  has commented on phase \""
						+ StringUtils.trim(milestone.getName(), 100)
						+ "\"",
				"templates/email/project/phaseCommentNotifier.mt");

		templateGenerator.putVariable("milestone", milestone);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(milestone, emailNotification));
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				milestone.getProjectid());
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", linkGenerator
				.generateUserPreviewFullLink(emailNotification.getChangeby()));
		return templateGenerator;
	}

	public class ProjectFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ProjectFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("name", "Phase Name");
			fieldNameMap.put("startdate", "Start Date");
			fieldNameMap.put("enddate", "End Date");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("ownerFullName", "Responsible User");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

}
