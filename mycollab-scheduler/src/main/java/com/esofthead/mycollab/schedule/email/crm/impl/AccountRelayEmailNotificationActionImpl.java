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
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.NoteService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
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
public class AccountRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleAccount> implements
		AccountRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private UserService userService;
	@Autowired
	private NoteService noteService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static AccountFieldNameMapper mapper = new AccountFieldNameMapper();

	private SimpleAccount simpleAccount;

	public AccountRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.ACCOUNT);
	}

	protected void setupMailHeaders(SimpleAccount account,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				LinkUtils.getSiteUrl(account.getSaccountid()));

		String summary = account.getAccountname();
		String summaryLink = crmLinkGenerator
				.generateAccountPreviewFullLink(account.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "account");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int recordAccountId = emailNotification.getTypeid();
		simpleAccount = accountService.findById(recordAccountId,
				emailNotification.getSaccountid());
		if (simpleAccount != null) {
			String subject = StringUtils.trim(simpleAccount.getAccountname(),
					100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has created the account  \"" + subject + "\"",
					"templates/email/crm/itemCreatedNotifier.mt");

			setupMailHeaders(simpleAccount, emailNotification,
					templateGenerator);

			templateGenerator.putVariable("context",
					new MailContext<SimpleAccount>(simpleAccount, user));
			templateGenerator.putVariable("mapper", mapper);

			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		simpleAccount = accountService.findById(emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(simpleAccount.getAccountname(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the account \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");

		setupMailHeaders(simpleAccount, emailNotification, templateGenerator);

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());

			templateGenerator.putVariable("historyLog", auditLog);

			templateGenerator.putVariable("mapper", mapper);
		}
		return templateGenerator;
	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int accountRecordId = emailNotification.getTypeid();
		simpleAccount = accountService.findById(accountRecordId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the account \""
						+ StringUtils.trim(simpleAccount.getAccountname(), 100)
						+ "\"", "templates/email/crm/itemAddNoteNotifier.mt");

		setupMailHeaders(simpleAccount, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public static class AccountAssigneeFieldFormat extends FieldFormat {

		public AccountAssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		public String formatField(MailContext<?> context) {
			SimpleAccount account = (SimpleAccount) context.getWrappedBean();
			String userLink = UserLinkUtils.generatePreviewFullUserLink(
					LinkUtils.getSiteUrl(account.getSaccountid()),
					account.getAssignuser());
			String userAvatarLink = LinkUtils.getAvatarLink(
					account.getAssignUserAvatarId(), 16);

			Span span = new Span();
			Img img = new Img("avatar", userAvatarLink);
			span.appendChild(img);

			A link = new A();
			link.setHref(userLink);
			link.appendText(account.getAssignUserFullName());
			span.appendChild(link);
			return span.write();
		}

	}

	public static class AccountFieldNameMapper extends ItemFieldMapper {

		public AccountFieldNameMapper() {
			put("accountname", "Account Name");
			put("phoneoffice", "Office Phone");
			put("website", "Website");
			put("numemployees", "Employees");
			put("fax", "Fax");
			put("alternatephone", "Other Phone");
			put("industry", "Industry");
			put("email", "Email");
			put("type", "Type");
			put("ownership", "Ownership");
			put("assignuser", new AccountAssigneeFieldFormat("assignuser",
					"Assign User"));
			put("annualrevenue", "Annual Revenue");
			put("billingaddress", "Billing Address");
			put("shippingaddress", "Shipping Address");
			put("city", "Billing City");
			put("shippingcity", "Shipping City");
			put("state", "Billing State");
			put("shippingstate", "Shipping State");
			put("postalcode", "Billing Postal Code");
			put("shippingpostalcode", "Shipping Postal Code");
			put("billingcountry", "Billing Country");
			put("shippingcountry", "Shipping Country");
			put("description", "Description");
		}
	}
}
