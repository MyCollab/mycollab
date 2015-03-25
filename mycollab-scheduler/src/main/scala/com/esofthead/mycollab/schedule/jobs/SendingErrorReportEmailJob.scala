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
package com.esofthead.mycollab.schedule.jobs

import java.util.Arrays

import com.esofthead.mycollab.common.dao.ReportBugIssueMapper
import com.esofthead.mycollab.common.domain.{MailRecipientField, ReportBugIssueExample, ReportBugIssueWithBLOBs}
import com.esofthead.mycollab.configuration.{EmailConfiguration, SiteConfiguration}
import com.esofthead.mycollab.core.MyCollabVersion
import com.esofthead.mycollab.module.mail.{DefaultMailer, IContentGenerator, IMailer, NullMailer}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.quartz.QuartzJobBean

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
class SendingErrorReportEmailJob extends QuartzJobBean {
  @Autowired var contentGenerator: IContentGenerator = _

  protected def executeInternal(context: JobExecutionContext) {
    val mapper: ReportBugIssueMapper = ApplicationContextUtil.getSpringBean(classOf[ReportBugIssueMapper])
    if (mapper != null) {
      import scala.collection.JavaConverters._
      val listIssues: List[ReportBugIssueWithBLOBs] = mapper.selectByExampleWithBLOBs(new ReportBugIssueExample)
        .asScala.toList
      if (listIssues.nonEmpty) {
        contentGenerator.putVariable("issueCol", listIssues)
        val emailConfiguration: EmailConfiguration = SiteConfiguration.getRelayEmailConfiguration
        val mailer: IMailer = if (emailConfiguration.getHost == "") new NullMailer else new DefaultMailer(emailConfiguration)
        try {
          mailer.sendHTMLMail("mail@mycollab.com", "Error Agent", Arrays.asList(new MailRecipientField(SiteConfiguration.getSendErrorEmail, SiteConfiguration.getSendErrorEmail)), null, null, contentGenerator.generateSubjectContent("My Collab Error Report " + MyCollabVersion.getVersion), contentGenerator.generateBodyContent("templates/email/errorReport.mt"), null)
        } finally {
          val ex: ReportBugIssueExample = new ReportBugIssueExample
          ex.createCriteria.andIdGreaterThan(0)
          mapper.deleteByExample(ex)
        }
      }
    }
  }
}