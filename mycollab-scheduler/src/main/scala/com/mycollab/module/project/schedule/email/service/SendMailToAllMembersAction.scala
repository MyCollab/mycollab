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

import com.hp.gagawa.java.elements.A
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.project.domain.{ProjectRelayEmailNotification, SimpleProjectMember}
import com.mycollab.module.project.service.{ProjectMemberService, ProjectNotificationSettingService, ProjectService}
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.mycollab.common.NotificationType
import com.mycollab.common.domain.criteria.CommentSearchCriteria
import com.mycollab.common.domain.{MailRecipientField, SimpleRelayEmailNotification}
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.common.service.{AuditLogService, CommentService}
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.db.arguments.{BasicSearchRequest, StringSearchField}
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.schedule.email.format.WebItem
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import org.springframework.beans.factory.annotation.Autowired

/**
  * @author MyCollab Ltd.
  * @since 4.6.0
  */
abstract class SendMailToAllMembersAction[B] extends SendingRelayEmailNotificationAction {
  @Autowired var extMailService: ExtMailService = _
  @Autowired var projectService: ProjectService = _
  @Autowired var projectMemberService: ProjectMemberService = _
  @Autowired var projectNotificationService: ProjectNotificationSettingService = _
  @Autowired val commentService: CommentService = null
  @Autowired var auditLogService: AuditLogService = _
  @Autowired protected var contentGenerator: IContentGenerator = _

  protected var bean: B = _
  protected var projectMember: SimpleProjectMember = _

  protected var siteUrl: String = _
  private var projectId: Integer = _

  private def getNotifyUsers(notification: ProjectRelayEmailNotification): Set[SimpleUser] = {
    import scala.collection.JavaConverters._
    var notifyUsers = projectMemberService.getActiveUsersInProject(notification.getProjectId,
      notification.getSaccountid).asScala.toSet
    val notificationSettings = projectNotificationService.findNotifications(notification.getProjectId,
      notification.getSaccountid).asScala.toList
    if (notificationSettings.nonEmpty) {
      for (setting <- notificationSettings) {
        if ((NotificationType.None.name == setting.getLevel) || (NotificationType.Minimal.name == setting.getLevel)) {
          notifyUsers = notifyUsers.filter(notifyUser => !(notifyUser.getUsername == setting.getUsername))
        }
      }
    }
    notifyUsers
  }

