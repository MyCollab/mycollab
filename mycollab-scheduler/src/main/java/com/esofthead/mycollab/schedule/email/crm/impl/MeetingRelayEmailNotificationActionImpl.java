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
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.crm.MeetingRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class MeetingRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleMeeting> implements
		MeetingRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private MeetingService meetingService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private final MeetingFieldNameMapper mapper;

	public MeetingRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.MEETING);
		mapper = new MeetingFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleMeeting meeting,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(meeting.getSaccountid()));

		String summary = meeting.getSubject();
		String summaryLink = crmLinkGenerator
				.generateMeetingPreviewFullLink(meeting.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "meeting");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleMeeting meeting, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(meeting.getSaccountid()));

		if (meeting.getStatus() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("status"),
					Arrays.asList(new MailItemLink(null, meeting.getStatus())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("status"), null);
		}

		if (meeting.getStartdate() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("startdate"), Arrays
							.asList(new MailItemLink(null, DateTimeUtils
									.converToStringWithUserTimeZone(
											meeting.getStartdate(),
											user.getTimezone()))));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("startdate"), null);
		}

		if (meeting.getTypeid() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("typeid"), Arrays
					.asList(generateRelatedItem(meeting.getType(),
							meeting.getTypeid(), meeting.getSaccountid(),
							crmLinkGenerator)));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("typeid"), null);
		}

		if (meeting.getEnddate() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("enddate"), Arrays
							.asList(new MailItemLink(null, DateTimeUtils
									.converToStringWithUserTimeZone(
											meeting.getEnddate(),
											user.getTimezone()))));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("enddate"), null);
		}

		if (meeting.getLocation() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("location"), Arrays
							.asList(new MailItemLink(null, meeting
									.getLocation())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("location"), null);
		}

		if (meeting.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new MailItemLink(null, meeting
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
		SimpleMeeting simpleMeeting = meetingService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleMeeting != null) {
			String subject = StringUtils.trim(simpleMeeting.getSubject(), 150);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has created the meeting \"" + subject + "\"",
					"templates/email/crm/itemCreatedNotifier.mt");
			setupMailHeaders(simpleMeeting, emailNotification,
					templateGenerator);
			templateGenerator.putVariable("properties",
					getListOfProperties(simpleMeeting, user));
			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleMeeting simpleMeeting = meetingService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(simpleMeeting.getSubject(), 150);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the meeting \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(simpleMeeting,
				user.getTimezone(), new String[] { "startdate", "enddate" });
		setupMailHeaders(simpleMeeting, emailNotification, templateGenerator);

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
		SimpleMeeting simpleMeeting = meetingService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the meeting \""
						+ StringUtils.trim(simpleMeeting.getSubject(), 100)
						+ "\"", "templates/email/crm/itemAddNoteNotifier.mt");
		setupMailHeaders(simpleMeeting, emailNotification, templateGenerator);
		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class MeetingFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		MeetingFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("subject", "Subject");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("startdate", "Start Date & Time");
			fieldNameMap.put("typeid", "Related to");
			fieldNameMap.put("enddate", "End Date & Time");
			fieldNameMap.put("location", "Location");
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
