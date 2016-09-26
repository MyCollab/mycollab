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
package com.mycollab.module.crm.schedule.email.service

import com.mycollab.html.FormatUtils
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.crm.CallRelayEmailNotificationAction
import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.{CallWithBLOBs, SimpleCall}
import com.mycollab.module.crm.i18n.CallI18nEnum
import com.mycollab.module.crm.service.CallService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.schedule.email.format.{DateTimeFieldFormat, FieldFormat}
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.mycollab.spring.AppContextUtil
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
class CallRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleCall] with CallRelayEmailNotificationAction {
  @Autowired var callService: CallService = _
  private val mapper: CallFieldNameMapper = new CallFieldNameMapper

  override protected def getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCall =
    callService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  override protected def getCreateSubjectKey: Enum[_] = CallI18nEnum.MAIL_CREATE_ITEM_SUBJECT

  override protected def getCommentSubjectKey: Enum[_] = CallI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

  override protected def getItemFieldMapper: ItemFieldMapper = mapper

  override protected def getItemName: String = StringUtils.trim(bean.getSubject, 100)

  override protected def buildExtraTemplateVariables(context: MailContext[SimpleCall]): Unit = {
    val summary = bean.getSubject
    val summaryLink = CrmLinkGenerator.generateCallPreviewFullLink(siteUrl, bean.getId)

    val emailNotification = context.getEmailNotification

    val avatarId = if (changeUser != null) changeUser.getAvatarid else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => CallI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => CallI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => CallI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("name", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  override protected def getUpdateSubjectKey: Enum[_] = CallI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

  class CallFieldNameMapper extends ItemFieldMapper {
    put(CallWithBLOBs.Field.subject, CallI18nEnum.FORM_SUBJECT, isColSpan = true)
    put(CallWithBLOBs.Field.status, GenericI18Enum.FORM_STATUS)
    put(CallWithBLOBs.Field.startdate, new DateTimeFieldFormat(CallWithBLOBs.Field.startdate.name, CallI18nEnum.FORM_START_DATE_TIME))
    put(CallWithBLOBs.Field.typeid, CallI18nEnum.FORM_RELATED)
    put(CallWithBLOBs.Field.durationinseconds, GenericI18Enum.FORM_DURATION)
    put(CallWithBLOBs.Field.purpose, CallI18nEnum.FORM_PURPOSE)
    put(CallWithBLOBs.Field.assignuser, new AssigneeFieldFormat(CallWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(CallWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    put(CallWithBLOBs.Field.result, CallI18nEnum.FORM_RESULT, isColSpan = true)
  }

  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val call = context.getWrappedBean.asInstanceOf[SimpleCall]
      if (call.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(call.getAssignUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(call.getSaccountid),
          call.getAssignuser)
        val link = FormatUtils.newA(userLink, call.getAssignUserFullName)
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
          val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
            user.getAccountId), user.getUsername)
          val img = FormatUtils.newImg("avatar", userAvatarLink)
          val link = FormatUtils.newA(userLink, user.getDisplayName)
          FormatUtils.newLink(img, link).write
        } else
          value
      }
    }
  }

}
