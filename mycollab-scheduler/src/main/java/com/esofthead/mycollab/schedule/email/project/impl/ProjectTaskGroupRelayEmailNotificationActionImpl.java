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
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
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

	private final ProjectFieldNameMapper mapper;

	public ProjectTaskGroupRelayEmailNotificationActionImpl() {
		mapper = new ProjectFieldNameMapper();
	}

	@Override
	public TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int taskId = emailNotification.getTypeid();
		SimpleTaskList taskList = projectTaskListService.findById(taskId,
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(taskList.getName(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$taskList.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has created the task group \"" + subject + "\"",
				"templates/email/project/taskGroupCreatedNotifier.mt");
		templateGenerator.putVariable("taskList", taskList);
		templateGenerator.putVariable("hyperLinks", createHyperLinks(taskList));
		return templateGenerator;
	}

	private Map<String, String> createHyperLinks(SimpleTaskList taskList) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				taskList.getProjectid());
		hyperLinks.put("taskListUrl", linkGenerator
				.generateTaskGroupPreviewFullLink(taskList.getId()));
		hyperLinks.put("taskListName",
				StringUtils.trim(taskList.getName(), 100));
		hyperLinks.put("projectUrl", linkGenerator.generateProjectFullLink());
		hyperLinks.put("ownerUrl",
				linkGenerator.generateUserPreviewFullLink(taskList.getOwner()));
		hyperLinks.put("milestoneUrl", linkGenerator
				.generateMilestonePreviewFullLink(taskList.getMilestoneid()));
		return hyperLinks;
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

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$taskList.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has updated the task \"" + subject + "\"",
				"templates/email/project/taskGroupUpdatedNotifier.mt");
		templateGenerator.putVariable("taskList", taskList);
		templateGenerator.putVariable("hyperLinks", createHyperLinks(taskList));

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());

			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("mapper", mapper);
		}
		templateGenerator.putVariable(
				"lstComment",
				getListComment(taskList.getSaccountid(),
						ProjectTypeConstants.TASK_LIST, taskList.getId()));
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
		ProjectMailLinkGenerator linkGenerator = new ProjectMailLinkGenerator(
				taskList.getProjectid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[$taskList.projectName]: "
						+ emailNotification.getChangeByUserFullName()
						+ " has commented on the task group \""
						+ StringUtils.trim(taskList.getName(), 100) + "\"",
				"templates/email/project/taskGroupCommentNotifier.mt");
		templateGenerator.putVariable("taskList", taskList);
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", linkGenerator
				.generateUserPreviewFullLink(emailNotification.getChangeby()));
		templateGenerator.putVariable("hyperLinks", createHyperLinks(taskList));

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
			fieldNameMap.put("milestonename", "Milestone Name");
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
