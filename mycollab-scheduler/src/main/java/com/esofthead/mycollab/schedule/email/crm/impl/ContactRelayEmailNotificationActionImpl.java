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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleContact;
import com.esofthead.mycollab.module.crm.service.ContactService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.crm.ContactRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class ContactRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction implements
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

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleContact simpleContact = contactService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleContact != null) {
			String subject = StringUtils.subString(
					simpleContact.getContactName(), 150);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					"Contact: \"" + subject + "\" has been created",
					"templates/email/crm/contactCreatedNotifier.mt");

			ScheduleUserTimeZoneUtils.formatDateTimeZone(simpleContact,
					user.getTimezone(), new String[] { "birthday" });
			templateGenerator.putVariable("simpleContact", simpleContact);
			templateGenerator.putVariable("hyperLinks",
					constructHyperLinks(simpleContact));
			return templateGenerator;
		} else {
			return null;
		}
	}

	private Map<String, String> constructHyperLinks(SimpleContact simpleContact) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		hyperLinks
				.put("contactURL",
						getSiteUrl(simpleContact.getSaccountid())
								+ CrmLinkGenerator.generateCrmItemLink(
										CrmTypeConstants.CONTACT,
										simpleContact.getId()));
		if (simpleContact.getAccountid() != null) {
			hyperLinks.put(
					"accountURL",
					getSiteUrl(simpleContact.getSaccountid())
							+ CrmLinkGenerator.generateCrmItemLink(
									CrmTypeConstants.ACCOUNT,
									simpleContact.getAccountid()));
		}

		if (simpleContact.getAssignuser() != null) {
			hyperLinks.put("assignUserURL", UserLinkUtils
					.generatePreviewFullUserLink(
							getSiteUrl(simpleContact.getSaccountid()),
							simpleContact.getAssignuser()));
		}
		return hyperLinks;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleContact simpleContact = contactService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.subString(simpleContact.getContactName(),
				150);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"Contact: \"" + subject + "...\" has been updated",
				"templates/email/crm/contactUpdatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(simpleContact,
				user.getTimezone(), new String[] { "birthday" });
		templateGenerator.putVariable("simpleContact", simpleContact);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(simpleContact));

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());

			ScheduleUserTimeZoneUtils.formatDate(auditLog, user.getTimezone(),
					new String[] { "birthday" });
			templateGenerator.putVariable("postedUserURL", UserLinkUtils
					.generatePreviewFullUserLink(
							getSiteUrl(simpleContact.getSaccountid()),
							auditLog.getPosteduser()));
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

		TemplateGenerator templateGenerator = new TemplateGenerator("[Contact]"
				+ emailNotification.getChangeByUserFullName()
				+ " has commented on "
				+ StringUtils.subString(simpleContact.getContactName(), 100)
				+ "\"", "templates/email/crm/contactAddNoteNotifier.mt");
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", UserLinkUtils
				.generatePreviewFullUserLink(
						getSiteUrl(simpleContact.getSaccountid()),
						emailNotification.getChangeby()));
		templateGenerator.putVariable("simpleContact", simpleContact);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(simpleContact));

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
			fieldNameMap.put("leadsource", "Leader Source");
			fieldNameMap.put("primaddress", "Address");
			fieldNameMap.put("primcity", "City");
			fieldNameMap.put("state", "State");
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
