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

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleMeeting;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.module.crm.service.MeetingService;
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
	private MeetingService meetingService;

	private static final MeetingFieldNameMapper mapper = new MeetingFieldNameMapper();

	@Override
	protected void buildExtraTemplateVariables(
			SimpleRelayEmailNotification emailNotification) {
		String summary = bean.getSubject();
		String summaryLink = CrmLinkGenerator.generateMeetingPreviewFullLink(
				siteUrl, bean.getId());

		contentGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		contentGenerator.putVariable("itemType", "meeting");
		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected Enum<?> getCreateSubjectKey() {
		return MeetingI18nEnum.MAIL_CREATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getUpdateSubjectKey() {
		return MeetingI18nEnum.MAIL_UPDATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getCommentSubjectKey() {
		return MeetingI18nEnum.MAIL_COMMENT_ITEM_SUBJECT;
	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getSubject(), 100);
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected SimpleMeeting getBeanInContext(MailContext<SimpleMeeting> context) {
		return meetingService.findById(context.getTypeid(),
				context.getSaccountid());
	}

	public static class MeetingFieldNameMapper extends ItemFieldMapper {

		public MeetingFieldNameMapper() {

			put("subject", MeetingI18nEnum.FORM_SUBJECT, true);

			put("status", MeetingI18nEnum.FORM_STATUS);
			put("startdate", new DateTimeFieldFormat("startdate",
					MeetingI18nEnum.FORM_START_DATE_TIME));

			put("location", MeetingI18nEnum.FORM_LOCATION);
			put("enddate", new DateTimeFieldFormat("enddate",
					MeetingI18nEnum.FORM_END_DATE_TIME));

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);
		}
	}
}
