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
package com.esofthead.mycollab.schedule.email.crm.service

import com.esofthead.mycollab.common.MonitorTypeConstants
import com.esofthead.mycollab.common.domain.SimpleRelayEmailNotification
import com.esofthead.mycollab.common.i18n.GenericI18Enum
import com.esofthead.mycollab.core.utils.StringUtils
import com.esofthead.mycollab.html.{FormatUtils, LinkUtils}
import com.esofthead.mycollab.module.crm.domain.{Opportunity, SimpleAccount, SimpleCampaign, SimpleOpportunity}
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum
import com.esofthead.mycollab.module.crm.service.{AccountService, CampaignService, OpportunityService}
import com.esofthead.mycollab.module.crm.{CrmLinkGenerator, CrmResources, CrmTypeConstants}
import com.esofthead.mycollab.module.mail.MailUtils
import com.esofthead.mycollab.module.user.AccountLinkGenerator
import com.esofthead.mycollab.module.user.domain.SimpleUser
import com.esofthead.mycollab.module.user.service.UserService
import com.esofthead.mycollab.schedule.email.crm.OpportunityRelayEmailNotificationAction
import com.esofthead.mycollab.schedule.email.format.{CurrencyFieldFormat, DateFieldFormat, FieldFormat}
import com.esofthead.mycollab.schedule.email.{ItemFieldMapper, MailContext}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.hp.gagawa.java.elements.{A, Img, Span, Text}
import org.slf4j.{Logger, LoggerFactory}
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
class OpportunityRelayEmailNotificationActionImpl extends CrmDefaultSendingRelayEmailAction[SimpleOpportunity] with OpportunityRelayEmailNotificationAction {
    private val LOG: Logger = LoggerFactory.getLogger(classOf[OpportunityRelayEmailNotificationActionImpl])

    @Autowired var opportunityService: OpportunityService = _
    private val mapper: OpportunityFieldNameMapper = new OpportunityFieldNameMapper

    override protected def getBeanInContext(context: MailContext[SimpleOpportunity]): SimpleOpportunity =
        opportunityService.findById(context.getTypeid.toInt, context.getSaccountid)

    override protected def getCreateSubjectKey: Enum[_] = OpportunityI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override protected def getUpdateSubjectKey: Enum[_] = OpportunityI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    override protected def getCommentSubjectKey: Enum[_] = OpportunityI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override protected def getItemFieldMapper: ItemFieldMapper = mapper

    override protected def getItemName: String = StringUtils.trim(bean.getOpportunityname, 100)

