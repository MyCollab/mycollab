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
import com.hp.gagawa.java.elements.Text
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.CrmResources
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.domain.Contact
import com.mycollab.module.crm.domain.SimpleContact
import com.mycollab.module.crm.i18n.ContactI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum
import com.mycollab.module.crm.service.AccountService
import com.mycollab.module.crm.service.ContactService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.ContactRelayEmailNotificationAction
import com.mycollab.schedule.email.format.*
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
class ContactRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleContact>(), ContactRelayEmailNotificationAction {

    @Autowired private lateinit var contactService: ContactService

    private val mapper = ContactFieldNameMapper()

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleContact? =
            contactService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getCreateSubjectKey(): Enum<*> = ContactI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = ContactI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getItemName(): String = StringUtils.trim(bean!!.contactName, 100)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleContact>) {
        val summary = bean!!.contactName
        val summaryLink = CrmLinkGenerator.generateContactPreviewFullLink(siteUrl, bean!!.id)

        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> ContactI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> ContactI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> ContactI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getUpdateSubjectKey(): Enum<*> = ContactI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    class ContactFieldNameMapper : ItemFieldMapper() {
        init {
            put(Contact.Field.firstname, GenericI18Enum.FORM_FIRSTNAME)
            put(Contact.Field.officephone, ContactI18nEnum.FORM_OFFICE_PHONE)
            put(Contact.Field.lastname, GenericI18Enum.FORM_LASTNAME)
            put(Contact.Field.mobile, ContactI18nEnum.FORM_MOBILE)
            put(Contact.Field.accountid, AccountFieldFormat(Contact.Field.accountid.name, ContactI18nEnum.FORM_ACCOUNTS))
            put(Contact.Field.homephone, ContactI18nEnum.FORM_HOME_PHONE)
            put(Contact.Field.title, ContactI18nEnum.FORM_TITLE)
            put(Contact.Field.otherphone, ContactI18nEnum.FORM_OTHER_PHONE)
            put(Contact.Field.department, ContactI18nEnum.FORM_DEPARTMENT)
            put(Contact.Field.fax, ContactI18nEnum.FORM_FAX)
            put(Contact.Field.email, EmailLinkFieldFormat(Contact.Field.email.name, GenericI18Enum.FORM_EMAIL))
            put(Contact.Field.birthday, DateFieldFormat(Contact.Field.birthday.name, ContactI18nEnum.FORM_BIRTHDAY))
            put(Contact.Field.assistant, ContactI18nEnum.FORM_ASSISTANT)
            put(Contact.Field.iscallable, ContactI18nEnum.FORM_IS_CALLABLE)
            put(Contact.Field.assistantphone, ContactI18nEnum.FORM_ASSISTANT_PHONE)
            put(Contact.Field.assignuser, AssigneeFieldFormat(Contact.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(Contact.Field.leadsource, I18nFieldFormat(Contact.Field.leadsource.name, ContactI18nEnum.FORM_LEAD_SOURCE,
                    OptionI18nEnum.OpportunityLeadSource::class.java, true))
            put(Contact.Field.primaddress, ContactI18nEnum.FORM_PRIMARY_ADDRESS)
            put(Contact.Field.otheraddress, ContactI18nEnum.FORM_OTHER_ADDRESS)
            put(Contact.Field.primcity, ContactI18nEnum.FORM_PRIMARY_CITY)
            put(Contact.Field.othercity, ContactI18nEnum.FORM_OTHER_CITY)
            put(Contact.Field.primstate, ContactI18nEnum.FORM_PRIMARY_STATE)
            put(Contact.Field.otherstate, ContactI18nEnum.FORM_OTHER_STATE)
            put(Contact.Field.primpostalcode, ContactI18nEnum.FORM_PRIMARY_POSTAL_CODE)
            put(Contact.Field.otherpostalcode, ContactI18nEnum.FORM_OTHER_POSTAL_CODE)
            put(Contact.Field.primcountry, CountryFieldFormat(Contact.Field.primcountry.name, ContactI18nEnum.FORM_PRIMARY_COUNTRY))
            put(Contact.Field.othercountry, CountryFieldFormat(Contact.Field.othercountry.name, ContactI18nEnum.FORM_OTHER_COUNTRY))
            put(Contact.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val contact = context.wrappedBean as SimpleContact
            return if (contact.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(contact.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
                        contact.saccountid), contact.assignuser)
                val link = FormatUtils.newA(userLink, contact.assignUserFullName)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            return if (StringUtils.isBlank(value)) {
                Span().write()
            } else {
                val userService = AppContextUtil.getSpringBean(UserService::class.java)
                val user = userService.findUserByUserNameInAccount(value, context.saccountid)
                if (user != null) {
                    val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
                            context.saccountid), user.username)
                    val img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link = FormatUtils.newA(userLink, user.displayName!!)
                    FormatUtils.newLink(img, link).write()
                } else value
            }
        }
    }

    class AccountFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val contact = context.wrappedBean as SimpleContact
            return if (contact.accountid != null) {
                val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
                val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, contact.accountid)
                val link = FormatUtils.newA(accountLink, contact.accountName!!)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            try {
                val accountId = value.toInt()
                val accountService = AppContextUtil.getSpringBean(AccountService::class.java)
                val account = accountService.findById(accountId, context.saccountid)
                if (account != null) {
                    val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
                    val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, account.id)
                    val link = FormatUtils.newA(accountLink, account.accountname)
                    return FormatUtils.newLink(img, link).write()
                }
            } catch (e: Exception) {
                LOG.error("Error", e)
            }
            return value
        }
    }
}