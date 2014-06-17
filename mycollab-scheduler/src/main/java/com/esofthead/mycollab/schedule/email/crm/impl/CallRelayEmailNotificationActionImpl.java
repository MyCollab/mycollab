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
package com.esofthead.mycollab.schedule.email.crm.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.i18n.CallI18nEnum;
import com.esofthead.mycollab.module.crm.service.CallService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.CallRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.DateTimeFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
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
public class CallRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleCall> implements
		CallRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private CallService callService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static final CallFieldNameMapper mapper = new CallFieldNameMapper();

	public CallRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.CALL);
	}

	protected void setupMailHeaders(SimpleCall call,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = call.getSubject();
		String summaryLink = CrmLinkGenerator.generateCallPreviewFullLink(
				siteUrl, call.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "call");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			MailContext<SimpleCall> context) {
		SimpleCall simpleCall = callService.findById(context.getTypeid(),
				context.getSaccountid());
		if (simpleCall != null) {
			String subject = StringUtils.trim(simpleCall.getSubject(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(CallI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
							context.getChangeByUserFullName(), subject),
					context.templatePath("templates/email/crm/itemCreatedNotifier.mt"));
			setupMailHeaders(simpleCall, context.getEmailNotification(),
					templateGenerator);

			context.setWrappedBean(simpleCall);
			templateGenerator.putVariable("context", context);
			templateGenerator.putVariable("mapper", mapper);

			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleCall> context) {
		SimpleCall simpleCall = callService.findById(context.getTypeid(),
				context.getSaccountid());

		if (simpleCall != null) {
			String subject = StringUtils.trim(simpleCall.getSubject(), 150);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(CallI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
							context.getChangeByUserFullName(), subject),
					context.templatePath("templates/email/crm/itemUpdatedNotifier.mt"));

			setupMailHeaders(simpleCall, context.getEmailNotification(),
					templateGenerator);

			if (context.getTypeid() != null) {
				SimpleAuditLog auditLog = auditLogService.findLatestLog(
						context.getTypeid(), context.getSaccountid());

				templateGenerator.putVariable("historyLog", auditLog);
				context.setWrappedBean(simpleCall);
				templateGenerator.putVariable("context", context);
				templateGenerator.putVariable("mapper", mapper);
			}
			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			MailContext<SimpleCall> context) {
		SimpleCall simpleCall = callService.findById(context.getTypeid(),
				context.getSaccountid());

		if (simpleCall != null) {
			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(CallI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
							context.getChangeByUserFullName(),
							StringUtils.trim(simpleCall.getSubject(), 100)),
					context.templatePath("templates/email/crm/itemAddNoteNotifier.mt"));
			setupMailHeaders(simpleCall, context.getEmailNotification(),
					templateGenerator);

			templateGenerator.putVariable("comment",
					context.getEmailNotification());

			return templateGenerator;
		} else {
			return null;
		}

	}

	public static class CallFieldNameMapper extends ItemFieldMapper {

		public CallFieldNameMapper() {
			put("subject", CallI18nEnum.FORM_SUBJECT, true);

			put("status", CallI18nEnum.FORM_STATUS);
			put("startdate", new DateTimeFieldFormat("startdate",
					CallI18nEnum.FORM_START_DATE_TIME));

			put("typeid", CallI18nEnum.FORM_RELATED);
			put("durationinseconds", CallI18nEnum.FORM_DURATION);

			put("purpose", CallI18nEnum.FORM_PURPOSE);
			put("assignuser", new AssigneeFieldFormat("assignuser",
					GenericI18Enum.FORM_ASSIGNEE_FIELD));

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

			put("result", CallI18nEnum.FORM_RESULT, true);
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleCall call = (SimpleCall) context.getWrappedBean();
			if (call.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						call.getAssignUserAvatarId(), 16);

				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(call.getSaccountid()),
						call.getAssignuser());
				A link = TagBuilder
						.newA(userLink, call.getAssignUserFullName());
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
