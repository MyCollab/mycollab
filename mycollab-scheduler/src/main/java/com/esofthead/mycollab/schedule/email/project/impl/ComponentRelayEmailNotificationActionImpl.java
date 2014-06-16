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
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.domain.SimpleComponent;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
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
		SendMailToAllMembersAction implements
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
				ProjectLinkUtils.generateProjectFullLink(siteUrl,
						component.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = component.getComponentname();
		String summaryLink = ProjectLinkUtils
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
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int componentId = emailNotification.getTypeid();
		SimpleComponent component = componentService.findById(componentId,
				emailNotification.getSaccountid());

		if (component == null) {
			return null;
		}

		SimpleProject project = projectService.findById(
				component.getProjectid(), emailNotification.getSaccountid());

		String subject = StringUtils.trim(component.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ project.getName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has created new component \"" + subject + "\"",
				"templates/email/project/itemCreatedNotifier.mt");

		setupMailHeaders(component, emailNotification, templateGenerator);

		templateGenerator.putVariable("context",
				new MailContext<SimpleComponent>(component, user, siteUrl));
		templateGenerator.putVariable("mapper", mapper);

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleComponent component = componentService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (component == null) {
			return null;
		}

		SimpleProject project = projectService.findById(
				component.getProjectid(), emailNotification.getSaccountid());

		String subject = StringUtils.trim(component.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ project.getName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has updated the component \"" + subject + "\"",
				"templates/email/project/itemUpdatedNotifier.mt");

		setupMailHeaders(component, emailNotification, templateGenerator);

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("context",
					new MailContext<SimpleComponent>(component, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);
		}

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		return null;
	}

	public static class ComponentFieldNameMapper extends ItemFieldMapper {
		public ComponentFieldNameMapper() {
			put("description", "Description", true);

			put("userlead", new LeadFieldFormat("userlead", "Lead"));
		}
	}

	public static class LeadFieldFormat extends FieldFormat {

		public LeadFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName, true);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleComponent component = (SimpleComponent) context
					.getWrappedBean();
			if (component.getUserlead() != null) {
				String userAvatarLink = LinkUtils.getAvatarLink(
						component.getUserLeadAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						LinkUtils.getSiteUrl(component.getSaccountid()),
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
				String userAvatarLink = LinkUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						LinkUtils.getSiteUrl(user.getAccountId()),
						user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}

	}

}
