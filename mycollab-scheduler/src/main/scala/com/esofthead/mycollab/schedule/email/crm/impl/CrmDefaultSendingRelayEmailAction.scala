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
import com.esofthead.mycollab.common.domain.{MailRecipientField, SimpleAuditLog, SimpleRelayEmailNotification}
import com.esofthead.mycollab.common.service.AuditLogService
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.core.arguments.{NumberSearchField, SearchRequest, StringSearchField, ValuedBean}
import com.esofthead.mycollab.core.utils.BeanUtility
import com.esofthead.mycollab.module.crm.domain.criteria.NoteSearchCriteria
import com.esofthead.mycollab.module.crm.domain.{CrmNotificationSetting, SimpleNote}
import com.esofthead.mycollab.module.crm.service.{CrmNotificationSettingService, NoteService}
import com.esofthead.mycollab.module.mail.service.ExtMailService
import com.esofthead.mycollab.module.mail.{IContentGenerator, MailUtils}
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext, SendingRelayEmailNotificationAction}
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import scala.collection.mutable
import scala.util.control.Breaks._

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 * @tparam B
 */
abstract class CrmDefaultSendingRelayEmailAction[B <: ValuedBean] extends SendingRelayEmailNotificationAction {
  private val LOG = LoggerFactory.getLogger(classOf[CrmDefaultSendingRelayEmailAction[_]])

  @Autowired protected val extMailService: ExtMailService = null
  @Autowired private val auditLogService: AuditLogService = null
  @Autowired protected val noteService: NoteService = null
  @Autowired protected val userService: UserService = null
  @Autowired protected val notificationService: CrmNotificationSettingService = null
  @Autowired protected val contentGenerator: IContentGenerator = null
  protected var bean: B = _
  protected var siteUrl: String = null