  def sendNotificationForCreateAction(notification: SimpleRelayEmailNotification) {
    val projectRelayEmailNotification = notification.asInstanceOf[ProjectRelayEmailNotification]
    val notifiers = getNotifyUsers(projectRelayEmailNotification)
    if (notifiers != null && notifiers.nonEmpty) {
      onInitAction(projectRelayEmailNotification)
      bean = getBeanInContext(projectRelayEmailNotification)
      if (bean != null) {
        contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.getSaccountid, notification.getAccountLogo))
        import scala.collection.JavaConversions._
        for (user <- notifiers) {
          val context = new MailContext[B](notification, user, siteUrl)
          context.setWrappedBean(bean)
          buildExtraTemplateVariables(context)
          contentGenerator.putVariable("context", context)
          contentGenerator.putVariable("mapper", getItemFieldMapper)
          contentGenerator.putVariable("userName", user.getDisplayName)
          contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
            DateTimeUtils.getCurrentYear))
          contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
          val userMail = new MailRecipientField(user.getEmail, user.getUsername)
          val recipients = List[MailRecipientField](userMail)
          extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients,
            getCreateSubject(context), contentGenerator.parseFile("mailProjectItemCreatedNotifier.ftl", context.getLocale))
        }
      }
    }
  }

  def sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification) {
    val projectRelayEmailNotification = notification.asInstanceOf[ProjectRelayEmailNotification]
    val notifiers = getNotifyUsers(projectRelayEmailNotification)
    if (notifiers != null && notifiers.nonEmpty) {
      onInitAction(projectRelayEmailNotification)
      bean = getBeanInContext(projectRelayEmailNotification)
      if (bean != null) {
        val auditLog = auditLogService.findLastestLog(notification.getTypeid.toInt, notification.getSaccountid)
        contentGenerator.putVariable("historyLog", auditLog)
        contentGenerator.putVariable("mapper", getItemFieldMapper)
        contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.getSaccountid, notification.getAccountLogo))
        
        val searchCriteria = new CommentSearchCriteria
        searchCriteria.setType(StringSearchField.and(notification.getType))
        searchCriteria.setTypeId(StringSearchField.and(notification.getTypeid))
        searchCriteria.setSaccountid(null)
        val comments = commentService.findPageableListByCriteria(new BasicSearchRequest[CommentSearchCriteria](searchCriteria, 0, 5))
        contentGenerator.putVariable("lastComments", comments)
        
        import scala.collection.JavaConversions._
        for (user <- notifiers) {
          val context = new MailContext[B](notification, user, siteUrl)
          if (comments.size() > 0) {
            contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Last_Comments_Value, "" + comments.size()))
          }
          contentGenerator.putVariable("Changes", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Changes))
          contentGenerator.putVariable("Field", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Field))
          contentGenerator.putVariable("Old_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Old_Value))
          contentGenerator.putVariable("New_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.New_Value))
          contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
            DateTimeUtils.getCurrentYear))
          contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
          contentGenerator.putVariable("context", context)
          context.setWrappedBean(bean)
          buildExtraTemplateVariables(context)
          val userMail = new MailRecipientField(user.getEmail, user.getUsername)
          val recipients = List[MailRecipientField](userMail)
          extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients,
            getUpdateSubject(context), contentGenerator.parseFile("mailProjectItemUpdatedNotifier.ftl", context.getLocale))
        }
      }
    }
  }

  def sendNotificationForCommentAction(notification: SimpleRelayEmailNotification) {
    val projectRelayEmailNotification = notification.asInstanceOf[ProjectRelayEmailNotification]
    val notifiers = getNotifyUsers(projectRelayEmailNotification)
    if (notifiers != null && notifiers.nonEmpty) {
      onInitAction(projectRelayEmailNotification)
      bean = getBeanInContext(projectRelayEmailNotification)
      if (bean != null) {
        val searchCriteria = new CommentSearchCriteria
        searchCriteria.setType(StringSearchField.and(notification.getType))
        searchCriteria.setTypeId(StringSearchField.and(notification.getTypeid))
        searchCriteria.setSaccountid(null)
        val comments = commentService.findPageableListByCriteria(new BasicSearchRequest[CommentSearchCriteria](searchCriteria, 0, 5))
        contentGenerator.putVariable("lastComments", comments)
        contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.getSaccountid, notification.getAccountLogo))
        
        import scala.collection.JavaConversions._
        for (user <- notifiers) {
          val context = new MailContext[B](notification, user, siteUrl)
          buildExtraTemplateVariables(context)
          contentGenerator.putVariable("comment", context.getEmailNotification)
          contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Last_Comments_Value, "" + comments.size()))
          contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
            DateTimeUtils.getCurrentYear))
          contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
          val userMail = new MailRecipientField(user.getEmail, user.getUsername)
          val recipients = List[MailRecipientField](userMail)
          extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients,
            getCommentSubject(context), contentGenerator.parseFile("mailProjectItemCommentNotifier.ftl", context.getLocale))
        }
      }
    }
  }

  private def onInitAction(notification: ProjectRelayEmailNotification): Unit = {
    projectId = notification.getProjectId
    siteUrl = MailUtils.getSiteUrl(notification.getSaccountid)
    val relatedProject = projectService.findById(notification.getProjectId, notification.getSaccountid)
    val projectHyperLink = new WebItem(relatedProject.getName, ProjectLinkGenerator.generateProjectFullLink(siteUrl, relatedProject.getId))
    contentGenerator.putVariable("projectHyperLink", projectHyperLink)
    projectMember = projectMemberService.findMemberByUsername(notification.getChangeby, notification.getProjectId,
      notification.getSaccountid)
  }

  protected def getBeanInContext(notification: ProjectRelayEmailNotification): B

  protected def buildExtraTemplateVariables(context: MailContext[B])
  
  private def getProjectFooter(context: MailContext[B]):String = LocalizationHelper.getMessage(context.locale,
    MailI18nEnum.Project_Footer, getProjectName, getProjectNotificationSettingLink(context))
  
  private def getProjectNotificationSettingLink(context: MailContext[B]): String = {
    new A(ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, projectId)).
      appendText(LocalizationHelper.getMessage(context.locale, MailI18nEnum.Project_Notification_Setting)).write()
  }

  protected def getItemName: String
  
  protected def getProjectName: String

  protected def getCreateSubject(context: MailContext[B]): String

  protected def getUpdateSubject(context: MailContext[B]): String

  protected def getCommentSubject(context: MailContext[B]): String

  protected def getItemFieldMapper: ItemFieldMapper
}
