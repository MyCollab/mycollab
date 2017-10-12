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
package com.mycollab.module.crm.schedule.email.service

import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.CampaignWithBLOBs
import com.mycollab.module.crm.domain.SimpleCampaign
import com.mycollab.module.crm.i18n.CampaignI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum
import com.mycollab.module.crm.service.CampaignService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.CampaignRelayEmailNotificationAction
import com.mycollab.schedule.email.format.CurrencyFieldFormat
import com.mycollab.schedule.email.format.DateFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
import com.mycollab.schedule.email.format.I18nFieldFormat
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
class CampaignRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleCampaign>(), CampaignRelayEmailNotificationAction {
    @Autowired private lateinit var campaignService: CampaignService
    private val mapper: CampaignFieldNameMapper = CampaignFieldNameMapper()

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCampaign? =
            campaignService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getCreateSubjectKey(): Enum<*> = CampaignI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = CampaignI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getItemName(): String = StringUtils.trim(bean!!.campaignname, 100)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleCampaign>) {
        val summary = bean!!.campaignname
        val summaryLink = CrmLinkGenerator.generateCampaignPreviewFullLink(siteUrl, bean!!.id)

        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = userAvatar.write() + " " + emailNotification.changeByUserFullName
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> CampaignI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> CampaignI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> CampaignI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getUpdateSubjectKey(): Enum<*> = CampaignI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    class CampaignFieldNameMapper : ItemFieldMapper() {
        init {
            put(CampaignWithBLOBs.Field.campaignname, GenericI18Enum.FORM_NAME, true)
            put(CampaignWithBLOBs.Field.status, I18nFieldFormat(CampaignWithBLOBs.Field.status.name, GenericI18Enum.FORM_STATUS,
                    OptionI18nEnum.CampaignStatus::class.java))
            put(CampaignWithBLOBs.Field.type, I18nFieldFormat(CampaignWithBLOBs.Field.type.name, GenericI18Enum.FORM_TYPE, OptionI18nEnum.CampaignType::class.java))
            put(CampaignWithBLOBs.Field.currencyid, CurrencyFieldFormat(CampaignWithBLOBs.Field.currencyid.name, GenericI18Enum.FORM_CURRENCY))
            put(CampaignWithBLOBs.Field.budget, CampaignI18nEnum.FORM_BUDGET)
            put(CampaignWithBLOBs.Field.expectedcost, CampaignI18nEnum.FORM_EXPECTED_COST)
            put(CampaignWithBLOBs.Field.expectedrevenue, CampaignI18nEnum.FORM_EXPECTED_REVENUE)
            put(CampaignWithBLOBs.Field.actualcost, CampaignI18nEnum.FORM_ACTUAL_COST)
            put(CampaignWithBLOBs.Field.assignuser, AssigneeFieldFormat(CampaignWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(CampaignWithBLOBs.Field.startdate, DateFieldFormat(CampaignWithBLOBs.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
            put(CampaignWithBLOBs.Field.enddate, DateFieldFormat(CampaignWithBLOBs.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
            put(CampaignWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val campaign = context.wrappedBean as SimpleCampaign
            return if (campaign.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(campaign.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
                        campaign.saccountid), campaign.assignuser)
                val link = FormatUtils.newA(userLink, campaign.assignUserFullName)
                FormatUtils.newLink(img, link).write()
            } else {
                ""
            }
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            return if (StringUtils.isBlank(value)) {
                ""
            } else {
                val userService = AppContextUtil.getSpringBean(UserService::class.java)
                val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
                return if (user != null) {
                    val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(
                            user.accountId), user.username)
                    val img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link = FormatUtils.newA(userLink, user.displayName)
                    FormatUtils.newLink(img, link).write()
                } else
                    value
            }
        }
    }

}