    override protected def buildExtraTemplateVariables(context: MailContext[SimpleOpportunity]): Unit = {
        val summary = bean.getOpportunityname
        val summaryLink = CrmLinkGenerator.generateOpportunityPreviewFullLink(siteUrl, bean.getId)

        val emailNotification = context.getEmailNotification
        val user = userService.findUserByUserNameInAccount(emailNotification.getChangeby, context.getSaccountid)

        val avatarId = if (user != null) user.getAvatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = userAvatar.toString + emailNotification.getChangeByUserFullName
        val actionEnum = emailNotification.getAction match {
            case MonitorTypeConstants.CREATE_ACTION => OpportunityI18nEnum.MAIL_CREATE_ITEM_HEADING
            case MonitorTypeConstants.UPDATE_ACTION => OpportunityI18nEnum.MAIL_UPDATE_ITEM_HEADING
            case MonitorTypeConstants.ADD_COMMENT_ACTION => OpportunityI18nEnum.MAIL_COMMENT_ITEM_HEADING
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("summary", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    class OpportunityFieldNameMapper extends ItemFieldMapper {
        put(Opportunity.Field.opportunityname, OpportunityI18nEnum.FORM_NAME)
        put(Opportunity.Field.accountid, new AccountFieldFormat(Opportunity.Field.accountid.name, OpportunityI18nEnum.FORM_ACCOUNT_NAME))
        put(Opportunity.Field.currencyid, new CurrencyFieldFormat(Opportunity.Field.currencyid.name, OpportunityI18nEnum.FORM_CURRENCY))
        put(Opportunity.Field.expectedcloseddate, new DateFieldFormat(Opportunity.Field.expectedcloseddate.name,
            OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE))
        put(Opportunity.Field.amount, OpportunityI18nEnum.FORM_AMOUNT)
        put(Opportunity.Field.opportunitytype, OpportunityI18nEnum.FORM_TYPE)
        put(Opportunity.Field.salesstage, OpportunityI18nEnum.FORM_SALE_STAGE)
        put(Opportunity.Field.source, OpportunityI18nEnum.FORM_LEAD_SOURCE)
        put(Opportunity.Field.probability, OpportunityI18nEnum.FORM_PROBABILITY)
        put(Opportunity.Field.campaignid, new CampaignFieldFormat(Opportunity.Field.campaignid.name, OpportunityI18nEnum.FORM_CAMPAIGN_NAME))
        put(Opportunity.Field.nextstep, OpportunityI18nEnum.FORM_NEXT_STEP)
        put(Opportunity.Field.assignuser, new AssigneeFieldFormat(Opportunity.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
        put(Opportunity.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
    }

    class AccountFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val simpleOpportunity = context.getWrappedBean.asInstanceOf[SimpleOpportunity]
            if (simpleOpportunity.getAccountid != null) {
                val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
                val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, simpleOpportunity
                    .getAccountid)
                val link = FormatUtils.newA(accountLink, simpleOpportunity.getAccountName)
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
            try {
                val accountId = value.toInt
                val accountService = ApplicationContextUtil.getSpringBean(classOf[AccountService])
                val account = accountService.findById(accountId, context.getUser.getAccountId)
                if (account != null) {
                    val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.ACCOUNT))
                    val accountLink = CrmLinkGenerator.generateAccountPreviewFullLink(context.siteUrl, account.getId)
                    val link = FormatUtils.newA(accountLink, account.getAccountname)
                    return FormatUtils.newLink(img, link).write
                }
            } catch {
                case e: Exception => LOG.error("Error", e)
            }
            value
        }
    }

    class CampaignFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val opportunity = context.getWrappedBean.asInstanceOf[SimpleOpportunity]
            if (opportunity.getCampaignid != null) {
                val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.CAMPAIGN))
                val campaignLink = CrmLinkGenerator.generateCampaignPreviewFullLink(context.siteUrl, opportunity
                    .getCampaignid)
                val link = FormatUtils.newA(campaignLink, opportunity.getCampaignName)
                FormatUtils.newLink(img, link).write
            } else {
                new Span().write
            }
        }

        def formatField(context: MailContext[_], value: String): String = {
            if (StringUtils.isBlank(value)) {
                new Span().write
            }
            try {
                val campaignId = value.toInt
                val campaignService = ApplicationContextUtil.getSpringBean(classOf[CampaignService])
                val campaign = campaignService.findById(campaignId, context.getUser.getAccountId)
                if (campaign != null) {
                    val img = new Text(CrmResources.getFontIconHtml(CrmTypeConstants.CAMPAIGN))
                    val campaignLink = CrmLinkGenerator.generateCampaignPreviewFullLink(context.siteUrl, campaign.getId)
                    val link = FormatUtils.newA(campaignLink, campaign.getCampaignname)
                    return FormatUtils.newLink(img, link).write
                }
            } catch {
                case e: Exception => LOG.error("Error", e)
            }

            value
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum[_]) extends FieldFormat(fieldName, displayName) {

        def formatField(context: MailContext[_]): String = {
            val opportunity = context.getWrappedBean.asInstanceOf[SimpleOpportunity]
            if (opportunity.getAssignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(opportunity.getAssignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(opportunity.getSaccountid),
                    opportunity.getAssignuser)
                val link = FormatUtils.newA(userLink, opportunity.getAssignUserFullName)
                FormatUtils.newLink(img, link).write
            } else {
                new Span().write
            }
        }

        def formatField(context: MailContext[_], value: String): String = {
            if (StringUtils.isBlank(value)) {
                new Span().write
            } else {
                val userService = ApplicationContextUtil.getSpringBean(classOf[UserService])
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

}