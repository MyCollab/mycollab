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

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
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
import com.hp.gagawa.java.elements.Span;

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
	private LeadService leadService;

	private static final LeadFieldNameMapper mapper = new LeadFieldNameMapper();

	@Override
	protected void buildExtraTemplateVariables(MailContext<SimpleLead> context) {
		String summary = bean.getLeadName();
		String summaryLink = CrmLinkGenerator.generateLeadPreviewFullLink(
				siteUrl, bean.getId());

		SimpleRelayEmailNotification emailNotification = context
				.getEmailNotification();

		String avatarId = "";

		SimpleUser user = userService.findUserByUserNameInAccount(
				emailNotification.getChangeby(), context.getSaccountid());

		if (user != null) {
			avatarId = user.getAvatarid();
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
					LeadI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					LeadI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					LeadI18nEnum.MAIL_COMMENT_ITEM_HEADING, makeChangeUser));
		}

		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected Enum<?> getCreateSubjectKey() {
		return LeadI18nEnum.MAIL_CREATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getUpdateSubjectKey() {
		return LeadI18nEnum.MAIL_UPDATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getCommentSubjectKey() {
		return LeadI18nEnum.MAIL_COMMENT_ITEM_SUBJECT;
	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getLeadName(), 100);
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected SimpleLead getBeanInContext(MailContext<SimpleLead> context) {
		return leadService.findById(Integer.parseInt(context.getTypeid()),
				context.getSaccountid());
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
					GenericI18Enum.FORM_ASSIGNEE));

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

		public LeadAssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleLead lead = (SimpleLead) context.getWrappedBean();
			if (lead.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						lead.getAssignUserAvatarId(), 16);

				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(lead.getSaccountid()),
								lead.getAssignuser());
				A link = TagBuilder
						.newA(userLink, lead.getAssignUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return new Span().write();
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return new Span().write();
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
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
