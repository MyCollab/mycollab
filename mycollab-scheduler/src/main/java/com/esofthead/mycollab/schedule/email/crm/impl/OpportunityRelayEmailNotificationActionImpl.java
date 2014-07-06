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

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmResources;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.AccountLinkUtils;
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
	private OpportunityService opportunityService;

	private static final OpportunityFieldNameMapper mapper = new OpportunityFieldNameMapper();

	@Override
	protected void buildExtraTemplateVariables(
			SimpleRelayEmailNotification emailNotification) {
		String summary = bean.getOpportunityname();
		String summaryLink = CrmLinkGenerator
				.generateOpportunityPreviewFullLink(siteUrl, bean.getId());

		contentGenerator.putVariable("makeChangeUser",
				emailNotification.getChangeByUserFullName());
		contentGenerator.putVariable("itemType", "opportunity");
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
		return opportunityService.findById(context.getTypeid(),
				context.getSaccountid());
	}

	public static class OpportunityFieldNameMapper extends ItemFieldMapper {

		public OpportunityFieldNameMapper() {
			put("opportunityname", OpportunityI18nEnum.FORM_NAME);
			put("accountid", new AccountFieldFormat("accountid",
					OpportunityI18nEnum.FORM_ACCOUNT_NAME));

			put("currencyid", new CurrencyFieldFormat("currency",
					OpportunityI18nEnum.FORM_CURRENCY));
			put("expectedcloseddate", new DateFieldFormat("expectedcloseddate",
					OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE));

			put("amount", OpportunityI18nEnum.FORM_AMOUNT);
			put("opportunitytype", OpportunityI18nEnum.FORM_TYPE);

			put("salesstage", OpportunityI18nEnum.FORM_SALE_STAGE);
			put("source", OpportunityI18nEnum.FORM_LEAD_SOURCE);

			put("probability", OpportunityI18nEnum.FORM_PROBABILITY);
			put("campaignid", new CampaignFieldFormat("campaignid",
					OpportunityI18nEnum.FORM_CAMPAIGN_NAME));

			put("nextstep", OpportunityI18nEnum.FORM_NEXT_STEP);
			put("assignuser", new AssigneeFieldFormat("assignuser",
					GenericI18Enum.FORM_ASSIGNEE));

			put("description", GenericI18Enum.FORM_DESCRIPTION, true);
		}
	}

	public static class AccountFieldFormat extends FieldFormat {

		public AccountFieldFormat(String fieldName, Enum<?> displayName) {
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

				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
						MailUtils.getSiteUrl(opportunity.getSaccountid()),
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
				String userAvatarLink = MailUtils.getAvatarLink(
						user.getAvatarid(), 16);
				String userLink = AccountLinkUtils.generatePreviewFullUserLink(
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
