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
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.schedule.email.project.ComponentRelayEmailNotificationAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.2
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ComponentRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction<SimpleComponent> implements
		ComponentRelayEmailNotificationAction {

	@Autowired
	private ComponentService componentService;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ProjectService projectService;

	private static final ComponentFieldNameMapper mapper = new ComponentFieldNameMapper();

	protected void setupMailHeaders(SimpleComponent component,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		HashMap<String, String> currentProject = new HashMap<String, String>();
		SimpleProject project = projectService.findById(
				component.getProjectid(), emailNotification.getSaccountid());
		currentProject.put("displayName", project.getName());
		currentProject.put(
				"webLink",
				ProjectLinkGenerator.generateProjectFullLink(siteUrl,
						component.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = component.getComponentname();
		String summaryLink = ProjectLinkGenerator
				.generateBugComponentPreviewFullLink(siteUrl,
						component.getProjectid(), component.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "component");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			MailContext<SimpleComponent> context) {
		SimpleComponent component = componentService.findById(
				context.getTypeid(), context.getSaccountid());

		if (component == null) {
			return null;
		}

		context.setWrappedBean(component);

		SimpleProject project = projectService.findById(
				component.getProjectid(), context.getSaccountid());

		String subject = StringUtils.trim(component.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(ComponentI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
						project.getName(), context.getChangeByUserFullName(),
						subject),
				context.templatePath("templates/email/project/itemCreatedNotifier.mt"));

		setupMailHeaders(component, context.getEmailNotification(),
				templateGenerator);

		templateGenerator.putVariable("context", context);
		templateGenerator.putVariable("mapper", mapper);

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleComponent> context) {
		SimpleComponent component = componentService.findById(
				context.getTypeid(), context.getSaccountid());
		if (component == null) {
			return null;
		}

		context.setWrappedBean(component);
		SimpleProject project = projectService.findById(
				component.getProjectid(), context.getSaccountid());

		String subject = StringUtils.trim(component.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(ComponentI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
						project.getName(), context.getChangeByUserFullName(),
						subject),
				context.templatePath("templates/email/project/itemUpdatedNotifier.mt"));

		setupMailHeaders(component, context.getEmailNotification(),
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
			MailContext<SimpleComponent> context) {
		return null;
	}

	public static class ComponentFieldNameMapper extends ItemFieldMapper {
		public ComponentFieldNameMapper() {
			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

			put("userlead", new LeadFieldFormat("userlead",
					ComponentI18nEnum.FORM_LEAD));
		}
	}

	public static class LeadFieldFormat extends FieldFormat {

		public LeadFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName, true);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleComponent component = (SimpleComponent) context
					.getWrappedBean();
			if (component.getUserlead() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						component.getUserLeadAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(component.getSaccountid()),
						component.getUserlead());
				A link = TagBuilder.newA(userLink,
						component.getUserLeadFullName());
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
