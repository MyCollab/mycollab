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
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CaseService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
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
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCase simpleCase = caseService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleCase != null) {
			String subject = StringUtils.trim(simpleCase.getSubject(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ "has created the case \"" + subject + "\"",
					"templates/email/crm/itemCreatedNotifier.mt");
			setupMailHeaders(simpleCase, emailNotification, templateGenerator);

			templateGenerator.putVariable("context",
					new MailContext<SimpleCase>(simpleCase, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);

			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCase simpleCase = caseService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(simpleCase.getSubject(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the case \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");

		setupMailHeaders(simpleCase, emailNotification, templateGenerator);

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());

			templateGenerator.putVariable("historyLog", auditLog);
			templateGenerator.putVariable("context",
					new MailContext<SimpleCase>(simpleCase, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);
		}
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCase simpleCase = caseService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on case \""
						+ StringUtils.trim(simpleCase.getSubject(), 100) + "\"",
				"templates/email/crm/itemAddNoteNotifier.mt");

		setupMailHeaders(simpleCase, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public static class CaseFieldNameMapper extends ItemFieldMapper {

		public CaseFieldNameMapper() {
			put("subject", "Subject", true);

			put("description", "Description");
			put("accountid", new AccountFieldFormat("accountid", "Account"));

			put("priority", "Priority");
			put("type", "Type");

			put("status", "Status");
			put("reason", "Reason");

			put("phonenumber", "Phone");
			put("email", "Email");

			put("origin", "Origin");
			put("assignuser", new AssigneeFieldFormat("assignuser", "Assignee"));

			put("resolution", "Resolution", true);
		}
	}

	public static class AccountFieldFormat extends FieldFormat {

		public AccountFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleCase simpleCase = (SimpleCase) context.getWrappedBean();
			String accountIconLink = CrmResources
					.getResourceLink(CrmTypeConstants.ACCOUNT);
			Img img = TagBuilder.newImg("avatar", accountIconLink);

			String accountLink = CrmLinkGenerator
					.generateAccountPreviewFullLink(context.getSiteUrl(),
							simpleCase.getAccountid());
			A link = TagBuilder.newA(accountLink, simpleCase.getAccountName());
			return TagBuilder.newLink(img, link).write();
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

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleCase simpleCase = (SimpleCase) context.getWrappedBean();

			String userAvatarLink = LinkUtils.getAvatarLink(
					simpleCase.getAssignUserAvatarId(), 16);

			Img img = TagBuilder.newImg("avatar", userAvatarLink);

			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(simpleCase.getSaccountid()),
					simpleCase.getAssignuser());
			A link = TagBuilder.newA(userLink,
					simpleCase.getAssignUserFullName());
			return TagBuilder.newLink(img, link).write();
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
				String userLink = UserLinkUtils.generatePreviewFullUserLink(
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
