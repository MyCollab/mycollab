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
package com.esofthead.mycollab.schedule.email.crm.service

import java.util.Locale

import com.esofthead.mycollab.common.MonitorTypeConstants
import com.esofthead.mycollab.common.domain.{MailRecipientField, SimpleAuditLog, SimpleRelayEmailNotification}
import com.esofthead.mycollab.common.service.AuditLogService
import com.esofthead.mycollab.configuration.SiteConfiguration
import com.esofthead.mycollab.core.utils.{BeanUtility, StringUtils}
import com.esofthead.mycollab.module.crm.service.CrmNotificationSettingService
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.mail.service.{ExtMailService, IContentGenerator}
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext, SendingRelayEmailNotificationAction}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

import scala.util.control.Breaks._

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 * @tparam B
 */
abstract class CrmDefaultSendingRelayEmailAction[B] extends SendingRelayEmailNotificationAction {
    private val LOG = LoggerFactory.getLogger(classOf[CrmDefaultSendingRelayEmailAction[_]])

    @Autowired protected val extMailService: ExtMailService = null
    @Autowired private val auditLogService: AuditLogService = null
    @Autowired protected val userService: UserService = null
    @Autowired protected val notificationService: CrmNotificationSettingService = null
    @Autowired protected val contentGenerator: IContentGenerator = null
    protected var bean: B = _
    protected var siteUrl: String = null

    override def sendNotificationForCreateAction(notification: SimpleRelayEmailNotification): Unit = {
        val notifiers = getListNotifyUserWithFilter(notification, MonitorTypeConstants.CREATE_ACTION)
        if ((notifiers != null) && notifiers.nonEmpty) {
            onInitAction(notification)
            import scala.collection.JavaConversions._
            for (user <- notifiers) {
                val notifierFullName = user.getDisplayName
                if (StringUtils.isBlank(notifierFullName)) {
                    LOG.error("Can not find user {} of notification {}", Array[AnyRef](BeanUtility.printBeanObj(user),
                        BeanUtility.printBeanObj(notification)))
                    return
                }
                val context = new MailContext[B](notification, user, siteUrl)
                bean = getBeanInContext(context)
                if (bean != null) {
                    val subject = context.getMessage(getCreateSubjectKey, context.getChangeByUserFullName, getItemName)
                    context.wrappedBean = bean
                    contentGenerator.putVariable("context", context)
                    contentGenerator.putVariable("mapper", getItemFieldMapper)
                    contentGenerator.putVariable("userName", notifierFullName)
                    buildExtraTemplateVariables(context)
                    val userMail = new MailRecipientField(user.getEmail, user.getUsername)
                    val recipients = List(userMail)
                    extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients,
                        null, null, contentGenerator.parseString(subject),
                        contentGenerator.parseFile(getCreateContentPath, context.getLocale, Locale.US), null)
                }
            }
        }
    }

    def sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification) {
        val notifiers = getListNotifyUserWithFilter(notification, MonitorTypeConstants.UPDATE_ACTION)
        if ((notifiers != null) && notifiers.nonEmpty) {
            onInitAction(notification)
            import scala.collection.JavaConversions._
            for (user <- notifiers) {
                val notifierFullName = user.getDisplayName
                if (notifierFullName == null) {
                    LOG.error("Can not find user {} of notification {}", Array[AnyRef](BeanUtility.printBeanObj(user),
                        BeanUtility.printBeanObj(notification)))
                    return
                }
                contentGenerator.putVariable("userName", notifierFullName)
                val context = new MailContext[B](notification, user, siteUrl)
                bean = getBeanInContext(context)
                if (bean != null) {
                    val subject = context.getMessage(getUpdateSubjectKey, context.getChangeByUserFullName, getItemName)
                    val auditLog = auditLogService.findLastestLog(context.getTypeid.toInt, context.getSaccountid)
                    contentGenerator.putVariable("historyLog", auditLog)
                    context.wrappedBean = bean
                    buildExtraTemplateVariables(context)
                    contentGenerator.putVariable("context", context)
                    contentGenerator.putVariable("mapper", getItemFieldMapper)
                    val userMail = new MailRecipientField(user.getEmail, user.getUsername)
                    val recipients = List(userMail)
                    extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, recipients,
                        null, null, contentGenerator.parseString(subject),
                        contentGenerator.parseFile(getUpdateContentPath, context.getLocale, Locale.US), null)
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
                val notifierFullName = user.getDisplayName

                breakable {
                    if (notifierFullName == null) {
                        LOG.error("Can not find user {} of notification {}", Array[AnyRef](BeanUtility.printBeanObj(user),
                            BeanUtility.printBeanObj(notification)))
                        break()
                    }
                }


                contentGenerator.putVariable("userName", notifierFullName)
                val context = new MailContext[B](notification, user, siteUrl)
                bean = getBeanInContext(context)
                context.setWrappedBean(bean)
                buildExtraTemplateVariables(context)
                contentGenerator.putVariable("comment", context.getEmailNotification)
                val subject = context.getMessage(getCommentSubjectKey, context.getChangeByUserFullName, getItemName)
                val userMail = new MailRecipientField(user.getEmail, user.getUsername)
                val recipients = List(userMail)
                extMailService.sendHTMLMail(SiteConfiguration.getNotifyEmail, SiteConfiguration.getDefaultSiteName, seqAsJavaList
                    (recipients), null, null,
                    contentGenerator.parseString(subject), contentGenerator.parseFile(getNoteContentPath,
                        context.getLocale, Locale.US), null)
            }
        }
    }

    private def getListNotifyUserWithFilter(notification: SimpleRelayEmailNotification, `type`: String): List[SimpleUser] = {
        import scala.collection.JavaConverters._

        val sendUsers = notification.getNotifyUsers.asScala
        sendUsers.toList
    }

    private def onInitAction(notification: SimpleRelayEmailNotification) {
        siteUrl = MailUtils.getSiteUrl(notification.getSaccountid)
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
