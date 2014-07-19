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

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleMessage;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.service.MessageService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.project.MessageRelayEmailNotificationAction;
import com.hp.gagawa.java.elements.Img;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MessageRelayEmailNotificationActionImpl extends
		SendMailToAllMembersAction<SimpleMessage> implements
		MessageRelayEmailNotificationAction {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ProjectService projectService;

	@Autowired
	private ProjectMemberService projectMemberService;

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getTitle(), 100);
	}

	@Override
	protected String getCreateSubject(MailContext<SimpleMessage> context) {
		return context.getMessage(MessageI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getUpdateSubject(MailContext<SimpleMessage> context) {
		return context.getMessage(MessageI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected String getCommentSubject(MailContext<SimpleMessage> context) {
		return context.getMessage(MessageI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
				bean.getProjectName(), context.getChangeByUserFullName(),
				getItemName());
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return null;
	}

	@Override
	protected SimpleMessage getBeanInContext(MailContext<SimpleMessage> context) {
		return messageService.findMessageById(context.getTypeid(),
				context.getSaccountid());
	}

	@Override
	protected void buildExtraTemplateVariables(
			MailContext<SimpleMessage> context) {
		List<Map<String, String>> listOfTitles = new ArrayList<Map<String, String>>();

		SimpleRelayEmailNotification emailNotification = context
				.getEmailNotification();

		HashMap<String, String> currentProject = new HashMap<String, String>();
		currentProject.put("displayName", bean.getProjectName());
		currentProject.put(
				"webLink",
				ProjectLinkGenerator.generateProjectFullLink(siteUrl,
						bean.getProjectid()));

		listOfTitles.add(currentProject);

		String summary = bean.getTitle();
		String summaryLink = ProjectLinkGenerator
				.generateMessagePreviewFullLink(siteUrl, bean.getProjectid(),
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
					MessageI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					MessageI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					MessageI18nEnum.MAIL_COMMENT_ITEM_HEADING, makeChangeUser));
		}

		contentGenerator.putVariable("titles", listOfTitles);
		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);
		contentGenerator.putVariable("message", bean.getMessage());

	}
}
