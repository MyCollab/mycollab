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

import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.tracker.domain.{SimpleVersion, Version}
import com.mycollab.module.tracker.service.VersionService
import com.mycollab.schedule.email.project.VersionRelayEmailNotificationAction
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.i18n.{GenericI18Enum, OptionI18nEnum}
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.i18n.VersionI18nEnum
import com.mycollab.schedule.email.format.{DateFieldFormat, I18nFieldFormat}
import com.mycollab.schedule.email.{ItemFieldMapper, MailContext}
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
class VersionRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[SimpleVersion] with VersionRelayEmailNotificationAction {
  @Autowired var versionService: VersionService = _
  private val mapper = new VersionFieldNameMapper

  protected def buildExtraTemplateVariables(context: MailContext[SimpleVersion]) {
    val emailNotification = context.getEmailNotification

    val summary = bean.getVersionname
    val summaryLink = ProjectLinkGenerator.generateBugComponentPreviewFullLink(siteUrl, bean.getProjectid, bean.getId)

    val avatarId = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => VersionI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => VersionI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => VersionI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }
  
    contentGenerator.putVariable("projectName", bean.getProjectName)
    contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean.getProjectid))
    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("summary", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  protected def getItemName: String = StringUtils.trim(bean.getDescription, 100)

  protected def getCreateSubject(context: MailContext[SimpleVersion]): String = context.getMessage(
    VersionI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getUpdateSubject(context: MailContext[SimpleVersion]): String = context.getMessage(
    VersionI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getCommentSubject(context: MailContext[SimpleVersion]): String = context.getMessage(
    VersionI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)

  protected def getItemFieldMapper: ItemFieldMapper = mapper

  protected def getBeanInContext(notification: ProjectRelayEmailNotification): SimpleVersion =
    versionService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  class VersionFieldNameMapper extends ItemFieldMapper {
    put(Version.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    put(Version.Field.status, new I18nFieldFormat(Version.Field.status.name, GenericI18Enum.FORM_STATUS,
      classOf[OptionI18nEnum.StatusI18nEnum]))
    put(Version.Field.versionname, GenericI18Enum.FORM_NAME)
    put(Version.Field.duedate, new DateFieldFormat(Version.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
  }

}
