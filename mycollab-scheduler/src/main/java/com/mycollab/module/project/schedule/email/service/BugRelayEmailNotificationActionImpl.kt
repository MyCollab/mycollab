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

import com.hp.gagawa.java.elements.Span
import com.hp.gagawa.java.elements.Text
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.NotificationType
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.FormatUtils.newA
import com.mycollab.html.FormatUtils.newImg
import com.mycollab.html.FormatUtils.newLink
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.ProjectResources
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.i18n.BugI18nEnum
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.project.service.ProjectNotificationSettingService
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.service.BugService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.format.DateFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
import com.mycollab.schedule.email.format.I18nFieldFormat
import com.mycollab.schedule.email.project.BugRelayEmailNotificationAction
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
class BugRelayEmailNotificationActionImpl : SendMailToFollowersAction<SimpleBug>(), BugRelayEmailNotificationAction {

    @Autowired private lateinit var bugService: BugService
    @Autowired private lateinit var projectNotificationService: ProjectNotificationSettingService

    private val mapper = BugFieldNameMapper()

    override fun buildExtraTemplateVariables(context: MailContext<SimpleBug>) {
        val emailNotification = context.emailNotification

        val summary = "#${bean!!.bugkey} - ${bean!!.name}"
        val summaryLink = ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, bean!!.bugkey, bean!!.projectShortName)

        val avatarId = if (projectMember != null) projectMember!!.memberAvatarId else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> BugI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> BugI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> BugI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("projectName", bean!!.projectname)
        contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean!!.projectid))
        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getBeanInContext(notification: ProjectRelayEmailNotification): SimpleBug? =
            bugService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getItemName(): String = StringUtils.trim(bean!!.name, 100)

    override fun getProjectName(): String = bean!!.projectname

    override fun getCreateSubject(context: MailContext<SimpleBug>): String = context.getMessage(BugI18nEnum.MAIL_CREATE_ITEM_SUBJECT,
            bean!!.projectname, context.changeByUserFullName, getItemName())

    override fun getUpdateSubject(context: MailContext<SimpleBug>): String = context.getMessage(BugI18nEnum.MAIL_UPDATE_ITEM_SUBJECT,
            bean!!.projectname, context.changeByUserFullName, getItemName())

    override fun getCommentSubject(context: MailContext<SimpleBug>): String = context.getMessage(BugI18nEnum.MAIL_COMMENT_ITEM_SUBJECT,
            bean!!.projectname, context.changeByUserFullName, getItemName())

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getListNotifyUsersWithFilter(notification: ProjectRelayEmailNotification): List<SimpleUser> {
        val notificationSettings = projectNotificationService.findNotifications(notification.projectId, notification.saccountid)
        var notifyUsers = notification.notifyUsers

        notificationSettings.forEach {
            if (NotificationType.None.name == it.level) {
                notifyUsers = notifyUsers.filter { notifyUser -> notifyUser.username != it.username }
            } else if (NotificationType.Minimal.name == it.level) {
                val findResult = notifyUsers.find { notifyUser -> notifyUser.username == it.username }
                if (findResult != null) {
                    val bug = bugService.findById(notification.typeid.toInt(), notification.saccountid)
                    if (it.username == bug!!.assignuser) {
                        val prjMember = projectMemberService.getActiveUserOfProject(it.username,
                                it.projectid, it.saccountid)
                        if (prjMember != null) {
                            notifyUsers += prjMember
                        }
                    }
                }
            } else if (NotificationType.Full.name == it.level) {
                val prjMember = projectMemberService.getActiveUserOfProject(it.username, it.projectid, it.saccountid)
                if (prjMember != null) notifyUsers += prjMember
            }
        }
        return notifyUsers
    }

    class BugFieldNameMapper : ItemFieldMapper() {
        init {
            put(BugWithBLOBs.Field.name, BugI18nEnum.FORM_SUMMARY, isColSpan = true)
            put(BugWithBLOBs.Field.environment, BugI18nEnum.FORM_ENVIRONMENT, isColSpan = true)
            put(BugWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
            put(BugWithBLOBs.Field.assignuser, AssigneeFieldFormat(BugWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(BugWithBLOBs.Field.milestoneid, MilestoneFieldFormat(BugWithBLOBs.Field.milestoneid.name, MilestoneI18nEnum.SINGLE))
            put(BugWithBLOBs.Field.status, I18nFieldFormat(BugWithBLOBs.Field.status.name, GenericI18Enum.FORM_STATUS, OptionI18nEnum.BugStatus::class.java))
            put(BugWithBLOBs.Field.resolution, I18nFieldFormat(BugWithBLOBs.Field.resolution.name, BugI18nEnum.FORM_RESOLUTION, OptionI18nEnum.BugResolution::class.java))
            put(BugWithBLOBs.Field.severity, I18nFieldFormat(BugWithBLOBs.Field.severity.name, BugI18nEnum.FORM_SEVERITY, OptionI18nEnum.BugSeverity::class.java))
            put(BugWithBLOBs.Field.priority, I18nFieldFormat(BugWithBLOBs.Field.priority.name, GenericI18Enum.FORM_PRIORITY,
                    OptionI18nEnum.Priority::class.java))
            put(BugWithBLOBs.Field.duedate, DateFieldFormat(BugWithBLOBs.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
            put(BugWithBLOBs.Field.createduser, LogUserFieldFormat(BugWithBLOBs.Field.createduser.name, BugI18nEnum.FORM_LOG_BY))
        }
    }

    class MilestoneFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val bug = context.wrappedBean as SimpleBug
            return if (bug.milestoneid == null || bug.milestoneName == null) {
                Span().write()
            } else {
                val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
                val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
                        bug.projectid, bug.milestoneid)
                val link = newA(milestoneLink, bug.milestoneName)
                FormatUtils.newLink(img, link).write()
            }
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            } else {
                val milestoneId = value.toInt()
                val milestoneService = AppContextUtil.getSpringBean(MilestoneService::class.java)
                val milestone = milestoneService.findById(milestoneId, context.user.accountId)
                return if (milestone != null) {
                    val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
                    val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
                            milestone.projectid, milestone.id)
                    val link = newA(milestoneLink, milestone.name)
                    return newLink(img, link).write()
                } else ""
            }
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val bug = context.wrappedBean as SimpleBug
            return if (bug.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(bug.assignUserAvatarId, 16)
                val img = newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(bug.saccountid), bug.assignuser)
                val link = newA(userLink, bug.assignuserFullName)
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
                val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
                if (user != null) {
                    val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.accountId), user.username)
                    val img = newImg("avatar", userAvatarLink)
                    val link = newA(userLink, user.displayName)
                    newLink(img, link).write()
                } else value
            }
        }
    }

    class LogUserFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val bug = context.wrappedBean as SimpleBug
            return if (bug.createduser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(bug.loguserAvatarId, 16)
                val img = newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(bug.saccountid), bug.createduser)
                val link = newA(userLink, bug.loguserFullName)
                newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value))
                return Span().write()

            val userService = AppContextUtil.getSpringBean(UserService::class.java)
            val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
            return if (user != null) {
                val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.accountId), user.username)
                val img = newImg("avatar", userAvatarLink)
                val link = newA(userLink, user.displayName)
                newLink(img, link).write()
            } else value
        }
    }

}