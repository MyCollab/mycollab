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
import com.esofthead.mycollab.module.crm.domain.{Lead, SimpleLead}
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum
import com.esofthead.mycollab.module.crm.service.LeadService
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.crm.LeadRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.format.{EmailLinkFieldFormat, FieldFormat}
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
class LeadRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleLead] with LeadRelayEmailNotificationAction {
    @Autowired var leadService: LeadService = _
    private val mapper = new LeadFieldNameMapper

    override protected def getBeanInContext(context: MailContext[SimpleLead]): SimpleLead = leadService.findById(context.getTypeid.toInt,
        context.getSaccountid)

    override protected def getCreateSubjectKey: Enum[_] = LeadI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override protected def getCommentSubjectKey: Enum[_] = LeadI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override protected def getItemFieldMapper: ItemFieldMapper = mapper

    override protected def getItemName: String = StringUtils.trim(bean.getLeadName, 100)

    override protected def buildExtraTemplateVariables(context: MailContext[SimpleLead]): Unit = {
        val summary = bean.getLeadName
        val summaryLink = CrmLinkGenerator.generateLeadPreviewFullLink(siteUrl, bean.getId)

        val emailNotification = context.getEmailNotification
        val user = userService.findUserByUserNameInAccount(emailNotification.getChangeby, context.getSaccountid)

        val avatarId = if (user != null) user.getAvatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
        val actionEnum = emailNotification.getAction match {
            case MonitorTypeConstants.CREATE_ACTION => LeadI18nEnum.MAIL_CREATE_ITEM_HEADING
            case MonitorTypeConstants.UPDATE_ACTION => LeadI18nEnum.MAIL_UPDATE_ITEM_HEADING
            case MonitorTypeConstants.ADD_COMMENT_ACTION => LeadI18nEnum.MAIL_COMMENT_ITEM_HEADING
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("summary", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override protected def getUpdateSubjectKey: Enum[_] = LeadI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    class LeadFieldNameMapper extends ItemFieldMapper {
        put(Lead.Field.firstname, LeadI18nEnum.FORM_FIRSTNAME)
        put(Lead.Field.email, new EmailLinkFieldFormat("email", LeadI18nEnum.FORM_EMAIL))
        put(Lead.Field.lastname, LeadI18nEnum.FORM_LASTNAME)
        put(Lead.Field.officephone, LeadI18nEnum.FORM_OFFICE_PHONE)
        put(Lead.Field.title, LeadI18nEnum.FORM_TITLE)
        put(Lead.Field.mobile, LeadI18nEnum.FORM_MOBILE)
        put(Lead.Field.department, LeadI18nEnum.FORM_DEPARTMENT)
        put(Lead.Field.otherphone, LeadI18nEnum.FORM_OTHER_PHONE)
        put(Lead.Field.accountname, LeadI18nEnum.FORM_ACCOUNT_NAME)
        put(Lead.Field.fax, LeadI18nEnum.FORM_FAX)
        put(Lead.Field.leadsourcedesc, LeadI18nEnum.FORM_LEAD_SOURCE)
        put(Lead.Field.website, LeadI18nEnum.FORM_WEBSITE)
        put(Lead.Field.industry, LeadI18nEnum.FORM_INDUSTRY)
        put(Lead.Field.status, LeadI18nEnum.FORM_STATUS)
        put(Lead.Field.noemployees, LeadI18nEnum.FORM_NO_EMPLOYEES)
        put(Lead.Field.assignuser, new LeadAssigneeFieldFormat(Lead.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
        put(Lead.Field.primaddress, LeadI18nEnum.FORM_PRIMARY_ADDRESS)
        put(Lead.Field.otheraddress, LeadI18nEnum.FORM_OTHER_ADDRESS)
        put(Lead.Field.primcity, LeadI18nEnum.FORM_PRIMARY_CITY)
        put(Lead.Field.othercity, LeadI18nEnum.FORM_OTHER_CITY)
        put(Lead.Field.primstate, LeadI18nEnum.FORM_PRIMARY_STATE)
        put(Lead.Field.otherstate, LeadI18nEnum.FORM_OTHER_STATE)
        put(Lead.Field.primpostalcode, LeadI18nEnum.FORM_PRIMARY_POSTAL_CODE)
        put(Lead.Field.otherpostalcode, LeadI18nEnum.FORM_OTHER_POSTAL_CODE)
        put(Lead.Field.primcountry, LeadI18nEnum.FORM_PRIMARY_COUNTRY)
        put(Lead.Field.othercountry, LeadI18nEnum.FORM_OTHER_COUNTRY)
        put(Lead.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    }

    class LeadAssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val lead = context.getWrappedBean.asInstanceOf[SimpleLead]
            if (lead.getAssignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(lead.getAssignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(lead.getSaccountid),
                    lead.getAssignuser)
                val link = FormatUtils.newA(userLink, lead.getAssignUserFullName)
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
