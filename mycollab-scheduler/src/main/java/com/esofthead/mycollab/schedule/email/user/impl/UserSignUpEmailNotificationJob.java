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
package com.esofthead.mycollab.schedule.email.user.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.i18n.LocalizationHelper;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.mail.IContentGenerator;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.accountsettings.localization.UserI18nEnum;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.schedule.jobs.GenericQuartzJobBean;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserSignUpEmailNotificationJob extends GenericQuartzJobBean {
	@Autowired
	private UserService userService;

	@Autowired
	private ExtMailService extMailService;

	@Autowired
	private IContentGenerator contentGenerator;

	static final String CONFIRM_EMAIL_TEMPLATE = "templates/email/billing/confirmUserSignUpNotification.mt";

	@SuppressWarnings("unchecked")
	@Override
	protected void executeJob(JobExecutionContext context)
			throws JobExecutionException {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setStatuses(new SetSearchField<String>(new String[] { null,
				UserStatusConstants.EMAIL_NOT_VERIFIED }));

		criteria.setSaccountid(null);
		List<SimpleUser> users = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));

		if (CollectionUtils.isNotEmpty(users)) {
			for (SimpleUser user : users) {
				sendConfirmEmailToUser(user);
				user.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST);
				userService.updateWithSession(user, user.getUsername());
			}
		}
	}

	void sendConfirmEmailToUser(SimpleUser user) {
		contentGenerator.putVariable("user", user);

		String siteUrl = GenericLinkUtils.generateSiteUrlByAccountId(user
				.getAccountId());

		contentGenerator.putVariable("siteUrl", siteUrl);

		String linkComfirm = siteUrl
				+ "user/confirm_signup/"
				+ UrlEncodeDecoder.encode(user.getUsername() + "/"
						+ user.getAccountId());
		contentGenerator.putVariable("linkConfirm", linkComfirm);

		extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail(),
				SiteConfiguration.getSiteName(), Arrays
						.asList(new MailRecipientField(user.getEmail(), user
								.getDisplayName())), null, null,
				contentGenerator.generateSubjectContent(LocalizationHelper
						.getMessage(SiteConfiguration.getDefaultLocale(),
								UserI18nEnum.MAIL_CONFIRM_PASSWORD_SUBJECT)),
				contentGenerator.generateBodyContent(CONFIRM_EMAIL_TEMPLATE,
						SiteConfiguration.getDefaultLocale()), null);
	}
}
