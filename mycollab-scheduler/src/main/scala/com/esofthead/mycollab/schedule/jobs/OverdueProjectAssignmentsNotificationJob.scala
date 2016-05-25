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

import java.util
import java.util.{Date, Locale}

import com.esofthead.mycollab.common.domain.MailRecipientField
import com.esofthead.mycollab.common.{FontAwesomeUtils, NotificationType}
import com.esofthead.mycollab.configuration.{SiteConfiguration, StorageFactory}
import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.core.arguments.{NumberSearchField, RangeDateSearchField, SearchField, SetSearchField}
import com.esofthead.mycollab.core.utils.{BeanUtility, DateTimeUtils}
import com.esofthead.mycollab.html.DivLessFormatter
import com.esofthead.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.esofthead.mycollab.module.project.domain.criteria.ProjectGenericTaskSearchCriteria
import com.esofthead.mycollab.module.project.domain.{ProjectGenericTask, ProjectNotificationSetting}
import com.esofthead.mycollab.module.project.service.{ProjectGenericTaskService, ProjectMemberService, ProjectNotificationSettingService}
import com.esofthead.mycollab.module.project.{ProjectLinkGenerator, ProjectTypeConstants}
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.schedule.jobs.OverdueProjectAssignmentsNotificationJob.OverdueAssignmentFormatter
import com.hp.gagawa.java.elements.{A, Div, Img}
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
object OverdueProjectAssignmentsNotificationJob {
  private val LOG = LoggerFactory.getLogger(classOf[OverdueProjectAssignmentsNotificationJob])

  class OverdueAssignmentFormatter {
    def formatDate(date: Date): String = DateTimeUtils.formatDate(date, "yyyy-MM-dd")

    def formatLink(subdomain: String, assignment: ProjectGenericTask): String = {
      try {
        assignment.getType match {
          case ProjectTypeConstants.BUG => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.BUG)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateBugPreviewFullLink(SiteConfiguration.getSiteUrl(subdomain),
              assignment.getExtraTypeId, assignment.getProjectShortName)).appendText(assignment.getName)).write()
          case ProjectTypeConstants.TASK => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.TASK)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateTaskPreviewFullLink(SiteConfiguration.getSiteUrl(subdomain),
              assignment.getExtraTypeId, assignment.getProjectShortName)).appendText(assignment.getName)).write()
          case ProjectTypeConstants.RISK => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.RISK)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateRiskPreviewFullLink(SiteConfiguration.getSiteUrl(subdomain),
              assignment.getProjectId, assignment.getTypeId)).appendText(assignment.getName)).write()
          case ProjectTypeConstants.MILESTONE => new Div().appendText(FontAwesomeUtils.toHtml(ProjectTypeConstants.MILESTONE)).
            appendChild(DivLessFormatter.EMPTY_SPACE, new A(ProjectLinkGenerator.generateMilestonePreviewFullLink(SiteConfiguration.getSiteUrl(subdomain),
              assignment.getProjectId, assignment.getTypeId)).appendText(assignment.getName)).write()
          case typeVal => throw new MyCollabException("Do not support type " + typeVal)
        }
      } catch {
        case e: Exception => {
          LOG.error("Error in format assignment", BeanUtility.printBeanObj(assignment))
          SiteConfiguration.getSiteUrl(subdomain)
        }
      }
    }

    def formatAssignUser(subdomain: String, assignment: ProjectGenericTask): String = {
      return new Div().appendChild(new Img("", StorageFactory.getInstance().getAvatarPath(assignment.getAssignUserAvatarId, 16)),
        new A(AccountLinkGenerator.generatePreviewFullUserLink(subdomain, assignment.getAssignUser)).
          appendText(assignment.getAssignUserFullName)).write()
    }
  }

}

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class OverdueProjectAssignmentsNotificationJob extends GenericQuartzJobBean {

  @Autowired private var projectGenericTaskService: ProjectGenericTaskService = _

  @Autowired private var extMailService: ExtMailService = _

  @Autowired private var contentGenerator: IContentGenerator = _

  @Autowired private var projectMemberService: ProjectMemberService = _

  @Autowired private var projectNotificationService: ProjectNotificationSettingService = _

  @throws(classOf[JobExecutionException])
  override protected def executeJob(context: JobExecutionContext): Unit = {
    val searchCriteria = new ProjectGenericTaskSearchCriteria
    searchCriteria.setSaccountid(null)
    val now = new LocalDate()
    val past = now.minusDays(10000)
    val rangeDate = new RangeDateSearchField(past.toDate, now.toDate)
    searchCriteria.setDateInRange(rangeDate)
    searchCriteria.setIsOpenned(new SearchField())
    import scala.collection.JavaConverters._
    val accountIds = projectGenericTaskService.getAccountsHasOverdueAssignments(searchCriteria).asScala.toList
    if (accountIds != null) {
      for (accountId <- accountIds) {
        searchCriteria.setSaccountid(new NumberSearchField(accountId.get("id").asInstanceOf[Integer]))
        import scala.collection.JavaConverters._
        val projectIds = projectGenericTaskService.getProjectsHasOverdueAssignments(searchCriteria).asScala.toList
        for (projectId <- projectIds) {
          searchCriteria.setProjectIds(new SetSearchField[Integer](projectId))
          val assignments = projectGenericTaskService.findAbsoluteListByCriteria(searchCriteria, 0, Integer.MAX_VALUE).asScala.toList
          if (assignments.nonEmpty) {
            val projectName = assignments(0).asInstanceOf[ProjectGenericTask].getProjectName
            val notifiers = getNotifiersOfProject(projectId, accountId.get("id").asInstanceOf[Integer])
            contentGenerator.putVariable("assignments", assignments)
            contentGenerator.putVariable("subdomain", accountId.get("subdomain"))
            contentGenerator.putVariable("formatter", new OverdueAssignmentFormatter)
            for (notifier <- notifiers) {
              val userMail = new MailRecipientField(notifier.getEmail, notifier.getDisplayName)
              val recipients = util.Arrays.asList(userMail)
              val content = contentGenerator.parseFile("templates/email/project/overdueAssignmentsNotifier.mt", Locale.US)
              extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients,
                null, null,
                contentGenerator.parseString("[" + projectName + "] Overdue assignments"), content, null)
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
    return notifyUsers
  }
}

