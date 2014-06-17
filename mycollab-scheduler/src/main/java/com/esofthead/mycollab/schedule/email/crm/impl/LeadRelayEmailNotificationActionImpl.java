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
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.LeadRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.EmailLinkFieldFormat;
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
public class LeadRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleLead> implements
		LeadRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private LeadService leadService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static final LeadFieldNameMapper mapper = new LeadFieldNameMapper();

	public LeadRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.LEAD);
	}

	protected void setupMailHeaders(SimpleLead lead,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = lead.getLeadName();
		String summaryLink = CrmLinkGenerator.generateLeadPreviewFullLink(
				siteUrl, lead.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "lead");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			MailContext<SimpleLead> context) {
		SimpleLead simpleLead = leadService.findById(context.getTypeid(),
				context.getSaccountid());
		if (simpleLead != null) {
			context.setWrappedBean(simpleLead);
			String subject = StringUtils.trim(simpleLead.getLeadName(), 150);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					context.getMessage(LeadI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
							context.getChangeByUserFullName(), subject),
					context.templatePath("templates/email/crm/itemCreatedNotifier.mt"));
			setupMailHeaders(simpleLead, context.getEmailNotification(),
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
			MailContext<SimpleLead> context) {
		SimpleLead lead = leadService.findById(context.getTypeid(),
				context.getSaccountid());
		if (lead == null) {
			return null;
		}

		context.setWrappedBean(lead);
		String subject = StringUtils.trim(lead.getLeadName(), 150);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(LeadI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
						context.getChangeByUserFullName(), subject),
				context.templatePath("templates/email/crm/itemUpdatedNotifier.mt"));
		setupMailHeaders(lead, context.getEmailNotification(),
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
			MailContext<SimpleLead> context) {
		SimpleLead simpleLead = leadService.findById(context.getTypeid(),
				context.getSaccountid());

		if (simpleLead == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				context.getMessage(LeadI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
						context.getChangeByUserFullName(),
						StringUtils.trim(simpleLead.getLeadName(), 100)),
				context.templatePath("templates/email/crm/itemAddNoteNotifier.mt"));

		setupMailHeaders(simpleLead, context.getEmailNotification(),
				templateGenerator);

		templateGenerator
				.putVariable("comment", context.getEmailNotification());

		return templateGenerator;
	}

	public static class LeadFieldNameMapper extends ItemFieldMapper {
		public LeadFieldNameMapper() {

			put("firstname", LeadI18nEnum.FORM_FIRSTNAME);
			put("email", new EmailLinkFieldFormat("email",
					LeadI18nEnum.FORM_EMAIL));

			put("lastname", LeadI18nEnum.FORM_LASTNAME);
			put("officephone", LeadI18nEnum.FORM_OFFICE_PHONE);

			put("title", LeadI18nEnum.FORM_TITLE);
			put("mobile", LeadI18nEnum.FORM_MOBILE);

			put("department", LeadI18nEnum.FORM_DEPARTMENT);
			put("otherphone", LeadI18nEnum.FORM_OTHER_PHONE);

			put("accountname", LeadI18nEnum.FORM_ACCOUNT_NAME);
			put("fax", LeadI18nEnum.FORM_FAX);

			put("leadsourcedesc", LeadI18nEnum.FORM_LEAD_SOURCE);
			put("website", LeadI18nEnum.FORM_WEBSITE);

			put("industry", LeadI18nEnum.FORM_INDUSTRY);
			put("status", LeadI18nEnum.FORM_STATUS);

			put("noemployees", LeadI18nEnum.FORM_NO_EMPLOYEES);
			put("assignuser", new LeadAssigneeFieldFormat("assignuser",
					GenericI18Enum.FORM_ASSIGNEE_FIELD));

			put("primaddress", LeadI18nEnum.FORM_PRIMARY_ADDRESS);
			put("otheraddress", LeadI18nEnum.FORM_OTHER_ADDRESS);

			put("primcity", LeadI18nEnum.FORM_PRIMARY_CITY);
			put("othercity", LeadI18nEnum.FORM_OTHER_CITY);

			put("primstate", LeadI18nEnum.FORM_PRIMARY_STATE);
			put("otherstate", LeadI18nEnum.FORM_OTHER_STATE);

			put("primpostalcode", LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE);
			put("otherpostalcode", LeadI18nEnum.FORM_OTHER_POSTAL_CODE);

			put("primcountry", LeadI18nEnum.FORM_PRIMARY_COUNTRY);
			put("othercountry", LeadI18nEnum.FORM_OTHER_COUNTRY);

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);

		}
	}

	public static class LeadAssigneeFieldFormat extends FieldFormat {

		public LeadAssigneeFieldFormat(String fieldName, Enum displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleLead lead = (SimpleLead) context.getWrappedBean();
			if (lead.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						lead.getAssignUserAvatarId(), 16);

				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(lead.getSaccountid()),
						lead.getAssignuser());
				A link = TagBuilder
						.newA(userLink, lead.getAssignUserFullName());
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
