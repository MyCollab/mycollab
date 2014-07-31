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
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.MilestoneStatus;
import com.esofthead.mycollab.module.project.service.MilestoneService;
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
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
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
public class ProjectMilestoneRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction<SimpleMilestone> implements
		ProjectMilestoneRelayEmailNotificationAction {
	@Autowired
	private MilestoneService milestoneService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectMemberService projectMemberService;

	private static final MilestoneFieldNameMapper mapper = new MilestoneFieldNameMapper();

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getName(), 100);
	}

	@Override
	protected String getCreateSubject(MailContext<SimpleMilestone> context) {
		return context.getMessage(MilestoneI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getUpdateSubject(MailContext<SimpleMilestone> context) {
		return context.getMessage(MilestoneI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getCommentSubject(MailContext<SimpleMilestone> context) {
		return context.getMessage(MilestoneI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected SimpleMilestone getBeanInContext(
			MailContext<SimpleMilestone> context) {
		return milestoneService.findById(context.getTypeid(),
				context.getSaccountid());
	}

	public static class MilestoneFieldNameMapper extends ItemFieldMapper {
		public MilestoneFieldNameMapper() {
			put("name", MilestoneI18nEnum.FORM_NAME_FIELD, true);

			put("status", new I18nFieldFormat("status",
					MilestoneI18nEnum.FORM_STATUS_FIELD, MilestoneStatus.class));
			put("owner", new AssigneeFieldFormat("owner",
					GenericI18Enum.FORM_ASSIGNEE));

			put("startdate", new DateFieldFormat("startdate",
					MilestoneI18nEnum.FORM_START_DATE_FIELD));
			put("enddate", new DateFieldFormat("enddate",
					MilestoneI18nEnum.FORM_END_DATE_FIELD));

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleMilestone milestone = (SimpleMilestone) context
					.getWrappedBean();
			if (milestone.getOwner() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						milestone.getOwnerAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(milestone.getSaccountid()),
								milestone.getOwner());
				A link = TagBuilder
						.newA(userLink, milestone.getOwnerFullName());
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

	@Override
	protected void buildExtraTemplateVariables(
			MailContext<SimpleMilestone> context) {
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

		String summary = bean.getName();
		String summaryLink = ProjectLinkGenerator
				.generateMilestonePreviewFullLink(siteUrl, bean.getProjectid(),
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
			contentGenerator
					.putVariable("actionHeading", context.getMessage(
							MilestoneI18nEnum.MAIL_CREATE_ITEM_HEADING,
							makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator
					.putVariable("actionHeading", context.getMessage(
							MilestoneI18nEnum.MAIL_UPDATE_ITEM_HEADING,
							makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context
					.getMessage(MilestoneI18nEnum.MAIL_COMMENT_ITEM_HEADING,
							makeChangeUser));
		}

		contentGenerator.putVariable("titles", listOfTitles);
		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);

	}
}
