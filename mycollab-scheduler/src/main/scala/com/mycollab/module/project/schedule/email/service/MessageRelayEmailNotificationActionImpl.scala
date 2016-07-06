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

import com.mycollab.module.project.domain.{ProjectRelayEmailNotification, SimpleMessage}
import com.mycollab.module.project.service.MessageService
import com.mycollab.schedule.email.project.MessageRelayEmailNotificationAction
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.i18n.MessageI18nEnum
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

/**
  * @author MyCollab Ltd.
  * @since 4.6.0
  */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class MessageRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[SimpleMessage] with MessageRelayEmailNotificationAction {
  @Autowired var messageService: MessageService = _

  protected def getItemName: String = StringUtils.trim(bean.getTitle, 100)

  protected def getCreateSubject(context: MailContext[SimpleMessage]): String = context.getMessage(
    MessageI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getUpdateSubject(context: MailContext[SimpleMessage]): String = context.getMessage(
    MessageI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getCommentSubject(context: MailContext[SimpleMessage]): String = context.getMessage(
    MessageI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getItemFieldMapper: ItemFieldMapper = null

  protected def getBeanInContext(notification: ProjectRelayEmailNotification): SimpleMessage =
    messageService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  protected def buildExtraTemplateVariables(context: MailContext[SimpleMessage]) {
    val emailNotification = context.getEmailNotification

    val summary = bean.getTitle
    val summaryLink = ProjectLinkGenerator.generateMessagePreviewFullLink(siteUrl, bean.getProjectid, bean.getId)

    val avatarId = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => MessageI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => MessageI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => MessageI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
    contentGenerator.putVariable("message", bean.getMessage)
  }
}
