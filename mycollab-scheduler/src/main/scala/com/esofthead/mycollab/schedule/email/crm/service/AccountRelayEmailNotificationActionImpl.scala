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
import com.esofthead.mycollab.module.crm.CrmLinkGenerator
import com.esofthead.mycollab.module.crm.domain.{Account, SimpleAccount}
import com.esofthead.mycollab.module.crm.i18n.{AccountI18nEnum, OptionI18nEnum}
import com.esofthead.mycollab.module.crm.service.AccountService
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.format.{FieldFormat, I18nFieldFormat}
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.hp.gagawa.java.elements.{A, Img, Span}
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

    override protected def getBeanInContext(context: MailContext[SimpleAccount]): SimpleAccount = accountService.findById(
        context.getTypeid.toInt, context.getSaccountid)

    override protected def getCreateSubjectKey: Enum[_] = AccountI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override protected def getCommentSubjectKey: Enum[_] = AccountI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override protected def getItemFieldMapper: ItemFieldMapper = mapper

    override protected def getItemName: String = StringUtils.trim(bean.getAccountname, 100)

    override protected def buildExtraTemplateVariables(context: MailContext[SimpleAccount]): Unit = {
        val summary = bean.getAccountname
        val summaryLink = CrmLinkGenerator.generateAccountPreviewFullLink(siteUrl, bean.getId)

        val emailNotification = context.getEmailNotification
        val user = userService.findUserByUserNameInAccount(emailNotification.getChangeby, context.getSaccountid)

        val avatarId = if (user != null) user.getAvatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
        val actionEnum = emailNotification.getAction match {
            case MonitorTypeConstants.CREATE_ACTION => AccountI18nEnum.MAIL_CREATE_ITEM_HEADING
            case MonitorTypeConstants.UPDATE_ACTION => AccountI18nEnum.MAIL_UPDATE_ITEM_HEADING
            case MonitorTypeConstants.ADD_COMMENT_ACTION => AccountI18nEnum.MAIL_COMMENT_ITEM_HEADING
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("summary", summary)
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
        put(Account.Field.industry, AccountI18nEnum.FORM_INDUSTRY)
        put(Account.Field.email, AccountI18nEnum.FORM_EMAIL)
        put(Account.Field.`type`, new I18nFieldFormat(Account.Field.`type`.name, AccountI18nEnum.FORM_TYPE,
            classOf[OptionI18nEnum.AccountType]))
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
        put(Account.Field.billingcountry, AccountI18nEnum.FORM_BILLING_COUNTRY)
        put(Account.Field.shippingcountry, AccountI18nEnum.FORM_SHIPPING_COUNTRY)
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
                val userService = ApplicationContextUtil.getSpringBean(classOf[UserService])
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
