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
package com.esofthead.mycollab.schedule.email.project.impl

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.common.{MonitorTypeConstants, NotificationType}
import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.html.{LinkUtils, FormatUtils}
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.project.domain._
import com.esofthead.mycollab.module.project.i18n.{OptionI18nEnum, TaskI18nEnum}
import com.esofthead.mycollab.module.project.service._
import com.esofthead.mycollab.module.project.{ProjectLinkGenerator, ProjectResources, ProjectTypeConstants}
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.format._
import com.esofthead.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.hp.gagawa.java.elements.{A, Img, Span}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

import scala.util.control.Breaks._

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ProjectTaskRelayEmailNotificationActionImpl extends SendMailToFollowersAction[SimpleTask] with ProjectTaskRelayEmailNotificationAction {


  private val LOG = LoggerFactory.getLogger(classOf[ProjectTaskRelayEmailNotificationActionImpl])

  @Autowired var projectTaskService: ProjectTaskService = _

  @Autowired var projectService: ProjectService = _

  @Autowired var projectMemberService: ProjectMemberService = _

  @Autowired var projectNotificationService: ProjectNotificationSettingService = _

  private val mapper = new TaskFieldNameMapper

  protected def buildExtraTemplateVariables(context: MailContext[SimpleTask]) {
    val currentProject = new WebItem(bean.getProjectName, ProjectLinkGenerator.generateProjectFullLink(siteUrl, bean
      .getProjectid))

    val emailNotification: SimpleRelayEmailNotification = context.getEmailNotification
    val relatedProject: SimpleProject = projectService.findById(bean.getProjectid, emailNotification.getSaccountid)
    val taskCode = new WebItem(("[" + relatedProject.getShortname + "-" + bean.getTaskkey + "]"),
      ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, bean.getTaskkey, bean.getProjectShortname))

    val summary: String = bean.getTaskname
    val summaryLink: String = ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, bean.getTaskkey, bean.getProjectShortname)
    val projectMember: SimpleProjectMember = projectMemberService.findMemberByUsername(emailNotification.getChangeby, bean.getProjectid, emailNotification.getSaccountid)

    val avatarId: String = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar: Img = LinkUtils.newAvatar(avatarId)

    val makeChangeUser: String = userAvatar.toString + emailNotification.getChangeByUserFullName
    val actionEnum: Enum[_] = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => TaskI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => TaskI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => TaskI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("titles", List(currentProject, taskCode))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  protected def getBeanInContext(context: MailContext[SimpleTask]): SimpleTask = projectTaskService.findById(context.getTypeid.toInt, context.getSaccountid)

  protected def getItemName(): String = StringUtils.trim(bean.getTaskname, 100)

  protected def getCreateSubject(context: MailContext[SimpleTask]): String = context.getMessage(TaskI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName())

  protected def getUpdateSubject(context: MailContext[SimpleTask]): String = context.getMessage(TaskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName())

  protected def getCommentSubject(context: MailContext[SimpleTask]): String = context.getMessage(TaskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName())

  protected def getItemFieldMapper: ItemFieldMapper = mapper

  protected def getListNotifyUsersWithFilter(notification: ProjectRelayEmailNotification): List[SimpleUser] = {
    import scala.collection.JavaConverters._
    val notificationSettings: List[ProjectNotificationSetting] = projectNotificationService.findNotifications(notification.getProjectId, notification.getSaccountid).asScala.toList

    val activeUsers: List[SimpleUser] = projectMemberService.getActiveUsersInProject(notification.getProjectId, notification.getSaccountid).asScala.toList

    val inListUsers: List[SimpleUser] = notification.getNotifyUsers.asScala.toList

    if (notificationSettings != null && notificationSettings.size > 0) {
      import scala.collection.JavaConversions._
      for (notificationSetting <- notificationSettings) {
        if (NotificationType.None.name == notificationSetting.getLevel) {
          {
            var i: Int = inListUsers.size - 1
            breakable {
              while (i >= 0) {
                {
                  val inUser: SimpleUser = inListUsers.get(i)
                  if ((inUser.getUsername != null) && (inUser.getUsername == notificationSetting.getUsername)) {
                    inListUsers.remove(i)
                    break()
                  }
                }
                i = i - 1
              }
            }
          }
        }
        else if (NotificationType.Minimal.name == notificationSetting.getLevel) {
          var isAlreadyInList: Boolean = false
          breakable {
            for (user <- inListUsers) {
              if ((user.getUsername != null) && (user.getUsername == notificationSetting.getUsername)) {
                isAlreadyInList = true
                break()
              }
            }
          }

          if (!isAlreadyInList) {
            val task: SimpleTask = projectTaskService.findById(notification.getTypeid.toInt, notification.getSaccountid)
            if (task.getAssignuser != null && (task.getAssignuser == notificationSetting.getUsername)) {
              import scala.collection.JavaConversions._
              breakable {
                for (user <- activeUsers) {
                  if ((user.getUsername != null) && (user.getUsername == notificationSetting.getUsername)) {
                    inListUsers.add(user)
                    break()
                  }
                }
              }
            }
          }
        }
        else if (NotificationType.Full.name == notificationSetting.getLevel) {
          var isAlreadyInList: Boolean = false
          breakable {
            for (user <- inListUsers) {
              if ((user.getUsername != null) && (user.getUsername == notificationSetting.getUsername)) {
                isAlreadyInList = true
                break()
              }
            }
          }

          if (!isAlreadyInList) {
            import scala.collection.JavaConversions._

            breakable {
              for (user <- activeUsers) {
                if ((user.getUsername != null) && (user.getUsername == notificationSetting.getUsername)) {
                  inListUsers.add(user)
                  break()
                }
              }
            }
          }
        }
      }
    }

    inListUsers
  }

  class TaskFieldNameMapper extends ItemFieldMapper {
    put(Task.Field.taskname, TaskI18nEnum.FORM_TASK_NAME, isColSpan = true)
    put(Task.Field.startdate, new DateFieldFormat(Task.Field.startdate.name, TaskI18nEnum.FORM_START_DATE))
    put(Task.Field.actualstartdate, new DateFieldFormat(Task.Field.actualstartdate.name, TaskI18nEnum.FORM_ACTUAL_START_DATE))
    put(Task.Field.enddate, new DateFieldFormat(Task.Field.enddate.name, TaskI18nEnum.FORM_END_DATE))
    put(Task.Field.actualenddate, new DateFieldFormat(Task.Field.actualenddate.name, TaskI18nEnum.FORM_ACTUAL_END_DATE))
    put(Task.Field.deadline, new DateFieldFormat(Task.Field.deadline.name, TaskI18nEnum.FORM_DEADLINE))
    put(Task.Field.percentagecomplete, TaskI18nEnum.FORM_PERCENTAGE_COMPLETE)
    put(Task.Field.priority, new I18nFieldFormat(Task.Field.priority.name, TaskI18nEnum.FORM_PRIORITY, classOf[OptionI18nEnum.TaskPriority]))
    put(Task.Field.assignuser, new AssigneeFieldFormat(Task.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(Task.Field.isestimated, TaskI18nEnum.FORM_IS_ESTIMATED)
    put(Task.Field.remainestimate, TaskI18nEnum.FORM_REMAIN_ESTIMATE)
    put(Task.Field.tasklistid, new TaskGroupFieldFormat(Task.Field.tasklistid.name, TaskI18nEnum.FORM_TASKGROUP))
    put(Task.Field.notes, TaskI18nEnum.FORM_NOTES)
  }

  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val task: SimpleTask = context.getWrappedBean.asInstanceOf[SimpleTask]
      if (task.getAssignuser != null) {
        val userAvatarLink: String = MailUtils.getAvatarLink(task.getAssignUserAvatarId, 16)
        val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(task.getSaccountid), task.getAssignuser)
        val link: A = FormatUtils.newA(userLink, task.getAssignUserFullName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
        new Span().write
      } else {
        val userService: UserService = ApplicationContextUtil.getSpringBean(classOf[UserService])
        val user: SimpleUser = userService.findUserByUserNameInAccount(value, context.getUser.getAccountId)
        if (user != null) {
          val userAvatarLink: String = MailUtils.getAvatarLink(user.getAvatarid, 16)
          val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
          val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
          val link: A = FormatUtils.newA(userLink, user.getDisplayName)
          FormatUtils.newLink(img, link).write
        } else
          value
      }
    }
  }

  class TaskGroupFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val task: SimpleTask = context.getWrappedBean.asInstanceOf[SimpleTask]
      if (task.getTasklistid != null) {
        val taskgroupIconLink: String = ProjectResources.getResourceLink(ProjectTypeConstants.TASK_LIST)
        val img: Img = FormatUtils.newImg("icon", taskgroupIconLink)
        val tasklistlink: String = ProjectLinkGenerator.generateTaskGroupPreviewFullLink(context.siteUrl, task.getProjectid,
          task.getTasklistid)
        val link: A = FormatUtils.newA(tasklistlink, task.getTaskListName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
        return new Span().write
      }
      try {
        val taskgroupId: Int = value.toInt
        val tasklistService: ProjectTaskListService = ApplicationContextUtil.getSpringBean(classOf[ProjectTaskListService])
        val taskgroup: SimpleTaskList = tasklistService.findById(taskgroupId, context.getUser.getAccountId)
        if (taskgroup != null) {
          val taskgroupIconLink: String = ProjectResources.getResourceLink(ProjectTypeConstants.TASK_LIST)
          val img: Img = FormatUtils.newImg("icon", taskgroupIconLink)
          val taskListLink: String = ProjectLinkGenerator.generateTaskGroupPreviewFullLink(context.siteUrl, taskgroup
            .getProjectid, taskgroup.getId)
          val link: A = FormatUtils.newA(taskListLink, taskgroup.getName)
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
