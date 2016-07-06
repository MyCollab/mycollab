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
package com.mycollab.module.project.schedule.email.service

import com.mycollab.html.FormatUtils
import com.mycollab.module.project.domain.{Milestone, ProjectRelayEmailNotification, SimpleMilestone}
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction
import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.i18n.{MilestoneI18nEnum, OptionI18nEnum}
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.schedule.email.format.{DateFieldFormat, FieldFormat, I18nFieldFormat}
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.mycollab.spring.AppContextUtil
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
class ProjectMilestoneRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[SimpleMilestone] with ProjectMilestoneRelayEmailNotificationAction {

  @Autowired var milestoneService: MilestoneService = _

  private val mapper = new MilestoneFieldNameMapper

  override protected def getItemName: String = StringUtils.trim(bean.getName, 100)

  override protected def getCreateSubject(context: MailContext[SimpleMilestone]): String = context.getMessage(
    MilestoneI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  override protected def getUpdateSubject(context: MailContext[SimpleMilestone]): String = context.getMessage(
    MilestoneI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  override protected def getCommentSubject(context: MailContext[SimpleMilestone]): String = context.getMessage(
    MilestoneI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  override protected def getItemFieldMapper: ItemFieldMapper = mapper

  override protected def getBeanInContext(notification: ProjectRelayEmailNotification): SimpleMilestone =
    milestoneService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  class MilestoneFieldNameMapper extends ItemFieldMapper {
    put(Milestone.Field.name, GenericI18Enum.FORM_NAME, isColSpan = true)
    put(Milestone.Field.status, new I18nFieldFormat(Milestone.Field.status.name, GenericI18Enum.FORM_STATUS,
      classOf[OptionI18nEnum.MilestoneStatus]))
    put(Milestone.Field.owner, new AssigneeFieldFormat(Milestone.Field.owner.name, GenericI18Enum.FORM_ASSIGNEE))
    put(Milestone.Field.startdate, new DateFieldFormat(Milestone.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
    put(Milestone.Field.enddate, new DateFieldFormat(Milestone.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
    put(Milestone.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
  }

  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val milestone = context.getWrappedBean.asInstanceOf[SimpleMilestone]
      if (milestone.getOwner != null) {
        val userAvatarLink = MailUtils.getAvatarLink(milestone.getOwnerAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(milestone.getSaccountid),
          milestone.getOwner)
        val link = FormatUtils.newA(userLink, milestone.getOwnerFullName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        return new Span().write
      }
      val userService = AppContextUtil.getSpringBean(classOf[UserService])
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

  override protected def buildExtraTemplateVariables(context: MailContext[SimpleMilestone]) {
    val emailNotification = context.getEmailNotification

    val summary = bean.getName
    val summaryLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, bean.getProjectid, bean.getId)

    val avatarId = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)
    val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName

    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => MilestoneI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => MilestoneI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => MilestoneI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }
}
