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
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.ContactRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
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
public class ContactRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleContact> implements
		ContactRelayEmailNotificationAction {

	private static Logger log = LoggerFactory
			.getLogger(ContactRelayEmailNotificationActionImpl.class);

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ContactService contactService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static final ContactFieldNameMapper mapper = new ContactFieldNameMapper();

	public ContactRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.CONTACT);
	}

	protected void setupMailHeaders(SimpleContact contact,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = contact.getContactName();
		String summaryLink = CrmLinkGenerator.generateContactPreviewFullLink(
				siteUrl, contact.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "contact");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleContact simpleContact = contactService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleContact != null) {
			String subject = StringUtils.trim(simpleContact.getContactName(),
					100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has created the contact \"" + subject + "\"",
					"templates/email/crm/itemCreatedNotifier.mt");

			setupMailHeaders(simpleContact, emailNotification,
					templateGenerator);

			templateGenerator
					.putVariable("context", new MailContext<SimpleContact>(
							simpleContact, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);

			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleContact simpleContact = contactService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		if (simpleContact != null) {
			String subject = StringUtils.trim(simpleContact.getContactName(),
					100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has updated the contact \"" + subject + "\"",
					"templates/email/crm/itemUpdatedNotifier.mt");

			setupMailHeaders(simpleContact, emailNotification,
					templateGenerator);

			if (emailNotification.getTypeid() != null) {
				SimpleAuditLog auditLog = auditLogService.findLatestLog(
						emailNotification.getTypeid(),
						emailNotification.getSaccountid());

				templateGenerator.putVariable("historyLog", auditLog);
				templateGenerator.putVariable("context",
						new MailContext<SimpleContact>(simpleContact, user,
								siteUrl));
				templateGenerator.putVariable("mapper", mapper);
			}
			return templateGenerator;
		} else {
			return null;
		}

	}

	@Override
	protected TemplateGenerator templateGeneratorForCommentAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		int accountRecordId = emailNotification.getTypeid();
		SimpleContact simpleContact = contactService.findById(accountRecordId,
				emailNotification.getSaccountid());

		if (simpleContact != null) {
			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has commented on the contact \""
							+ StringUtils.trim(simpleContact.getContactName(),
									100) + "\"",
					"templates/email/crm/itemAddNoteNotifier.mt");

			setupMailHeaders(simpleContact, emailNotification,
					templateGenerator);

			templateGenerator.putVariable("comment", emailNotification);

			return templateGenerator;
		} else {
			return null;
		}

	}

	public static class ContactFieldNameMapper extends ItemFieldMapper {

		ContactFieldNameMapper() {
			put("firstname", "First Name");
			put("officephone", "Office Phone");

			put("lastname", "Last Name");
			put("mobile", "Mobile");

			put("accountid", new AccountFieldFormat("accountid", "Account"));
			put("homephone", "Home Phone");

			put("title", "Title");
			put("otherphone", "Other Phone");

			put("department", "Department");
			put("fax", "Fax");

			put("email", new EmailLinkFieldFormat("email", "Email"));
			put("birthday", new DateFieldFormat("birthday", "Birthday"));

			put("assistant", "Assistant");
			put("iscallable", "Callable");

			put("assistantphone", "Assistant Phone");
			put("assignuser", new AssigneeFieldFormat("assignuser", "Assignee"));

			put("leadsource", "Lead Source", true);

			put("primaddress", "Address");
			put("otheraddress", "Other Address");

			put("primcity", "City");
			put("othercity", "Other City");

			put("primstate", "State");
			put("otherstate", "Other State");

			put("primpostalcode", "Postal Code");
			put("otherpostalcode", "Other Postal Code");

			put("primcountry", "Country");
			put("othercountry", "Other Country");

			put("description", "Description", true);

		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleContact contact = (SimpleContact) context.getWrappedBean();
			if (contact.getAssignuser() != null) {
				String userAvatarLink = LinkUtils.getAvatarLink(
						contact.getAssignUserAvatarId(), 16);

				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = UserLinkUtils.generatePreviewFullUserLink(
						LinkUtils.getSiteUrl(contact.getSaccountid()),
						contact.getAssignuser());
				A link = TagBuilder.newA(userLink,
						contact.getAssignUserFullName());
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

	public static class AccountFieldFormat extends FieldFormat {

		public AccountFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleContact contact = (SimpleContact) context.getWrappedBean();
			if (contact.getAccountid() != null) {
				String accountIconLink = CrmResources
						.getResourceLink(CrmTypeConstants.ACCOUNT);
				Img img = TagBuilder.newImg("icon", accountIconLink);

				String accountLink = CrmLinkGenerator
						.generateAccountPreviewFullLink(context.getSiteUrl(),
								contact.getAccountid());
				A link = TagBuilder.newA(accountLink, contact.getAccountName());
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
				int accountId = Integer.parseInt(value);
				AccountService accountService = ApplicationContextUtil
						.getSpringBean(AccountService.class);
				SimpleAccount account = accountService.findById(accountId,
						context.getUser().getAccountId());
				if (account != null) {
					String accountIconLink = CrmResources
							.getResourceLink(CrmTypeConstants.ACCOUNT);
					Img img = TagBuilder.newImg("icon", accountIconLink);
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
}
