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
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProblemService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.project.ProjectProblemRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class ProjectProblemRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		ProjectProblemRelayEmailNotificationAction {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AuditLogService auditLogService;

	private final ProjectFieldNameMapper mapper;

	public ProjectProblemRelayEmailNotificationActionImpl() {
		mapper = new ProjectFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleProblem problem,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				problem.getProjectid());

		SimpleProject relatedProject = projectService.findById(
				problem.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put("webLink", linkGenerator.generateProjectFullLink());

		listOfTitles.add(currentProject);

		String summary = problem.getIssuename();
		String summaryLink = ProjectLinkUtils.generateProblemPreviewLink(
				problem.getProjectid(), problem.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "problem");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<ProblemLinkMapper>> getListOfProperties(
			SimpleProblem problem, SimpleUser user) {
		Map<String, List<ProblemLinkMapper>> listOfDisplayProperties = new LinkedHashMap<String, List<ProblemLinkMapper>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				problem.getProjectid());

		listOfDisplayProperties.put(mapper.getFieldLabel("raisedbyuser"),
				Arrays.asList(new ProblemLinkMapper(
						linkGenerator.generateUserPreviewFullLink(problem
								.getRaisedbyuser()), problem
								.getRaisedByUserFullName())));

		if (problem.getAssigntouser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assigntouser"),
					Arrays.asList(new ProblemLinkMapper(linkGenerator
							.generateUserPreviewFullLink(problem
									.getAssigntouser()), problem
							.getAssignedUserFullName())));
		}

		if (problem.getDatedue() != null)
			listOfDisplayProperties
					.put(mapper.getFieldLabel("datedue"), Arrays
							.asList(new ProblemLinkMapper(null, DateTimeUtils
									.converToStringWithUserTimeZone(
											problem.getDatedue(),
											user.getTimezone()))));
		else {
			listOfDisplayProperties.put(mapper.getFieldLabel("datedue"), null);
		}

		listOfDisplayProperties
				.put(mapper.getFieldLabel("status"),
						Arrays.asList(new ProblemLinkMapper(null, problem
								.getStatus())));

		if (problem.getImpact() != null)
			listOfDisplayProperties.put(mapper.getFieldLabel("impact"), Arrays
					.asList(new ProblemLinkMapper(null, problem.getImpact())));
		else {
			listOfDisplayProperties.put(mapper.getFieldLabel("impact"), null);
		}

		listOfDisplayProperties.put(mapper.getFieldLabel("priority"), Arrays
				.asList(new ProblemLinkMapper(null, problem.getPriority())));

		if (problem.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new ProblemLinkMapper(null, problem
							.getDescription())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					null);
		}

		return listOfDisplayProperties;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int problemId = emailNotification.getTypeid();
		SimpleProblem problem = problemService.findById(problemId, 0);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ problem.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has created the problem \""
				+ StringUtils.trim(problem.getIssuename(), 100) + "\"",
				"templates/email/project/itemCreatedNotifier.mt");

		setupMailHeaders(problem, emailNotification, templateGenerator);

		templateGenerator.putVariable("properties",
				getListOfProperties(problem, user));

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int problemId = emailNotification.getTypeid();
		SimpleProblem problem = problemService.findById(problemId, 0);
		if (problem == null) {
			return null;
		}

		String subject = StringUtils.trim(problem.getIssuename(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ problem.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has updated the problem \"" + subject + "...\" edited",
				"templates/email/project/itemUpdatedNotifier.mt");

		setupMailHeaders(problem, emailNotification, templateGenerator);

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
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int problemId = emailNotification.getTypeid();
		SimpleProblem problem = problemService.findById(problemId, 0);
		if (problem == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ problem.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has commented on the problem \""
				+ StringUtils.trim(problem.getIssuename(), 100) + "\"",
				"templates/email/project/itemCommentNotifier.mt");
		setupMailHeaders(problem, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class ProjectFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ProjectFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("issuename", "Issue name");
			fieldNameMap.put("assigntouser", "Assigned to");
			fieldNameMap.put("datedue", "Due date");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("impact", "Impact");
			fieldNameMap.put("priority", "Priority");
			fieldNameMap.put("raisedbyuser", "Raised By");
			fieldNameMap.put("description", "Description");
			fieldNameMap.put("resolution", "Resolution");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

	public class ProblemLinkMapper implements Serializable {
		private static final long serialVersionUID = 2212688618608788187L;

		private String link;
		private String displayname;

		public ProblemLinkMapper(String link, String displayname) {
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
