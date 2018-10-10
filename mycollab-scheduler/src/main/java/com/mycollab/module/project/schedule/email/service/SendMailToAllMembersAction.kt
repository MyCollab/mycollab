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
package com.mycollab.module.project.schedule.email.service

import com.google.common.eventbus.AsyncEventBus
import com.hp.gagawa.java.elements.A
import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.NotificationType
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.domain.SimpleAuditLog
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.domain.criteria.CommentSearchCriteria
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.common.service.AuditLogService
import com.mycollab.common.service.CommentService
import com.mycollab.configuration.ApplicationConfiguration
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.event.BatchInsertNotificationItemsEvent
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.project.service.ProjectNotificationSettingService
import com.mycollab.module.project.service.ProjectService
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.mycollab.schedule.email.format.WebItem
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
abstract class SendMailToAllMembersAction<B> : SendingRelayEmailNotificationAction {
    @Autowired
    private lateinit var applicationConfiguration: ApplicationConfiguration
    @Autowired
    private lateinit var extMailService: ExtMailService
    @Autowired
    private lateinit var projectService: ProjectService
    @Autowired
    private lateinit var projectMemberService: ProjectMemberService
    @Autowired
    private lateinit var projectNotificationService: ProjectNotificationSettingService
    @Autowired
    private lateinit var commentService: CommentService
    @Autowired
    private lateinit var auditLogService: AuditLogService
    @Autowired
    protected lateinit var contentGenerator: IContentGenerator
    @Autowired
    private lateinit var eventBus: AsyncEventBus

    protected var bean: B? = null
    protected var projectMember: SimpleProjectMember? = null

    protected lateinit var siteUrl: String
    private var projectId: Int? = null

    private fun getNotifyUsers(notification: ProjectRelayEmailNotification): Set<SimpleUser> {
        var notifyUsers = projectMemberService.getActiveUsersInProject(notification.projectId,
                notification.saccountid)
        val notificationSettings = projectNotificationService.findNotifications(notification.projectId,
                notification.saccountid)
        if (notificationSettings.isNotEmpty()) {
            notificationSettings.forEach {
                if ((NotificationType.None.name == it.level) || (NotificationType.Minimal.name == it.level)) {
                    notifyUsers = notifyUsers.filter { notifyUser -> notifyUser.username != it.username }
                }
            }
        }
        return notifyUsers.toSet()
    }

