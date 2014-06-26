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
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.project.ProjectLinkUtils;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.tracker.domain.SimpleVersion;
import com.esofthead.mycollab.module.tracker.service.VersionService;
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
		SendMailToAllMembersAction<SimpleVersion> implements
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
			MailContext<SimpleVersion> context) {
		SimpleVersion version = versionService.findById(context.getTypeid(),
				context.getSaccountid());

		if (version == null) {
			return null;
		}
		context.setWrappedBean(version);
		SimpleProject project = projectService.findById(version.getProjectid(),
				context.getSaccountid());

		String subject = StringUtils.trim(version.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(VersionI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
						project.getName(), context.getChangeByUserFullName(),
						subject),
				context.templatePath("templates/email/project/itemCreatedNotifier.mt"));

		setupMailHeaders(version, context.getEmailNotification(),
				templateGenerator);

		templateGenerator.putVariable("context", context);
		templateGenerator.putVariable("mapper", mapper);

		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleVersion> context) {
		SimpleVersion version = versionService.findById(context.getTypeid(),
				context.getSaccountid());
		if (version == null) {
			return null;
		}
		context.setWrappedBean(version);
		SimpleProject project = projectService.findById(version.getProjectid(),
				context.getSaccountid());

		String subject = StringUtils.trim(version.getDescription(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(VersionI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
						project.getName(), context.getChangeByUserFullName(),
						subject),
				context.templatePath("templates/email/project/itemUpdatedNotifier.mt"));

		setupMailHeaders(version, context.getEmailNotification(),
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
			MailContext<SimpleVersion> context) {
		return null;
	}

	public static class VersionFieldNameMapper extends ItemFieldMapper {
		public VersionFieldNameMapper() {
			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

			put("duedate", new DateFieldFormat("duedate",
					VersionI18nEnum.FORM_DUE_DATE));
		}
	}

}
