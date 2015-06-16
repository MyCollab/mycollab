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

import com.esofthead.mycollab.common.MonitorTypeConstants
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.html.{FormatUtils, LinkUtils}
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.project.domain._
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum
import com.esofthead.mycollab.module.project.service.{MilestoneService, ProjectService, ProjectTaskListService}
import com.esofthead.mycollab.module.project.{ProjectLinkGenerator, ProjectResources, ProjectTypeConstants}
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.format.{FieldFormat, WebItem}
import com.esofthead.mycollab.schedule.email.project.ProjectTaskGroupRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.hp.gagawa.java.elements.{A, Img, Span, Text}
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd.
 * @since 4.6.0
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class ProjectTaskGroupRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[SimpleTaskList] with
ProjectTaskGroupRelayEmailNotificationAction {

    private val LOG = LoggerFactory.getLogger(classOf[ProjectTaskGroupRelayEmailNotificationActionImpl])

    @Autowired var projectTaskListService: ProjectTaskListService = _

    @Autowired var projectService: ProjectService = _

    private val mapper = new ProjectFieldNameMapper

    protected def getItemName: String = StringUtils.trim(bean.getName, 100)

    protected def getCreateSubject(context: MailContext[SimpleTaskList]): String = context.getMessage(
        TaskGroupI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

    protected def getUpdateSubject(context: MailContext[SimpleTaskList]): String = context.getMessage(
        TaskGroupI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

    protected def getCommentSubject(context: MailContext[SimpleTaskList]): String = context.getMessage(
        TaskGroupI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

    protected def getItemFieldMapper: ItemFieldMapper = mapper

    protected def getBeanInContext(context: MailContext[SimpleTaskList]): SimpleTaskList = projectTaskListService.findById(
        context.getTypeid.toInt, context.getSaccountid)

    protected def buildExtraTemplateVariables(context: MailContext[SimpleTaskList]) {
        val emailNotification: SimpleRelayEmailNotification = context.getEmailNotification
        val relatedProject: SimpleProject = projectService.findById(bean.getProjectid, emailNotification.getSaccountid)

        val currentProject = new WebItem(relatedProject.getName, ProjectLinkGenerator.generateProjectFullLink(siteUrl,
            bean.getProjectid))

        val summary: String = bean.getName
        val summaryLink: String = ProjectLinkGenerator.generateTaskGroupPreviewFullLink(siteUrl, bean.getProjectid, bean.getId)
        val projectMember: SimpleProjectMember = projectMemberService.findMemberByUsername(emailNotification.getChangeby,
            bean.getProjectid, emailNotification.getSaccountid)

        val avatarId: String = if (projectMember != null) projectMember.getMemberAvatarId else ""
        val userAvatar: Img = LinkUtils.newAvatar(avatarId)

        val makeChangeUser: String = userAvatar.toString + emailNotification.getChangeByUserFullName
        val headerEnum: Enum[_] = emailNotification.getAction match {
            case MonitorTypeConstants.CREATE_ACTION => TaskGroupI18nEnum.MAIL_CREATE_ITEM_HEADING
            case MonitorTypeConstants.UPDATE_ACTION => TaskGroupI18nEnum.MAIL_UPDATE_ITEM_HEADING
            case MonitorTypeConstants.ADD_COMMENT_ACTION => TaskGroupI18nEnum.MAIL_COMMENT_ITEM_HEADING
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(headerEnum, makeChangeUser))
        contentGenerator.putVariable("titles", List(currentProject))
        contentGenerator.putVariable("summary", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    class ProjectFieldNameMapper extends ItemFieldMapper {
        put(TaskList.Field.name, TaskGroupI18nEnum.FORM_NAME_FIELD, isColSpan = true)
        put(TaskList.Field.owner, new AssigneeFieldFormat(TaskList.Field.owner.name, GenericI18Enum.FORM_ASSIGNEE))
        put(TaskList.Field.status, TaskGroupI18nEnum.FORM_STATUS)
        put(TaskList.Field.milestoneid, new MilestoneFieldFormat(TaskList.Field.milestoneid.name, TaskGroupI18nEnum.FORM_PHASE_FIELD, true))
        put(TaskList.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
        def formatField(context: MailContext[_]): String = {
            val taskList: SimpleTaskList = context.getWrappedBean.asInstanceOf[SimpleTaskList]
            if (taskList.getOwner != null) {
                val userAvatarLink: String = MailUtils.getAvatarLink(taskList.getOwnerAvatarId, 16)
                val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(taskList.getSaccountid),
                    taskList.getOwner)
                val link: A = FormatUtils.newA(userLink, taskList.getOwnerFullName)
                FormatUtils.newLink(img, link).write
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
                    val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId),
                        user.getUsername)
                    val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link: A = FormatUtils.newA(userLink, user.getDisplayName)
                    FormatUtils.newLink(img, link).write
                } else
                    value
            }
        }
    }

    class MilestoneFieldFormat(fieldName: String, displayName: Enum[_], isColSpan: Boolean) extends FieldFormat(fieldName,
        displayName, isColSpan) {
        def formatField(context: MailContext[_]): String = {
            val taskList: SimpleTaskList = context.wrappedBean.asInstanceOf[SimpleTaskList]
            if (taskList.getMilestoneid != null) {
                val img: Text = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE));
                val milestoneLink: String = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl, taskList
                    .getProjectid, taskList.getMilestoneid)
                val link: A = FormatUtils.newA(milestoneLink, taskList.getMilestoneName)
                FormatUtils.newLink(img, link).write
            }
            else {
                new Span().write
            }
        }

        def formatField(context: MailContext[_], value: String): String = {
            if (org.apache.commons.lang3.StringUtils.isBlank(value)) {
                return new Span().write
            }
            try {
                val milestoneId: Int = value.toInt
                val milestoneService: MilestoneService = ApplicationContextUtil.getSpringBean(classOf[MilestoneService])
                val milestone: SimpleMilestone = milestoneService.findById(milestoneId, context.getUser.getAccountId)
                if (milestone != null) {
                    val img: Text = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
                    val milestoneLink: String = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
                        milestone.getProjectid, milestone.getId)
                    val link: A = FormatUtils.newA(milestoneLink, milestone.getName)
                    return FormatUtils.newLink(img, link).write
                }
            }
            catch {
                case e: Exception => LOG.error("Error", e)
            }
            value
        }
    }

}
