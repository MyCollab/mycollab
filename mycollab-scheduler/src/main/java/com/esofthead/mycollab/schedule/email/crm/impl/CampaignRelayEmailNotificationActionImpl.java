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

import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
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
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.user.AccountLinkGenerator;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.email.ItemFieldMapper;
import com.esofthead.mycollab.schedule.email.MailContext;
import com.esofthead.mycollab.schedule.email.crm.CampaignRelayEmailNotificationAction;
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
public class CampaignRelayEmailNotificationActionImpl extends
		CrmDefaultSendingRelayEmailAction<SimpleCampaign> implements
		CampaignRelayEmailNotificationAction {

	@Autowired
	private CampaignService campaignService;

	private static final CampaignFieldNameMapper mapper = new CampaignFieldNameMapper();

	@Override
	protected void buildExtraTemplateVariables(
			MailContext<SimpleCampaign> context) {
		String summary = bean.getCampaignname();
		String summaryLink = CrmLinkGenerator.generateCampaignPreviewFullLink(
				siteUrl, bean.getId());

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
					CampaignI18nEnum.MAIL_CREATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.UPDATE_ACTION.equals(emailNotification
				.getAction())) {
			contentGenerator.putVariable("actionHeading", context.getMessage(
					CampaignI18nEnum.MAIL_UPDATE_ITEM_HEADING, makeChangeUser));
		} else if (MonitorTypeConstants.ADD_COMMENT_ACTION
				.equals(emailNotification.getAction())) {
			contentGenerator
					.putVariable("actionHeading", context.getMessage(
							CampaignI18nEnum.MAIL_COMMENT_ITEM_HEADING,
							makeChangeUser));
		}

		contentGenerator.putVariable("summary", summary);
		contentGenerator.putVariable("summaryLink", summaryLink);
	}

	@Override
	protected Enum<?> getCreateSubjectKey() {
		return CampaignI18nEnum.MAIL_CREATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getUpdateSubjectKey() {
		return CampaignI18nEnum.MAIL_UPDATE_ITEM_SUBJECT;
	}

	@Override
	protected Enum<?> getCommentSubjectKey() {
		return CampaignI18nEnum.MAIL_COMMENT_ITEM_SUBJECT;
	}

	@Override
	protected String getItemName() {
		return StringUtils.trim(bean.getCampaignname(), 100);
	}

	@Override
	protected ItemFieldMapper getItemFieldMapper() {
		return mapper;
	}

	@Override
	protected SimpleCampaign getBeanInContext(
			MailContext<SimpleCampaign> context) {
		return campaignService.findById(Integer.parseInt(context.getTypeid()),
				context.getSaccountid());
	}

	public static class CampaignFieldNameMapper extends ItemFieldMapper {

		public CampaignFieldNameMapper() {
			put(CampaignWithBLOBs.Field.campaignname, CampaignI18nEnum.FORM_CAMPAIGN_NAME, true);

			put(CampaignWithBLOBs.Field.status, CampaignI18nEnum.FORM_STATUS);
			put(CampaignWithBLOBs.Field.type, CampaignI18nEnum.FORM_TYPE);

			put(CampaignWithBLOBs.Field.currencyid, new CurrencyFieldFormat(CampaignWithBLOBs.Field.currencyid.name(),
					CampaignI18nEnum.FORM_CURRENCY));
			put(CampaignWithBLOBs.Field.budget, CampaignI18nEnum.FORM_BUDGET);

			put(CampaignWithBLOBs.Field.expectedcost, CampaignI18nEnum.FORM_EXPECTED_COST);
			put(CampaignWithBLOBs.Field.expectedrevenue, CampaignI18nEnum.FORM_EXPECTED_REVENUE);

			put(CampaignWithBLOBs.Field.actualcost, CampaignI18nEnum.FORM_ACTUAL_COST);
			put(CampaignWithBLOBs.Field.assignuser, new AssigneeFieldFormat(CampaignWithBLOBs.Field.assignuser.name(),
					GenericI18Enum.FORM_ASSIGNEE));

			put(CampaignWithBLOBs.Field.startdate, new DateFieldFormat(CampaignWithBLOBs.Field.startdate.name(),
					CampaignI18nEnum.FORM_START_DATE));
			put(CampaignWithBLOBs.Field.enddate, new DateFieldFormat(CampaignWithBLOBs.Field.enddate.name(),
					CampaignI18nEnum.FORM_END_DATE));

			put(CampaignWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, true);
		}
	}

	public static class AssigneeFieldFormat extends FieldFormat {

		public AssigneeFieldFormat(String fieldName, Enum<?> displayName) {
			super(fieldName, displayName);
		}

		@Override
		public String formatField(MailContext<?> context) {
			SimpleCampaign campaign = (SimpleCampaign) context.getWrappedBean();
			if (campaign.getAssignuser() != null) {
				String userAvatarLink = MailUtils.getAvatarLink(
						campaign.getAssignUserAvatarId(), 16);
				Img img = TagBuilder.newImg("avatar", userAvatarLink);

				String userLink = AccountLinkGenerator
						.generatePreviewFullUserLink(
								MailUtils.getSiteUrl(campaign.getSaccountid()),
								campaign.getAssignuser());
				A link = TagBuilder.newA(userLink,
						campaign.getAssignUserFullName());
				return TagBuilder.newLink(img, link).write();
			} else {
				return "";
			}
		}

		@Override
		public String formatField(MailContext<?> context, String value) {
			if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
				return "";
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
