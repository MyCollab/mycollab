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
package com.esofthead.mycollab.schedule.jobs;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.domain.RelayEmailWithBLOBs;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.esofthead.mycollab.module.mail.DefaultMailer;
import com.esofthead.mycollab.module.mail.service.MailRelayService;
import com.esofthead.mycollab.schedule.email.SendingRelayEmailsAction;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SendingRelayEmailJob extends GenericQuartzJobBean {
	private static final Logger LOG = LoggerFactory
			.getLogger(SendingRelayEmailJob.class);

	@Autowired
	private MailRelayService mailRelayService;

	@Override
	protected void executeJob(JobExecutionContext context) {
		List<RelayEmailWithBLOBs> relayEmails = mailRelayService
				.getRelayEmails();
		mailRelayService.cleanEmails();
		for (RelayEmailWithBLOBs relayEmail : relayEmails) {
			if (relayEmail.getEmailhandlerbean() == null) {
				String recipientVal = relayEmail.getRecipients();
				String[][] recipientArr = (String[][]) JsonDeSerializer
						.fromJson(recipientVal, String[][].class);

				try {
					List<MailRecipientField> toMailList = new ArrayList<MailRecipientField>();
					for (int i = 0; i < recipientArr[0].length; i++) {
						toMailList.add(new MailRecipientField(
								recipientArr[0][i], recipientArr[1][i]));
					}

					DefaultMailer mailer = new DefaultMailer(
							SiteConfiguration.getEmailConfiguration());
					mailer.sendHTMLMail(relayEmail.getFromemail(),
							relayEmail.getFromname(), toMailList, null, null,
							relayEmail.getSubject(),
							relayEmail.getBodycontent(), null);
				} catch (Exception e) {
					LOG.error("Error when send relay email", e);
				}
			} else {
				try {
					SendingRelayEmailsAction emailNotificationAction = (SendingRelayEmailsAction) ApplicationContextUtil
							.getSpringBean(Class.forName(relayEmail
									.getEmailhandlerbean()));
					emailNotificationAction.sendEmail(relayEmail);
				} catch (ClassNotFoundException e) {
					throw new MyCollabException(e);
				}
			}
		}
	}
}
