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

import java.io.Serializable;
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

	protected void setupMailHeaders(SimpleMilestone milestone,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				milestone.getProjectid());

		SimpleProject relatedProject = projectService.findById(
				milestone.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put("webLink", linkGenerator.generateProjectFullLink());

		listOfTitles.add(currentProject);

		String summary = milestone.getName();
		String summaryLink = ProjectLinkUtils.generateMilestonePreviewLink(
				milestone.getProjectid(), milestone.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "phase");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int milestoneId = emailNotification.getTypeid();
		SimpleMilestone milestone = milestoneService.findById(milestoneId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ milestone.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has created the phase \""
				+ StringUtils.trim(milestone.getName(), 100) + "\"",
				"templates/email/project/itemCreatedNotifier.mt");

		setupMailHeaders(milestone, emailNotification, templateGenerator);

		templateGenerator.putVariable("properties",
				getListOfProperties(milestone, user));

		return templateGenerator;
	}

	protected Map<String, List<MilestoneLinkMapper>> getListOfProperties(
			SimpleMilestone milestone, SimpleUser user) {
		Map<String, List<MilestoneLinkMapper>> listOfDisplayProperties = new LinkedHashMap<String, List<MilestoneLinkMapper>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				milestone.getProjectid());

		if (milestone.getStartdate() != null)
			listOfDisplayProperties.put(mapper.getFieldLabel("startdate"),
					Arrays.asList(new MilestoneLinkMapper(null, DateTimeUtils
							.converToStringWithUserTimeZone(
									milestone.getStartdate(),
									user.getTimezone()))));
		else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("startdate"), null);
		}

		listOfDisplayProperties.put(mapper.getFieldLabel("status"), Arrays
				.asList(new MilestoneLinkMapper(null, milestone.getStatus())));

		if (milestone.getEnddate() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("enddate"), Arrays
							.asList(new MilestoneLinkMapper(null, DateTimeUtils
									.converToStringWithUserTimeZone(
											milestone.getEnddate(),
											user.getTimezone()))));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("enddate"), null);
		}

		listOfDisplayProperties.put(mapper.getFieldLabel("owner"), Arrays
				.asList(new MilestoneLinkMapper(linkGenerator
						.generateUserPreviewFullLink(milestone.getOwner()),
						milestone.getOwnerFullName())));

		if (milestone.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new MilestoneLinkMapper(null, milestone
							.getDescription())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					null);
		}

		return listOfDisplayProperties;
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
		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ milestone.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has updated the phase \"" + subject + "\"",
				"templates/email/project/itemUpdatedNotifier.mt");

		setupMailHeaders(milestone, emailNotification, templateGenerator);

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

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ milestone.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ "  has commented on phase \""
				+ StringUtils.trim(milestone.getName(), 100) + "\"",
				"templates/email/project/itemCommentNotifier.mt");

		setupMailHeaders(milestone, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);
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
			fieldNameMap.put("owner", "Responsible User");
			fieldNameMap.put("description", "Description");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

	public class MilestoneLinkMapper implements Serializable {
		private static final long serialVersionUID = 2212688618608788187L;

		private String link;
		private String displayname;

		public MilestoneLinkMapper(String link, String displayname) {
			this.link = link;
			this.displayname = displayname;
		}

		public String getWebLink() {
			return link;
		}

		public void setWebLink(String link) {
			this.link = link;
		}

		public String getDisplayName() {
			return displayname;
		}

		public void setDisplayName(String displayname) {
			this.displayname = displayname;
		}
	}

}
