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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.ProjectTaskListService;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.LinkFieldFormat;
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectTaskGroupRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		ProjectTaskGroupRelayEmailNotificationAction {

	@Autowired
	private ProjectTaskListService projectTaskListService;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ProjectService projectService;

	private static final ProjectFieldNameMapper mapper = new ProjectFieldNameMapper();

	protected void setupMailHeaders(SimpleTaskList tasklist,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		SimpleProject relatedProject = projectService.findById(
				tasklist.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put(
				"webLink",
				ProjectLinkUtils.generateProjectFullLink(siteUrl,
						tasklist.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = tasklist.getName();
		String summaryLink = ProjectLinkUtils.generateTaskGroupPreviewFullLink(
				siteUrl, tasklist.getProjectid(), tasklist.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "task group");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
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

		templateGenerator.putVariable("context",
				new MailContext<SimpleTaskList>(taskList, user, siteUrl));
		templateGenerator.putVariable("mapper", mapper);

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

	public static class ProjectFieldNameMapper extends ItemFieldMapper {

		public ProjectFieldNameMapper() {

			put("name", "Task Group Name");
			put("owner", new AssigneeFieldFormat("owner", "Owner"));
			put("milestoneid", new MilestoneFieldFormat("milestoneid",
					"Milestone"));
			put("description", "Description");

		}
	}

	public static class AssigneeFieldFormat extends LinkFieldFormat {

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		protected Img buildImage(MailContext<?> context) {
			SimpleTaskList tasklist = (SimpleTaskList) context.getWrappedBean();
			String userAvatarLink = LinkUtils.getAvatarLink(
					tasklist.getOwnerAvatarId(), 16);
			Img img = new Img("avatar", userAvatarLink);
			return img;
		}

		@Override
		protected A buildLink(MailContext<?> context) {
			SimpleTaskList tasklist = (SimpleTaskList) context.getWrappedBean();
			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(tasklist.getSaccountid()),
					tasklist.getOwner());
			A link = new A();
			link.setHref(userLink);
			link.appendText(tasklist.getOwnerFullName());
			return link;
		}
	}

	public static class MilestoneFieldFormat extends LinkFieldFormat {

		public MilestoneFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		protected Img buildImage(MailContext<?> context) {
			String milestoneIconLink = ProjectResources
					.getResourceLink(ProjectTypeConstants.MILESTONE);
			return new Img("icon", milestoneIconLink);
		}

		@Override
		protected A buildLink(MailContext<?> context) {
			SimpleTaskList tasklist = (SimpleTaskList) context.getWrappedBean();
			A link = new A();
			link.setHref(ProjectLinkUtils.generateMilestonePreviewFullLink(
					context.getSiteUrl(), tasklist.getProjectid(),
					tasklist.getMilestoneid()));
			link.appendText(tasklist.getMilestoneName());
			return link;
		}

	}
}
