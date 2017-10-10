/**
 * mycollab-scheduler - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.schedule.email.service

import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.i18n.ComponentI18nEnum
import com.mycollab.module.tracker.domain.Component.Field
import com.mycollab.module.tracker.domain.SimpleComponent
import com.mycollab.module.tracker.service.ComponentService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.format.FieldFormat
import com.mycollab.schedule.email.format.I18nFieldFormat
import com.mycollab.schedule.email.project.ComponentRelayEmailNotificationAction
import com.mycollab.spring.AppContextUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ComponentRelayEmailNotificationActionImpl : SendMailToAllMembersAction<SimpleComponent>(), ComponentRelayEmailNotificationAction {
    @Autowired private lateinit var componentService: ComponentService
    private val mapper = ComponentFieldNameMapper()

    override fun buildExtraTemplateVariables(context: MailContext<SimpleComponent>) {
        val emailNotification = context.emailNotification

        val summary = bean!!.name
        val summaryLink = ProjectLinkGenerator.generateBugComponentPreviewFullLink(siteUrl, bean!!.projectid, bean!!.id)

        val avatarId = if (projectMember != null) projectMember!!.memberAvatarId else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = userAvatar.write() + " " + emailNotification.changeByUserFullName
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> ComponentI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> ComponentI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> ComponentI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("projectName", bean!!.projectName)
        contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean!!.projectid))
        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getUpdateSubject(context: MailContext<SimpleComponent>): String = context.getMessage(
            ComponentI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getBeanInContext(notification: ProjectRelayEmailNotification): SimpleComponent =
            componentService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getItemName(): String = StringUtils.trim(bean!!.description, 100)

    override fun getProjectName(): String = bean!!.projectName

    override fun getCreateSubject(context: MailContext<SimpleComponent>): String = context.getMessage(
            ComponentI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getCommentSubject(context: MailContext<SimpleComponent>): String = context.getMessage(
            ComponentI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    class ComponentFieldNameMapper : ItemFieldMapper() {
        init {
            put(Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
            put(Field.status, I18nFieldFormat(Field.status.name, GenericI18Enum.FORM_STATUS,
                    OptionI18nEnum.StatusI18nEnum::class.java))
            put(Field.userlead, LeadFieldFormat(Field.userlead.name, ComponentI18nEnum.FORM_LEAD))
        }
    }

    class LeadFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {
        override fun formatField(context: MailContext<*>): String {
            val component = context.wrappedBean as SimpleComponent
            return if (component.userlead != null) {
                val userAvatarLink = MailUtils.getAvatarLink(component.userLeadAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(component.saccountid),
                        component.userlead)
                val link = FormatUtils.newA(userLink, component.userLeadFullName)
                FormatUtils.newLink(img, link).write()
            } else
                Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            val userService = AppContextUtil.getSpringBean(UserService::class.java)
            val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
            return if (user != null) {
                val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.accountId),
                        user.username)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val link = FormatUtils.newA(userLink, user.displayName)
                FormatUtils.newLink(img, link).write()
            } else value
        }
    }

}