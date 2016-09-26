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
package com.mycollab.module.project.schedule.email.service

import java.util
import java.util.{Date, Locale}

import com.hp.gagawa.java.elements.{A, Div, Img}
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.common.{FontAwesomeUtils, NotificationType}
import com.mycollab.configuration.{SiteConfiguration, StorageFactory}
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.{BeanUtility, DateTimeUtils}
import com.mycollab.db.arguments.{NumberSearchField, RangeDateSearchField, SearchField, SetSearchField}
import com.mycollab.html.DivLessFormatter
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.module.project.domain.{ProjectNotificationSetting, ProjectTicket}
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum
import com.mycollab.module.project.schedule.email.service.OverdueProjectTicketsNotificationJob.OverdueAssignmentFormatter
import com.mycollab.module.project.service.{ProjectTicketService, ProjectMemberService, ProjectNotificationSettingService}
import com.mycollab.module.project.{ProjectLinkGenerator, ProjectTypeConstants}
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.schedule.jobs.GenericQuartzJobBean
import org.joda.time.LocalDate
import org.quartz.{JobExecutionContext, JobExecutionException}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd
  * @since 5.2.6
  */
object OverdueProjectTicketsNotificationJob {
  private val LOG = LoggerFactory.getLogger(classOf[OverdueProjectTicketsNotificationJob])

  class OverdueAssignmentFormatter {
    def formatDate(date: Date): String = DateTimeUtils.formatDate(date, "yyyy-MM-dd", Locale.US)

    def formatLink(subDomain: String, assignment: ProjectTicket): String = {
      try {
        assignment.getType match {
          case ProjectTypeConstants.BUG => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.BUG)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateBugPreviewFullLink(SiteConfiguration.getSiteUrl(subDomain),
              assignment.getExtraTypeId, assignment.getProjectShortName)).appendText(assignment.getName)).write()
          case ProjectTypeConstants.TASK => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.TASK)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateTaskPreviewFullLink(SiteConfiguration.getSiteUrl(subDomain),
              assignment.getExtraTypeId, assignment.getProjectShortName)).appendText(assignment.getName)).write()
          case ProjectTypeConstants.RISK => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.RISK)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateRiskPreviewFullLink(SiteConfiguration.getSiteUrl(subDomain),
              assignment.getProjectId, assignment.getTypeId)).appendText(assignment.getName)).write()
          case ProjectTypeConstants.MILESTONE => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.MILESTONE)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateMilestonePreviewFullLink(SiteConfiguration.getSiteUrl(subDomain),
              assignment.getProjectId, assignment.getTypeId)).appendText(assignment.getName)).write()
          case typeVal => throw new MyCollabException("Do not support type " + typeVal)
        }
      } catch {
        case e: Exception =>
          LOG.error("Error in format assignment", BeanUtility.printBeanObj(assignment))
          SiteConfiguration.getSiteUrl(subDomain)
      }
    }

    def formatAssignUser(subDomain: String, assignment: ProjectTicket): String = {
      new Div().appendChild(new Img("", StorageFactory.getAvatarPath(assignment.getAssignUserAvatarId, 16)),
        new A(AccountLinkGenerator.generatePreviewFullUserLink(subDomain, assignment.getAssignUser)).
          appendText(assignment.getAssignUserFullName)).write()
    }
  }

}

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class OverdueProjectTicketsNotificationJob extends GenericQuartzJobBean {

  @Autowired private val projectAssignmentService: ProjectTicketService = null

  @Autowired private val extMailService: ExtMailService = null

  @Autowired private val contentGenerator: IContentGenerator = null

  @Autowired private val projectMemberService: ProjectMemberService = null

  @Autowired private val projectNotificationService: ProjectNotificationSettingService = null

  @throws(classOf[JobExecutionException])
  override protected def executeJob(context: JobExecutionContext): Unit = {
    val searchCriteria = new ProjectTicketSearchCriteria
    searchCriteria.setSaccountid(null)
    val now = new LocalDate()
    val past = now.minusDays(10000)
    val rangeDate = new RangeDateSearchField(past.toDate, now.toDate)
    searchCriteria.setDateInRange(rangeDate)
    searchCriteria.setIsOpenned(new SearchField())
    import scala.collection.JavaConverters._
    val accounts = projectAssignmentService.getAccountsHasOverdueAssignments(searchCriteria).asScala.toList
    if (accounts != null) {
      for (account <- accounts) {
        searchCriteria.setSaccountid(new NumberSearchField(account.getId))
        import scala.collection.JavaConverters._
        val projectIds = projectAssignmentService.getProjectsHasOverdueAssignments(searchCriteria).asScala.toList
        for (projectId <- projectIds) {
          searchCriteria.setProjectIds(new SetSearchField[Integer](projectId))
          val siteUrl = SiteConfiguration.getSiteUrl(account.getSubdomain)
          contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, projectId))
          val assignments = projectAssignmentService.findAbsoluteListByCriteria(searchCriteria, 0, Integer.MAX_VALUE).asScala.toList
          if (assignments.nonEmpty) {
            val projectName = assignments.head.asInstanceOf[ProjectTicket].getProjectName
            val notifiers = getNotifiersOfProject(projectId, account.getId)
            contentGenerator.putVariable("assignments", assignments)
            contentGenerator.putVariable("subDomain", account.getSubdomain)
            contentGenerator.putVariable("formatter", new OverdueAssignmentFormatter)
            for (notifier <- notifiers) {
              val userMail = new MailRecipientField(notifier.getEmail, notifier.getDisplayName)
              val recipients = util.Arrays.asList(userMail)
              val userLocale = LocalizationHelper.getLocaleInstance(notifier.getLanguage)
              contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(userLocale, MailI18nEnum.Copyright,
                DateTimeUtils.getCurrentYear))
              val projectSettingUrl = new A(ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, projectId)).
                appendText(LocalizationHelper.getMessage(userLocale, MailI18nEnum.Project_Notification_Setting)).write()
              val projectFooter = LocalizationHelper.getMessage(userLocale, MailI18nEnum.Project_Footer, projectName, projectSettingUrl)
              contentGenerator.putVariable("Project_Footer", projectFooter)
              val content = contentGenerator.parseFile("mailProjectOverdueAssignmentsNotifier.ftl", Locale.US)
              val overdueAssignments = LocalizationHelper.getMessage(userLocale, ProjectCommonI18nEnum.OPT_OVERDUE_ASSIGNMENTS_VALUE,
                String.valueOf(assignments.length))
              contentGenerator.putVariable("overdueAssignments", overdueAssignments)
              extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients,
                "[%s] %s".format(projectName, overdueAssignments), content)
            }
          }
        }
      }
    }
  }
  
  

  private def getNotifiersOfProject(projectId: Integer, accountId: Integer): Set[SimpleUser] = {
    import scala.collection.JavaConverters._
    var notifyUsers: Set[SimpleUser] = projectMemberService.getActiveUsersInProject(projectId, accountId).asScala.toSet
    val notificationSettings: List[ProjectNotificationSetting] = projectNotificationService.findNotifications(projectId, accountId).asScala.toList
    if (notificationSettings.nonEmpty) {
      for (setting <- notificationSettings) {
        if ((NotificationType.None.name == setting.getLevel) || (NotificationType.Minimal.name == setting.getLevel)) {
          notifyUsers = notifyUsers.filter(notifyUser => !(notifyUser.getUsername == setting.getUsername))
        }
      }
    }
    notifyUsers
  }
}

