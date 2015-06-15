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
package com.esofthead.mycollab.schedule.email.project.impl

import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.common.{MonitorTypeConstants, NotificationType}
import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.html.FormatUtils._
import com.esofthead.mycollab.html.LinkUtils
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.project.domain._
import com.esofthead.mycollab.module.project.i18n.{BugI18nEnum, OptionI18nEnum}
import com.esofthead.mycollab.module.project.service.{MilestoneService, ProjectMemberService, ProjectNotificationSettingService, ProjectService}
import com.esofthead.mycollab.module.project.{ProjectLinkGenerator, ProjectResources, ProjectTypeConstants}
import com.esofthead.mycollab.module.tracker.domain.{BugWithBLOBs, SimpleBug}
import com.esofthead.mycollab.module.tracker.service.BugService
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.format._
import com.esofthead.mycollab.schedule.email.project.BugRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.hp.gagawa.java.elements.{A, Img, Span, Text}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

import scala.collection.mutable
import scala.util.control.Breaks._

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class BugRelayEmailNotificationActionImpl extends SendMailToFollowersAction[SimpleBug] with BugRelayEmailNotificationAction {
    private val LOG = LoggerFactory.getLogger(classOf[BugRelayEmailNotificationActionImpl])

    @Autowired var bugService: BugService = _

    @Autowired var projectService: ProjectService = _

    @Autowired var projectMemberService: ProjectMemberService = _

    @Autowired var projectNotificationService: ProjectNotificationSettingService = _

    private val mapper = new BugFieldNameMapper

    protected def buildExtraTemplateVariables(context: MailContext[SimpleBug]) {
        val currentProject = new WebItem(bean.getProjectname, ProjectLinkGenerator.generateProjectFullLink(siteUrl, bean.getProjectid))

        val emailNotification: SimpleRelayEmailNotification = context.getEmailNotification
        val relatedProject: SimpleProject = projectService.findById(bean.getProjectid, emailNotification.getSaccountid)
        val bugCode = new WebItem(("[" + relatedProject.getShortname + "-" + bean.getBugkey + "]"),
            ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, bean.getBugkey, bean.getProjectShortName))

        val summary = bean.getSummary
        val summaryLink: String = ProjectLinkGenerator.generateBugPreviewFullLink(siteUrl, bean.getBugkey, bean.getProjectShortName)
        val projectMember: SimpleProjectMember = projectMemberService.findMemberByUsername(emailNotification.getChangeby, bean.getProjectid, emailNotification.getSaccountid)

        val avatarId: String = if (projectMember != null) projectMember.getMemberAvatarId else ""
        val userAvatar: Img = LinkUtils.newAvatar(avatarId)

        val makeChangeUser: String = userAvatar.toString + emailNotification.getChangeByUserFullName
        val actionEnum: Enum[_] = emailNotification.getAction match {
            case MonitorTypeConstants.CREATE_ACTION => BugI18nEnum.MAIL_CREATE_ITEM_HEADING
            case MonitorTypeConstants.UPDATE_ACTION => BugI18nEnum.MAIL_UPDATE_ITEM_HEADING
            case MonitorTypeConstants.ADD_COMMENT_ACTION => BugI18nEnum.MAIL_COMMENT_ITEM_HEADING
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("titles", List(currentProject, bugCode))
        contentGenerator.putVariable("summary", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    protected def getBeanInContext(context: MailContext[SimpleBug]): SimpleBug = bugService.findById(context.getTypeid.toInt, context.getSaccountid)

    protected def getItemName: String = StringUtils.trim(bean.getSummary, 100)

    protected def getCreateSubject(context: MailContext[SimpleBug]): String = context.getMessage(BugI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectname, context.getChangeByUserFullName, getItemName)

    protected def getUpdateSubject(context: MailContext[SimpleBug]): String = context.getMessage(BugI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectname, context.getChangeByUserFullName, getItemName)

    protected def getCommentSubject(context: MailContext[SimpleBug]): String = context.getMessage(BugI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectname, context.getChangeByUserFullName, getItemName)

    protected def getItemFieldMapper: ItemFieldMapper = mapper

    protected def getListNotifyUsersWithFilter(notification: ProjectRelayEmailNotification): Set[SimpleUser] = {
        import scala.collection.JavaConverters._
        val notificationSettings: List[ProjectNotificationSetting] = projectNotificationService.
            findNotifications(notification.getProjectId, notification.getSaccountid).asScala.toList
        var notifyUsers: Set[SimpleUser] = notification.getNotifyUsers.asScala.toSet

        for (notificationSetting <- notificationSettings) {
            if (NotificationType.None.name == notificationSetting.getLevel) {
                notifyUsers = notifyUsers.filter(notifyUser => !(notifyUser.getUsername == notificationSetting.getUsername))
            }
            else if (NotificationType.Minimal.name == notificationSetting.getLevel) {
                val findResult: Option[SimpleUser] = notifyUsers.find(notifyUser => notifyUser.getUsername == notificationSetting.getUsername);
                findResult match {
                    case Some(user) => notifyUsers = notifyUsers + user
                    case None => {
                        val bug: SimpleBug = bugService.findById(notification.getTypeid.toInt, notification.getSaccountid)
                        if (notificationSetting.getUsername == bug.getAssignuser) {
                            val prjMember: SimpleUser = projectMemberService.getActiveUserOfProject(notificationSetting.getUsername,
                                notificationSetting.getProjectid, notificationSetting.getSaccountid)
                            if (prjMember != null) {
                                notifyUsers = notifyUsers + prjMember
                            }
                        }
                    }
                }
            }
            else if (NotificationType.Full.name == notificationSetting.getLevel) {
                val prjMember: SimpleUser = projectMemberService.getActiveUserOfProject(notificationSetting.getUsername,
                    notificationSetting.getProjectid, notificationSetting.getSaccountid)
                if (prjMember != null) {
                    notifyUsers = notifyUsers + prjMember
                }
            }
        }

        notifyUsers
    }

    class BugFieldNameMapper extends ItemFieldMapper {
        put(BugWithBLOBs.Field.summary, BugI18nEnum.FORM_SUMMARY, isColSpan = true)
        put(BugWithBLOBs.Field.environment, BugI18nEnum.FORM_ENVIRONMENT, isColSpan = true)
        put(BugWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
        put(BugWithBLOBs.Field.assignuser, new AssigneeFieldFormat(BugWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
        put(BugWithBLOBs.Field.milestoneid, new MilestoneFieldFormat(BugWithBLOBs.Field.milestoneid.name, BugI18nEnum.FORM_PHASE))
        put(BugWithBLOBs.Field.status, new I18nFieldFormat(BugWithBLOBs.Field.status.name, BugI18nEnum.FORM_STATUS, classOf[OptionI18nEnum.BugStatus]))
        put(BugWithBLOBs.Field.resolution, new I18nFieldFormat(BugWithBLOBs.Field.resolution.name, BugI18nEnum.FORM_RESOLUTION, classOf[OptionI18nEnum.BugResolution]))
        put(BugWithBLOBs.Field.severity, new I18nFieldFormat(BugWithBLOBs.Field.severity.name, BugI18nEnum.FORM_SEVERITY, classOf[OptionI18nEnum.BugSeverity]))
        put(BugWithBLOBs.Field.priority, new I18nFieldFormat(BugWithBLOBs.Field.priority.name, BugI18nEnum.FORM_PRIORITY, classOf[OptionI18nEnum.BugPriority]))
        put(BugWithBLOBs.Field.duedate, new DateFieldFormat(BugWithBLOBs.Field.duedate.name, BugI18nEnum.FORM_DUE_DATE))
        put(BugWithBLOBs.Field.logby, new LogUserFieldFormat(BugWithBLOBs.Field.logby.name, BugI18nEnum.FORM_LOG_BY))
    }

    class MilestoneFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val bug: SimpleBug = context.getWrappedBean.asInstanceOf[SimpleBug]
            if (bug.getMilestoneid == null || bug.getMilestoneName == null) {
                new Span().write
            } else {
                val img: Text = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE));
                val milestoneLink: String = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
                    bug.getProjectid, bug.getMilestoneid)
                val link: A = newA(milestoneLink, bug.getMilestoneName)
                newLink(img, link).write
            }
        }

        def formatField(context: MailContext[_], value: String): String = {
            if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
                new Span().write
            } else {
                try {
                    val milestoneId: Int = value.toInt
                    val milestoneService: MilestoneService = ApplicationContextUtil.getSpringBean(classOf[MilestoneService])
                    val milestone: SimpleMilestone = milestoneService.findById(milestoneId, context.getUser.getAccountId)
                    if (milestone != null) {
                        val img: Text = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE));
                        val milestoneLink: String = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl, milestone
                            .getProjectid, milestone.getId)
                        val link: A = newA(milestoneLink, milestone.getName)
                        return newLink(img, link).write
                    }
                }
                catch {
                    case e: Exception => LOG.error("Error", e)
                }
                value
            }
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val bug: SimpleBug = context.getWrappedBean.asInstanceOf[SimpleBug]
            if (bug.getAssignuser != null) {
                val userAvatarLink: String = MailUtils.getAvatarLink(bug.getAssignUserAvatarId, 16)
                val img: Img = newImg("avatar", userAvatarLink)
                val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(bug.getSaccountid), bug.getAssignuser)
                val link: A = newA(userLink, bug.getAssignuserFullName)
                newLink(img, link).write
            }
            else {
                new Span().write
            }
        }

        def formatField(context: MailContext[_], value: String): String = {
            if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
                new Span().write
            } else {
                val userService: UserService = ApplicationContextUtil.getSpringBean(classOf[UserService])
                val user: SimpleUser = userService.findUserByUserNameInAccount(value, context.getUser.getAccountId)
                if (user != null) {
                    val userAvatarLink: String = MailUtils.getAvatarLink(user.getAvatarid, 16)
                    val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
                    val img: Img = newImg("avatar", userAvatarLink)
                    val link: A = newA(userLink, user.getDisplayName)
                    newLink(img, link).write
                } else
                    value
            }
        }
    }

    class LogUserFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val bug: SimpleBug = context.getWrappedBean.asInstanceOf[SimpleBug]
            if (bug.getLogby != null) {
                val userAvatarLink: String = MailUtils.getAvatarLink(bug.getLoguserAvatarId, 16)
                val img: Img = newImg("avatar", userAvatarLink)
                val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(bug.getSaccountid), bug.getLogby)
                val link: A = newA(userLink, bug.getLoguserFullName)
                newLink(img, link).write
            }
            else
                new Span().write
        }

        def formatField(context: MailContext[_], value: String): String = {
            if (org.apache.commons.lang3.StringUtils.isBlank(value))
                return new Span().write

            val userService: UserService = ApplicationContextUtil.getSpringBean(classOf[UserService])
            val user: SimpleUser = userService.findUserByUserNameInAccount(value, context.getUser.getAccountId)
            if (user != null) {
                val userAvatarLink: String = MailUtils.getAvatarLink(user.getAvatarid, 16)
                val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
                val img: Img = newImg("avatar", userAvatarLink)
                val link: A = newA(userLink, user.getDisplayName)
                newLink(img, link).write
            } else
                value
        }
    }

}