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
import com.mycollab.module.crm.domain.Contact
import com.mycollab.module.crm.service.ContactService
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.crm.ContactRelayEmailNotificationAction
import com.hp.gagawa.java.elements.{Span, Text}
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.domain.{Contact, SimpleContact}
import com.mycollab.module.crm.{CrmLinkGenerator, CrmResources, CrmTypeConstants}
import com.mycollab.module.crm.i18n.ContactI18nEnum
import com.mycollab.module.crm.service.{AccountService, ContactService}
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.schedule.email.format.{DateFieldFormat, EmailLinkFieldFormat, FieldFormat}
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.mycollab.spring.AppContextUtil
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
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ContactRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleContact] with ContactRelayEmailNotificationAction {
  private val LOG = LoggerFactory.getLogger(classOf[ContactRelayEmailNotificationActionImpl])

  @Autowired var contactService: ContactService = _

  private val mapper = new ContactFieldNameMapper

  override protected def getBeanInContext(notification: SimpleRelayEmailNotification): SimpleContact =
    contactService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  override protected def getCreateSubjectKey: Enum[_] = ContactI18nEnum.MAIL_CREATE_ITEM_SUBJECT

  override protected def getCommentSubjectKey: Enum[_] = ContactI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

  override protected def getItemFieldMapper: ItemFieldMapper = mapper

  override protected def getItemName: String = StringUtils.trim(bean.getContactName, 100)

  override protected def buildExtraTemplateVariables(context: MailContext[SimpleContact]): Unit = {
    val summary = bean.getContactName
    val summaryLink = CrmLinkGenerator.generateContactPreviewFullLink(siteUrl, bean.getId)

    val emailNotification = context.getEmailNotification

    val avatarId = if (changeUser != null) changeUser.getAvatarid else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => ContactI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => ContactI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => ContactI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  override protected def getUpdateSubjectKey: Enum[_] = ContactI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

  class ContactFieldNameMapper extends ItemFieldMapper {
    put(Contact.Field.firstname, ContactI18nEnum.FORM_FIRSTNAME)
    put(Contact.Field.officephone, ContactI18nEnum.FORM_OFFICE_PHONE)
    put(Contact.Field.lastname, ContactI18nEnum.FORM_LASTNAME)
    put(Contact.Field.mobile, ContactI18nEnum.FORM_MOBILE)
    put(Contact.Field.accountid, new AccountFieldFormat(Contact.Field.accountid.name, ContactI18nEnum.FORM_ACCOUNTS))
    put(Contact.Field.homephone, ContactI18nEnum.FORM_HOME_PHONE)
    put(Contact.Field.title, ContactI18nEnum.FORM_TITLE)
    put(Contact.Field.otherphone, ContactI18nEnum.FORM_OTHER_PHONE)
    put(Contact.Field.department, ContactI18nEnum.FORM_DEPARTMENT)
    put(Contact.Field.fax, ContactI18nEnum.FORM_FAX)
    put(Contact.Field.email, new EmailLinkFieldFormat(Contact.Field.email.name, GenericI18Enum.FORM_EMAIL))
    put(Contact.Field.birthday, new DateFieldFormat(Contact.Field.birthday.name, ContactI18nEnum.FORM_BIRTHDAY))
    put(Contact.Field.assistant, ContactI18nEnum.FORM_ASSISTANT)
    put(Contact.Field.iscallable, ContactI18nEnum.FORM_IS_CALLABLE)
    put(Contact.Field.assistantphone, ContactI18nEnum.FORM_ASSISTANT_PHONE)
    put(Contact.Field.assignuser, new AssigneeFieldFormat(Contact.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(Contact.Field.leadsource, ContactI18nEnum.FORM_LEAD_SOURCE, isColSpan = true)
    put(Contact.Field.primaddress, ContactI18nEnum.FORM_PRIMARY_ADDRESS)
    put(Contact.Field.otheraddress, ContactI18nEnum.FORM_OTHER_ADDRESS)
    put(Contact.Field.primcity, ContactI18nEnum.FORM_PRIMARY_CITY)
    put(Contact.Field.othercity, ContactI18nEnum.FORM_OTHER_CITY)
    put(Contact.Field.primstate, ContactI18nEnum.FORM_PRIMARY_STATE)
    put(Contact.Field.otherstate, ContactI18nEnum.FORM_OTHER_STATE)
    put(Contact.Field.primpostalcode, ContactI18nEnum.FORM_PRIMARY_POSTAL_CODE)
    put(Contact.Field.otherpostalcode, ContactI18nEnum.FORM_OTHER_POSTAL_CODE)
    put(Contact.Field.primcountry, ContactI18nEnum.FORM_PRIMARY_COUNTRY)
    put(Contact.Field.othercountry, ContactI18nEnum.FORM_OTHER_COUNTRY)
    put(Contact.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
  }

  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val contact = context.getWrappedBean.asInstanceOf[SimpleContact]
      if (contact.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(contact.getAssignUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
          contact.getSaccountid), contact.getAssignuser)
        val link = FormatUtils.newA(userLink, contact.getAssignUserFullName)
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

  class AccountFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val contact = context.getWrappedBean.asInstanceOf[SimpleContact]
      if (contact.getAccountid != null) {
        val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
        val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, contact.getAccountid)
        val link = FormatUtils.newA(accountLink, contact.getAccountName)
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

}
