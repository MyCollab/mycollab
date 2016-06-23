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

import com.esofthead.mycollab.common.MonitorTypeConstants
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.html.LinkUtils
import com.esofthead.mycollab.module.crm.CrmLinkGenerator
import com.esofthead.mycollab.module.crm.domain.{MeetingWithBLOBs, SimpleMeeting}
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum
import com.esofthead.mycollab.module.crm.service.MeetingService
import com.esofthead.mycollab.schedule.email.crm.MeetingRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.format.DateTimeFieldFormat
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
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
class MeetingRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleMeeting] with MeetingRelayEmailNotificationAction {
  @Autowired var meetingService: MeetingService = _
  private val mapper = new MeetingFieldNameMapper

  protected def buildExtraTemplateVariables(context: MailContext[SimpleMeeting]) {
    val summary = bean.getSubject
    val summaryLink = CrmLinkGenerator.generateMeetingPreviewFullLink(siteUrl, bean.getId)
    val emailNotification = context.getEmailNotification

    val avatarId = if (changeUser != null) changeUser.getAvatarid else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => MeetingI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => MeetingI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => MeetingI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  protected def getCreateSubjectKey: Enum[_] = MeetingI18nEnum.MAIL_CREATE_ITEM_SUBJECT

  protected def getUpdateSubjectKey: Enum[_] = MeetingI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

  protected def getCommentSubjectKey: Enum[_] = MeetingI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

  protected def getItemName: String = StringUtils.trim(bean.getSubject, 100)

  protected def getItemFieldMapper: ItemFieldMapper = mapper

  protected def getBeanInContext(notification: SimpleRelayEmailNotification): SimpleMeeting =
    meetingService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  class MeetingFieldNameMapper extends ItemFieldMapper {
    put(MeetingWithBLOBs.Field.subject, MeetingI18nEnum.FORM_SUBJECT, isColSpan = true)
    put(MeetingWithBLOBs.Field.status, GenericI18Enum.FORM_STATUS)
    put(MeetingWithBLOBs.Field.startdate, new DateTimeFieldFormat(MeetingWithBLOBs.Field.startdate.name,
      MeetingI18nEnum.FORM_START_DATE_TIME))
    put(MeetingWithBLOBs.Field.location, MeetingI18nEnum.FORM_LOCATION)
    put(MeetingWithBLOBs.Field.enddate, new DateTimeFieldFormat(MeetingWithBLOBs.Field.enddate.name,
      MeetingI18nEnum.FORM_END_DATE_TIME))
    put(MeetingWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
  }

}
