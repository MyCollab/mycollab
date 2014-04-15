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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.NoteService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction;

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

	private final AccountFieldNameMapper mapper;

	private SimpleAccount simpleAccount;

	public AccountRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.ACCOUNT);
		mapper = new AccountFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleAccount account,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = account.getAccountname();
		String summaryLink = CrmLinkGenerator
				.generateAccountPreviewLink(account.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "account");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleAccount account, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		if (account.getWebsite() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("website"),
							Arrays.asList(new MailItemLink(null, account
									.getWebsite())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("website"), null);
		}

		if (account.getFax() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("fax"),
					Arrays.asList(new MailItemLink(null, account.getFax())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("fax"), null);
		}

		if (account.getNumemployees() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("numemployees"),
					Arrays.asList(new MailItemLink(null, account
							.getNumemployees().toString())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("numemployees"),
					null);
		}

		if (account.getAlternatephone() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("alternatephone"),
					Arrays.asList(new MailItemLink(null, account
							.getAlternatephone())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("alternatephone"),
					null);
		}

		if (account.getIndustry() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("industry"), Arrays
							.asList(new MailItemLink(null, account
									.getIndustry())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("industry"), null);
		}

		if (account.getEmail() != null) {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("email"),
					Arrays.asList(new MailItemLink("mailto:"
							+ account.getEmail(), account.getEmail())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("email"), null);
		}

		if (account.getType() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("type"),
					Arrays.asList(new MailItemLink(null, account.getType())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("type"), null);
		}

		if (account.getOwnership() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("ownership"), Arrays
							.asList(new MailItemLink(null, account
									.getOwnership())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("ownership"), null);
		}

		if (account.getAssignuser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					Arrays.asList(new MailItemLink(UserLinkUtils
							.generatePreviewFullUserLink(
									getSiteUrl(account.getSaccountid()),
									account.getAssignuser()), account
							.getAssignUserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					null);
		}

		if (account.getAnnualrevenue() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("annualrevenue"),
					Arrays.asList(new MailItemLink(null, account
							.getAnnualrevenue())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("annualrevenue"),
					null);
		}

		if (account.getBillingaddress() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("billingaddress"),
					Arrays.asList(new MailItemLink(null, account
							.getBillingaddress())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("billingaddress"),
					null);
		}

		if (account.getShippingaddress() != null) {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("shippingaddress"), Arrays
							.asList(new MailItemLink(null, account
									.getShippingaddress())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("shippingaddress"), null);
		}

		if (account.getCity() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("city"),
					Arrays.asList(new MailItemLink(null, account.getCity())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("city"), null);
		}

		if (account.getShippingcity() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("shippingcity"),
					Arrays.asList(new MailItemLink(null, account
							.getShippingcity())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("shippingcity"),
					null);
		}

		if (account.getState() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("state"),
					Arrays.asList(new MailItemLink(null, account.getState())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("state"), null);
		}

		if (account.getShippingstate() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("shippingstate"),
					Arrays.asList(new MailItemLink(null, account
							.getShippingstate())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("shippingstate"),
					null);
		}

		if (account.getPostalcode() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("postalcode"),
					Arrays.asList(new MailItemLink(null, account
							.getPostalcode())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("postalcode"),
					null);
		}

		if (account.getShippingpostalcode() != null) {
			listOfDisplayProperties.put(mapper
					.getFieldLabel("shippingpostalcode"), Arrays
					.asList(new MailItemLink(null, account
							.getShippingpostalcode())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("shippingpostalcode"), null);
		}

		if (account.getBillingcountry() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("billingcountry"),
					Arrays.asList(new MailItemLink(null, account
							.getBillingcountry())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("billingcountry"),
					null);
		}

		if (account.getShippingcountry() != null) {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("shippingcountry"), Arrays
							.asList(new MailItemLink(null, account
									.getShippingcountry())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("shippingcountry"), null);
		}

		if (account.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new MailItemLink(null, account
							.getDescription())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					null);
		}

		return listOfDisplayProperties;
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

			templateGenerator.putVariable("properties",
					getListOfProperties(simpleAccount, user));

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
		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	class AccountFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		AccountFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("accountname", "Account Name");
			fieldNameMap.put("phoneoffice", "Office Phone");
			fieldNameMap.put("website", "Website");
			fieldNameMap.put("numemployees", "Employees");
			fieldNameMap.put("fax", "Fax");
			fieldNameMap.put("alternatephone", "Other Phone");
			fieldNameMap.put("industry", "Industry");
			fieldNameMap.put("email", "Email");
			fieldNameMap.put("type", "Type");
			fieldNameMap.put("ownership", "Ownership");
			fieldNameMap.put("assignuser", "Assignee");
			fieldNameMap.put("annualrevenue", "Annual Revenue");
			fieldNameMap.put("billingaddress", "Billing Address");
			fieldNameMap.put("shippingaddress", "Shipping Address");
			fieldNameMap.put("city", "Billing City");
			fieldNameMap.put("shippingcity", "Shipping City");
			fieldNameMap.put("state", "Billing State");
			fieldNameMap.put("shippingstate", "Shipping State");
			fieldNameMap.put("postalcode", "Billing Postal Code");
			fieldNameMap.put("shippingpostalcode", "Shipping Postal Code");
			fieldNameMap.put("billingcountry", "Billing Country");
			fieldNameMap.put("shippingcountry", "Shipping Country");
			fieldNameMap.put("description", "Description");
		}

		public boolean hasField(String fieldName) {
			return fieldNameMap.containsKey(fieldName);
		}

		public String getFieldLabel(String fieldName) {
			return fieldNameMap.get(fieldName);
		}
	}

}
