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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.SimpleAuditLog;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.service.AuditLogService;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.LinkUtils;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.OpportunityRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.CurrencyFieldFormat;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OpportunityRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleOpportunity> implements
		OpportunityRelayEmailNotificationAction {

	private static Logger log = LoggerFactory
			.getLogger(OpportunityRelayEmailNotificationActionImpl.class);

	@Autowired
	private AuditLogService auditLogService;
	@Autowired
	private OpportunityService opportunityService;

	@Autowired
	private CrmNotificationSettingService notificationService;

	private static final OpportunityFieldNameMapper mapper = new OpportunityFieldNameMapper();

	public OpportunityRelayEmailNotificationActionImpl() {
		super(CrmTypeConstants.OPPORTUNITY);
	}

	protected void setupMailHeaders(SimpleOpportunity simpleOpportunity,
			SimpleRelayEmailNotification emailNotification,
			TemplateGenerator templateGenerator) {

		String summary = simpleOpportunity.getOpportunityname();
		String summaryLink = CrmLinkGenerator
				.generateOpportunityPreviewFullLink(siteUrl,
						simpleOpportunity.getId());

		templateGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		templateGenerator.putVariable("itemType", "opportunity");
		templateGenerator.putVariable("summary", summary);
		templateGenerator.putVariable("summaryLink", summaryLink);
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

			templateGenerator.putVariable("context",
					new MailContext<SimpleOpportunity>(simpleOpportunity, user,
							siteUrl));
			templateGenerator.putVariable("mapper", mapper);

			return templateGenerator;
		} else {
			return null;
		}
	}

	@Override
	protected TemplateGenerator templateGeneratorForUpdateAction(
			SimpleRelayEmailNotification emailNotification, SimpleUser user) {
		SimpleOpportunity simpleOpportunity = opportunityService.findById(
				emailNotification.getTypeid(),
				emailNotification.getSaccountid());

		if (simpleOpportunity == null) {
			return null;
		}
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
			templateGenerator.putVariable("context",
					new MailContext<SimpleOpportunity>(simpleOpportunity, user,
							siteUrl));
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

		if (simpleOpportunity == null) {
			return null;
		}
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

	public static class OpportunityFieldNameMapper extends ItemFieldMapper {

		public OpportunityFieldNameMapper() {
			put("opportunityname", "Opportunity Name");
			put("accountid", new AccountFieldFormat("accountid", "Account"));

			put("currencyid", new CurrencyFieldFormat("currency", "Currency"));
			put("expectedcloseddate", new DateFieldFormat("expectedcloseddate",
					"Expected Close Date"));

			put("amount", "Amount");
			put("opportunitytype", "Type");

			put("salesstage", "Sales Stage");
			put("source", "Lead Source");

			put("probability", "Probability (%)");
			put("campaignid", new CampaignFieldFormat("campaignid", "Campaign"));

			put("nextstep", "Next Step");
			put("assignuser", new AssigneeFieldFormat("assignuser", "Assignee"));

			put("description", "Description", true);
		}
	}

	public static class AccountFieldFormat extends FieldFormat {

		public AccountFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleOpportunity opportunity = (SimpleOpportunity) context
					.getWrappedBean();
			String accountIconLink = CrmResources
					.getResourceLink(CrmTypeConstants.ACCOUNT);
			Img img = TagBuilder.newImg("icon", accountIconLink);

			String accountLink = CrmLinkGenerator
					.generateAccountPreviewFullLink(context.getSiteUrl(),
							opportunity.getAccountid());
			A link = TagBuilder.newA(accountLink, opportunity.getAccountName());
			return TagBuilder.newLink(img, link).write();
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			return value;
		}
	}

	public static class CampaignFieldFormat extends FieldFormat {

		public CampaignFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleOpportunity opportunity = (SimpleOpportunity) context
					.getWrappedBean();
			if (opportunity.getCampaignid() != null) {
				String campaignIconLink = CrmResources
						.getResourceLink(CrmTypeConstants.CAMPAIGN);
				Img img = TagBuilder.newImg("icon", campaignIconLink);

				String campaignLink = CrmLinkGenerator
						.generateCampaignPreviewFullLink(context.getSiteUrl(),
								opportunity.getCampaignid());
				A link = TagBuilder.newA(campaignLink,
						opportunity.getCampaignName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return "";
			}

		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return "";
			}

			try {
				Integer campaignId = Integer.parseInt(value);
				CampaignService campaignService = ApplicationContextUtil
						.getSpringBean(CampaignService.class);
				SimpleCampaign campaign = campaignService.findById(campaignId,
						context.getUser().getAccountId());

				if (campaign != null) {
					String campaignIconLink = CrmResources
							.getResourceLink(CrmTypeConstants.CAMPAIGN);
					Img img = TagBuilder.newImg("icon", campaignIconLink);

					String campaignLink = CrmLinkGenerator
							.generateCampaignPreviewFullLink(
									context.getSiteUrl(), campaign.getId());
					A link = TagBuilder.newA(campaignLink,
							campaign.getCampaignname());
					return TagBuilder.newLink(img, link).write();
				}
			} catch (Exception e) {
				log.error("Error", e);
			}
			return value;
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, String displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleOpportunity opportunity = (SimpleOpportunity) context
					.getWrappedBean();

			if (opportunity.getAssignuser() != null) {
				String userAvatarLink = LinkUtils.getAvatarLink(
						opportunity.getAssignUserAvatarId(), 16);

				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						LinkUtils.getSiteUrl(opportunity.getSaccountid()),
						opportunity.getAssignuser());
				A link = TagBuilder.newA(userLink,
						opportunity.getAssignUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return "";
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return "";
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = LinkUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						LinkUtils.getSiteUrl(user.getAccountId()),
						user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}
	}

}