  override def sendNotificationForCreateAction(notification: SimpleRelayEmailNotification): Unit = {
    val notifiers: List[SimpleUser] = getListNotifyUserWithFilter(notification, MonitorTypeConstants.CREATE_ACTION)
    if ((notifiers != null) && notifiers.nonEmpty) {
      onInitAction(notification)
      import scala.collection.JavaConversions._
      for (user <- notifiers) {
        val notifierFullName: String = user.getDisplayName
        if (StringUtils.isBlank(notifierFullName)) {
          LOG.error("Can not find user {} of notification {}", Array[AnyRef](BeanUtility.printBeanObj(user), BeanUtility.printBeanObj(notification)))
          return
        }
        val context: MailContext[B] = new MailContext[B](notification, user, siteUrl)
        bean = getBeanInContext(context)
        if (bean != null) {
          val subject: String = context.getMessage(getCreateSubjectKey, context.getChangeByUserFullName, getItemName)
          context.wrappedBean = bean
          contentGenerator.putVariable("context", context)
          contentGenerator.putVariable("mapper", getItemFieldMapper)
          contentGenerator.putVariable("userName", notifierFullName)
          buildExtraTemplateVariables(context)
          val userMail: MailRecipientField = new MailRecipientField(user.getEmail, user.getUsername)
          val recipients: List[MailRecipientField] = List(userMail)
          extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getSiteName, recipients, null, null, contentGenerator.generateSubjectContent(subject), contentGenerator.generateBodyContent(getCreateContentPath, context.getLocale, SiteConfiguration.getDefaultLocale), null)
        }
      }
    }
  }

  def sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification) {
    val notifiers: List[SimpleUser] = getListNotifyUserWithFilter(notification, MonitorTypeConstants.UPDATE_ACTION)
    if ((notifiers != null) && notifiers.nonEmpty) {
      onInitAction(notification)
      import scala.collection.JavaConversions._
      for (user <- notifiers) {
        val notifierFullName: String = user.getDisplayName
        if (notifierFullName == null) {
          LOG.error("Can not find user {} of notification {}", Array[AnyRef](BeanUtility.printBeanObj(user), BeanUtility.printBeanObj(notification)))
          return
        }
        contentGenerator.putVariable("userName", notifierFullName)
        val context: MailContext[B] = new MailContext[B](notification, user, siteUrl)
        bean = getBeanInContext(context)
        if (bean != null) {
          val subject: String = context.getMessage(getUpdateSubjectKey, context.getChangeByUserFullName, getItemName)
          val auditLog: SimpleAuditLog = auditLogService.findLatestLog(context.getTypeid.toInt, context.getSaccountid)
          contentGenerator.putVariable("historyLog", auditLog)
          context.wrappedBean = bean
          buildExtraTemplateVariables(context)
          contentGenerator.putVariable("context", context)
          contentGenerator.putVariable("mapper", getItemFieldMapper)
          val userMail: MailRecipientField = new MailRecipientField(user.getEmail, user.getUsername)
          val recipients: List[MailRecipientField] = List(userMail)
          extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getSiteName, recipients, null, null, contentGenerator.generateSubjectContent(subject), contentGenerator.generateBodyContent(getUpdateContentPath, context.getLocale, SiteConfiguration.getDefaultLocale), null)
        }
      }
    }
  }

  def sendNotificationForCommentAction(notification: SimpleRelayEmailNotification) {
    val notifiers: List[SimpleUser] = getListNotifyUserWithFilter(notification, MonitorTypeConstants.ADD_COMMENT_ACTION)
    if ((notifiers != null) && notifiers.nonEmpty) {
      onInitAction(notification)

      import scala.collection.JavaConversions._

      for (user <- notifiers) {
        val notifierFullName: String = user.getDisplayName

        breakable {
          if (notifierFullName == null) {
            LOG.error("Can not find user {} of notification {}", Array[AnyRef](BeanUtility.printBeanObj(user), BeanUtility.printBeanObj(notification)))
            break()
          }
        }

        contentGenerator.putVariable("userName", notifierFullName)
        val context: MailContext[B] = new MailContext[B](notification, user, siteUrl)
        bean = getBeanInContext(context)
        context.setWrappedBean(bean)
        buildExtraTemplateVariables(context)
        contentGenerator.putVariable("comment", context.getEmailNotification)
        val subject: String = context.getMessage(getCommentSubjectKey, context.getChangeByUserFullName, getItemName)
        val userMail: MailRecipientField = new MailRecipientField(user.getEmail, user.getUsername)
        val recipients: List[MailRecipientField] = List(userMail)
        extMailService.sendHTMLMail(SiteConfiguration.getNoReplyEmail, SiteConfiguration.getSiteName, seqAsJavaList
          (recipients), null, null,
          contentGenerator.generateSubjectContent(subject), contentGenerator.generateBodyContent(getNoteContentPath, context.getLocale, SiteConfiguration.getDefaultLocale), null)
      }
    }
  }

  private def getListNotifyUserWithFilter(notification: SimpleRelayEmailNotification, `type`: String): List[SimpleUser] = {
    import scala.collection.JavaConverters._

    val notificationSettings: List[CrmNotificationSetting] = notificationService.findNotifications(notification
      .getSaccountid).asScala.toList
    val inListUsers = notification.getNotifyUsers.asScala
    val noteSearchCriteria: NoteSearchCriteria = new NoteSearchCriteria
    noteSearchCriteria.setType(new StringSearchField(notification.getType))
    noteSearchCriteria.setTypeid(new NumberSearchField(notification.getTypeid.toInt))
    noteSearchCriteria.setSaccountid(new NumberSearchField(notification.getSaccountid))
    val lstNote: List[SimpleNote] = noteService.findPagableListByCriteria(new SearchRequest[NoteSearchCriteria]
    (noteSearchCriteria, 0, Integer.MAX_VALUE)).asScala.toList.asInstanceOf[List[SimpleNote]]
    if (lstNote != null && lstNote.nonEmpty) {
      for (note <- lstNote) {
        if (note.getCreateduser != null) {
          val user: SimpleUser = userService.findUserByUserNameInAccount(note.getCreateduser, note.getSaccountid)
          if (user != null && !checkExistInList(inListUsers, user)) {
            inListUsers += user
          }
        }
      }
    }
    {
      var i: Int = 0
      while (i < inListUsers.size) {
        {
          val user: SimpleUser = inListUsers(i)
          import scala.collection.JavaConversions._
          for (notificationSetting <- notificationSettings) {
            if (user.getUsername != null && (user.getUsername == notificationSetting.getUsername)) {
              if (notificationSetting.getLevel == "None") {
                inListUsers.remove(user)
                i -= 1
              }
              else if ((notificationSetting.getLevel == "Minimal") && (`type` == MonitorTypeConstants.ADD_COMMENT_ACTION)) {
                inListUsers.remove(user)
                i -= 1
              }
            }
          }
        }
      }
    }

    inListUsers.toList
  }

  private def onInitAction(notification: SimpleRelayEmailNotification) {
    siteUrl = MailUtils.getSiteUrl(notification.getSaccountid)
  }

  private def checkExistInList(users: mutable.Buffer[SimpleUser], user: SimpleUser): Boolean = {
    for (simpleUser <- users) {
      if (simpleUser.getUsername != null && (simpleUser.getUsername == user.getUsername) || (simpleUser.getEmail == user.getUsername)) {
        true
      }
    }
    false
  }

  protected def getBeanInContext(context: MailContext[B]): B

  private def getCreateContentPath: String = "templates/email/crm/itemCreatedNotifier.mt"

  private def getUpdateContentPath: String = "templates/email/crm/itemUpdatedNotifier.mt"

  private def getNoteContentPath: String = "templates/email/crm/itemAddNoteNotifier.mt"

  protected def buildExtraTemplateVariables(context: MailContext[B])

  protected def getCreateSubjectKey: Enum[_]

  protected def getUpdateSubjectKey: Enum[_]

  protected def getCommentSubjectKey: Enum[_]

  protected def getItemName: String

  protected def getItemFieldMapper: ItemFieldMapper
}
