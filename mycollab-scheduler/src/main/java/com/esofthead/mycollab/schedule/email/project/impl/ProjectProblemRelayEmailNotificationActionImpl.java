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
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProblemService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
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

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int problemId = emailNotification.getTypeid();
		SimpleProblem problem = problemService.findById(problemId, 0);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has created the problem \""
						+ StringUtils.trim(problem.getIssuename(), 100) + "\"",
				"templates/email/project/problemCreatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(problem,
				user.getTimezone(), new String[] { "dateraised", "datedue",
						"actualstartdate", "actualenddate" });
		templateGenerator.putVariable("problem", problem);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(problem, emailNotification));

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

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has updated the problem \"" + subject
						+ "...\" edited",
				"templates/email/project/problemUpdateNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(problem,
				user.getTimezone(), new String[] { "dateraised", "datedue",
						"actualstartdate", "actualenddate" });

		templateGenerator.putVariable("problem", problem);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(problem, emailNotification));
		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			ScheduleUserTimeZoneUtils.formatDate(auditLog, user.getTimezone(),
					new String[] { "dateraised", "datedue", "actualstartdate",
							"actualenddate" });
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("mapper", mapper);
		}
		templateGenerator.putVariable(
				"lstComment",
				getListComment(problem.getSaccountid(),
						ProjectTypeConstants.PROBLEM, problem.getId()));
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

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has commented on the problem \""
						+ StringUtils.trim(problem.getIssuename(), 100) + "\"",
				"templates/email/project/problemCommentNotifier.mt");

		templateGenerator.putVariable("problem", problem);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(problem, emailNotification));
		templateGenerator.putVariable("comment", emailNotification);
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				problem.getProjectid());
		templateGenerator.putVariable("userComment", linkGenerator
				.generateUserPreviewFullLink(emailNotification.getChangeby()));

		return templateGenerator;
	}

	private Map<String, String> createHyperLinks(SimpleProblem problem,
			SimpleRelayEmailNotification emailNotification) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				problem.getProjectid());

		hyperLinks.put("problemURL",
				linkGenerator.generateProblemPreviewFullLink(problem.getId()));

		hyperLinks.put("projectUrl", linkGenerator.generateProjectFullLink());
		hyperLinks
				.put("assignUserUrl", linkGenerator
						.generateUserPreviewFullLink(problem
								.getAssignedUserFullName()));
		hyperLinks
				.put("raiseUserUrl", linkGenerator
						.generateUserPreviewFullLink(problem
								.getRaisedByUserFullName()));

		SimpleProject project = projectService.findById(problem.getProjectid(),
				emailNotification.getSaccountid());
		if (project != null) {
			hyperLinks.put("projectName", project.getName());
		}

		return hyperLinks;
	}

	public class ProjectFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ProjectFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("issuename", "Issue name");
			fieldNameMap.put("assignedUserFullName", "Assigned to");
			fieldNameMap.put("datedue", "Due date");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("impact", "Impact");
			fieldNameMap.put("priority", "Priority");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

}
