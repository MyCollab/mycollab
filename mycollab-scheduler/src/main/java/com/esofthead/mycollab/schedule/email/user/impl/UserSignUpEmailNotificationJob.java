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

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.GenericLinkUtils;
import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.service.UserService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UserSignUpEmailNotificationJob extends QuartzJobBean {
	private static Logger log = LoggerFactory
			.getLogger(UserSignUpEmailNotificationJob.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ExtMailService extMailService;

	private static final String userSignUpEmailNotificationTemplate = "templates/email/billing/confirmUserSignUpNotification.mt";

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		UserSearchCriteria criteria = new UserSearchCriteria();
		criteria.setStatus(new SetSearchField<String>(new String[] { null,
				UserStatusConstants.EMAIL_NOT_VERIFIED }));
		criteria.setSaccountid(null);
		List<SimpleUser> lstSimpleUsers = userService
				.findPagableListByCriteria(new SearchRequest<UserSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));
		if (lstSimpleUsers != null && lstSimpleUsers.size() > 0) {
			for (SimpleUser user : lstSimpleUsers) {
				TemplateGenerator templateGenerator = new TemplateGenerator(
						"Please confirm your email",
						userSignUpEmailNotificationTemplate);
				templateGenerator.putVariable("user", user);

				String siteUrl = GenericLinkUtils
						.generateSiteUrlByAccountId(user.getAccountId());

				templateGenerator.putVariable("siteUrl", siteUrl);

				String linkComfirm = siteUrl
						+ "user/confirm_signup/"
						+ UrlEncodeDecoder.encode(user.getUsername() + "/"
								+ user.getAccountId());
				templateGenerator.putVariable("linkConfirm", linkComfirm);
				try {

					log.debug("Start generate template");
					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", Arrays.asList(new MailRecipientField(
									user.getEmail(), user.getDisplayName())),
							null, null, templateGenerator
									.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);

					user.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST);
					userService.updateWithSession(user, user.getUsername());
				} catch (Exception e) {
					log.error("Error whle generate template", e);
				}
			}
		}
	}
}
