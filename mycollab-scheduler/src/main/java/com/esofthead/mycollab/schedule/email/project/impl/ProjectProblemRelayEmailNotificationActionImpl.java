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

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleProblem;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum;
import com.esofthead.mycollab.module.project.service.ProblemService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.I18nFieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.ProjectProblemRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;

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
	private ProjectMemberService projectMemberService;

	private static final ProjectFieldNameMapper mapper = new ProjectFieldNameMapper();

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getIssuename(), 100);
	}

	@Override
	protected String getCreateSubject(MailContext<SimpleProblem> context) {
		return context.getMessage(ProblemI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getUpdateSubject(MailContext<SimpleProblem> context) {
		return context.getMessage(ProblemI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getCommentSubject(MailContext<SimpleProblem> context) {
		return context.getMessage(ProblemI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected SimpleProblem getBeanInContext(MailContext<SimpleProblem> context) {
		return problemService.findById(Integer.parseInt(context.getTypeid()),
				context.getSaccountid());
	}

	@Override
	protected void buildExtraTemplateVariables(
			MailContext<SimpleProblem> context) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		SimpleRelayEmailNotification emailNotification = context
				.getEmailNotification();

		SimpleProject relatedProject = projectService.findById(
				bean.getProjectid(), emailNotification.getSaccountid());

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", relatedProject.getName());
		currentProject.put(
				"webLink",
				ProjectLinkGenerator.generateProjectFullLink(siteUrl,
						bean.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = bean.getIssuename();
		String summaryLink = ProjectLinkGenerator
				.generateProblemPreviewFullLink(siteUrl, bean.getProjectid(),
						bean.getId());

		String avatarId = "";

		SimpleProjectMember projectMember = projectMemberService
				.findMemberByUsername(emailNotification.getChangeby(),
						bean.getProjectid(), emailNotification.getSaccountid());
		if (projectMember != null) {
			avatarId = projectMember.getMemberAvatarId();
		}
		Img userAvatar = new Img("", SiteConfiguration.getAvatarLink(avatarId,
				16));
		userAvatar.setWidth("16");
		userAvatar.setHeight("16");
		userAvatar.setStyle("display: inline-block; vertical-align: top;");

		String makeChangeUser = userAvatar.toString()
				+ emailNotification.getChangeByUserFullName();

		if (MonitorTypeConstants.CREATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					ProblemI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					ProblemI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					ProblemI18nEnum.MAIL_COMMENT_ITEM_HEADING, makeChangeUser));
		}

		contentGenerator.putVariable("titles", listOfTitles);
		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);

	}

	public static class ProjectFieldNameMapper extends ItemFieldMapper {
		public ProjectFieldNameMapper() {
			put("issuename", ProblemI18nEnum.FORM_NAME, true);

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

			put("datedue", new DateFieldFormat("datedue",
					ProblemI18nEnum.FORM_DATE_DUE));
			put("status", new I18nFieldFormat("status",
					ProblemI18nEnum.FORM_STATUS, StatusI18nEnum.class));

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

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			if (problem.getAssigntouser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						problem.getAssignUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(problem.getSaccountid()),
								problem.getAssigntouser());
				A link = TagBuilder.newA(userLink,
						problem.getAssignedUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return new Span().write();
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return new Span().write();
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
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

		public RaisedByFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleProblem problem = (SimpleProblem) context.getWrappedBean();
			if (problem.getRaisedbyuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						problem.getRaisedByUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(problem.getSaccountid()),
								problem.getRaisedbyuser());
				A link = TagBuilder.newA(userLink,
						problem.getRaisedByUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return new Span().write();
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return new Span().write();
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
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
