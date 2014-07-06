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
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.mail.IContentGenerator;
import com.esofthead.mycollab.module.mail.MailUtils;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.dao.UserAccountInvitationMapper;
import com.esofthead.mycollab.module.user.dao.UserAccountInvitationMapperExt;
import com.esofthead.mycollab.module.user.domain.SimpleUserAccountInvitation;
import com.esofthead.mycollab.schedule.jobs.GenericQuartzJobBean;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SendUserInvitationEmailJob extends GenericQuartzJobBean {
	private static Logger log = LoggerFactory
			.getLogger(SendUserInvitationEmailJob.class);

	@Autowired
	private UserAccountInvitationMapper userAccountInvitationMapper;

	@Autowired
	private UserAccountInvitationMapperExt userAccountInvitationMapperExt;

	@Autowired
	private IContentGenerator contentGenerator;

	@Autowired
	private ExtMailService extMailService;

	@Override
	protected void executeJob(JobExecutionContext context)
			throws JobExecutionException {
		List<SimpleUserAccountInvitation> invitations = userAccountInvitationMapperExt
				.findAccountInvitations(RegisterStatusConstants.VERIFICATING);

		for (SimpleUserAccountInvitation invitation : invitations) {
			log.debug("Send invitation email to user {} of subdomain {}",
					invitation.getUsername(), invitation.getSubdomain());

			contentGenerator.putVariable("invitation", invitation);

			contentGenerator.putVariable(
					"urlAccept",
					SiteConfiguration.getSiteUrl(invitation.getSubdomain())
							+ "user/confirm_invite/"
							+ UrlEncodeDecoder.encode(invitation.getAccountid()
									+ "/" + invitation.getUsername() + "/"
									+ invitation.getSubdomain()));

			String inviterName = invitation.getInviterFullName();
			String inviterMail = invitation.getInviteuser();
			String subdomain = invitation.getSubdomain();
			contentGenerator.putVariable(
					"urlDeny",
					SiteConfiguration.getSiteUrl(invitation.getSubdomain())
							+ "user/deny_invite/"
							+ UrlEncodeDecoder.encode(invitation.getAccountid()
									+ "/" + invitation.getUsername() + "/"
									+ inviterName + "/" + inviterMail + "/"
									+ subdomain));
			String userName = (invitation.getUsername() != null) ? invitation
					.getUsername() : "there";
			contentGenerator.putVariable("userName", userName);
			contentGenerator.putVariable("inviterName", inviterName);
			extMailService
					.sendHTMLMail(
							"noreply@mycollab.com",
							SiteConfiguration.getSiteName(),
							Arrays.asList(new MailRecipientField(invitation
									.getUsername(), invitation.getUsername())),
							null,
							null,
							contentGenerator
									.generateSubjectContent("You are invited to join the MyCollab!"),
							contentGenerator.generateBodyContent(MailUtils
									.templatePath(
											"templates/email/user/userInvitationNotifier.mt",
											SiteConfiguration
													.getDefaultLocale())), null);

			// Send email and change register status of user to
			// RegisterStatusConstants.SENT_VERIFICATION_EMAIL
			invitation
					.setInvitationstatus(RegisterStatusConstants.SENT_VERIFICATION_EMAIL);
			userAccountInvitationMapper.updateByPrimaryKeySelective(invitation);
		}

	}
}
