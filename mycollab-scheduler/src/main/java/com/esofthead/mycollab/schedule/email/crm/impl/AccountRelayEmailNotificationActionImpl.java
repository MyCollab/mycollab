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
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.OptionI18nEnum.AccountType;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.I18nFieldFormat;
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
public class AccountRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleAccount> implements
		AccountRelayEmailNotificationAction {

	@Autowired
	private AccountService accountService;

	private static AccountFieldNameMapper mapper = new AccountFieldNameMapper();

	@Override
	protected Enum<?> getCreateSubjectKey() {
		return AccountI18nEnum.MAIL_CREATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getUpdateSubjectKey() {
		return AccountI18nEnum.MAIL_UPDATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getCommentSubjectKey() {
		return AccountI18nEnum.MAIL_COMMENT_ITEM_SUBJECT;
	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getAccountname(), 100);
	}

	@Override
	protected void buildExtraTemplateVariables(
			MailContext<SimpleAccount> context) {
		String summary = bean.getAccountname();
		String summaryLink = CrmLinkGenerator.generateAccountPreviewFullLink(
				siteUrl, bean.getId());

		SimpleRelayEmailNotification emailNotification = context
				.getEmailNotification();

		String avatarId = "";

		SimpleUser user = userService.findUserByUserNameInAccount(
				emailNotification.getChangeby(), context.getSaccountid());

		if (user != null) {
			avatarId = user.getAvatarid();
		}
		Img userAvatar = new Img("", StorageManager.getAvatarLink(avatarId, 16));
		userAvatar.setWidth("16");
		userAvatar.setHeight("16");
		userAvatar.setStyle("display: inline-block; vertical-align: top;");

		String makeChangeUser = userAvatar.toString()
				+ emailNotification.getChangeByUserFullName();

		if (MonitorTypeConstants.CREATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					AccountI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					AccountI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					AccountI18nEnum.MAIL_COMMENT_ITEM_HEADING, makeChangeUser));
		}

		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected SimpleAccount getBeanInContext(MailContext<SimpleAccount> context) {
		return accountService.findById(Integer.parseInt(context.getTypeid()),
				context.getSaccountid());
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleAccount account = (SimpleAccount) context.getWrappedBean();
			if (account.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						account.getAssignUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(account.getSaccountid()),
								account.getAssignuser());

				A link = TagBuilder.newA(userLink,
						account.getAssignUserFullName());

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

	public static class AccountFieldNameMapper extends ItemFieldMapper {

		public AccountFieldNameMapper() {
			put("accountname", AccountI18nEnum.FORM_ACCOUNT_NAME);
			put("phoneoffice", AccountI18nEnum.FORM_OFFICE_PHONE);

			put("website", AccountI18nEnum.FORM_WEBSITE);
			put("numemployees", AccountI18nEnum.FORM_EMPLOYEES);

			put("fax", AccountI18nEnum.FORM_FAX);
			put("alternatephone", AccountI18nEnum.FORM_OTHER_PHONE);

			put("industry", AccountI18nEnum.FORM_INDUSTRY);
			put("email", AccountI18nEnum.FORM_EMAIL);

			put("type", new I18nFieldFormat("type", AccountI18nEnum.FORM_TYPE,
					AccountType.class));
			put("ownership", AccountI18nEnum.FORM_OWNERSHIP);

			put("assignuser", new AssigneeFieldFormat("assignuser",
					GenericI18Enum.FORM_ASSIGNEE));
			put("annualrevenue", AccountI18nEnum.FORM_ANNUAL_REVENUE);

			put("billingaddress", AccountI18nEnum.FORM_BILLING_ADDRESS);
			put("shippingaddress", AccountI18nEnum.FORM_SHIPPING_ADDRESS);

			put("city", AccountI18nEnum.FORM_BILLING_CITY);
			put("shippingcity", AccountI18nEnum.FORM_SHIPPING_CITY);

			put("state", AccountI18nEnum.FORM_BILLING_STATE);
			put("shippingstate", AccountI18nEnum.FORM_SHIPPING_STATE);

			put("postalcode", AccountI18nEnum.FORM_BILLING_POSTAL_CODE);
			put("shippingpostalcode", AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE);

			put("billingcountry", AccountI18nEnum.FORM_BILLING_COUNTRY);
			put("shippingcountry", AccountI18nEnum.FORM_SHIPPING_COUNTRY);

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);
		}
	}
}
