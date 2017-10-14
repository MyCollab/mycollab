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
import com.mycollab.module.crm.domain.CaseWithBLOBs
import com.mycollab.module.crm.domain.SimpleCase
import com.mycollab.module.crm.i18n.CaseI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum
import com.mycollab.module.crm.service.AccountService
import com.mycollab.module.crm.service.CaseService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.CaseRelayEmailNotificationAction
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
class CaseRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleCase>(), CaseRelayEmailNotificationAction {

    @Autowired private lateinit var caseService: CaseService
    private val mapper = CaseFieldNameMapper()

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCase? =
            caseService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getCreateSubjectKey(): Enum<*> = CaseI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = CaseI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getItemName(): String = StringUtils.trim(bean!!.subject, 100)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleCase>) {
        val summary = bean!!.subject
        val summaryLink = CrmLinkGenerator.generateCasePreviewFullLink(siteUrl, bean!!.id)

        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> CaseI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> CaseI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> CaseI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getUpdateSubjectKey(): Enum<*> = CaseI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    class CaseFieldNameMapper() : ItemFieldMapper() {
        init {
            put(CaseWithBLOBs.Field.subject, CaseI18nEnum.FORM_SUBJECT, true)
            put(CaseWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION)
            put(CaseWithBLOBs.Field.accountid, AccountFieldFormat(CaseWithBLOBs.Field.accountid.name, CaseI18nEnum.FORM_ACCOUNT))
            put(CaseWithBLOBs.Field.priority, I18nFieldFormat(CaseWithBLOBs.Field.priority.name, CaseI18nEnum.FORM_PRIORITY, OptionI18nEnum.CasePriority::class.java))
            put(CaseWithBLOBs.Field.`type`, I18nFieldFormat(CaseWithBLOBs.Field.`type`.name, GenericI18Enum.FORM_TYPE, OptionI18nEnum.CaseType::class.java))
            put(CaseWithBLOBs.Field.status, I18nFieldFormat(CaseWithBLOBs.Field.status.name, GenericI18Enum.FORM_STATUS, OptionI18nEnum.CaseStatus::class.java))
            put(CaseWithBLOBs.Field.reason, I18nFieldFormat(CaseWithBLOBs.Field.reason.name, CaseI18nEnum.FORM_REASON, OptionI18nEnum.CaseReason::class.java))
            put(CaseWithBLOBs.Field.phonenumber, GenericI18Enum.FORM_PHONE)
            put(CaseWithBLOBs.Field.email, GenericI18Enum.FORM_EMAIL)
            put(CaseWithBLOBs.Field.origin, I18nFieldFormat(CaseWithBLOBs.Field.origin.name, CaseI18nEnum.FORM_ORIGIN, OptionI18nEnum.CaseOrigin::class.java))
            put(CaseWithBLOBs.Field.assignuser, AssigneeFieldFormat(CaseWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(CaseWithBLOBs.Field.resolution, CaseI18nEnum.FORM_RESOLUTION, true)
        }
    }

    class AccountFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val simpleCase = context.wrappedBean as SimpleCase
            return if (simpleCase.accountid != null) {
                val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
                val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, simpleCase.accountid)
                val link = FormatUtils.newA(accountLink, simpleCase.accountName)
                FormatUtils.newLink(img, link).write()
            } else {
                Span().write()
            }
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            try {
                val accountId = value.toInt()
                val accountService = AppContextUtil.getSpringBean(AccountService::class.java)
                val account = accountService.findById(accountId, context.user.accountId)
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

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val simpleCase = context.wrappedBean as SimpleCase
            return if (simpleCase.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(simpleCase.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(simpleCase.saccountid),
                        simpleCase.assignuser)
                val link = FormatUtils.newA(userLink, simpleCase.assignUserFullName)
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
                val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
                if (user != null) {
                    val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.accountId),
                            user.username)
                    val img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link = FormatUtils.newA(userLink, user.displayName)
                    FormatUtils.newLink(img, link).write()
                } else
                    value
            }
        }
    }

}