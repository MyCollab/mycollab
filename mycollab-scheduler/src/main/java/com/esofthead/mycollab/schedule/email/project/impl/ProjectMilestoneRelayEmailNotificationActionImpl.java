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
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.LinkFieldFormat;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProjectMilestoneRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		ProjectMilestoneRelayEmailNotificationAction {
	@Autowired
	private MilestoneService milestoneService;

	@Autowired
	private AuditLogService auditLogService;

	@Autowired
	private ProjectService projectService;

	private static final MilestoneFieldNameMapper mapper = new MilestoneFieldNameMapper();

	protected void setupMailHeaders(SimpleMilestone milestone,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		SimpleProject relatedProject = projectService.findById(
				milestone.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put(
				"webLink",
				ProjectLinkUtils.generateProjectFullLink(siteUrl,
						milestone.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = milestone.getName();
		String summaryLink = ProjectLinkUtils.generateMilestonePreviewFullLink(
				siteUrl, milestone.getProjectid(), milestone.getId());

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

		templateGenerator.putVariable("context",
				new MailContext<SimpleMilestone>(milestone, user, siteUrl));
		templateGenerator.putVariable("mapper", mapper);

		return templateGenerator;
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

	public static class MilestoneFieldNameMapper extends ItemFieldMapper {
		public MilestoneFieldNameMapper() {
			put("name", "Phase Name");
			put("startdate", new DateFieldFormat("startdate", "Start Date"));
			put("enddate", new DateFieldFormat("enddate", "End Date"));
			put("status", "Status");
			put("owner", new AssigneeFieldFormat("owner", "Owner"));
			put("description", "Description");
		}
	}

	public static class AssigneeFieldFormat extends LinkFieldFormat {

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		protected Img buildImage(MailContext<?> context) {
			SimpleMilestone milestone = (SimpleMilestone) context
					.getWrappedBean();
			String userAvatarLink = LinkUtils.getAvatarLink(
					milestone.getOwnerAvatarId(), 16);
			Img img = new Img("avatar", userAvatarLink);
			return img;
		}

		@Override
		protected A buildLink(MailContext<?> context) {
			SimpleMilestone milestone = (SimpleMilestone) context
					.getWrappedBean();
			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(milestone.getSaccountid()),
					milestone.getOwner());
			A link = new A();
			link.setHref(userLink);
			link.appendText(milestone.getOwnerFullName());
			return link;
		}

	}
}
