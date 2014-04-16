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
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.crm.CampaignRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class CampaignRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleCampaign> implements
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

	protected void setupMailHeaders(SimpleCampaign campaign,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(campaign.getSaccountid()));

		String summary = campaign.getCampaignname();
		String summaryLink = crmLinkGenerator
				.generateCasePreviewFullLink(campaign.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "campaign");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleCampaign campaign, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(campaign.getSaccountid()));

		if (campaign.getStatus() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("status"),
							Arrays.asList(new MailItemLink(null, campaign
									.getStatus())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("status"), null);
		}

		if (campaign.getType() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("type"),
					Arrays.asList(new MailItemLink(null, campaign.getType())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("type"), null);
		}

		if (campaign.getStartdate() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("startdate"), Arrays
							.asList(new MailItemLink(null, DateTimeUtils
									.converToStringWithUserTimeZone(
											campaign.getStartdate(),
											user.getTimezone()))));
		} else {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("startdate"), null);
		}

		if (campaign.getEnddate() != null) {
			listOfDisplayProperties
					.put(mapper.getFieldLabel("enddate"), Arrays
							.asList(new MailItemLink(null, DateTimeUtils
									.converToStringWithUserTimeZone(
											campaign.getEnddate(),
											user.getTimezone()))));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("enddate"), null);
		}

		if (campaign.getAssignuser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					Arrays.asList(new MailItemLink(crmLinkGenerator
							.generateUserPreviewFullLink(campaign
									.getAssignuser()), campaign
							.getAssignUserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					null);
		}

		if (campaign.getCurrencyid() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("currency"),
					Arrays.asList(new MailItemLink(null, campaign.getCurrency()
							.getIsocode())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("currency"), null);
		}

		if (campaign.getBudget() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("budget"), Arrays
					.asList(new MailItemLink(null, campaign.getBudget()
							.toString())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("budget"), null);
		}

		if (campaign.getExpectedcost() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("expectedcost"),
					Arrays.asList(new MailItemLink(null, campaign
							.getExpectedcost().toString())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("expectedcost"),
					null);
		}

		if (campaign.getExpectedrevenue() != null) {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("expectedrevenue"), Arrays
							.asList(new MailItemLink(null, campaign
									.getExpectedrevenue().toString())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("expectedrevenue"), null);
		}

		if (campaign.getActualcost() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("actualcost"),
					Arrays.asList(new MailItemLink(null, campaign
							.getActualcost().toString())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("actualcost"),
					null);
		}

		if (campaign.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new MailItemLink(null, campaign
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
		SimpleCampaign simpleCampaign = campaignService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleCampaign != null) {
			String subject = StringUtils.trim(simpleCampaign.getCampaignname(),
					100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has created the campaign \"" + subject + "\"",
					"templates/email/crm/itemCreatedNotifier.mt");

			setupMailHeaders(simpleCampaign, emailNotification,
					templateGenerator);

			templateGenerator.putVariable("properties",
					getListOfProperties(simpleCampaign, user));

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

		String subject = StringUtils
				.trim(simpleCampaign.getCampaignname(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the campaign \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");

		setupMailHeaders(simpleCampaign, emailNotification, templateGenerator);

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
		SimpleCampaign simpleCampaign = campaignService.findById(
				accountRecordId, emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the campaign \""
						+ StringUtils.trim(simpleCampaign.getCampaignname(),
								100) + "\"",
				"templates/email/crm/itemAddNoteNotifier.mt");
		setupMailHeaders(simpleCampaign, emailNotification, templateGenerator);

		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
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
			fieldNameMap.put("currency", "Currency");
			fieldNameMap.put("budget", "Budget");
			fieldNameMap.put("expectedcost", "Expected Cost");
			fieldNameMap.put("expectedrevenue", "Expected Revenue");
			fieldNameMap.put("actualcost", "Actual Cost");
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
