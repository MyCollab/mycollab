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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.CaseRelayEmailNotificationAction;
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
public class CaseRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleCase> implements
		CaseRelayEmailNotificationAction {

	private static Logger log = LoggerFactory
			.getLogger(CaseRelayEmailNotificationActionImpl.class);

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private CaseService caseService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static final CaseFieldNameMapper mapper = new CaseFieldNameMapper();

	public CaseRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.CASE);
	}

	protected void setupMailHeaders(SimpleCase simpleCase,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = simpleCase.getSubject();
		String summaryLink = CrmLinkGenerator.generateCasePreviewFullLink(
				siteUrl, simpleCase.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "case");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			MailContext<SimpleCase> context) {
		SimpleCase simpleCase = caseService.findById(context.getTypeid(),
				context.getSaccountid());
		if (simpleCase != null) {
			context.setWrappedBean(simpleCase);
			String subject = StringUtils.trim(simpleCase.getSubject(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(CaseI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
							context.getChangeByUserFullName(), subject),
					context.templatePath("templates/email/crm/itemCreatedNotifier.mt"));
			setupMailHeaders(simpleCase, context.getEmailNotification(),
					templateGenerator);

			templateGenerator.putVariable("context", context);
			templateGenerator.putVariable("mapper", mapper);

			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			MailContext<SimpleCase> context) {
		SimpleCase simpleCase = caseService.findById(context.getTypeid(),
				context.getSaccountid());

		if (simpleCase != null) {
			context.setWrappedBean(simpleCase);
			String subject = StringUtils.trim(simpleCase.getSubject(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(CaseI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
							context.getChangeByUserFullName(), subject),
					context.templatePath("templates/email/crm/itemUpdatedNotifier.mt"));

			setupMailHeaders(simpleCase, context.getEmailNotification(),
					templateGenerator);

			if (context.getTypeid() != null) {
				SimpleAuditLog auditLog = auditLogService.findLatestLog(
						context.getTypeid(), context.getSaccountid());

				templateGenerator.putVariable("historyLog", auditLog);
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
			MailContext<SimpleCase> context) {
		SimpleCase simpleCase = caseService.findById(context.getTypeid(),
				context.getSaccountid());

		if (simpleCase != null) {
			context.setWrappedBean(simpleCase);
			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(CaseI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
							context.getChangeByUserFullName(),
							StringUtils.trim(simpleCase.getSubject(), 100)),
					context.templatePath("templates/email/crm/itemAddNoteNotifier.mt"));

			setupMailHeaders(simpleCase, context.getEmailNotification(),
					templateGenerator);

			templateGenerator.putVariable("comment",
					context.getEmailNotification());

			return templateGenerator;
		} else {
			return null;
		}
	}

	public static class CaseFieldNameMapper extends ItemFieldMapper {

		public CaseFieldNameMapper() {
			put("subject", CaseI18nEnum.FORM_SUBJECT, true);

			put("description", GenericI18Enum.FORM_DESCRIPTION);
			put("accountid", new AccountFieldFormat("accountid",
					CaseI18nEnum.FORM_ACCOUNT));

			put("priority", CaseI18nEnum.FORM_PRIORITY);
			put("type", CaseI18nEnum.FORM_TYPE);

			put("status", CaseI18nEnum.FORM_STATUS);
			put("reason", CaseI18nEnum.FORM_REASON);

			put("phonenumber", CaseI18nEnum.FORM_PHONE);
			put("email", CaseI18nEnum.FORM_EMAIL);

			put("origin", CaseI18nEnum.FORM_ORIGIN);
			put("assignuser", new AssigneeFieldFormat("assignuser",
					GenericI18Enum.FORM_ASSIGNEE_FIELD));

			put("resolution", CaseI18nEnum.FORM_RESOLUTION, true);
		}
	}

	public static class AccountFieldFormat extends FieldFormat {

		public AccountFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleCase simpleCase = (SimpleCase) context.getWrappedBean();
			if (simpleCase.getAccountid() != null) {
				String accountIconLink = CrmResources
						.getResourceLink(CrmTypeConstants.ACCOUNT);
				Img img = TagBuilder.newImg("avatar", accountIconLink);

				String accountLink = CrmLinkGenerator
						.generateAccountPreviewFullLink(context.getSiteUrl(),
								simpleCase.getAccountid());
				A link = TagBuilder.newA(accountLink,
						simpleCase.getAccountName());
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

			try {
				Integer accountId = Integer.parseInt(value);
				AccountService accountService = ApplicationContextUtil
						.getSpringBean(AccountService.class);
				SimpleAccount account = accountService.findById(accountId,
						context.getUser().getAccountId());

				if (account != null) {
					String accountIconLink = CrmResources
							.getResourceLink(CrmTypeConstants.ACCOUNT);
					Img img = TagBuilder.newImg("avatar", accountIconLink);

					String accountLink = CrmLinkGenerator
							.generateAccountPreviewFullLink(
									context.getSiteUrl(), account.getId());
					A link = TagBuilder.newA(accountLink,
							account.getAccountname());
					return TagBuilder.newLink(img, link).write();
				}
			} catch (Exception e) {
				log.error("Error", e);
			}
			return value;
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleCase simpleCase = (SimpleCase) context.getWrappedBean();
			if (simpleCase.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						simpleCase.getAssignUserAvatarId(), 16);

				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(simpleCase.getSaccountid()),
						simpleCase.getAssignuser());
				A link = TagBuilder.newA(userLink,
						simpleCase.getAssignUserFullName());
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
