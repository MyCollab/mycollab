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
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleRisk;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.service.RiskService;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.ProjectRiskRelayEmailNotificationAction;
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
public class ProjectRiskRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction<SimpleRisk> implements
		ProjectRiskRelayEmailNotificationAction {

	@Autowired
	private RiskService riskService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectMemberService projectMemberService;

	private static final ProjectFieldNameMapper mapper = new ProjectFieldNameMapper();

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getRiskname(), 100);
	}

	@Override
	protected String getCreateSubject(MailContext<SimpleRisk> context) {
		return context.getMessage(RiskI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getUpdateSubject(MailContext<SimpleRisk> context) {
		return context.getMessage(RiskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getCommentSubject(MailContext<SimpleRisk> context) {
		return context.getMessage(RiskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected SimpleRisk getBeanInContext(MailContext<SimpleRisk> context) {
		return riskService.findById(context.getTypeid(),
				context.getSaccountid());
	}

	@Override
	protected void buildExtraTemplateVariables(MailContext<SimpleRisk> context) {
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

		String summary = bean.getRiskname();
		String summaryLink = ProjectLinkGenerator.generateRiskPreviewFullLink(
				siteUrl, bean.getProjectid(), bean.getId());

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
					RiskI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					RiskI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					RiskI18nEnum.MAIL_COMMENT_ITEM_HEADING, makeChangeUser));
		}

		contentGenerator.putVariable("titles", listOfTitles);
		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);

	}

	public static class ProjectFieldNameMapper extends ItemFieldMapper {

		public ProjectFieldNameMapper() {
			put("riskname", RiskI18nEnum.FORM_NAME, true);
			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

			put("probability", RiskI18nEnum.FORM_PROBABILITY);
			put("consequence", RiskI18nEnum.FORM_CONSEQUENCE);

			put("datedue", new DateFieldFormat("datedue",
					RiskI18nEnum.FORM_DATE_DUE));
			put("status", RiskI18nEnum.FORM_STATUS);

			put("assigntouser", new AssigneeFieldFormat("assigntouser",
					GenericI18Enum.FORM_ASSIGNEE));
			put("raisedbyuser", new RaisedByFieldFormat("raisedbyuser",
					RiskI18nEnum.FORM_RAISED_BY));

			put("response", RiskI18nEnum.FORM_RESPONSE, true);

		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleRisk risk = (SimpleRisk) context.getWrappedBean();
			if (risk.getAssigntouser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						risk.getAssignToUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(risk.getSaccountid()),
								risk.getAssigntouser());
				A link = TagBuilder.newA(userLink,
						risk.getAssignedToUserFullName());
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
			SimpleRisk risk = (SimpleRisk) context.getWrappedBean();
			if (risk.getRaisedbyuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						risk.getRaisedByUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(risk.getSaccountid()),
								risk.getRaisedbyuser());
				A link = TagBuilder.newA(userLink,
						risk.getRaisedByUserFullName());
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
