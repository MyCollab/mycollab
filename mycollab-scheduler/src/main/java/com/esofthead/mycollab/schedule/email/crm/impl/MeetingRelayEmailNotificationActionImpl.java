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
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.crm.MeetingRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class MeetingRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction implements
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

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleMeeting simpleMeeting = meetingService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleMeeting != null) {
			String subject = StringUtils.trim(simpleMeeting.getSubject(),
					150);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has created the meeting \"" + subject + "\"",
					"templates/email/crm/meetingCreatedNotifier.mt");

			ScheduleUserTimeZoneUtils
					.formatDateTimeZone(simpleMeeting, user.getTimezone(),
							new String[] { "startdate", "enddate" });
			templateGenerator.putVariable("simpleMeeting", simpleMeeting);
			templateGenerator.putVariable("hyperLinks",
					constructHyperLinks(simpleMeeting));
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

		String subject = StringUtils
				.trim(simpleMeeting.getSubject(), 150);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the meeting \"" + subject + "\"",
				"templates/email/crm/meetingUpdatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(simpleMeeting,
				user.getTimezone(), new String[] { "startdate", "enddate" });
		templateGenerator.putVariable("simpleMeeting", simpleMeeting);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(simpleMeeting));

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			templateGenerator.putVariable("postedUserURL", UserLinkUtils
					.generatePreviewFullUserLink(
							getSiteUrl(simpleMeeting.getSaccountid()),
							auditLog.getPosteduser()));
			ScheduleUserTimeZoneUtils.formatDate(auditLog, user.getTimezone(),
					new String[] { "startdate", "enddate" });
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
						+ StringUtils.trim(simpleMeeting.getSubject(),
								100) + "\"",
				"templates/email/crm/meetingAddNoteNotifier.mt");
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", UserLinkUtils
				.generatePreviewFullUserLink(
						getSiteUrl(simpleMeeting.getSaccountid()),
						emailNotification.getChangeby()));
		templateGenerator.putVariable("simpleMeeting", simpleMeeting);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(simpleMeeting));

		return templateGenerator;
	}

	private Map<String, String> constructHyperLinks(SimpleMeeting simpleMeeting) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		hyperLinks
				.put("meetingURL",
						getSiteUrl(simpleMeeting.getSaccountid())
								+ CrmLinkGenerator.generateCrmItemLink(
										CrmTypeConstants.MEETING,
										simpleMeeting.getId()));
		return hyperLinks;
	}

	public class MeetingFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		MeetingFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("subject", "Subject");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("startdate", "Start Date & Time");
			fieldNameMap.put("relatedTo", "Related to");
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
