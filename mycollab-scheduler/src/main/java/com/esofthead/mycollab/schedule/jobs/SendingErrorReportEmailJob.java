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

import java.util.Arrays;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.esofthead.mycollab.common.dao.ReportBugIssueMapper;
import com.esofthead.mycollab.common.domain.MailRecipientField;
import com.esofthead.mycollab.common.domain.ReportBugIssueExample;
import com.esofthead.mycollab.common.domain.ReportBugIssueWithBLOBs;
import com.esofthead.mycollab.configuration.EmailConfiguration;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.module.mail.DefaultMailer;
import com.esofthead.mycollab.module.mail.IContentGenerator;
import com.esofthead.mycollab.module.mail.IMailer;
import com.esofthead.mycollab.module.mail.NullMailer;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class SendingErrorReportEmailJob extends QuartzJobBean {

	@Autowired
	private IContentGenerator contentGenerator;

	@Override
	protected void executeInternal(JobExecutionContext context) {
		ReportBugIssueMapper mapper = ApplicationContextUtil
				.getSpringBean(ReportBugIssueMapper.class);
		if (mapper != null) {
			List<ReportBugIssueWithBLOBs> listIssues = mapper
					.selectByExampleWithBLOBs(new ReportBugIssueExample());

			if (!listIssues.isEmpty()) {
				contentGenerator.putVariable("issueCol", listIssues);
				EmailConfiguration emailConfiguration = SiteConfiguration
						.getRelayEmailConfiguration();
				IMailer mailer;
				if (emailConfiguration.getHost().equals("")) {
					mailer = new NullMailer();
				} else {
					mailer = new DefaultMailer(emailConfiguration);
				}

				mailer.sendHTMLMail(
						"mail@mycollab.com",
						"Error Agent",
						Arrays.asList(new MailRecipientField(SiteConfiguration
								.getSendErrorEmail(), SiteConfiguration
								.getSendErrorEmail())),
						null,
						null,
						contentGenerator
								.generateSubjectContent("My Collab Error Report"),
						contentGenerator
								.generateBodyContent("templates/email/errorReport.mt"),
						null);

				// Remove all issues in table
				ReportBugIssueExample ex = new ReportBugIssueExample();
				ex.createCriteria().andIdGreaterThan(0);
				mapper.deleteByExample(ex);
			}

		}
	}

}
