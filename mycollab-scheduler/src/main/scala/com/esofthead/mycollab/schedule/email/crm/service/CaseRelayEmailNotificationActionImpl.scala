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
import com.esofthead.mycollab.html.{FormatUtils, LinkUtils}
import com.esofthead.mycollab.module.crm.domain.{CaseWithBLOBs, SimpleCase}
import com.esofthead.mycollab.module.crm.i18n.CaseI18nEnum
import com.esofthead.mycollab.module.crm.service.{AccountService, CaseService}
import com.esofthead.mycollab.module.crm.{CrmLinkGenerator, CrmResources, CrmTypeConstants}
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.crm.CaseRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.format.FieldFormat
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.esofthead.mycollab.spring.AppContextUtil
import com.hp.gagawa.java.elements.{Span, Text}
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
class CaseRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleCase] with CaseRelayEmailNotificationAction {
  private val LOG = LoggerFactory.getLogger(classOf[CaseRelayEmailNotificationActionImpl])

  @Autowired var caseService: CaseService = _
  private val mapper = new CaseFieldNameMapper

  override protected def getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCase =
    caseService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  override protected def getCreateSubjectKey: Enum[_] = CaseI18nEnum.MAIL_CREATE_ITEM_SUBJECT

  override protected def getCommentSubjectKey: Enum[_] = CaseI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

  override protected def getItemFieldMapper: ItemFieldMapper = mapper

  override protected def getItemName: String = StringUtils.trim(bean.getSubject, 100)

  override protected def buildExtraTemplateVariables(context: MailContext[SimpleCase]): Unit = {
    val summary = bean.getSubject
    val summaryLink = CrmLinkGenerator.generateCasePreviewFullLink(siteUrl, bean.getId)

    val emailNotification = context.getEmailNotification

    val avatarId = if (changeUser != null) changeUser.getAvatarid else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => CaseI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => CaseI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => CaseI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  override protected def getUpdateSubjectKey: Enum[_] = CaseI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

  class CaseFieldNameMapper extends ItemFieldMapper {
    put(CaseWithBLOBs.Field.subject, CaseI18nEnum.FORM_SUBJECT, isColSpan = true)
    put(CaseWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION)
    put(CaseWithBLOBs.Field.accountid, new AccountFieldFormat(CaseWithBLOBs.Field.accountid.name, CaseI18nEnum
      .FORM_ACCOUNT))
    put(CaseWithBLOBs.Field.priority, CaseI18nEnum.FORM_PRIORITY)
    put(CaseWithBLOBs.Field.`type`, GenericI18Enum.FORM_TYPE)
    put(CaseWithBLOBs.Field.status, GenericI18Enum.FORM_STATUS)
    put(CaseWithBLOBs.Field.reason, CaseI18nEnum.FORM_REASON)
    put(CaseWithBLOBs.Field.phonenumber, CaseI18nEnum.FORM_PHONE)
    put(CaseWithBLOBs.Field.email, CaseI18nEnum.FORM_EMAIL)
    put(CaseWithBLOBs.Field.origin, CaseI18nEnum.FORM_ORIGIN)
    put(CaseWithBLOBs.Field.assignuser, new AssigneeFieldFormat(CaseWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(CaseWithBLOBs.Field.resolution, CaseI18nEnum.FORM_RESOLUTION, isColSpan = true)
  }

  class AccountFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val simpleCase = context.getWrappedBean.asInstanceOf[SimpleCase]
      if (simpleCase.getAccountid != null) {
        val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
        val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, simpleCase.getAccountid)
        val link = FormatUtils.newA(accountLink, simpleCase.getAccountName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        new Span().write
      }
      try {
        val accountId = value.toInt
        val accountService = AppContextUtil.getSpringBean(classOf[AccountService])
        val account = accountService.findById(accountId, context.getUser.getAccountId)
        if (account != null) {
          val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
          val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, account.getId)
          val link = FormatUtils.newA(accountLink, account.getAccountname)
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
      val simpleCase = context.getWrappedBean.asInstanceOf[SimpleCase]
      if (simpleCase.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(simpleCase.getAssignUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(simpleCase.getSaccountid),
          simpleCase.getAssignuser)
        val link = FormatUtils.newA(userLink, simpleCase.getAssignUserFullName)
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
          val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId),
            user.getUsername)
          val img = FormatUtils.newImg("avatar", userAvatarLink)
          val link = FormatUtils.newA(userLink, user.getDisplayName)
          FormatUtils.newLink(img, link).write
        } else
          value
      }
    }
  }

}