    override fun sendNotificationForCreateAction(notification: SimpleRelayEmailNotification) {
        val projectRelayEmailNotification = notification as ProjectRelayEmailNotification
        val notifiers = getNotifyUsers(projectRelayEmailNotification)
        if (notifiers.isNotEmpty()) {
            onInitAction(projectRelayEmailNotification)
            bean = getBeanInContext(projectRelayEmailNotification)
            val notifyUsersForCreateAction = mutableListOf<String>()
            val notificationMessages = mutableListOf<String>()
            if (bean != null) {
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))
                notifiers.forEach {
                    val context = MailContext<B>(notification, it, siteUrl)
                    context.wrappedBean = bean
                    buildExtraTemplateVariables(context)
                    contentGenerator.putVariable("context", context)
                    contentGenerator.putVariable("mapper", getItemFieldMapper())
                    contentGenerator.putVariable("userName", it.displayName!!)
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
                    val userMail = MailRecipientField(it.email, it.username)
                    val recipients = listOf(userMail)
                    extMailService.sendHTMLMail(applicationConfiguration.notifyEmail, applicationConfiguration.siteName, recipients,
                            getCreateSubject(context), contentGenerator.parseFile("mailProjectItemCreatedNotifier.ftl", context.locale))
                    if (it.username != notification.changeby) {
                        notifyUsersForCreateAction.add(it.username)
                        notificationMessages.add(getCreateSubjectNotification(context))
                    }
                }
                eventBus.post(BatchInsertNotificationItemsEvent(notifyUsersForCreateAction, ModuleNameConstants.PRJ,
                        getType(), getTypeId(), notificationMessages, notification.saccountid))
            }
        }
    }

    override fun sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification) {
        val projectRelayEmailNotification = notification as ProjectRelayEmailNotification
        val notifiers = getNotifyUsers(projectRelayEmailNotification)
        if (notifiers.isNotEmpty()) {
            onInitAction(projectRelayEmailNotification)
            bean = getBeanInContext(projectRelayEmailNotification)
            if (bean != null) {
                val auditLog = auditLogService.findLastestLogs(notification.typeid.toInt(), notification.saccountid)
                contentGenerator.putVariable("historyLog", auditLog ?: SimpleAuditLog())
                contentGenerator.putVariable("mapper", getItemFieldMapper())
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))

                val searchCriteria = CommentSearchCriteria()
                searchCriteria.type = StringSearchField.and(notification.type)
                searchCriteria.typeId = StringSearchField.and(notification.typeid)
                searchCriteria.saccountid = null
                val comments = commentService.findPageableListByCriteria(BasicSearchRequest(searchCriteria, 0, 5))
                contentGenerator.putVariable("lastComments", comments)
                val notifyUsersForUpdateAction = mutableListOf<String>()
                val notificationMessages = mutableListOf<String>()

                notifiers.forEach {
                    val context = MailContext<B>(notification, it, siteUrl)
                    if (comments.isNotEmpty()) {
                        contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Last_Comments_Value, "${comments.size}"))
                    }
                    contentGenerator.putVariable("Changes", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Changes))
                    contentGenerator.putVariable("Field", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Field))
                    contentGenerator.putVariable("Old_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Old_Value))
                    contentGenerator.putVariable("New_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.New_Value))
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
                    contentGenerator.putVariable("context", context)
                    context.wrappedBean = bean
                    buildExtraTemplateVariables(context)
                    val userMail = MailRecipientField(it.email, it.username)
                    val recipients = listOf(userMail)
                    extMailService.sendHTMLMail(applicationConfiguration.notifyEmail, applicationConfiguration.siteName, recipients,
                            getUpdateSubject(context), contentGenerator.parseFile("mailProjectItemUpdatedNotifier.ftl", context.locale))
                    if (it.username != notification.changeby) {
                        notifyUsersForUpdateAction.add(it.username)
                        notificationMessages.add(getUpdateSubjectNotification(context))
                    }
                }

                eventBus.post(BatchInsertNotificationItemsEvent(notifyUsersForUpdateAction, ModuleNameConstants.PRJ,
                        getType(), getTypeId(), notificationMessages, notification.saccountid))
            }
        }
    }

    override fun sendNotificationForCommentAction(notification: SimpleRelayEmailNotification) {
        val projectRelayEmailNotification = notification as ProjectRelayEmailNotification
        val notifiers = getNotifyUsers(projectRelayEmailNotification)
        if (notifiers.isNotEmpty()) {
            onInitAction(projectRelayEmailNotification)
            bean = getBeanInContext(projectRelayEmailNotification)
            if (bean != null) {
                val searchCriteria = CommentSearchCriteria()
                searchCriteria.type = StringSearchField.and(notification.type)
                searchCriteria.typeId = StringSearchField.and(notification.typeid)
                searchCriteria.saccountid = null
                val comments = commentService.findPageableListByCriteria(BasicSearchRequest(searchCriteria, 0, 5))
                contentGenerator.putVariable("lastComments", comments)
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))

                val notifyUsersForCommentAction = mutableListOf<String>()
                val notificationMessages = mutableListOf<String>()

                notifiers.forEach {
                    val context = MailContext<B>(notification, it, siteUrl)
                    buildExtraTemplateVariables(context)
                    contentGenerator.putVariable("comment", context.emailNotification)
                    contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Last_Comments_Value, "$comments.size"))
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
                    val userMail = MailRecipientField(it.email, it.username)
                    val recipients = listOf(userMail)
                    extMailService.sendHTMLMail(applicationConfiguration.notifyEmail, applicationConfiguration.siteName, recipients,
                            getCommentSubject(context), contentGenerator.parseFile("mailProjectItemCommentNotifier.ftl", context.locale))
                    if (it.username != notification.changeby) {
                        notifyUsersForCommentAction.add(it.username)
                        notificationMessages.add(getCommentSubjectNotification(context))
                    }
                }
                eventBus.post(BatchInsertNotificationItemsEvent(notifyUsersForCommentAction, ModuleNameConstants.PRJ,
                        getType(), getTypeId(), notificationMessages, notification.saccountid))
            }
        }
    }

    private fun onInitAction(notification: ProjectRelayEmailNotification) {
        projectId = notification.projectId
        siteUrl = MailUtils.getSiteUrl(notification.saccountid)
        val relatedProject = projectService.findById(notification.projectId, notification.saccountid)
        if (relatedProject != null) {
            val projectHyperLink = WebItem(relatedProject.name, ProjectLinkGenerator.generateProjectFullLink(siteUrl, relatedProject.id))
            contentGenerator.putVariable("projectHyperLink", projectHyperLink)
            projectMember = projectMemberService.findMemberByUsername(notification.changeby, notification.projectId,
                    notification.saccountid)
        } else {
            throw ResourceNotFoundException("Can not find the project ${notification.projectId} in the account ${notification.saccountid}")
        }
    }

    protected abstract fun getBeanInContext(notification: ProjectRelayEmailNotification): B?

    protected abstract fun buildExtraTemplateVariables(context: MailContext<B>)

    private fun getProjectFooter(context: MailContext<B>): String = LocalizationHelper.getMessage(context.locale,
            MailI18nEnum.Project_Footer, getProjectName(), getProjectNotificationSettingLink(context))

    private fun getProjectNotificationSettingLink(context: MailContext<B>) =
            A(ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, projectId!!)).appendText(LocalizationHelper.getMessage(context.locale, MailI18nEnum.Project_Notification_Setting)).write()


    protected abstract fun getItemName(): String

    protected abstract fun getType(): String

    protected abstract fun getTypeId(): String

    protected abstract fun getProjectName(): String

    protected abstract fun getCreateSubject(context: MailContext<B>): String

    protected abstract fun getCreateSubjectNotification(context: MailContext<B>): String

    protected abstract fun getUpdateSubject(context: MailContext<B>): String

    protected abstract fun getUpdateSubjectNotification(context: MailContext<B>): String

    protected abstract fun getCommentSubject(context: MailContext<B>): String

    protected abstract fun getCommentSubjectNotification(context: MailContext<B>): String

    protected abstract fun getItemFieldMapper(): ItemFieldMapper
}