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
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.project.ProjectMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
public class ProjectTaskGroupRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		ProjectTaskGroupRelayEmailNotificationAction {

	@Autowired
	private ProjectTaskListService projectTaskListService;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ProjectService projectService;

	private final ProjectFieldNameMapper mapper;

	public ProjectTaskGroupRelayEmailNotificationActionImpl() {
		mapper = new ProjectFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleTaskList tasklist,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				tasklist.getProjectid());

		SimpleProject relatedProject = projectService.findById(
				tasklist.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put("webLink", linkGenerator.generateProjectFullLink());

		listOfTitles.add(currentProject);

		String summary = tasklist.getName();
		String summaryLink = ProjectLinkUtils.generateTaskGroupPreviewLink(
				tasklist.getProjectid(), tasklist.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "task group");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleTaskList tasklist) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				tasklist.getProjectid());

		if (tasklist.getMilestoneid() != null)
			listOfDisplayProperties.put(mapper.getFieldLabel("milestoneid"),
					Arrays.asList(new MailItemLink(linkGenerator
							.generateMilestonePreviewFullLink(tasklist
									.getMilestoneid()), tasklist
							.getMilestoneName())));
		else {
			listOfDisplayProperties.put(mapper.getFieldLabel("milestoneid"),
					null);
		}

		if (tasklist.getOwner() != null) {

			listOfDisplayProperties.put(mapper.getFieldLabel("owner"), Arrays
					.asList(new MailItemLink(linkGenerator
							.generateUserPreviewFullLink(tasklist.getOwner()),
							tasklist.getOwnerFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("owner"), null);
		}

		if (tasklist.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new MailItemLink(null, tasklist
							.getDescription())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					null);
		}

		return listOfDisplayProperties;
	}

	@Override
	public TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int taskId = emailNotification.getTypeid();
		SimpleTaskList taskList = projectTaskListService.findById(taskId,
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(taskList.getName(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ taskList.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has created the task group \"" + subject + "\"",
				"templates/email/project/itemCreatedNotifier.mt");
		setupMailHeaders(taskList, emailNotification, templateGenerator);

		templateGenerator.putVariable("properties",
				getListOfProperties(taskList));

		return templateGenerator;
	}

	@Override
	public TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int taskListId = emailNotification.getTypeid();
		SimpleTaskList taskList = projectTaskListService.findById(taskListId,
				emailNotification.getSaccountid());
		if (taskList == null) {
			return null;
		}

		String subject = StringUtils.trim(taskList.getName(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ taskList.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has updated the task \"" + subject + "\"",
				"templates/email/project/itemUpdatedNotifier.mt");

		setupMailHeaders(taskList, emailNotification, templateGenerator);

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
		int taskId = emailNotification.getTypeid();
		SimpleTaskList taskList = projectTaskListService.findById(taskId,
				emailNotification.getSaccountid());
		if (taskList == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ taskList.getProjectName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has commented on the task group \""
				+ StringUtils.trim(taskList.getName(), 100) + "\"",
				"templates/email/project/itemCommentNotifier.mt");
		setupMailHeaders(taskList, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class ProjectFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ProjectFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("name", "Task Group Name");
			fieldNameMap.put("owner", "Owner");
			fieldNameMap.put("percentagecomplete", "Complete (%)");
			fieldNameMap.put("priority", "Priority");
			fieldNameMap.put("taskListname", "Task Group");
			fieldNameMap.put("description", "Description");
			fieldNameMap.put("milestoneid", "Milestone Name");
			fieldNameMap.put("projectname", "Project Name");

		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}
}
