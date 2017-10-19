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
import com.mycollab.module.crm.domain.Lead
import com.mycollab.module.crm.domain.SimpleLead
import com.mycollab.module.crm.i18n.AccountI18nEnum
import com.mycollab.module.crm.i18n.LeadI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum
import com.mycollab.module.crm.service.LeadService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.LeadRelayEmailNotificationAction
import com.mycollab.schedule.email.format.CountryFieldFormat
import com.mycollab.schedule.email.format.EmailLinkFieldFormat
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
class LeadRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleLead>(), LeadRelayEmailNotificationAction {

    @Autowired private lateinit var leadService: LeadService

    private val mapper = LeadFieldNameMapper()

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleLead? =
            leadService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getCreateSubjectKey(): Enum<*> = LeadI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = LeadI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getItemName(): String = StringUtils.trim(bean!!.leadName, 100)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleLead>) {
        val summary = bean!!.leadName
        val summaryLink = CrmLinkGenerator.generateLeadPreviewFullLink(siteUrl, bean!!.id)

        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> LeadI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> LeadI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> LeadI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getUpdateSubjectKey(): Enum<*> = LeadI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    class LeadFieldNameMapper : ItemFieldMapper() {
        init {
            put(Lead.Field.firstname, GenericI18Enum.FORM_FIRSTNAME)
            put(Lead.Field.email, EmailLinkFieldFormat("email", GenericI18Enum.FORM_EMAIL))
            put(Lead.Field.lastname, GenericI18Enum.FORM_LASTNAME)
            put(Lead.Field.officephone, LeadI18nEnum.FORM_OFFICE_PHONE)
            put(Lead.Field.title, LeadI18nEnum.FORM_TITLE)
            put(Lead.Field.mobile, LeadI18nEnum.FORM_MOBILE)
            put(Lead.Field.department, LeadI18nEnum.FORM_DEPARTMENT)
            put(Lead.Field.otherphone, LeadI18nEnum.FORM_OTHER_PHONE)
            put(Lead.Field.accountname, LeadI18nEnum.FORM_ACCOUNT_NAME)
            put(Lead.Field.fax, LeadI18nEnum.FORM_FAX)
            put(Lead.Field.leadsourcedesc, I18nFieldFormat(Lead.Field.leadsourcedesc.name, LeadI18nEnum.FORM_LEAD_SOURCE,
                    OptionI18nEnum.OpportunityLeadSource::class.java))
            put(Lead.Field.website, LeadI18nEnum.FORM_WEBSITE)
            put(Lead.Field.industry, I18nFieldFormat(Lead.Field.industry.name, AccountI18nEnum.FORM_INDUSTRY, OptionI18nEnum.AccountIndustry::class.java))
            put(Lead.Field.status, I18nFieldFormat(Lead.Field.status.name, GenericI18Enum.FORM_STATUS, OptionI18nEnum.LeadStatus::class.java))
            put(Lead.Field.noemployees, LeadI18nEnum.FORM_NO_EMPLOYEES)
            put(Lead.Field.assignuser, LeadAssigneeFieldFormat(Lead.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(Lead.Field.primaddress, LeadI18nEnum.FORM_PRIMARY_ADDRESS)
            put(Lead.Field.otheraddress, LeadI18nEnum.FORM_OTHER_ADDRESS)
            put(Lead.Field.primcity, LeadI18nEnum.FORM_PRIMARY_CITY)
            put(Lead.Field.othercity, LeadI18nEnum.FORM_OTHER_CITY)
            put(Lead.Field.primstate, LeadI18nEnum.FORM_PRIMARY_STATE)
            put(Lead.Field.otherstate, LeadI18nEnum.FORM_OTHER_STATE)
            put(Lead.Field.primpostalcode, LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE)
            put(Lead.Field.otherpostalcode, LeadI18nEnum.FORM_OTHER_POSTAL_CODE)
            put(Lead.Field.primcountry, CountryFieldFormat(Lead.Field.primcountry.name, LeadI18nEnum.FORM_PRIMARY_COUNTRY))
            put(Lead.Field.othercountry, CountryFieldFormat(Lead.Field.othercountry.name, LeadI18nEnum.FORM_OTHER_COUNTRY))
            put(Lead.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
        }
    }

    class LeadAssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val lead = context.wrappedBean as SimpleLead
            return if (lead.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(lead.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(lead.saccountid),
                        lead.assignuser)
                val link = FormatUtils.newA(userLink, lead.assignUserFullName)
                FormatUtils.newLink(img, link).write()
            } else {
                Span().write()
            }
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            } else {
                val userService = AppContextUtil.getSpringBean(UserService::class.java)
                val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
                return if (user != null) {
                    val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.accountId),
                            user.username)
                    val img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link = FormatUtils.newA(userLink, user.displayName)
                    FormatUtils.newLink(img, link).write()
                } else {
                    value
                }
            }
        }
    }
}