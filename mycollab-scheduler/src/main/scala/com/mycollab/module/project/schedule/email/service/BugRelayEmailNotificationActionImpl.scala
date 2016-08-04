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

import com.hp.gagawa.java.elements.{Span, Text}
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.{MonitorTypeConstants, NotificationType}
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils._
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.domain._
import com.mycollab.module.project.i18n.{BugI18nEnum, OptionI18nEnum}
import com.mycollab.module.project.service.{MilestoneService, ProjectNotificationSettingService}
import com.mycollab.module.project.{ProjectLinkGenerator, ProjectResources, ProjectTypeConstants}
import com.mycollab.module.tracker.domain.{BugWithBLOBs, SimpleBug}
import com.mycollab.module.tracker.service.BugService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.format.{DateFieldFormat, FieldFormat, I18nFieldFormat}
import com.mycollab.schedule.email.project.BugRelayEmailNotificationAction
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.mycollab.spring.AppContextUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
  * @author MyCollab Ltd.
  * @since 4.6.0
  */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class BugRelayEmailNotificationActionImpl extends SendMailToFollowersAction[SimpleBug] with BugRelayEmailNotificationAction {
  private val LOG = LoggerFactory.getLogger(classOf[BugRelayEmailNotificationActionImpl])
  
  @Autowired var bugService: BugService = _
  @Autowired var projectNotificationService: ProjectNotificationSettingService = _
  
  private val mapper = new BugFieldNameMapper
  
  protected def buildExtraTemplateVariables(context: MailContext[SimpleBug]) {
    val emailNotification = context.getEmailNotification
    
    val summary = "#" + bean.getBugkey + " - " + bean.getSummary
    val summaryLink = ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, bean.getBugkey, bean.getProjectShortName)
    
    val avatarId = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)
    
    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => BugI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => BugI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => BugI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }
    
    contentGenerator.putVariable("projectName", bean.getProjectname)
    contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean.getProjectid))
    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }
  
  protected def getBeanInContext(notification: ProjectRelayEmailNotification): SimpleBug =
    bugService.findById(notification.getTypeid.toInt, notification.getSaccountid)
  
  protected def getItemName: String = StringUtils.trim(bean.getSummary, 100)
  
  protected def getCreateSubject(context: MailContext[SimpleBug]): String = context.getMessage(BugI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
    bean.getProjectname, context.getChangeByUserFullName, getItemName)
  
  protected def getUpdateSubject(context: MailContext[SimpleBug]): String = context.getMessage(BugI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
    bean.getProjectname, context.getChangeByUserFullName, getItemName)
  
  protected def getCommentSubject(context: MailContext[SimpleBug]): String = context.getMessage(BugI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
    bean.getProjectname, context.getChangeByUserFullName, getItemName)
  
  protected def getItemFieldMapper: ItemFieldMapper = mapper
  
  protected def getListNotifyUsersWithFilter(notification: ProjectRelayEmailNotification): Set[SimpleUser] = {
    import scala.collection.JavaConverters._
    val notificationSettings = projectNotificationService.findNotifications(notification.getProjectId, notification.getSaccountid).asScala.toList
    var notifyUsers = notification.getNotifyUsers.asScala.toSet
    
    for (notificationSetting <- notificationSettings) {
      if (NotificationType.None.name == notificationSetting.getLevel) {
        notifyUsers = notifyUsers.filter(notifyUser => !(notifyUser.getUsername == notificationSetting.getUsername))
      }
      else if (NotificationType.Minimal.name == notificationSetting.getLevel) {
        val findResult = notifyUsers.find(notifyUser => notifyUser.getUsername == notificationSetting.getUsername)
        findResult match {
          case None =>
            val bug = bugService.findById(notification.getTypeid.toInt, notification.getSaccountid)
            if (notificationSetting.getUsername == bug.getAssignuser) {
              val prjMember = projectMemberService.getActiveUserOfProject(notificationSetting.getUsername,
                notificationSetting.getProjectid, notificationSetting.getSaccountid)
              if (prjMember != null) {
                notifyUsers += prjMember
              }
            }
          case Some(user) =>
        }
      }
      else if (NotificationType.Full.name == notificationSetting.getLevel) {
        val prjMember = projectMemberService.getActiveUserOfProject(notificationSetting.getUsername,
          notificationSetting.getProjectid, notificationSetting.getSaccountid)
        if (prjMember != null) {
          notifyUsers += prjMember
        }
      }
    }
    
    notifyUsers
  }
  
  class BugFieldNameMapper extends ItemFieldMapper {
    put(BugWithBLOBs.Field.summary, BugI18nEnum.FORM_SUMMARY, isColSpan = true)
    put(BugWithBLOBs.Field.environment, BugI18nEnum.FORM_ENVIRONMENT, isColSpan = true)
    put(BugWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    put(BugWithBLOBs.Field.assignuser, new AssigneeFieldFormat(BugWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(BugWithBLOBs.Field.milestoneid, new MilestoneFieldFormat(BugWithBLOBs.Field.milestoneid.name, BugI18nEnum.FORM_PHASE))
    put(BugWithBLOBs.Field.status, new I18nFieldFormat(BugWithBLOBs.Field.status.name, GenericI18Enum.FORM_STATUS, classOf[OptionI18nEnum.BugStatus]))
    put(BugWithBLOBs.Field.resolution, new I18nFieldFormat(BugWithBLOBs.Field.resolution.name, BugI18nEnum.FORM_RESOLUTION, classOf[OptionI18nEnum.BugResolution]))
    put(BugWithBLOBs.Field.severity, new I18nFieldFormat(BugWithBLOBs.Field.severity.name, BugI18nEnum.FORM_SEVERITY, classOf[OptionI18nEnum.BugSeverity]))
    put(BugWithBLOBs.Field.priority, new I18nFieldFormat(BugWithBLOBs.Field.priority.name, BugI18nEnum.FORM_PRIORITY, classOf[OptionI18nEnum.BugPriority]))
    put(BugWithBLOBs.Field.duedate, new DateFieldFormat(BugWithBLOBs.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
    put(BugWithBLOBs.Field.logby, new LogUserFieldFormat(BugWithBLOBs.Field.logby.name, BugI18nEnum.FORM_LOG_BY))
  }
  
  class MilestoneFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    
    def formatField(context: MailContext[_]): String = {
      val bug = context.getWrappedBean.asInstanceOf[SimpleBug]
      if (bug.getMilestoneid == null || bug.getMilestoneName == null) {
        new Span().write
      } else {
        val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
        val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
          bug.getProjectid, bug.getMilestoneid)
        val link = newA(milestoneLink, bug.getMilestoneName)
        newLink(img, link).write
      }
    }
    
    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        new Span().write
      } else {
        try {
          val milestoneId = value.toInt
          val milestoneService = AppContextUtil.getSpringBean(classOf[MilestoneService])
          val milestone = milestoneService.findById(milestoneId, context.getUser.getAccountId)
          if (milestone != null) {
            val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
            val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
              milestone.getProjectid, milestone.getId)
            val link = newA(milestoneLink, milestone.getName)
            return newLink(img, link).write
          }
        }
        catch {
          case e: Exception => LOG.error("Error", e)
        }
        value
      }
    }
  }
  
  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    
    def formatField(context: MailContext[_]): String = {
      val bug = context.getWrappedBean.asInstanceOf[SimpleBug]
      if (bug.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(bug.getAssignUserAvatarId, 16)
        val img = newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(bug.getSaccountid), bug.getAssignuser)
        val link = newA(userLink, bug.getAssignuserFullName)
        newLink(img, link).write
      }
      else {
        new Span().write
      }
    }
    
    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        new Span().write
      } else {
        val userService = AppContextUtil.getSpringBean(classOf[UserService])
        val user = userService.findUserByUserNameInAccount(value, context.getUser.getAccountId)
        if (user != null) {
          val userAvatarLink = MailUtils.getAvatarLink(user.getAvatarid, 16)
          val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
          val img = newImg("avatar", userAvatarLink)
          val link = newA(userLink, user.getDisplayName)
          newLink(img, link).write
        } else
          value
      }
    }
  }
  
  class LogUserFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    
    def formatField(context: MailContext[_]): String = {
      val bug = context.getWrappedBean.asInstanceOf[SimpleBug]
      if (bug.getLogby != null) {
        val userAvatarLink = MailUtils.getAvatarLink(bug.getLoguserAvatarId, 16)
        val img = newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(bug.getSaccountid), bug.getLogby)
        val link = newA(userLink, bug.getLoguserFullName)
        newLink(img, link).write
      }
      else
        new Span().write
    }
    
    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value))
        return new Span().write
      
      val userService = AppContextUtil.getSpringBean(classOf[UserService])
      val user = userService.findUserByUserNameInAccount(value, context.getUser.getAccountId)
      if (user != null) {
        val userAvatarLink = MailUtils.getAvatarLink(user.getAvatarid, 16)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
        val img = newImg("avatar", userAvatarLink)
        val link = newA(userLink, user.getDisplayName)
        newLink(img, link).write
      } else
        value
    }
  }
  
}