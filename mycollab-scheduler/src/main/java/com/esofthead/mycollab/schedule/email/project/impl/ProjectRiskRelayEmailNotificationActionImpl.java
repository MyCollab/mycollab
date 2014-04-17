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
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
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

	protected void setupMailHeaders(SimpleRisk risk,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				risk.getProjectid());

		SimpleProject relatedProject = projectService.findById(
				risk.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put("webLink", linkGenerator.generateProjectFullLink());

		listOfTitles.add(currentProject);

		String summary = risk.getRiskname();
		String summaryLink = ProjectLinkUtils.generateRiskPreview(
				risk.getProjectid(), risk.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "risk");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<RiskLinkMapper>> getListOfProperties(
			SimpleRisk risk, SimpleUser user) {
		Map<String, List<RiskLinkMapper>> listOfDisplayProperties = new LinkedHashMap<String, List<RiskLinkMapper>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				risk.getProjectid());

		listOfDisplayProperties.put(mapper.getFieldLabel("raisedbyuser"),
				Arrays.asList(new RiskLinkMapper(linkGenerator
						.generateUserPreviewFullLink(risk.getRaisedbyuser()),
						risk.getRaisedByUserFullName())));

		if (risk.getAssigntouser() != null) {
			listOfDisplayProperties.put(mapper
					.getFieldLabel("assignedToUserFullName"), Arrays
					.asList(new RiskLinkMapper(
							linkGenerator.generateUserPreviewFullLink(risk
									.getAssigntouser()), risk
									.getAssignedToUserFullName())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("assignedToUserFullName"), null);
		}

		if (risk.getConsequence() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("consequence"),
					Arrays.asList(new RiskLinkMapper(null, risk
							.getConsequence())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("consequence"),
					null);
		}

		if (risk.getProbalitity() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("probability"),
					Arrays.asList(new RiskLinkMapper(null, risk
							.getProbalitity())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("probability"),
					null);
		}

		if (risk.getDatedue() != null)
			listOfDisplayProperties.put(mapper.getFieldLabel("datedue"), Arrays
					.asList(new RiskLinkMapper(null, DateTimeUtils
							.converToStringWithUserTimeZone(risk.getDatedue(),
									user.getTimezone()))));
		else {
			listOfDisplayProperties.put(mapper.getFieldLabel("datedue"), null);
		}

		listOfDisplayProperties.put(mapper.getFieldLabel("status"),
				Arrays.asList(new RiskLinkMapper(null, risk.getStatus())));

		listOfDisplayProperties.put(mapper.getFieldLabel("description"),
				Arrays.asList(new RiskLinkMapper(null, risk.getDescription())));

		return listOfDisplayProperties;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int riskId = emailNotification.getTypeid();
		SimpleRisk risk = riskService.findById(riskId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ risk.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has created the risk \""
				+ StringUtils.trim(risk.getRiskname(), 100) + "\"",
				"templates/email/project/itemCreatedNotifier.mt");
		setupMailHeaders(risk, emailNotification, templateGenerator);

		templateGenerator.putVariable("properties",
				getListOfProperties(risk, user));

		return templateGenerator;
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

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ risk.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has updated the risk \"" + subject + "\"",
				"templates/email/project/itemUpdatedNotifier.mt");

		setupMailHeaders(risk, emailNotification, templateGenerator);

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
		int riskId = emailNotification.getTypeid();
		SimpleRisk risk = riskService.findById(riskId,
				emailNotification.getSaccountid());
		if (risk == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ risk.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has commented the risk \""
				+ StringUtils.trim(risk.getRiskname(), 100) + "\"",
				"templates/email/project/itemCommentNotifier.mt");

		setupMailHeaders(risk, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class ProjectFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ProjectFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("riskname", "Risk Name");
			fieldNameMap.put("assignedToUserFullName", "Assigned to");
			fieldNameMap.put("consequence", "Consequence");
			fieldNameMap.put("probability", "Probability");
			fieldNameMap.put("raisedbyuser", "Raised By");
			fieldNameMap.put("description", "Description");
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

	public class RiskLinkMapper implements Serializable {
		private static final long serialVersionUID = 2212688618608788187L;

		private String link;
		private String displayname;

		public RiskLinkMapper(String link, String displayname) {
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
