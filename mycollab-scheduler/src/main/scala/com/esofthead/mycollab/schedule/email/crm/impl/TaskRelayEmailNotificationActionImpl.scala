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
package com.esofthead.mycollab.schedule.email.crm.impl

import com.esofthead.mycollab.common.MonitorTypeConstants
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.html.{LinkUtils, FormatUtils}
import com.esofthead.mycollab.module.crm.domain.{SimpleContact, SimpleTask, Task}
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum
import com.esofthead.mycollab.module.crm.service.{ContactService, TaskService}
import com.esofthead.mycollab.module.crm.{CrmLinkGenerator, CrmResources, CrmTypeConstants}
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.crm.TaskRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.format.{DateFieldFormat, FieldFormat}
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.hp.gagawa.java.elements.{Text, A, Img, Span}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 4.6.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE) class TaskRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleTask] with TaskRelayEmailNotificationAction {
  private val LOG = LoggerFactory.getLogger(classOf[TaskRelayEmailNotificationActionImpl])

  @Autowired var taskService: TaskService = _

  private val mapper = new TaskFieldNameMapper

  protected def buildExtraTemplateVariables(context: MailContext[SimpleTask]) {
    val summary: String = bean.getSubject
    val summaryLink: String = CrmLinkGenerator.generateTaskPreviewFullLink(siteUrl, bean.getId)
    val emailNotification: SimpleRelayEmailNotification = context.getEmailNotification
    val user: SimpleUser = userService.findUserByUserNameInAccount(emailNotification.getChangeby, context.getSaccountid)

    val avatarId = if (user != null) user.getAvatarid else ""
    val userAvatar: Img = LinkUtils.newAvatar(avatarId)

    val makeChangeUser: String = userAvatar.toString + emailNotification.getChangeByUserFullName
    val actionEnum:Enum[_] = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => TaskI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => TaskI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => TaskI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  protected def getCreateSubjectKey: Enum[_] = TaskI18nEnum.MAIL_CREATE_ITEM_SUBJECT

  protected def getUpdateSubjectKey: Enum[_] = TaskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

  protected def getCommentSubjectKey: Enum[_] = TaskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

  protected def getItemName: String = StringUtils.trim(bean.getSubject, 100)

  protected def getItemFieldMapper: ItemFieldMapper = mapper

  protected def getBeanInContext(context: MailContext[SimpleTask]): SimpleTask = taskService.findById(context.getTypeid.toInt, context.getSaccountid)

  class TaskFieldNameMapper extends ItemFieldMapper {
    put(Task.Field.subject, TaskI18nEnum.FORM_SUBJECT, isColSpan = true)
    put(Task.Field.status, TaskI18nEnum.FORM_STATUS)
    put(Task.Field.startdate, new DateFieldFormat(Task.Field.startdate.name, TaskI18nEnum.FORM_START_DATE))
    put(Task.Field.assignuser, new AssigneeFieldFormat(Task.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(Task.Field.duedate, new DateFieldFormat(Task.Field.duedate.name, TaskI18nEnum.FORM_DUE_DATE))
    put(Task.Field.contactid, new ContactFieldFormat(Task.Field.contactid.name, TaskI18nEnum.FORM_CONTACT))
    put(Task.Field.priority, TaskI18nEnum.FORM_PRIORITY)
    put(Task.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
  }

  class ContactFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val task: SimpleTask = context.getWrappedBean.asInstanceOf[SimpleTask]
      if (task.getContactid != null) {
        val img: Text = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.CONTACT))
        val contactLink: String = CrmLinkGenerator.generateContactPreviewFullLink(context.siteUrl, task.getContactid)
        val link: A = FormatUtils.newA(contactLink, task.getContactName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
        new Span().write
      }
      try {
        val contactId: Int = value.toInt
        val contactService: ContactService = ApplicationContextUtil.getSpringBean(classOf[ContactService])
        val contact: SimpleContact = contactService.findById(contactId, context.getUser.getAccountId)
        if (contact != null) {
          val img: Text = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.CONTACT))
          val contactLink: String = CrmLinkGenerator.generateContactPreviewFullLink(context.siteUrl, contact.getId)
          val link: A = FormatUtils.newA(contactLink, contact.getDisplayName)
          return FormatUtils.newLink(img, link).write
        }
      }
      catch {
        case e: Exception => LOG.error("Error", e)
      }
      value
    }
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

}
