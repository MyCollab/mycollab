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
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.tracker.domain.{Component, SimpleComponent}
import com.mycollab.module.tracker.service.ComponentService
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.project.ComponentRelayEmailNotificationAction
import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.i18n.{GenericI18Enum, OptionI18nEnum}
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.i18n.ComponentI18nEnum
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.schedule.email.format.{FieldFormat, I18nFieldFormat}
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.mycollab.spring.AppContextUtil
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
class ComponentRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[SimpleComponent] with ComponentRelayEmailNotificationAction {
  @Autowired var componentService: ComponentService = _
  private val mapper: ComponentFieldNameMapper = new ComponentFieldNameMapper

  protected def buildExtraTemplateVariables(context: MailContext[SimpleComponent]) {
    val emailNotification = context.getEmailNotification

    val summary = bean.getComponentname
    val summaryLink = ProjectLinkGenerator.generateBugComponentPreviewFullLink(siteUrl, bean.getProjectid, bean.getId)

    val avatarId = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => ComponentI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => ComponentI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => ComponentI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }
  
    contentGenerator.putVariable("projectName", bean.getProjectName)
    contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean.getProjectid))
    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  protected def getUpdateSubject(context: MailContext[SimpleComponent]): String = context.getMessage(
    ComponentI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getBeanInContext(notification: ProjectRelayEmailNotification): SimpleComponent =
    componentService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  protected def getItemName: String = StringUtils.trim(bean.getDescription, 100)

  protected def getCreateSubject(context: MailContext[SimpleComponent]): String = context.getMessage(
    ComponentI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getCommentSubject(context: MailContext[SimpleComponent]): String = context.getMessage(
    ComponentI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getItemFieldMapper: ItemFieldMapper = mapper

  class ComponentFieldNameMapper extends ItemFieldMapper {
    put(Component.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    put(Component.Field.status, new I18nFieldFormat(Component.Field.status.name, GenericI18Enum.FORM_STATUS,
      classOf[OptionI18nEnum.StatusI18nEnum]))
    put(Component.Field.userlead, new LeadFieldFormat(Component.Field.userlead.name, ComponentI18nEnum.FORM_LEAD))
  }

  class LeadFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    def formatField(context: MailContext[_]): String = {
      val component = context.getWrappedBean.asInstanceOf[SimpleComponent]
      if (component.getUserlead != null) {
        val userAvatarLink = MailUtils.getAvatarLink(component.getUserLeadAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(component.getSaccountid),
          component.getUserlead)
        val link = FormatUtils.newA(userLink, component.getUserLeadFullName)
        FormatUtils.newLink(img, link).write
      }
      else
        new Span().write
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

}
