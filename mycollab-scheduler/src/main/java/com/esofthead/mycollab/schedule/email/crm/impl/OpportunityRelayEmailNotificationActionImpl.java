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

import com.esofthead.mycollab.module.crm.domain.Opportunity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.MonitorTypeConstants;
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.StorageManager;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.OpportunityRelayEmailNotificationAction;
import com.esofthead.mycollab.schedule.email.format.CurrencyFieldFormat;
import com.esofthead.mycollab.schedule.email.format.DateFieldFormat;
import com.esofthead.mycollab.schedule.email.format.FieldFormat;
import com.esofthead.mycollab.schedule.email.format.html.TagBuilder;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Span;

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

	private static final Logger LOG = LoggerFactory
			.getLogger(OpportunityRelayEmailNotificationActionImpl.class);

	@Autowired
	private OpportunityService opportunityService;

	private static final OpportunityFieldNameMapper mapper = new OpportunityFieldNameMapper();

	@Override
	protected void buildExtraTemplateVariables(
			MailContext<SimpleOpportunity> context) {
		String summary = bean.getOpportunityname();
		String summaryLink = CrmLinkGenerator
				.generateOpportunityPreviewFullLink(siteUrl, bean.getId());

		SimpleRelayEmailNotification emailNotification = context
				.getEmailNotification();

		String avatarId = "";

		SimpleUser user = userService.findUserByUserNameInAccount(
				emailNotification.getChangeby(), context.getSaccountid());

		if (user != null) {
			avatarId = user.getAvatarid();
		}
		Img userAvatar = new Img("", StorageManager.getAvatarLink(avatarId, 16));
		userAvatar.setWidth("16");
		userAvatar.setHeight("16");
		userAvatar.setStyle("display: inline-block; vertical-align: top;");

		String makeChangeUser = userAvatar.toString()
				+ emailNotification.getChangeByUserFullName();

		if (MonitorTypeConstants.CREATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					OpportunityI18nEnum.MAIL_CREATE_ITEM_HEADING,
					makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					OpportunityI18nEnum.MAIL_UPDATE_ITEM_HEADING,
					makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					OpportunityI18nEnum.MAIL_COMMENT_ITEM_HEADING,
					makeChangeUser));
		}

		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected Enum<?> getCreateSubjectKey() {
		return OpportunityI18nEnum.MAIL_CREATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getUpdateSubjectKey() {
		return OpportunityI18nEnum.MAIL_UPDATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getCommentSubjectKey() {
		return OpportunityI18nEnum.MAIL_COMMENT_ITEM_SUBJECT;
	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getOpportunityname(), 100);
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected SimpleOpportunity getBeanInContext(
			MailContext<SimpleOpportunity> context) {
		return opportunityService.findById(
				Integer.parseInt(context.getTypeid()), context.getSaccountid());
	}

	public static class OpportunityFieldNameMapper extends ItemFieldMapper {

		public OpportunityFieldNameMapper() {
			put(Opportunity.Field.opportunityname, OpportunityI18nEnum.FORM_NAME);
			put(Opportunity.Field.accountid, new AccountFieldFormat(Opportunity.Field.accountid.name(),
					OpportunityI18nEnum.FORM_ACCOUNT_NAME));

			put(Opportunity.Field.currencyid, new CurrencyFieldFormat(Opportunity.Field.currencyid.name(),
					OpportunityI18nEnum.FORM_CURRENCY));
			put(Opportunity.Field.expectedcloseddate, new DateFieldFormat(Opportunity.Field.expectedcloseddate.name(),
					OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE));

			put(Opportunity.Field.amount, OpportunityI18nEnum.FORM_AMOUNT);
			put(Opportunity.Field.opportunitytype, OpportunityI18nEnum.FORM_TYPE);

			put(Opportunity.Field.salesstage, OpportunityI18nEnum.FORM_SALE_STAGE);
			put(Opportunity.Field.source, OpportunityI18nEnum.FORM_LEAD_SOURCE);

			put(Opportunity.Field.probability, OpportunityI18nEnum.FORM_PROBABILITY);
			put(Opportunity.Field.campaignid, new CampaignFieldFormat(Opportunity.Field.campaignid.name(),
					OpportunityI18nEnum.FORM_CAMPAIGN_NAME));

			put(Opportunity.Field.nextstep, OpportunityI18nEnum.FORM_NEXT_STEP);
			put(Opportunity.Field.assignuser, new AssigneeFieldFormat(Opportunity.Field.assignuser.name(),
					GenericI18Enum.FORM_ASSIGNEE));

			put(Opportunity.Field.description, GenericI18Enum.FORM_DESCRIPTION, true);
		}
	}

	public static class AccountFieldFormat extends FieldFormat {

		public AccountFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleOpportunity simpleOpportunity = (SimpleOpportunity) context
					.getWrappedBean();
			if (simpleOpportunity.getAccountid() != null) {
				String accountIconLink = CrmResources
						.getResourceLink(CrmTypeConstants.ACCOUNT);
				Img img = TagBuilder.newImg("avatar", accountIconLink);

				String accountLink = CrmLinkGenerator
						.generateAccountPreviewFullLink(context.getSiteUrl(),
								simpleOpportunity.getAccountid());
				A link = TagBuilder.newA(accountLink,
						simpleOpportunity.getAccountName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return new Span().write();
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
				return new Span().write();
			}

			try {
				Integer accountId = Integer.parseInt(value);
				AccountService accountService = ApplicationContextUtil
						.getSpringBean(AccountService.class);
				SimpleAccount account = accountService.findById(accountId,
						context.getUser().getAccountId());

				if (account != null) {
					String accountIconLink = CrmResources
							.getResourceLink(CrmTypeConstants.ACCOUNT);
					Img img = TagBuilder.newImg("avatar", accountIconLink);

					String accountLink = CrmLinkGenerator
							.generateAccountPreviewFullLink(
									context.getSiteUrl(), account.getId());
					A link = TagBuilder.newA(accountLink,
							account.getAccountname());
					return TagBuilder.newLink(img, link).write();
				}
			} catch (Exception e) {
				LOG.error("Error", e);
			}
			return value;
		}
	}

	public static class CampaignFieldFormat extends FieldFormat {

		public CampaignFieldFormat(String fieldName, Enum<?> displayName) {
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
				return new Span().write();
			}

		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
				return new Span().write();
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
				LOG.error("Error", e);
			}
			return value;
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleOpportunity opportunity = (SimpleOpportunity) context
					.getWrappedBean();

			if (opportunity.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						opportunity.getAssignUserAvatarId(), 16);

				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(MailUtils
								.getSiteUrl(opportunity.getSaccountid()),
								opportunity.getAssignuser());
				A link = TagBuilder.newA(userLink,
						opportunity.getAssignUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return new Span().write();
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (value == null || "".equals(value)) {
				return new Span().write();
			}

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(value,
					context.getUser().getAccountId());
			if (user != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(user.getAccountId()),
								user.getUsername());
				Img img = TagBuilder.newImg("avatar", userAvatarLink);
				A link = TagBuilder.newA(userLink, user.getDisplayName());
				return TagBuilder.newLink(img, link).write();
			}
			return value;
		}
	}
}
