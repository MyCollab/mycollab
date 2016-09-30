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
import com.mycollab.html.{FormatUtils, LinkUtils}
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.domain._
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority
import com.mycollab.module.project.i18n.{MilestoneI18nEnum, OptionI18nEnum, TaskI18nEnum}
import com.mycollab.module.project.service._
import com.mycollab.module.project.{ProjectLinkGenerator, ProjectResources, ProjectTypeConstants}
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.format.{DateFieldFormat, FieldFormat, I18nFieldFormat}
import com.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction
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
class ProjectTaskRelayEmailNotificationActionImpl extends SendMailToFollowersAction[SimpleTask] with ProjectTaskRelayEmailNotificationAction {
  private val LOG = LoggerFactory.getLogger(classOf[ProjectTaskRelayEmailNotificationActionImpl])

  @Autowired var projectTaskService: ProjectTaskService = _

  @Autowired var projectNotificationService: ProjectNotificationSettingService = _

  private val mapper = new TaskFieldNameMapper

  protected def buildExtraTemplateVariables(context: MailContext[SimpleTask]) {
    val emailNotification = context.getEmailNotification

    val summary = "#" + bean.getTaskkey + " - " + bean.getName
    val summaryLink = ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, bean.getTaskkey, bean.getProjectShortname)

    val avatarId = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => TaskI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => TaskI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => TaskI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }
  
    contentGenerator.putVariable("projectName", bean.getProjectName)
    contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean.getProjectid))
    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("name", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  protected def getBeanInContext(notification: ProjectRelayEmailNotification): SimpleTask =
    projectTaskService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  protected def getItemName: String = StringUtils.trim(bean.getName, 100)
  
  override protected def getProjectName: String = bean.getProjectName
  
  protected def getCreateSubject(context: MailContext[SimpleTask]): String = context.getMessage(TaskI18nEnum
    .MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getUpdateSubject(context: MailContext[SimpleTask]): String = context.getMessage(TaskI18nEnum
    .MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getCommentSubject(context: MailContext[SimpleTask]): String = context.getMessage(TaskI18nEnum
    .MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getItemFieldMapper: ItemFieldMapper = mapper

  protected def getListNotifyUsersWithFilter(notification: ProjectRelayEmailNotification): Set[SimpleUser] = {
    import scala.collection.JavaConverters._
    val notificationSettings = projectNotificationService.findNotifications(notification.getProjectId, notification.getSaccountid).asScala.toList
    var notifyUsers = notification.getNotifyUsers.asScala.toSet

    if (notificationSettings != null && notificationSettings.size > 0) {
      for (notificationSetting <- notificationSettings) {
        if (NotificationType.None.name == notificationSetting.getLevel) {
          notifyUsers = notifyUsers.filter(notifyUser => !(notifyUser.getUsername == notificationSetting.getUsername))
        }
        else if (NotificationType.Minimal.name == notificationSetting.getLevel) {
          val findResult = notifyUsers.find(notifyUser => notifyUser.getUsername == notificationSetting.getUsername);
          findResult match {
            case None => {
              val task = projectTaskService.findById(notification.getTypeid.toInt, notification.getSaccountid)
              if (notificationSetting.getUsername == task.getAssignuser) {
                val prjMember: SimpleUser = projectMemberService.getActiveUserOfProject(notificationSetting.getUsername,
                  notificationSetting.getProjectid, notificationSetting.getSaccountid)
                if (prjMember != null) {
                  notifyUsers += prjMember
                }
              }
            }
            case Some(user) => {}
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
    }

    notifyUsers
  }

  class TaskFieldNameMapper extends ItemFieldMapper {
    put(Task.Field.name, GenericI18Enum.FORM_NAME, isColSpan = true)
    put(Task.Field.startdate, new DateFieldFormat(Task.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
    put(Task.Field.enddate, new DateFieldFormat(Task.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
    put(Task.Field.duedate, new DateFieldFormat(Task.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
    put(Task.Field.percentagecomplete, TaskI18nEnum.FORM_PERCENTAGE_COMPLETE)
    put(Task.Field.priority, new I18nFieldFormat(Task.Field.priority.name, GenericI18Enum.FORM_PRIORITY, classOf[Priority]))
    put(Task.Field.assignuser, new AssigneeFieldFormat(Task.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(Task.Field.isestimated, TaskI18nEnum.FORM_IS_ESTIMATED)
    put(Task.Field.remainestimate, TaskI18nEnum.FORM_REMAIN_ESTIMATE)
    put(Task.Field.milestoneid, new MilestoneFieldFormat(Task.Field.milestoneid.name, MilestoneI18nEnum.SINGLE))
    put(Task.Field.parenttaskid, new TaskFieldFormat(Task.Field.parenttaskid.name, TaskI18nEnum.FORM_PARENT_TASK))
    put(Task.Field.description, GenericI18Enum.FORM_DESCRIPTION)
    put(Task.Field.status, GenericI18Enum.FORM_STATUS)
  }

  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val task = context.getWrappedBean.asInstanceOf[SimpleTask]
      if (task.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(task.getAssignUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(task.getSaccountid), task.getAssignuser)
        val link = FormatUtils.newA(userLink, task.getAssignUserFullName)
        FormatUtils.newLink(img, link).write
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
          val img = FormatUtils.newImg("avatar", userAvatarLink)
          val link = FormatUtils.newA(userLink, user.getDisplayName)
          FormatUtils.newLink(img, link).write
        } else
          value
      }
    }
  }

  class TaskFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val task = context.getWrappedBean.asInstanceOf[SimpleTask]
      if (task.getParenttaskid != null) {
        val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.TASK))
        val parentTaskLink = ProjectLinkGenerator.generateTaskPreviewFullLink(context.siteUrl, task.getParentTaskKey,
          task.getProjectShortname)
        val link = FormatUtils.newA(parentTaskLink, task.getName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        return new Span().write
      }
      try {
        val taskId = value.toInt
        val taskService = AppContextUtil.getSpringBean(classOf[ProjectTaskService])
        val task = taskService.findById(taskId, context.getUser.getAccountId)
        if (task != null) {
          val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.TASK));
          val taskListLink = ProjectLinkGenerator.generateTaskPreviewFullLink(context.siteUrl, task.getTaskkey, task.getProjectShortname)
          val link = FormatUtils.newA(taskListLink, task.getName)
          return FormatUtils.newLink(img, link).write
        }
      }
      catch {
        case e: Exception => LOG.error("Error", e)
      }
      value
    }
  }

  class MilestoneFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val task = context.getWrappedBean.asInstanceOf[SimpleTask]
      if (task.getMilestoneid != null) {
        val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
        val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl, task.getProjectid,
          task.getMilestoneid)
        val link = FormatUtils.newA(milestoneLink, task.getMilestoneName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        return new Span().write
      }
      try {
        val milestoneId = value.toInt
        val milestoneService = AppContextUtil.getSpringBean(classOf[MilestoneService])
        val milestone = milestoneService.findById(milestoneId, context.getUser.getAccountId)
        if (milestone != null) {
          val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
          val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
            milestone.getProjectid, milestone.getId)
          val link = FormatUtils.newA(milestoneLink, milestone.getName)
          return FormatUtils.newLink(img, link).write
        }
      }
      catch {
        case e: Exception => LOG.error("Error", e)
      }
      value
    }
  }

}
