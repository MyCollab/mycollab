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
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProblemService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.LinkFieldFormat;
import com.esofthead.mycollab.schedule.email.project.ProjectProblemRelayEmailNotificationAction;
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
public class ProjectProblemRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		ProjectProblemRelayEmailNotificationAction {

	@Autowired
	private ProblemService problemService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private AuditLogService auditLogService;

	private static final ProjectFieldNameMapper mapper = new ProjectFieldNameMapper();

	protected void setupMailHeaders(SimpleProblem problem,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		SimpleProject relatedProject = projectService.findById(
				problem.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put(
				"webLink",
				ProjectLinkUtils.generateProjectFullLink(siteUrl,
						problem.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = problem.getIssuename();
		String summaryLink = ProjectLinkUtils.generateProblemPreviewFullLink(
				siteUrl, problem.getProjectid(), problem.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "problem");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
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

		templateGenerator.putVariable("context",
				new MailContext<SimpleProblem>(problem, user, siteUrl));
		templateGenerator.putVariable("mapper", mapper);

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

	public static class ProjectFieldNameMapper extends ItemFieldMapper {
		public ProjectFieldNameMapper() {
			put("issuename", "Issue name");
			put("assigntouser", new AssigneeFieldFormat("assigntouser",
					"Assignee"));
			put("datedue", new DateFieldFormat("datedue", "Due Date"));
			put("status", "Status");
			put("impact", "Impact");
			put("priority", "Priority");
			put("raisedbyuser", new RaisedByFieldFormat("raisedbyuser",
					"Raised By"));
			put("description", "Description");
			put("resolution", "Resolution");
		}
	}

	public static class AssigneeFieldFormat extends LinkFieldFormat {

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		protected Img buildImage(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			String userAvatarLink = LinkUtils.getAvatarLink(
					problem.getAssignUserAvatarId(), 16);
			Img img = new Img("avatar", userAvatarLink);
			return img;
		}

		@Override
		protected A buildLink(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(problem.getSaccountid()),
					problem.getAssigntouser());
			A link = new A();
			link.setHref(userLink);
			link.appendText(problem.getAssignedUserFullName());
			return link;
		}

	}

	public static class RaisedByFieldFormat extends LinkFieldFormat {

		public RaisedByFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		protected Img buildImage(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			String userAvatarLink = LinkUtils.getAvatarLink(
					problem.getRaisedByUserAvatarId(), 16);
			Img img = new Img("avatar", userAvatarLink);
			return img;
		}

		@Override
		protected A buildLink(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(problem.getSaccountid()),
					problem.getRaisedbyuser());
			A link = new A();
			link.setHref(userLink);
			link.appendText(problem.getRaisedByUserFullName());
			return link;
		}

	}
}
