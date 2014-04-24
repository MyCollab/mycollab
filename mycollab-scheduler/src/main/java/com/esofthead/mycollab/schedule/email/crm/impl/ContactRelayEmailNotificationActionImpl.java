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
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.crm.ContactRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class ContactRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleContact> implements
		ContactRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private ContactService contactService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private final ContactFieldNameMapper mapper;

	public ContactRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.CONTACT);
		mapper = new ContactFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleContact contact,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(contact.getSaccountid()));

		String summary = contact.getContactName();
		String summaryLink = crmLinkGenerator
				.generateContactPreviewFullLink(contact.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "contact");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleContact contact, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(contact.getSaccountid()));

		if (contact.getFirstname() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("firstname"), Arrays
							.asList(new MailItemLink(null, contact
									.getFirstname())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("firstname"), null);
		}

		if (contact.getOfficephone() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("officephone"),
					Arrays.asList(new MailItemLink(null, contact
							.getOfficephone())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("officephone"),
					null);
		}

		listOfDisplayProperties.put(mapper.getFieldLabel("lastname"),
				Arrays.asList(new MailItemLink(null, contact.getLastname())));

		if (contact.getMobile() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("mobile"),
					Arrays.asList(new MailItemLink(null, contact.getMobile())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("mobile"), null);
		}

		if (contact.getAccountid() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("accountName"), Arrays
							.asList(new MailItemLink(crmLinkGenerator
									.generateAccountPreviewFullLink(contact
											.getAccountid()), contact
									.getAccountName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("account"), null);
		}

		if (contact.getHomephone() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("homephone"), Arrays
							.asList(new MailItemLink(null, contact
									.getHomephone())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("homephone"), null);
		}

		if (contact.getTitle() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("title"),
					Arrays.asList(new MailItemLink(null, contact.getTitle())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("title"), null);
		}

		if (contact.getOtherphone() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("otherphone"),
					Arrays.asList(new MailItemLink(null, contact
							.getOtherphone())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("otherphone"),
					null);
		}

		if (contact.getDepartment() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("department"),
					Arrays.asList(new MailItemLink(null, contact
							.getDepartment())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("department"),
					null);
		}

		if (contact.getFax() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("fax"),
					Arrays.asList(new MailItemLink(null, contact.getFax())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("fax"), null);
		}

		if (contact.getEmail() != null) {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("email"),
					Arrays.asList(new MailItemLink("mailto:"
							+ contact.getEmail(), contact.getEmail())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("email"), null);
		}

		if (contact.getBirthday() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("birthday"), Arrays
							.asList(new MailItemLink(null, DateTimeUtils
									.converToStringWithUserTimeZone(
											contact.getBirthday(),
											user.getTimezone()))));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("birthday"), null);
		}

		if (contact.getAssistant() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("assistant"), Arrays
							.asList(new MailItemLink(null, contact
									.getAssistant())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("assistant"), null);
		}

		if (contact.getIscallable() != null && contact.getIscallable().booleanValue()) {
			listOfDisplayProperties.put(mapper.getFieldLabel("iscallable"),
					Arrays.asList(new MailItemLink(null, "yes")));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("iscallable"),
					Arrays.asList(new MailItemLink(null, "no")));
		}

		if (contact.getAssistantphone() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assistantphone"),
					Arrays.asList(new MailItemLink(null, contact
							.getAssistantphone())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assistantphone"),
					null);
		}

		if (contact.getAssignuser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					Arrays.asList(new MailItemLink(UserLinkUtils
							.generatePreviewFullUserLink(
									getSiteUrl(contact.getSaccountid()),
									contact.getAssignuser()), contact
							.getAssignUserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					null);
		}

		if (contact.getPrimaddress() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("primaddress"),
					Arrays.asList(new MailItemLink(null, contact
							.getPrimaddress())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("primaddress"),
					null);
		}

		if (contact.getOtheraddress() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("otheraddress"),
					Arrays.asList(new MailItemLink(null, contact
							.getOtheraddress())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("otheraddress"),
					null);
		}

		if (contact.getPrimcity() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("primcity"), Arrays
							.asList(new MailItemLink(null, contact
									.getPrimcity())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("primcity"), null);
		}

		if (contact.getOthercity() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("othercity"), Arrays
							.asList(new MailItemLink(null, contact
									.getOthercity())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("othercity"), null);
		}

		if (contact.getPrimstate() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("primstate"), Arrays
							.asList(new MailItemLink(null, contact
									.getPrimstate())));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("primstate"), null);
		}

		if (contact.getOtherstate() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("otherstate"),
					Arrays.asList(new MailItemLink(null, contact
							.getOtherstate())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("otherstate"),
					null);
		}

		if (contact.getPrimpostalcode() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("primpostalcode"),
					Arrays.asList(new MailItemLink(null, contact
							.getPrimpostalcode())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("primpostalcode"),
					null);
		}

		if (contact.getOtherpostalcode() != null) {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("otherpostalcode"), Arrays
							.asList(new MailItemLink(null, contact
									.getOtherpostalcode())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("otherpostalcode"), null);
		}

		if (contact.getPrimcountry() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("primcountry"),
					Arrays.asList(new MailItemLink(null, contact
							.getPrimcountry())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("primcountry"),
					null);
		}

		if (contact.getOthercountry() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("othercountry"),
					Arrays.asList(new MailItemLink(null, contact
							.getOthercountry())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("othercountry"),
					null);
		}

		if (contact.getLeadsource() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("leadsource"),
					Arrays.asList(new MailItemLink(null, contact
							.getLeadsource())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("leadsource"),
					null);
		}

		if (contact.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new MailItemLink(null, contact
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

			templateGenerator.putVariable("properties",
					getListOfProperties(simpleContact, user));

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

		String subject = StringUtils.trim(simpleContact.getContactName(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the contact \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");

		setupMailHeaders(simpleContact, emailNotification, templateGenerator);

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
		SimpleContact simpleContact = contactService.findById(accountRecordId,
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the contact \""
						+ StringUtils.trim(simpleContact.getContactName(), 100)
						+ "\"", "templates/email/crm/itemAddNoteNotifier.mt");

		setupMailHeaders(simpleContact, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class ContactFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		ContactFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("firstname", "First Name");
			fieldNameMap.put("officephone", "Office Phone");
			fieldNameMap.put("lastname", "Last Name");
			fieldNameMap.put("mobile", "Mobile");
			fieldNameMap.put("accountName", "Account");
			fieldNameMap.put("homephone", "Home Phone");
			fieldNameMap.put("title", "Title");
			fieldNameMap.put("otherphone", "Other Phone");
			fieldNameMap.put("department", "Department");
			fieldNameMap.put("fax", "Fax");
			fieldNameMap.put("email", "Email");
			fieldNameMap.put("birthday", "Birthday");
			fieldNameMap.put("assistant", "Assistant");
			fieldNameMap.put("iscallable", "Callable");
			fieldNameMap.put("assistantphone", "Assistant Phone");
			fieldNameMap.put("assignuser", "Assignee");
			fieldNameMap.put("leadsource", "Lead Source");
			fieldNameMap.put("primaddress", "Address");
			fieldNameMap.put("primcity", "City");
			fieldNameMap.put("state", "State");
			fieldNameMap.put("otheraddress", "Other Address");
			fieldNameMap.put("othercity", "Other City");
			fieldNameMap.put("otherstate", "Other State");
			fieldNameMap.put("primpostalcode", "Postal Code");
			fieldNameMap.put("otherpostalcode", "Other Postal Code");
			fieldNameMap.put("primcountry", "Country");
			fieldNameMap.put("othercountry", "Other Country");
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
