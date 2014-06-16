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
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.service.VersionService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.project.VersionRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.2
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class VersionRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction implements
		VersionRelayEmailNotificationAction {

	@Autowired
	private VersionService versionService;
	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ProjectService projectService;

	private VersionFieldNameMapper mapper = new VersionFieldNameMapper();

	protected void setupMailHeaders(SimpleVersion version,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		HashMap<String, String> currentProject = new HashMap<String, String>();
		SimpleProject project = projectService.findById(version.getProjectid(),
				emailNotification.getSaccountid());
		currentProject.put("displayName", project.getName());
		currentProject.put(
				"webLink",
				ProjectLinkUtils.generateProjectFullLink(siteUrl,
						version.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = version.getVersionname();
		String summaryLink = ProjectLinkUtils
				.generateBugComponentPreviewFullLink(siteUrl,
						version.getProjectid(), version.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "version");
		templateGenerator.putVariable("titles", listOfTitles);
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int versionId = emailNotification.getTypeid();
		SimpleVersion version = versionService.findById(versionId,
				emailNotification.getSaccountid());

		if (version == null) {
			return null;
		}

		SimpleProject project = projectService.findById(version.getProjectid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(version.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ project.getName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has created new version \"" + subject + "\"",
				"templates/email/project/itemCreatedNotifier.mt");

		setupMailHeaders(version, emailNotification, templateGenerator);

		templateGenerator.putVariable("context",
				new MailContext<SimpleVersion>(version, user, siteUrl));
		templateGenerator.putVariable("mapper", mapper);

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleVersion version = versionService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (version == null) {
			return null;
		}

		SimpleProject project = projectService.findById(version.getProjectid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(version.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator("["
				+ project.getName() + "]: "
				+ emailNotification.getChangeByUserFullName()
				+ " has updated the version \"" + subject + "\"",
				"templates/email/project/itemUpdatedNotifier.mt");

		setupMailHeaders(version, emailNotification, templateGenerator);

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("context",
					new MailContext<SimpleVersion>(version, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);
		}

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification) {
		// TODO Auto-generated method stub
		return null;
	}

	public static class VersionFieldNameMapper extends ItemFieldMapper {
		public VersionFieldNameMapper() {
			put("description", "Description", true);

			put("duedate", new DateFieldFormat("duedate", "Due date"));
		}
	}

}
