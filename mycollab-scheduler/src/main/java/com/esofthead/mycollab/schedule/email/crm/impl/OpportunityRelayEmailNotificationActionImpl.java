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
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.UserLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.schedule.email.MailItemLink;
import com.esofthead.mycollab.schedule.email.crm.CrmMailLinkGenerator;
import com.esofthead.mycollab.schedule.email.crm.OpportunityRelayEmailNotificationAction;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
public class OpportunityRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction implements
		OpportunityRelayEmailNotificationAction {

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private OpportunityService opportunityService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private final OpportunityFieldNameMapper mapper;

	public OpportunityRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.OPPORTUNITY);
		mapper = new OpportunityFieldNameMapper();
	}

	protected void setupMailHeaders(SimpleOpportunity simpleOpportunity,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(simpleOpportunity.getSaccountid()));

		String summary = simpleOpportunity.getOpportunityname();
		String summaryLink = crmLinkGenerator
				.generateContactPreviewFullLink(simpleOpportunity.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "opportunity");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
	}

	protected Map<String, List<MailItemLink>> getListOfProperties(
			SimpleOpportunity simpleOpportunity, SimpleUser user) {
		Map<String, List<MailItemLink>> listOfDisplayProperties = new LinkedHashMap<String, List<MailItemLink>>();

		CrmMailLinkGenerator crmLinkGenerator = new CrmMailLinkGenerator(
				getSiteUrl(simpleOpportunity.getSaccountid()));

		listOfDisplayProperties.put(mapper.getFieldLabel("accountid"), Arrays
				.asList(new MailItemLink(crmLinkGenerator
						.generateAccountPreviewFullLink(simpleOpportunity
								.getAccountid()), simpleOpportunity
						.getAccountName())));

		if (simpleOpportunity.getOpportunitytype() != null) {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("opportunitytype"), Arrays
							.asList(new MailItemLink(null, simpleOpportunity
									.getOpportunitytype())));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("opportunitytype"), null);
		}

		if (simpleOpportunity.getCurrencyid() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("currencyid"),
					Arrays.asList(new MailItemLink(null, simpleOpportunity
							.getCurrency().getIsocode())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("currencyid"),
					null);
		}

		if (simpleOpportunity.getAmount() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("amount"), Arrays
					.asList(new MailItemLink(null, simpleOpportunity
							.getAmount().toString())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("amount"), null);
		}

		if (simpleOpportunity.getExpectedcloseddate() != null) {
			listOfDisplayProperties.put(mapper
					.getFieldLabel("expectedcloseddate"), Arrays
					.asList(new MailItemLink(null, DateTimeUtils
							.converToStringWithUserTimeZone(
									simpleOpportunity.getExpectedcloseddate(),
									user.getTimezone()))));
		} else {
			listOfDisplayProperties.put(
					mapper.getFieldLabel("expectedcloseddate"), null);
		}

		if (simpleOpportunity.getSalesstage() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("salesstage"),
					Arrays.asList(new MailItemLink(null, simpleOpportunity
							.getSalesstage())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("salesstage"),
					null);
		}

		if (simpleOpportunity.getSource() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("source"), Arrays
					.asList(new MailItemLink(null, simpleOpportunity
							.getSource())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("source"), null);
		}

		if (simpleOpportunity.getProbability() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("probability"),
					Arrays.asList(new MailItemLink(null, simpleOpportunity
							.getProbability().toString())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("probability"),
					null);
		}

		if (simpleOpportunity.getCampaignid() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("campaignid"),
					Arrays.asList(new MailItemLink(crmLinkGenerator
							.generateCampainPreviewFullLilnk(simpleOpportunity
									.getCampaignid()), simpleOpportunity
							.getCampaignName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("campaignid"),
					null);
		}

		if (simpleOpportunity.getNextstep() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("nextstep"),
					Arrays.asList(new MailItemLink(null, simpleOpportunity
							.getNextstep())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("nextstep"), null);
		}

		if (simpleOpportunity.getAssignuser() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					Arrays.asList(new MailItemLink(crmLinkGenerator
							.generateUserPreviewFullLink(simpleOpportunity
									.getAssignuser()), simpleOpportunity
							.getAssignUserFullName())));
		} else {
			listOfDisplayProperties.put(mapper.getFieldLabel("assignuser"),
					null);
		}

		if (simpleOpportunity.getDescription() != null) {
			listOfDisplayProperties.put(mapper.getFieldLabel("description"),
					Arrays.asList(new MailItemLink(null, simpleOpportunity
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
		SimpleOpportunity simpleOpportunity = opportunityService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());
		if (simpleOpportunity != null) {
			String subject = StringUtils.trim(
					simpleOpportunity.getOpportunityname(), 100);

			TemplateGenerator templateGenerator = new TemplateGenerator(
					emailNotification.getChangeByUserFullName()
							+ " has created the opportunity \"" + subject
							+ "\"",
					"templates/email/crm/itemCreatedNotifier.mt");
			setupMailHeaders(simpleOpportunity, emailNotification,
					templateGenerator);

			templateGenerator.putVariable("properties",
					getListOfProperties(simpleOpportunity, user));

			return templateGenerator;
		} else {
			return null;
		}
	}

	private Map<String, String> constructHyperLinks(
			SimpleOpportunity simpleOpportunity) {
		Map<String, String> hyperLinks = new HashMap<String, String>();
		hyperLinks.put(
				"opportunityURL",
				getSiteUrl(simpleOpportunity.getSaccountid())
						+ CrmLinkGenerator.generateCrmItemLink(
								CrmTypeConstants.OPPORTUNITY,
								simpleOpportunity.getId()));
		if (simpleOpportunity.getAssignuser() != null) {
			hyperLinks.put("assignUserURL", UserLinkUtils
					.generatePreviewFullUserLink(
							getSiteUrl(simpleOpportunity.getSaccountid()),
							simpleOpportunity.getAssignuser()));
		}
		return hyperLinks;
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleOpportunity simpleOpportunity = opportunityService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		String subject = StringUtils.trim(
				simpleOpportunity.getOpportunityname(), 100);

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has updated the opportunity \"" + subject + "\"",
				"templates/email/crm/itemUpdatedNotifier.mt");
		setupMailHeaders(simpleOpportunity, emailNotification,
				templateGenerator);

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
		SimpleOpportunity simpleOpportunity = opportunityService.findById(
				accountRecordId, emailNotification.getSaccountid());

		TemplateGenerator templateGenerator = new TemplateGenerator(
				emailNotification.getChangeByUserFullName()
						+ " has commented on the opportunity \""
						+ StringUtils.trim(
								simpleOpportunity.getOpportunityname(), 100)
						+ "\"", "templates/email/crm/itemAddNoteNotifier.mt");
		setupMailHeaders(simpleOpportunity, emailNotification,
				templateGenerator);
		templateGenerator.putVariable("comment", emailNotification);

		return templateGenerator;
	}

	public class OpportunityFieldNameMapper {
		private final Map<String, String> fieldNameMap;

		OpportunityFieldNameMapper() {
			fieldNameMap = new HashMap<String, String>();

			fieldNameMap.put("opportunityname", "Opportunity Name");
			fieldNameMap.put("accountid", "Account Name");
			fieldNameMap.put("status", "Status");
			fieldNameMap.put("currencyid", "Currency");
			fieldNameMap.put("expectedcloseddate", "Expected Close Date");
			fieldNameMap.put("amount", "Amount");
			fieldNameMap.put("opportunitytype", "Type");
			fieldNameMap.put("salesstage", "Sales Stage");
			fieldNameMap.put("source", "Lead Source");
			fieldNameMap.put("probability", "Probability (%)");
			fieldNameMap.put("campaignid", "Campaign");
			fieldNameMap.put("nextstep", "Next Step");
			fieldNameMap.put("assignuser", "Assignee");
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
