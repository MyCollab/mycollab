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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.module.billing.service.BillingService;
import com.esofthead.mycollab.module.mail.TemplateGenerator;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.domain.BillingAccount;
import com.esofthead.mycollab.module.user.domain.BillingAccountWithOwners;
import com.esofthead.mycollab.module.user.domain.BillingPlan;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.BillingAccountService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class BillingSendingNotificationJobs extends QuartzJobBean {

	private static final Integer DATE_REMIND_FOR_FREEPLAN_1ST = 25;
	private static final Integer DATE_REMIND_FOR_FREEPLAN_2ND = 30;
	private static final Integer DATE_NOTIFY_EXPIRE = 32;
	private static final Integer NUM_DAY_FREE_TRIAL = 30;

	private static Logger log = LoggerFactory
			.getLogger(BillingSendingNotificationJobs.class);

	@Autowired
	private BillingService billingService;

	@Autowired
	private BillingAccountService billingAccountService;

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		List<BillingAccountWithOwners> trialAccountsWithOwners = billingService
				.getTrialAccountsWithOwners();
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.add(Calendar.DATE, (-1) * DATE_REMIND_FOR_FREEPLAN_1ST);
		Date dateRemind1st = cal.getTime();

		cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.add(Calendar.DATE, (-1) * DATE_REMIND_FOR_FREEPLAN_2ND);
		Date dateRemind2nd = cal.getTime();

		cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.add(Calendar.DATE, (-1) * DATE_NOTIFY_EXPIRE);
		Date dateExpire = cal.getTime();

		if (trialAccountsWithOwners != null
				&& trialAccountsWithOwners.size() > 0) {
			for (BillingAccountWithOwners account : trialAccountsWithOwners) {
				log.debug("Check whether account exceed 25 days to remind user upgrade account");
				if (df.format(account.getCreatedtime()).equals(
						df.format(dateRemind1st))) {
					AccountBillingSendEmail notificationFactory = new AccountBillingSendEmail(
							account, DATE_REMIND_FOR_FREEPLAN_1ST);
					notificationFactory.sendingEmail();
				} else if (df.format(account.getCreatedtime()).equals(
						df.format(dateRemind2nd))) {
					log.debug("Check whether account exceed 30 days to inform him it is the end of day to upgrade account");
					AccountBillingSendEmail notificationFactory = new AccountBillingSendEmail(
							account, DATE_REMIND_FOR_FREEPLAN_2ND);
					notificationFactory.sendingEmail();
				} else if (df.format(account.getCreatedtime()).equals(
						df.format(dateExpire))) {
					log.debug("Check whether account exceed 32 days to convert to basic plan");
					BillingAccount billingAccount = billingAccountService
							.findByPrimaryKey(account.getId(), account.getId());
					BillingPlan freeBillingPlan = billingService
							.getFreeBillingPlan();
					billingAccount.setBillingplanid(freeBillingPlan.getId());
					billingAccountService.updateWithSession(billingAccount, "");
					AccountBillingSendEmail notificationFactory = new AccountBillingSendEmail(
							account, DATE_NOTIFY_EXPIRE);
					notificationFactory.sendingEmail();
				}
			}
		}
	}

	private static class AccountBillingSendEmail {
		private static final String remindAccountIsAboutEndTemplate = "templates/email/billing/remindAccountIsAboutExpiredNotification.mt";
		private static final String informAccountIsExpiredTemplate = "templates/email/billing/informAccountIsExpiredNotification.mt";
		private Integer afterDay;
		private BillingAccountWithOwners account;
		private ExtMailService extMailService;

		public AccountBillingSendEmail(BillingAccountWithOwners account,
				Integer afterDay) {
			this.afterDay = afterDay;
			this.account = account;
			this.extMailService = ApplicationContextUtil
					.getSpringBean(ExtMailService.class);
		}

		public void sendingEmail() {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			if (afterDay == BillingSendingNotificationJobs.DATE_REMIND_FOR_FREEPLAN_2ND
					|| afterDay == BillingSendingNotificationJobs.DATE_REMIND_FOR_FREEPLAN_1ST) {
				for (SimpleUser user : account.getOwners()) {
					log.info("Send mail after " + afterDay
							+ "days for username {} , mail {}",
							user.getUsername(), user.getEmail());
					TemplateGenerator templateGenerator = new TemplateGenerator(
							"Your trial is about to end",
							remindAccountIsAboutEndTemplate);
					templateGenerator.putVariable("account", account);

					String link = GenericLinkUtils
							.generateSiteUrlByAccountId(account.getId())
							+ GenericLinkUtils.URL_PREFIX_PARAM
							+ "account/billing";

					Calendar cal = Calendar.getInstance(TimeZone
							.getTimeZone("UTC"));
					cal.setTime(account.getCreatedtime());
					cal.add(Calendar.DATE, NUM_DAY_FREE_TRIAL);

					templateGenerator.putVariable("expireDay",
							df.format(cal.getTime()));
					templateGenerator.putVariable("userName",
							user.getUsername());
					templateGenerator.putVariable("link", link);
					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", Arrays.asList(new MailRecipientField(
									user.getEmail(), user.getDisplayName())),
							null, null, templateGenerator
									.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);
				}
			} else if (afterDay == BillingSendingNotificationJobs.DATE_NOTIFY_EXPIRE) {
				for (SimpleUser user : account.getOwners()) {
					log.info(
							"Send mail after 32 days for username {} , mail {}",
							user.getUsername(), user.getEmail());
					TemplateGenerator templateGenerator = new TemplateGenerator(
							"Your trial has ended",
							informAccountIsExpiredTemplate);
					templateGenerator.putVariable("account", account);
					templateGenerator.putVariable("userName",
							user.getUsername());
					String link = GenericLinkUtils
							.generateSiteUrlByAccountId(account.getId())
							+ GenericLinkUtils.URL_PREFIX_PARAM
							+ "account/billing";
					templateGenerator.putVariable("link", link);

					extMailService.sendHTMLMail("noreply@mycollab.com",
							"MyCollab", Arrays.asList(new MailRecipientField(
									user.getEmail(), user.getDisplayName())),
							null, null, templateGenerator
									.generateSubjectContent(),
							templateGenerator.generateBodyContent(), null);
				}
			}
		}
	}
}
