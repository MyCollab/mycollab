/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.schedule.email.service

import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.Account
import com.mycollab.module.crm.domain.SimpleAccount
import com.mycollab.module.crm.i18n.AccountI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum
import com.mycollab.module.crm.service.AccountService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.AccountRelayEmailNotificationAction
import com.mycollab.schedule.email.format.CountryFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
import com.mycollab.schedule.email.format.I18nFieldFormat
import com.mycollab.spring.AppContextUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class AccountRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleAccount>(), AccountRelayEmailNotificationAction {
    @Autowired private lateinit var accountService: AccountService

    private val mapper = AccountFieldNameMapper()

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleAccount? =
            accountService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getCreateSubjectKey(): Enum<*> = AccountI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getUpdateSubjectKey(): Enum<*> = AccountI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = AccountI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getItemName(): String = StringUtils.trim(bean!!.accountname, 100)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleAccount>) {
        val summary = bean!!.accountname
        val summaryLink = CrmLinkGenerator.generateAccountPreviewFullLink(siteUrl, bean!!.id)

        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum =  when(emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> AccountI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> AccountI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> AccountI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    class AccountFieldNameMapper : ItemFieldMapper() {
        init {
            put(Account.Field.accountname, AccountI18nEnum.FORM_ACCOUNT_NAME)
            put(Account.Field.phoneoffice, AccountI18nEnum.FORM_OFFICE_PHONE)
            put(Account.Field.website, AccountI18nEnum.FORM_WEBSITE)
            put(Account.Field.numemployees, AccountI18nEnum.FORM_EMPLOYEES)
            put(Account.Field.fax, AccountI18nEnum.FORM_FAX)
            put(Account.Field.alternatephone, AccountI18nEnum.FORM_OTHER_PHONE)
            put(Account.Field.industry, I18nFieldFormat(Account.Field.industry.name, AccountI18nEnum.FORM_INDUSTRY, OptionI18nEnum.AccountIndustry::class.java))
            put(Account.Field.email, GenericI18Enum.FORM_EMAIL)
            put(Account.Field.type, I18nFieldFormat(Account.Field.`type`.name, GenericI18Enum.FORM_TYPE, OptionI18nEnum.AccountType::class.java))
            put(Account.Field.ownership, AccountI18nEnum.FORM_OWNERSHIP)
            put(Account.Field.assignuser, AssigneeFieldFormat(Account.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(Account.Field.annualrevenue, AccountI18nEnum.FORM_ANNUAL_REVENUE)
            put(Account.Field.billingaddress, AccountI18nEnum.FORM_BILLING_ADDRESS)
            put(Account.Field.shippingaddress, AccountI18nEnum.FORM_SHIPPING_ADDRESS)
            put(Account.Field.city, AccountI18nEnum.FORM_BILLING_CITY)
            put(Account.Field.shippingcity, AccountI18nEnum.FORM_SHIPPING_CITY)
            put(Account.Field.state, AccountI18nEnum.FORM_BILLING_STATE)
            put(Account.Field.shippingstate, AccountI18nEnum.FORM_SHIPPING_STATE)
            put(Account.Field.postalcode, AccountI18nEnum.FORM_BILLING_POSTAL_CODE)
            put(Account.Field.shippingpostalcode, AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE)
            put(Account.Field.billingcountry, CountryFieldFormat(Account.Field.billingcountry.name, AccountI18nEnum.FORM_BILLING_COUNTRY))
            put(Account.Field.shippingcountry, CountryFieldFormat(Account.Field.shippingcountry.name, AccountI18nEnum.FORM_SHIPPING_COUNTRY))
            put(Account.Field.description, GenericI18Enum.FORM_DESCRIPTION, true)
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val account = context.wrappedBean as SimpleAccount
            return if (account.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(account.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(
                        MailUtils.getSiteUrl(account.saccountid), account.assignuser)
                val link = FormatUtils.newA(userLink, account.assignUserFullName)
                FormatUtils.newLink(img, link).write()
            } else {
                Span().write()
            }
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            return if (StringUtils.isBlank(value)) {
                Span().write()
            } else {
                val userService = AppContextUtil.getSpringBean(UserService::class.java)
                val user = userService.findUserByUserNameInAccount(value, context.saccountid)
                when {
                    user != null -> {
                        val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(
                                MailUtils.getSiteUrl(user.accountId!!), user.username)
                        val img = FormatUtils.newImg("avatar", userAvatarLink)
                        val link = FormatUtils.newA(userLink, user.displayName!!)
                        FormatUtils.newLink(img, link).write()
                    }
                    else -> value
                }
            }
        }
    }
}