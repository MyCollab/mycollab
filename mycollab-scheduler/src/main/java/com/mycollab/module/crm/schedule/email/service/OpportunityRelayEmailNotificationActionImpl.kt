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

import com.hp.gagawa.java.elements.Span
import com.hp.gagawa.java.elements.Text
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.CrmResources
import com.mycollab.module.crm.CrmTypeConstants
import com.mycollab.module.crm.domain.Opportunity
import com.mycollab.module.crm.domain.SimpleOpportunity
import com.mycollab.module.crm.i18n.OpportunityI18nEnum
import com.mycollab.module.crm.i18n.OptionI18nEnum
import com.mycollab.module.crm.service.AccountService
import com.mycollab.module.crm.service.CampaignService
import com.mycollab.module.crm.service.OpportunityService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.OpportunityRelayEmailNotificationAction
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
class OpportunityRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleOpportunity>(), OpportunityRelayEmailNotificationAction {
    @Autowired private lateinit var opportunityService: OpportunityService
    private val mapper = OpportunityFieldNameMapper()

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleOpportunity? =
            opportunityService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getCreateSubjectKey(): Enum<*> = OpportunityI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getUpdateSubjectKey(): Enum<*> = OpportunityI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = OpportunityI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getItemName(): String = StringUtils.trim(bean!!.opportunityname, 100)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleOpportunity>) {
        val summary = bean!!.opportunityname
        val summaryLink = CrmLinkGenerator.generateOpportunityPreviewFullLink(siteUrl, bean!!.id)

        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> OpportunityI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> OpportunityI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> OpportunityI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    class OpportunityFieldNameMapper : ItemFieldMapper() {
        init {
            put(Opportunity.Field.opportunityname, GenericI18Enum.FORM_NAME)
            put(Opportunity.Field.accountid, AccountFieldFormat(Opportunity.Field.accountid.name, OpportunityI18nEnum.FORM_ACCOUNT_NAME))
            put(Opportunity.Field.currencyid, CurrencyFieldFormat(Opportunity.Field.currencyid.name, GenericI18Enum.FORM_CURRENCY))
            put(Opportunity.Field.expectedcloseddate, DateFieldFormat(Opportunity.Field.expectedcloseddate.name,
                    OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE))
            put(Opportunity.Field.amount, OpportunityI18nEnum.FORM_AMOUNT)
            put(Opportunity.Field.opportunitytype, I18nFieldFormat(Opportunity.Field.opportunitytype.name, GenericI18Enum.FORM_TYPE,
                    OptionI18nEnum.OpportunityType::class.java))
            put(Opportunity.Field.salesstage, I18nFieldFormat(Opportunity.Field.salesstage.name, OpportunityI18nEnum.FORM_SALE_STAGE,
                    OptionI18nEnum.OpportunitySalesStage::class.java))
            put(Opportunity.Field.source, I18nFieldFormat(Opportunity.Field.source.name, OpportunityI18nEnum.FORM_LEAD_SOURCE,
                    OptionI18nEnum.OpportunityLeadSource::class.java))
            put(Opportunity.Field.probability, OpportunityI18nEnum.FORM_PROBABILITY)
            put(Opportunity.Field.campaignid, CampaignFieldFormat(Opportunity.Field.campaignid.name, OpportunityI18nEnum.FORM_CAMPAIGN_NAME))
            put(Opportunity.Field.nextstep, OpportunityI18nEnum.FORM_NEXT_STEP)
            put(Opportunity.Field.assignuser, AssigneeFieldFormat(Opportunity.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(Opportunity.Field.description, GenericI18Enum.FORM_DESCRIPTION, true)
        }
    }

    class AccountFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val simpleOpportunity = context.wrappedBean as SimpleOpportunity
            return if (simpleOpportunity.accountid != null) {
                val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
                val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, simpleOpportunity.accountid)
                val link = FormatUtils.newA(accountLink, simpleOpportunity.accountName!!)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            try {
                val accountId = value.toInt()
                val accountService = AppContextUtil.getSpringBean(AccountService::class.java)
                val account = accountService.findById(accountId, context.saccountid)
                if (account != null) {
                    val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
                    val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, account.id)
                    val link = FormatUtils.newA(accountLink, account.accountname)
                    return FormatUtils.newLink(img, link).write()
                }
            } catch (e: Exception) {
                LOG.error("Error", e)
            }
            return value
        }
    }

    class CampaignFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val opportunity = context.wrappedBean as SimpleOpportunity
            return if (opportunity.campaignid != null) {
                val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.CAMPAIGN))
                val campaignLink = CrmLinkGenerator.generateCampaignPreviewFullLink(context.siteUrl, opportunity
                        .campaignid)
                val link = FormatUtils.newA(campaignLink, opportunity.campaignName!!)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            try {
                val campaignId = value.toInt()
                val campaignService = AppContextUtil.getSpringBean(CampaignService::class.java)
                val campaign = campaignService.findById(campaignId, context.saccountid)
                if (campaign != null) {
                    val img = Text(CrmResources.getFontIconHtml(CrmTypeConstants.CAMPAIGN))
                    val campaignLink = CrmLinkGenerator.generateCampaignPreviewFullLink(context.siteUrl, campaign.id)
                    val link = FormatUtils.newA(campaignLink, campaign.campaignname)
                    return FormatUtils.newLink(img, link).write()
                }
            } catch (e: Exception) {
                LOG.error("Error", e)
            }

            return value
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val opportunity = context.wrappedBean as SimpleOpportunity
            return if (opportunity.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(opportunity.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(opportunity.saccountid),
                        opportunity.assignuser)
                val link = FormatUtils.newA(userLink, opportunity.assignUserFullName)
                FormatUtils.newLink(img, link).write()
            } else {
                Span().write()
            }
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            return if (StringUtils.isBlank(value)) {
                Span().write()
            } else {
                val userService = AppContextUtil.getSpringBean(UserService::class.java)
                val user = userService.findUserByUserNameInAccount(value, context.saccountid)
                if (user != null) {
                    val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(context.saccountid),
                            user.username)
                    val img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link = FormatUtils.newA(userLink, user.displayName!!)
                    FormatUtils.newLink(img, link).write()
                } else value
            }
        }
    }
}