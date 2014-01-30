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
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.ScheduleUserTimeZoneUtils;
import com.esofthead.mycollab.schedule.email.crm.CampaignRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class CampaignRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction implements
		CampaignRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private CampaignService campaignService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private final CampaignFieldNameMapper mapper;

	public CampaignRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.CAMPAIGN);
		mapper = new CampaignFieldNameMapper();
	}

	@Override
	protected TemplateGenerator templateGeneratorForCreateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCampaign simpleCampaign = campaignService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleCampaign != null) {
			String subject = StringUtils.subString(
					simpleCampaign.getCampaignname(), 150);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					"Campaign: \"" + subject + "\" has been created",
					"templates/email/crm/campaignCreatedNotifier.mt");

			ScheduleUserTimeZoneUtils
					.formatDateTimeZone(simpleCampaign, user.getTimezone(),
							new String[] { "startdate", "enddate" });
			templateGenerator.putVariable("simpleCampaign", simpleCampaign);
			templateGenerator.putVariable("hyperLinks",
					constructHyperLinks(simpleCampaign));
			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleCampaign simpleCampaign = campaignService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.subString(
				simpleCampaign.getCampaignname(), 150);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"Campaign: \"" + subject + "...\" has been updated",
				"templates/email/crm/campaignUpdatedNotifier.mt");
		ScheduleUserTimeZoneUtils.formatDateTimeZone(simpleCampaign,
				user.getTimezone(), new String[] { "startdate", "enddate" });
		templateGenerator.putVariable("simpleCampaign", simpleCampaign);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(simpleCampaign));

		if (emailNotification.getTypeid() != null) {
			SimpleAuditLog auditLog = auditLogService.findLatestLog(
					emailNotification.getTypeid(),
					emailNotification.getSaccountid());
			templateGenerator.putVariable("postedUserURL", UserLinkUtils
					.generatePreviewFullUserLink(
							getSiteUrl(simpleCampaign.getSaccountid()),
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
		int accountRecordId = emailNotification.getTypeid();
		SimpleCampaign simpleCampaign = campaignService.findById(
				accountRecordId, emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				"[Campaign]"
						+ emailNotification.getChangeByUserFullName()
						+ " has commented on "
						+ StringUtils.subString(
								simpleCampaign.getCampaignname(), 100) + "\"",
				"templates/email/crm/campaignAddNoteNotifier.mt");
		templateGenerator.putVariable("comment", emailNotification);
		templateGenerator.putVariable("userComment", UserLinkUtils
				.generatePreviewFullUserLink(
						getSiteUrl(simpleCampaign.getSaccountid()),
						emailNotification.getChangeby()));
		templateGenerator.putVariable("simpleCampaign", simpleCampaign);
		templateGenerator.putVariable("hyperLinks",
				constructHyperLinks(simpleCampaign));

		return templateGenerator;
	}

	private Map<String, String> constructHyperLinks(
			SimpleCampaign simpleCampaign) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		hyperLinks.put(
				"campaignURL",
				getSiteUrl(simpleCampaign.getSaccountid())
						+ CrmLinkGenerator.generateCrmItemLink(
								CrmTypeConstants.CAMPAIGN,
								simpleCampaign.getId()));
		if (simpleCampaign.getAssignuser() != null) {
			hyperLinks.put("assignUserURL", UserLinkUtils
					.generatePreviewFullUserLink(
							getSiteUrl(simpleCampaign.getSaccountid()),
							simpleCampaign.getAssignuser()));
		}
		return hyperLinks;
	}

	public class CampaignFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		CampaignFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("campaignname", "Name");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("startdate", "StartDate");
			fieldNameMap.put("type", "Type");
			fieldNameMap.put("enddate", "EndDate");
			fieldNameMap.put("assignuser", "Assignee");
			// fieldNameMap.put("currency.symbol", "Currency");
			fieldNameMap.put("budget", "Budget");
			fieldNameMap.put("expectedcost", "Expected Cost");
			fieldNameMap.put("budget", "Budget");
			fieldNameMap.put("actualcost", "Actual Cost");
			fieldNameMap.put("expectedcost", "Expected Revenue");
			fieldNameMap.put("actualcost", "Actual Cost");
			fieldNameMap.put("expectedrevenue", "Expected Revenue");
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
