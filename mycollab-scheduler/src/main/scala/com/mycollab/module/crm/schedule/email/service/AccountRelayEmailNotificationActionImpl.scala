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

import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.{FormatUtils, LinkUtils}
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.{Account, SimpleAccount}
import com.mycollab.module.crm.i18n.AccountI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum.{AccountIndustry, AccountType}
import com.mycollab.module.crm.service.AccountService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction
import com.mycollab.schedule.email.format.{CountryFieldFormat, FieldFormat, I18nFieldFormat}
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
class AccountRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleAccount] with AccountRelayEmailNotificationAction {
  @Autowired var accountService: AccountService = _
  private val mapper = new AccountFieldNameMapper
  
  override protected def getBeanInContext(notification: SimpleRelayEmailNotification): SimpleAccount =
    accountService.findById(notification.getTypeid.toInt, notification.getSaccountid)
  
  override protected def getCreateSubjectKey: Enum[_] = AccountI18nEnum.MAIL_CREATE_ITEM_SUBJECT
  
  override protected def getCommentSubjectKey: Enum[_] = AccountI18nEnum.MAIL_COMMENT_ITEM_SUBJECT
  
  override protected def getItemFieldMapper: ItemFieldMapper = mapper
  
  override protected def getItemName: String = StringUtils.trim(bean.getAccountname, 100)
  
  override protected def buildExtraTemplateVariables(context: MailContext[SimpleAccount]): Unit = {
    val summary = bean.getAccountname
    val summaryLink = CrmLinkGenerator.generateAccountPreviewFullLink(siteUrl, bean.getId)
    
    val emailNotification = context.getEmailNotification
    
    val avatarId = if (changeUser != null) changeUser.getAvatarid else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)
    
    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => AccountI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => AccountI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => AccountI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }
    
    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("name", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }
  
  override protected def getUpdateSubjectKey: Enum[_] = AccountI18nEnum.MAIL_UPDATE_ITEM_SUBJECT
  
  class AccountFieldNameMapper extends ItemFieldMapper {
    put(Account.Field.accountname, AccountI18nEnum.FORM_ACCOUNT_NAME)
    put(Account.Field.phoneoffice, AccountI18nEnum.FORM_OFFICE_PHONE)
    put(Account.Field.website, AccountI18nEnum.FORM_WEBSITE)
    put(Account.Field.numemployees, AccountI18nEnum.FORM_EMPLOYEES)
    put(Account.Field.fax, AccountI18nEnum.FORM_FAX)
    put(Account.Field.alternatephone, AccountI18nEnum.FORM_OTHER_PHONE)
    put(Account.Field.industry, new I18nFieldFormat(Account.Field.industry.name, AccountI18nEnum.FORM_INDUSTRY, classOf[AccountIndustry]))
    put(Account.Field.email, GenericI18Enum.FORM_EMAIL)
    put(Account.Field.`type`, new I18nFieldFormat(Account.Field.`type`.name, GenericI18Enum.FORM_TYPE, classOf[AccountType]))
    put(Account.Field.ownership, AccountI18nEnum.FORM_OWNERSHIP)
    put(Account.Field.assignuser, new AssigneeFieldFormat(Account.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(Account.Field.annualrevenue, AccountI18nEnum.FORM_ANNUAL_REVENUE)
    put(Account.Field.billingaddress, AccountI18nEnum.FORM_BILLING_ADDRESS)
    put(Account.Field.shippingaddress, AccountI18nEnum.FORM_SHIPPING_ADDRESS)
    put(Account.Field.city, AccountI18nEnum.FORM_BILLING_CITY)
    put(Account.Field.shippingcity, AccountI18nEnum.FORM_SHIPPING_CITY)
    put(Account.Field.state, AccountI18nEnum.FORM_BILLING_STATE)
    put(Account.Field.shippingstate, AccountI18nEnum.FORM_SHIPPING_STATE)
    put(Account.Field.postalcode, AccountI18nEnum.FORM_BILLING_POSTAL_CODE)
    put(Account.Field.shippingpostalcode, AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE)
    put(Account.Field.billingcountry, new CountryFieldFormat(Account.Field.billingcountry.name, AccountI18nEnum.FORM_BILLING_COUNTRY))
    put(Account.Field.shippingcountry, new CountryFieldFormat(Account.Field.shippingcountry.name, AccountI18nEnum.FORM_SHIPPING_COUNTRY))
    put(Account.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
  }
  
  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    
    def formatField(context: MailContext[_]): String = {
      val account = context.getWrappedBean.asInstanceOf[SimpleAccount]
      if (account.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(account.getAssignUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(
          MailUtils.getSiteUrl(account.getSaccountid), account.getAssignuser)
        val link = FormatUtils.newA(userLink, account.getAssignUserFullName)
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
          val userLink = AccountLinkGenerator.generatePreviewFullUserLink(
            MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
          val img = FormatUtils.newImg("avatar", userAvatarLink)
          val link = FormatUtils.newA(userLink, user.getDisplayName)
          FormatUtils.newLink(img, link).write
        } else
          value
      }
    }
  }
  
}
