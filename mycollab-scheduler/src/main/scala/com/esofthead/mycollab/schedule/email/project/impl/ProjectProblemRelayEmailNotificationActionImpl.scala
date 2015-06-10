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
import com.esofthead.mycollab.common.i18n.{GenericI18Enum, OptionI18nEnum}
import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.html.{FormatUtils, LinkUtils}
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.project.ProjectLinkGenerator
import com.esofthead.mycollab.module.project.domain.{Problem, SimpleProblem, SimpleProject, SimpleProjectMember}
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum
import com.esofthead.mycollab.module.project.service.{ProblemService, ProjectService}
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.format._
import com.esofthead.mycollab.schedule.email.project.ProjectProblemRelayEmailNotificationAction
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
class ProjectProblemRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[SimpleProblem] with ProjectProblemRelayEmailNotificationAction {

    @Autowired var problemService: ProblemService = _

    @Autowired var projectService: ProjectService = _

    private val mapper = new ProjectFieldNameMapper

    protected def getItemName: String = StringUtils.trim(bean.getIssuename, 100)

    protected def getCreateSubject(context: MailContext[SimpleProblem]): String = context.getMessage(ProblemI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

    protected def getUpdateSubject(context: MailContext[SimpleProblem]): String = context.getMessage(ProblemI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

    protected def getCommentSubject(context: MailContext[SimpleProblem]): String = context.getMessage(ProblemI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

    protected def getItemFieldMapper: ItemFieldMapper = mapper

    protected def getBeanInContext(context: MailContext[SimpleProblem]): SimpleProblem = problemService.findById(context.getTypeid.toInt, context.getSaccountid)

    protected def buildExtraTemplateVariables(context: MailContext[SimpleProblem]) {
        val emailNotification: SimpleRelayEmailNotification = context.getEmailNotification
        val relatedProject: SimpleProject = projectService.findById(bean.getProjectid, emailNotification.getSaccountid)
        val currentProject = new WebItem(relatedProject.getName, ProjectLinkGenerator.generateProjectFullLink(siteUrl,
            bean.getProjectid))

        val summary: String = bean.getIssuename
        val summaryLink: String = ProjectLinkGenerator.generateProblemPreviewFullLink(siteUrl, bean.getProjectid, bean.getId)
        val projectMember: SimpleProjectMember = projectMemberService.findMemberByUsername(emailNotification.getChangeby, bean.getProjectid, emailNotification.getSaccountid)

        val avatarId: String = if (projectMember != null) projectMember.getMemberAvatarId else ""
        val userAvatar: Img = LinkUtils.newAvatar(avatarId)

        val makeChangeUser: String = userAvatar.toString + emailNotification.getChangeByUserFullName
        val actionEnum: Enum[_] = emailNotification.getAction match {
            case MonitorTypeConstants.CREATE_ACTION => ProblemI18nEnum.MAIL_CREATE_ITEM_HEADING
            case MonitorTypeConstants.UPDATE_ACTION => ProblemI18nEnum.MAIL_UPDATE_ITEM_HEADING
            case MonitorTypeConstants.ADD_COMMENT_ACTION => ProblemI18nEnum.MAIL_COMMENT_ITEM_HEADING
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("titles", List(currentProject))
        contentGenerator.putVariable("summary", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    class ProjectFieldNameMapper extends ItemFieldMapper {
        put(Problem.Field.issuename, ProblemI18nEnum.FORM_NAME, isColSpan = true)
        put(Problem.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
        put(Problem.Field.datedue, new DateFieldFormat(Problem.Field.datedue.name, ProblemI18nEnum.FORM_DATE_DUE))
        put(Problem.Field.status, new I18nFieldFormat(Problem.Field.status.name, ProblemI18nEnum.FORM_STATUS, classOf[OptionI18nEnum.StatusI18nEnum]))
        put(Problem.Field.impact, ProblemI18nEnum.FORM_IMPACT)
        put(Problem.Field.priority, ProblemI18nEnum.FORM_PRIORITY)
        put(Problem.Field.assigntouser, new AssigneeFieldFormat(Problem.Field.assigntouser.name, GenericI18Enum.FORM_ASSIGNEE))
        put(Problem.Field.raisedbyuser, new RaisedByFieldFormat(Problem.Field.raisedbyuser.name, ProblemI18nEnum.FORM_RAISED_BY))
        put(Problem.Field.resolution, ProblemI18nEnum.FORM_RESOLUTION, isColSpan = true)
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val problem: SimpleProblem = context.getWrappedBean.asInstanceOf[SimpleProblem]
            if (problem.getAssigntouser != null) {
                val userAvatarLink: String = MailUtils.getAvatarLink(problem.getAssignUserAvatarId, 16)
                val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(problem.getSaccountid), problem.getAssigntouser)
                val link: A = FormatUtils.newA(userLink, problem.getAssignedUserFullName)
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
                    val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
                    val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link: A = FormatUtils.newA(userLink, user.getDisplayName)
                    FormatUtils.newLink(img, link).write
                } else
                    value
            }
        }
    }

    class RaisedByFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val problem: SimpleProblem = context.getWrappedBean.asInstanceOf[SimpleProblem]
            if (problem.getRaisedbyuser != null) {
                val userAvatarLink: String = MailUtils.getAvatarLink(problem.getRaisedByUserAvatarId, 16)
                val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(problem.getSaccountid), problem.getRaisedbyuser)
                val link: A = FormatUtils.newA(userLink, problem.getRaisedByUserFullName)
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
                    val userLink: String = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.getAccountId), user.getUsername)
                    val img: Img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link: A = FormatUtils.newA(userLink, user.getDisplayName)
                    FormatUtils.newLink(img, link).write
                } else
                    value
            }
        }
    }

}
