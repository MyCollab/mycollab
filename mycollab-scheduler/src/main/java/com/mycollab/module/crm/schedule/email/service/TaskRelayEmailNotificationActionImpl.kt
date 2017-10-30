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
import com.mycollab.html.FormatUtils.newA
import com.mycollab.html.FormatUtils.newImg
import com.mycollab.html.FormatUtils.newLink
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.CrmResources
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.domain.CrmTask
import com.mycollab.module.crm.domain.SimpleCrmTask
import com.mycollab.module.crm.i18n.TaskI18nEnum
import com.mycollab.module.crm.service.ContactService
import com.mycollab.module.crm.service.TaskService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.TaskRelayEmailNotificationAction
import com.mycollab.schedule.email.format.DateFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
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
class TaskRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleCrmTask>(), TaskRelayEmailNotificationAction {

    @Autowired private lateinit var taskService: TaskService
    private val mapper = TaskFieldNameMapper()

    override fun buildExtraTemplateVariables(context: MailContext<SimpleCrmTask>) {
        val summary = bean!!.subject
        val summaryLink = CrmLinkGenerator.generateTaskPreviewFullLink(siteUrl, bean!!.id)
        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> TaskI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> TaskI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> TaskI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getCreateSubjectKey(): Enum<*> = TaskI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getUpdateSubjectKey(): Enum<*> = TaskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = TaskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemName(): String = StringUtils.trim(bean!!.subject, 100)

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCrmTask? =
            taskService.findById(notification.typeid.toInt(), notification.saccountid)

    class TaskFieldNameMapper : ItemFieldMapper() {
        init {
            put(CrmTask.Field.subject, TaskI18nEnum.FORM_SUBJECT, isColSpan = true)
            put(CrmTask.Field.status, GenericI18Enum.FORM_STATUS)
            put(CrmTask.Field.startdate, DateFieldFormat(CrmTask.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
            put(CrmTask.Field.assignuser, AssigneeFieldFormat(CrmTask.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(CrmTask.Field.duedate, DateFieldFormat(CrmTask.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
            put(CrmTask.Field.contactid, ContactFieldFormat(CrmTask.Field.contactid.name, TaskI18nEnum.FORM_CONTACT))
            put(CrmTask.Field.priority, TaskI18nEnum.FORM_PRIORITY)
            put(CrmTask.Field.description, GenericI18Enum.FORM_DESCRIPTION, true)
        }
    }

    class ContactFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val task = context.wrappedBean as SimpleCrmTask
            return if (task.contactid != null) {
                val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.CONTACT))
                val contactLink = CrmLinkGenerator.generateContactPreviewFullLink(context.siteUrl, task.contactid)
                val link = newA(contactLink, task.contactName!!)
                newLink(img, link).write()
            } else {
                Span().write()
            }
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            try {
                val contactId = value.toInt()
                val contactService = AppContextUtil.getSpringBean(ContactService::class.java)
                val contact = contactService.findById(contactId, context.saccountid)
                if (contact != null) {
                    val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.CONTACT))
                    val contactLink = CrmLinkGenerator.generateContactPreviewFullLink(context.siteUrl, contact.id)
                    val link = newA(contactLink, contact.displayName)
                    return newLink(img, link).write()
                }
            } catch (e: Exception) {
                LOG.error("Error", e)
            }
            return value
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {
        override fun formatField(context: MailContext<*>): String {
            val task = context.wrappedBean as SimpleCrmTask
            return if (task.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(task.assignUserAvatarId, 16)
                val img = newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(task.saccountid),
                        task.assignuser)
                val link = newA(userLink, task.assignUserFullName)
                newLink(img, link).write()
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
                        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(context.saccountid),
                                user.username)
                        val img = newImg("avatar", userAvatarLink)
                        val link = newA(userLink, user.displayName!!)
                        newLink(img, link).write()
                    }
                    else -> value
                }
            }
        }
    }

}