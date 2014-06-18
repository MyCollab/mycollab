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
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum;
import com.esofthead.mycollab.module.project.service.ProblemService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.ProjectProblemRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
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
		SendMailToAllMembersAction<SimpleProblem> implements
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
			MailContext<SimpleProblem> context) {
		SimpleProblem problem = problemService.findById(context.getTypeid(),
				context.getSaccountid());

		if (problem == null) {
			return null;
		}
		context.setWrappedBean(problem);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(ProblemI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
						problem.getProjectName(),
						context.getChangeByUserFullName(),
						StringUtils.trim(problem.getIssuename(), 100)),
				context.templatePath("templates/email/project/itemCreatedNotifier.mt"));

		setupMailHeaders(problem, context.getEmailNotification(),
				templateGenerator);

		templateGenerator.putVariable("context", context);
		templateGenerator.putVariable("mapper", mapper);

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleProblem> context) {
		SimpleProblem problem = problemService.findById(context.getTypeid(),
				context.getSaccountid());
		if (problem == null) {
			return null;
		}
		context.setWrappedBean(problem);
		String subject = StringUtils.trim(problem.getIssuename(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(ProblemI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
						problem.getProjectName(),
						context.getChangeByUserFullName(), subject),
				context.templatePath("templates/email/project/itemUpdatedNotifier.mt"));

		setupMailHeaders(problem, context.getEmailNotification(),
				templateGenerator);

		if (context.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					context.getTypeid(), context.getSaccountid());

			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("context", context);
			templateGenerator.putVariable("mapper", mapper);
		}

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			MailContext<SimpleProblem> context) {
		SimpleProblem problem = problemService.findById(context.getTypeid(),
				context.getSaccountid());
		if (problem == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(MessageI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
						problem.getProjectName(),
						context.getChangeByUserFullName(),
						StringUtils.trim(problem.getIssuename(), 100)),
				context.templatePath("templates/email/project/itemCommentNotifier.mt"));
		setupMailHeaders(problem, context.getEmailNotification(),
				templateGenerator);

		templateGenerator
				.putVariable("comment", context.getEmailNotification());

		return templateGenerator;
	}

	public static class ProjectFieldNameMapper extends ItemFieldMapper {
		public ProjectFieldNameMapper() {
			put("issuename", ProblemI18nEnum.FORM_NAME, true);

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

			put("datedue", new DateFieldFormat("datedue",
					ProblemI18nEnum.FORM_DATE_DUE));
			put("status", ProblemI18nEnum.FORM_STATUS);

			put("impact", ProblemI18nEnum.FORM_IMPACT);
			put("priority", ProblemI18nEnum.FORM_PRIORITY);

			put("assigntouser", new AssigneeFieldFormat("assigntouser",
					GenericI18Enum.FORM_ASSIGNEE));
			put("raisedbyuser", new RaisedByFieldFormat("raisedbyuser",
					ProblemI18nEnum.FORM_RAISED_BY));

			put("resolution", ProblemI18nEnum.FORM_RESOLUTION, true);
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			if (problem.getAssigntouser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						problem.getAssignUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(problem.getSaccountid()),
						problem.getAssigntouser());
				A link = TagBuilder.newA(userLink,
						problem.getAssignedUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return "";
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return "";
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(user.getAccountId()),
						user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}
	}

	public static class RaisedByFieldFormat extends FieldFormat {

		public RaisedByFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			if (problem.getRaisedbyuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						problem.getRaisedByUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(problem.getSaccountid()),
						problem.getRaisedbyuser());
				A link = TagBuilder.newA(userLink,
						problem.getRaisedByUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return "";
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return "";
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(user.getAccountId()),
						user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}
	}
}
