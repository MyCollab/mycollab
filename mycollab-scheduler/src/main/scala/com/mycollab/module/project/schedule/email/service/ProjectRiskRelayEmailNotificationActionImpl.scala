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

import com.hp.gagawa.java.elements.{Span, Text}
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.i18n.{GenericI18Enum, OptionI18nEnum}
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.{FormatUtils, LinkUtils}
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.domain.{ProjectRelayEmailNotification, Risk, SimpleRisk}
import com.mycollab.module.project.i18n.OptionI18nEnum.{RiskConsequence, RiskProbability}
import com.mycollab.module.project.i18n.RiskI18nEnum
import com.mycollab.module.project.service.{MilestoneService, RiskService}
import com.mycollab.module.project.{ProjectLinkGenerator, ProjectResources, ProjectTypeConstants}
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.format.{DateFieldFormat, FieldFormat, I18nFieldFormat}
import com.mycollab.schedule.email.project.ProjectRiskRelayEmailNotificationAction
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
class ProjectRiskRelayEmailNotificationActionImpl extends SendMailToAllMembersAction[SimpleRisk] with ProjectRiskRelayEmailNotificationAction {
  @Autowired var riskService: RiskService = _
  private val mapper = new ProjectFieldNameMapper
  
  override protected def getItemName: String = StringUtils.trim(bean.getName, 100)
  
  override protected def getProjectName: String = bean.getProjectName
  
  override protected def getCreateSubject(context: MailContext[SimpleRisk]): String = context.getMessage(
    RiskI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)
  
  override protected def getUpdateSubject(context: MailContext[SimpleRisk]): String = context.getMessage(
    RiskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)
  
  override protected def getCommentSubject(context: MailContext[SimpleRisk]): String = context.getMessage(
    RiskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean.getProjectName, context.getChangeByUserFullName, getItemName)
  
  override protected def getItemFieldMapper: ItemFieldMapper = mapper
  
  override protected def getBeanInContext(notification: ProjectRelayEmailNotification): SimpleRisk =
    riskService.findById(notification.getTypeid.toInt, notification.getSaccountid)
  
  override protected def buildExtraTemplateVariables(context: MailContext[SimpleRisk]) {
    val emailNotification = context.getEmailNotification
    val summary = bean.getName
    val summaryLink = ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl, bean.getProjectid, bean.getId)
    
    val avatarId = if (projectMember != null) projectMember.getMemberAvatarId else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)
    
    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => RiskI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => RiskI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => RiskI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }
    
    contentGenerator.putVariable("projectName", bean.getProjectName)
    contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean.getProjectid))
    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("name", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }
  
  class ProjectFieldNameMapper extends ItemFieldMapper {
    put(Risk.Field.name, GenericI18Enum.FORM_NAME, isColSpan = true)
    put(Risk.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    put(Risk.Field.probalitity, new I18nFieldFormat(Risk.Field.probalitity.name, RiskI18nEnum.FORM_PROBABILITY,
      classOf[RiskProbability]))
    put(Risk.Field.consequence, new I18nFieldFormat(Risk.Field.consequence.name, RiskI18nEnum.FORM_CONSEQUENCE, classOf[RiskConsequence]))
    put(Risk.Field.startdate, new DateFieldFormat(Risk.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
    put(Risk.Field.enddate, new DateFieldFormat(Risk.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
    put(Risk.Field.duedate, new DateFieldFormat(Risk.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
    put(Risk.Field.status, new I18nFieldFormat(Risk.Field.status.name, GenericI18Enum.FORM_STATUS,
      classOf[OptionI18nEnum.StatusI18nEnum]))
    put(Risk.Field.milestoneid, new MilestoneFieldFormat(Risk.Field.milestoneid.name, RiskI18nEnum.FORM_PHASE))
    put(Risk.Field.assignuser, new AssigneeFieldFormat(Risk.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(Risk.Field.createduser, new RaisedByFieldFormat(Risk.Field.createduser.name, RiskI18nEnum.FORM_RAISED_BY))
    put(Risk.Field.response, RiskI18nEnum.FORM_RESPONSE, isColSpan = true)
  }
  
  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    def formatField(context: MailContext[_]): String = {
      val risk = context.getWrappedBean.asInstanceOf[SimpleRisk]
      if (risk.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(risk.getAssignToUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(risk.getSaccountid),
          risk.getAssignuser)
        val link = FormatUtils.newA(userLink, risk.getAssignedToUserFullName)
        FormatUtils.newLink(img, link).write
      }
      else {
        new Span().write
      }
    }
    
    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        new Span().write
      } else {
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
  
  class RaisedByFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    def formatField(context: MailContext[_]): String = {
      val risk = context.getWrappedBean.asInstanceOf[SimpleRisk]
      if (risk.getCreateduser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(risk.getRaisedByUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(risk.getSaccountid),
          risk.getCreateduser)
        val link = FormatUtils.newA(userLink, risk.getRaisedByUserFullName)
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
  
  class MilestoneFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {
    
    def formatField(context: MailContext[_]): String = {
      val risk = context.getWrappedBean.asInstanceOf[SimpleRisk]
      if (risk.getMilestoneid != null) {
        val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
        val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl, risk.getProjectid,
          risk.getMilestoneid)
        val link = FormatUtils.newA(milestoneLink, risk.getMilestoneName)
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
      val milestoneId = value.toInt
      val milestoneService = AppContextUtil.getSpringBean(classOf[MilestoneService])
      val milestone = milestoneService.findById(milestoneId, context.getUser.getAccountId)
      if (milestone != null) {
        val img = new Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
        val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
          milestone.getProjectid, milestone.getId)
        val link = FormatUtils.newA(milestoneLink, milestone.getName)
        return FormatUtils.newLink(img, link).write
      }
      value
    }
  }
  
}
