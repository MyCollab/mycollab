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

import com.hp.gagawa.java.elements.A
import com.hp.gagawa.java.elements.Span
import com.hp.gagawa.java.elements.Text
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.NotificationType
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.ProjectResources
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.module.project.domain.Task
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority
import com.mycollab.module.project.i18n.TaskI18nEnum
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.project.service.ProjectNotificationSettingService
import com.mycollab.module.project.service.ProjectTaskService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.format.DateFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
import com.mycollab.schedule.email.format.I18nFieldFormat
import com.mycollab.schedule.email.project.ProjectTaskRelayEmailNotificationAction
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
class ProjectTaskRelayEmailNotificationActionImpl : SendMailToFollowersAction<SimpleTask>(), ProjectTaskRelayEmailNotificationAction {

    @Autowired private lateinit var projectTaskService: ProjectTaskService

    @Autowired private lateinit var projectNotificationService: ProjectNotificationSettingService

    private val mapper = TaskFieldNameMapper()

    override fun buildExtraTemplateVariables(context: MailContext<SimpleTask>) {
        val emailNotification = context.emailNotification

        val summary = "#${bean!!.taskkey} - ${bean!!.name}"
        val summaryLink = ProjectLinkGenerator.generateTaskPreviewFullLink(siteUrl, bean!!.taskkey, bean!!.projectShortname!!)

        val avatarId = if (projectMember != null) projectMember!!.memberAvatarId else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> TaskI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> TaskI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> TaskI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("projectName", bean!!.projectName!!)
        contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean!!.projectid))
        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getBeanInContext(notification: ProjectRelayEmailNotification): SimpleTask? =
            projectTaskService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getItemName(): String = StringUtils.trim(bean!!.name, 100)

    override fun getProjectName(): String = bean!!.name

    override fun getCreateSubject(context: MailContext<SimpleTask>): String =
            context.getMessage(TaskI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getCreateSubjectNotification(context: MailContext<SimpleTask>): String =
            context.getMessage(TaskI18nEnum.MAIL_CREATE_ITEM_SUBJECT, projectLink(), userLink(context), taskLink())

    override fun getUpdateSubject(context: MailContext<SimpleTask>): String =
            context.getMessage(TaskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getUpdateSubjectNotification(context: MailContext<SimpleTask>): String =
            context.getMessage(TaskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, projectLink(), userLink(context), taskLink())

    override fun getCommentSubject(context: MailContext<SimpleTask>): String =
            context.getMessage(TaskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getCommentSubjectNotification(context: MailContext<SimpleTask>): String =
            context.getMessage(TaskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, projectLink(), userLink(context), taskLink())

    private fun projectLink() = A(ProjectLinkGenerator.generateProjectLink(bean!!.projectid)).appendText(bean!!.projectName).write()

    private fun userLink(context: MailContext<SimpleTask>) = A(AccountLinkGenerator.generateUserLink(context.user.username)).appendText(context.changeByUserFullName).write()

    private fun taskLink() = A(ProjectLinkGenerator.generateTaskPreviewLink(bean!!.taskkey, bean!!.projectShortname!!)).appendText(getItemName()).write()

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getListNotifyUsersWithFilter(notification: ProjectRelayEmailNotification): List<SimpleUser> {
        val notificationSettings = projectNotificationService.findNotifications(notification.projectId, notification.saccountid)
        var notifyUsers = notification.notifyUsers

        notificationSettings.forEach { notificationSetting ->
            if (NotificationType.None.name == notificationSetting.level) {
                notifyUsers = notifyUsers.filter { it.username != notificationSetting.username }
            } else if (NotificationType.Minimal.name == notificationSetting.level) {
                val findResult = notifyUsers.find { it.username == notificationSetting.username }
                if (findResult == null) {
                    val task = projectTaskService.findById(notification.typeid.toInt(), notification.saccountid)
                    if (task != null && notificationSetting.username == task.assignuser) {
                        val prjMember = projectMemberService.getActiveUserOfProject(notificationSetting.username,
                                notificationSetting.projectid, notificationSetting.saccountid)
                        if (prjMember != null) {
                            notifyUsers += prjMember
                        }
                    }
                }
            } else if (NotificationType.Full.name == notificationSetting.level) {
                val prjMember = projectMemberService.getActiveUserOfProject(notificationSetting.username,
                        notificationSetting.projectid, notificationSetting.saccountid)
                if (prjMember != null) {
                    notifyUsers += prjMember
                }
            }
        }

        return notifyUsers
    }

    override fun getType(): String = ProjectTypeConstants.TASK

    override fun getTypeId(): String = "${bean!!.id}"

    class TaskFieldNameMapper : ItemFieldMapper() {
        init {
            put(Task.Field.name, GenericI18Enum.FORM_NAME, isColSpan = true)
            put(Task.Field.startdate, DateFieldFormat(Task.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
            put(Task.Field.enddate, DateFieldFormat(Task.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
            put(Task.Field.duedate, DateFieldFormat(Task.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
            put(Task.Field.percentagecomplete, TaskI18nEnum.FORM_PERCENTAGE_COMPLETE)
            put(Task.Field.priority, I18nFieldFormat(Task.Field.priority.name, GenericI18Enum.FORM_PRIORITY, Priority::class.java))
            put(Task.Field.assignuser, AssigneeFieldFormat(Task.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(Task.Field.isestimated, TaskI18nEnum.FORM_IS_ESTIMATED)
            put(Task.Field.remainestimate, TaskI18nEnum.FORM_REMAIN_ESTIMATE)
            put(Task.Field.milestoneid, MilestoneFieldFormat(Task.Field.milestoneid.name, MilestoneI18nEnum.SINGLE))
            put(Task.Field.parenttaskid, TaskFieldFormat(Task.Field.parenttaskid.name, TaskI18nEnum.FORM_PARENT_TASK))
            put(Task.Field.description, GenericI18Enum.FORM_DESCRIPTION)
            put(Task.Field.status, GenericI18Enum.FORM_STATUS)
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val task = context.wrappedBean as SimpleTask
            return if (task.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(task.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(task.saccountid), task.assignuser)
                val link = FormatUtils.newA(userLink, task.assignUserFullName)
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
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(context.saccountid), user.username)
                    val img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link = FormatUtils.newA(userLink, user.displayName!!)
                    FormatUtils.newLink(img, link).write()
                } else value
            }
        }
    }

    class TaskFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val task = context.wrappedBean as SimpleTask
            return if (task.parenttaskid != null) {
                val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.TASK))
                val parentTaskLink = ProjectLinkGenerator.generateTaskPreviewFullLink(context.siteUrl, task.parentTaskKey,
                        task.projectShortname!!)
                val link = FormatUtils.newA(parentTaskLink, task.name)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }

            val taskId = value.toInt()
            val taskService = AppContextUtil.getSpringBean(ProjectTaskService::class.java)
            val task = taskService.findById(taskId, context.saccountid)
            return if (task != null) {
                val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.TASK))
                val taskListLink = ProjectLinkGenerator.generateTaskPreviewFullLink(context.siteUrl, task.taskkey, task.projectShortname!!)
                val link = FormatUtils.newA(taskListLink, task.name)
                return FormatUtils.newLink(img, link).write()
            } else value
        }
    }

    class MilestoneFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val task = context.wrappedBean as SimpleTask
            return if (task.milestoneid != null) {
                val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
                val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl, task.projectid,
                        task.milestoneid)
                val link = FormatUtils.newA(milestoneLink, task.milestoneName!!)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }

            val milestoneId = value.toInt()
            val milestoneService = AppContextUtil.getSpringBean(MilestoneService::class.java)
            val milestone = milestoneService.findById(milestoneId, context.saccountid)
            return if (milestone != null) {
                val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
                val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
                        milestone.projectid, milestone.id)
                val link = FormatUtils.newA(milestoneLink, milestone.name)
                FormatUtils.newLink(img, link).write()
            } else value
        }
    }

}