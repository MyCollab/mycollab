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
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction;
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
public class ProjectMilestoneRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction<SimpleMilestone> implements
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
			MailContext<SimpleMilestone> context) {
		SimpleMilestone milestone = milestoneService.findById(
				context.getTypeid(), context.getSaccountid());

		if (milestone == null) {
			return null;
		}

		context.setWrappedBean(milestone);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(MilestoneI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
						milestone.getProjectName(),
						context.getChangeByUserFullName(),
						StringUtils.trim(milestone.getName(), 100)),
				context.templatePath("templates/email/project/itemCreatedNotifier.mt"));

		setupMailHeaders(milestone, context.getEmailNotification(),
				templateGenerator);

		templateGenerator.putVariable("context", context);
		templateGenerator.putVariable("mapper", mapper);

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleMilestone> context) {
		SimpleMilestone milestone = milestoneService.findById(
				context.getTypeid(), context.getSaccountid());
		if (milestone == null) {
			return null;
		}
		context.setWrappedBean(milestone);
		String subject = StringUtils.trim(milestone.getName(), 100);
		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(MilestoneI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
						milestone.getProjectName(),
						context.getChangeByUserFullName(), subject),
				context.templatePath("templates/email/project/itemUpdatedNotifier.mt"));

		setupMailHeaders(milestone, context.getEmailNotification(),
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
			MailContext<SimpleMilestone> context) {
		SimpleMilestone milestone = milestoneService.findById(
				context.getTypeid(), context.getSaccountid());
		if (milestone == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(MilestoneI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
						milestone.getProjectName(),
						context.getChangeByUserFullName(),
						StringUtils.trim(milestone.getName(), 100)),
				context.templatePath("templates/email/project/itemCommentNotifier.mt"));

		setupMailHeaders(milestone, context.getEmailNotification(),
				templateGenerator);

		templateGenerator
				.putVariable("comment", context.getEmailNotification());
		return templateGenerator;
	}

	public static class MilestoneFieldNameMapper extends ItemFieldMapper {
		public MilestoneFieldNameMapper() {
			put("name", MilestoneI18nEnum.FORM_NAME_FIELD, true);

			put("status", MilestoneI18nEnum.FORM_STATUS_FIELD);
			put("owner", new AssigneeFieldFormat("owner",
					GenericI18Enum.FORM_ASSIGNEE_FIELD));

			put("startdate", new DateFieldFormat("startdate",
					MilestoneI18nEnum.FORM_START_DATE_FIELD));
			put("enddate", new DateFieldFormat("enddate",
					MilestoneI18nEnum.FORM_END_DATE_FIELD));

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum displayName) {
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

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(milestone.getSaccountid()),
						milestone.getOwner());
				A link = TagBuilder
						.newA(userLink, milestone.getOwnerFullName());
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
