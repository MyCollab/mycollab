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
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.MeetingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.MeetingRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.DateTimeFieldFormat;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MeetingRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleMeeting> implements
		MeetingRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private MeetingService meetingService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static final MeetingFieldNameMapper mapper = new MeetingFieldNameMapper();

	public MeetingRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.MEETING);
	}

	protected void setupMailHeaders(SimpleMeeting meeting,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = meeting.getSubject();
		String summaryLink = CrmLinkGenerator.generateMeetingPreviewFullLink(
				siteUrl, meeting.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "meeting");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
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
			templateGenerator
					.putVariable("context", new MailContext<SimpleMeeting>(
							simpleMeeting, user, siteUrl));
			templateGenerator.putVariable("mapper", mapper);
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

		if (simpleMeeting == null) {
			return null;
		}

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
			templateGenerator
					.putVariable("context", new MailContext<SimpleMeeting>(
							simpleMeeting, user, siteUrl));
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
		
		if (simpleMeeting == null) {
			return null;
		}

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the meeting \""
						+ StringUtils.trim(simpleMeeting.getSubject(), 100)
						+ "\"", "templates/email/crm/itemAddNoteNotifier.mt");
		setupMailHeaders(simpleMeeting, emailNotification, templateGenerator);
		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public static class MeetingFieldNameMapper extends ItemFieldMapper {

		public MeetingFieldNameMapper() {

			put("subject", "Subject", true);

			put("status", "Status");
			put("startdate", new DateTimeFieldFormat("startdate",
					"Start Date & Time"));

			put("location", "Location");
			put("enddate",
					new DateTimeFieldFormat("enddate", "End Date & Time"));

			put("typeid", "Related to", true);
			put("description", "Description", true);
		}
	}

}
