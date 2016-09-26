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
package com.mycollab.module.crm.schedule.email.service

import com.mycollab.html.FormatUtils
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.crm.CampaignRelayEmailNotificationAction
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.{CampaignWithBLOBs, SimpleCampaign}
import com.mycollab.module.crm.i18n.CampaignI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum.{CampaignStatus, CampaignType}
import com.mycollab.module.crm.service.CampaignService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.schedule.email.format.{CurrencyFieldFormat, DateFieldFormat, FieldFormat, I18nFieldFormat}
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
class CampaignRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleCampaign] with CampaignRelayEmailNotificationAction {
  @Autowired var campaignService: CampaignService = _
  private val mapper: CampaignFieldNameMapper = new CampaignFieldNameMapper

  override protected def getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCampaign =
    campaignService.findById(notification.getTypeid.toInt, notification.getSaccountid)

  override protected def getCreateSubjectKey: Enum[_] = CampaignI18nEnum.MAIL_CREATE_ITEM_SUBJECT

  override protected def getCommentSubjectKey: Enum[_] = CampaignI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

  override protected def getItemFieldMapper: ItemFieldMapper = mapper

  override protected def getItemName: String = StringUtils.trim(bean.getCampaignname, 100)

  override protected def buildExtraTemplateVariables(context: MailContext[SimpleCampaign]): Unit = {
    val summary = bean.getCampaignname
    val summaryLink = CrmLinkGenerator.generateCampaignPreviewFullLink(siteUrl, bean.getId)

    val emailNotification = context.getEmailNotification

    val avatarId = if (changeUser != null) changeUser.getAvatarid else ""
    val userAvatar = LinkUtils.newAvatar(avatarId)

    val makeChangeUser = userAvatar.toString + " " + emailNotification.getChangeByUserFullName
    val actionEnum = emailNotification.getAction match {
      case MonitorTypeConstants.CREATE_ACTION => CampaignI18nEnum.MAIL_CREATE_ITEM_HEADING
      case MonitorTypeConstants.UPDATE_ACTION => CampaignI18nEnum.MAIL_UPDATE_ITEM_HEADING
      case MonitorTypeConstants.ADD_COMMENT_ACTION => CampaignI18nEnum.MAIL_COMMENT_ITEM_HEADING
    }

    contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
    contentGenerator.putVariable("name", summary)
    contentGenerator.putVariable("summaryLink", summaryLink)
  }

  override protected def getUpdateSubjectKey: Enum[_] = CampaignI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

  class CampaignFieldNameMapper extends ItemFieldMapper {
    put(CampaignWithBLOBs.Field.campaignname, GenericI18Enum.FORM_NAME, isColSpan = true)
    put(CampaignWithBLOBs.Field.status, new I18nFieldFormat(CampaignWithBLOBs.Field.status.name, GenericI18Enum.FORM_STATUS,
      classOf[CampaignStatus]))
    put(CampaignWithBLOBs.Field.`type`, new I18nFieldFormat(CampaignWithBLOBs.Field.`type`.name, GenericI18Enum.FORM_TYPE, classOf[CampaignType]))
    put(CampaignWithBLOBs.Field.currencyid, new CurrencyFieldFormat(CampaignWithBLOBs.Field.currencyid.name, GenericI18Enum.FORM_CURRENCY))
    put(CampaignWithBLOBs.Field.budget, CampaignI18nEnum.FORM_BUDGET)
    put(CampaignWithBLOBs.Field.expectedcost, CampaignI18nEnum.FORM_EXPECTED_COST)
    put(CampaignWithBLOBs.Field.expectedrevenue, CampaignI18nEnum.FORM_EXPECTED_REVENUE)
    put(CampaignWithBLOBs.Field.actualcost, CampaignI18nEnum.FORM_ACTUAL_COST)
    put(CampaignWithBLOBs.Field.assignuser, new AssigneeFieldFormat(CampaignWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
    put(CampaignWithBLOBs.Field.startdate, new DateFieldFormat(CampaignWithBLOBs.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
    put(CampaignWithBLOBs.Field.enddate, new DateFieldFormat(CampaignWithBLOBs.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
    put(CampaignWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
  }

  class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

    def formatField(context: MailContext[_]): String = {
      val campaign = context.getWrappedBean.asInstanceOf[SimpleCampaign]
      if (campaign.getAssignuser != null) {
        val userAvatarLink = MailUtils.getAvatarLink(campaign.getAssignUserAvatarId, 16)
        val img = FormatUtils.newImg("avatar", userAvatarLink)
        val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
          campaign.getSaccountid), campaign.getAssignuser)
        val link = FormatUtils.newA(userLink, campaign.getAssignUserFullName)
        FormatUtils.newLink(img, link).write
      }
      else {
        ""
      }
    }

    def formatField(context: MailContext[_], value: String): String = {
      if (StringUtils.isBlank(value)) {
        ""
      } else {
        val userService = AppContextUtil.getSpringBean(classOf[UserService])
        val user = userService.findUserByUserNameInAccount(value, context.getUser.getAccountId)
        if (user != null) {
          val userAvatarLink = MailUtils.getAvatarLink(user.getAvatarid, 16)
          val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
            user.getAccountId), user.getUsername)
          val img = FormatUtils.newImg("avatar", userAvatarLink)
          val link = FormatUtils.newA(userLink, user.getDisplayName)
          FormatUtils.newLink(img, link).write
        } else
          value
      }
    }
  }

}
