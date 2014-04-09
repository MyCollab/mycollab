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
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.project.ProjectRiskRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class ProjectRiskRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		ProjectRiskRelayEmailNotificationAction {

	@Autowired
	private RiskService riskService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AuditLogService auditLogService;

	private final ProjectFieldNameMapper mapper;

	public ProjectRiskRelayEmailNotificationActionImpl() {
		mapper = new ProjectFieldNameMapper();
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int riskId = emailNotification.getTypeid();
		SimpleRisk risk = riskService.findById(riskId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has created the risk \""
						+ StringUtils.trim(risk.getRiskname(), 100) + "\"",
				"templates/email/project/riskCreatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(risk, user.getTimezone(),
				new String[] { "dateraised", "datedue" });
		templateGenerator.putVariable("risk", risk);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(risk, emailNotification));

		return templateGenerator;
	}

	private Map<String, String> createHyperLinks(SimpleRisk risk,
			SimpleRelayEmailNotification emailNotification) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				risk.getProjectid());
		hyperLinks.put("riskURL",
				linkGenerator.generateRiskPreviewFullLink(risk.getId()));

		hyperLinks.put("projectUrl", linkGenerator.generateProjectFullLink());
		hyperLinks.put("raiseUserUrl", linkGenerator
				.generateUserPreviewFullLink(risk.getRaisedByUserFullName()));
		hyperLinks.put("assignUserURL", linkGenerator
				.generateUserPreviewFullLink(risk.getAssignedToUserFullName()));

		SimpleProject project = projectService.findById(risk.getProjectid(),
				emailNotification.getSaccountid());
		if (project != null) {
			hyperLinks.put("projectName", project.getName());
		}
		return hyperLinks;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int riskId = emailNotification.getTypeid();
		SimpleRisk risk = riskService.findById(riskId,
				emailNotification.getSaccountid());
		if (risk == null) {
			return null;
		}

		String subject = StringUtils.trim(risk.getRiskname(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has updated the risk \"" + subject + "\"",
				"templates/email/project/riskUpdateNotifier.mt");

		ScheduleUserTimeZoneUtils.formatDateTimeZone(risk, user.getTimezone(),
				new String[] { "dateraised", "datedue" });
		templateGenerator.putVariable("risk", risk);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(risk, emailNotification));
		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			ScheduleUserTimeZoneUtils.formatDate(auditLog, user.getTimezone(),
					new String[] { "dateraised", "datedue" });
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("mapper", mapper);
		}
		templateGenerator.putVariable(
				"lstComment",
				getListComment(risk.getSaccountid(), ProjectTypeConstants.RISK,
						risk.getId()));

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		int riskId = emailNotification.getTypeid();
		SimpleRisk risk = riskService.findById(riskId,
				emailNotification.getSaccountid());
		if (risk == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$hyperLinks.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has commented the risk \""
						+ StringUtils.trim(risk.getRiskname(), 100) + "\"",
				"templates/email/project/riskCommentNotifier.mt");
		templateGenerator.putVariable("risk", risk);
		templateGenerator.putVariable("hyperLinks",
				createHyperLinks(risk, emailNotification));
		templateGenerator.putVariable("comment", emailNotification);

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				risk.getProjectid());
		templateGenerator.putVariable("userComment", linkGenerator
				.generateUserPreviewFullLink(emailNotification.getChangeby()));

		return templateGenerator;
	}

	public class ProjectFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ProjectFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("riskname", "Risk Name");
			fieldNameMap.put("assignedToUserFullName", "Assigned to");
			fieldNameMap.put("consequence", "Consequence");
			fieldNameMap.put("probalitity", "Probability");

			fieldNameMap.put("datedue", "Due date");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("response", "Response");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

}
