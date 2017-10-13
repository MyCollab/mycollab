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
package com.mycollab.module.project.schedule.email.service

import com.hp.gagawa.java.elements.Span
import com.hp.gagawa.java.elements.Text
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.ProjectResources
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.domain.Risk
import com.mycollab.module.project.domain.SimpleRisk
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskConsequence
import com.mycollab.module.project.i18n.OptionI18nEnum.RiskProbability
import com.mycollab.module.project.i18n.RiskI18nEnum
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.project.service.RiskService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.format.DateFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
import com.mycollab.schedule.email.format.I18nFieldFormat
import com.mycollab.schedule.email.project.ProjectRiskRelayEmailNotificationAction
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
class ProjectRiskRelayEmailNotificationActionImpl : SendMailToAllMembersAction<SimpleRisk>(), ProjectRiskRelayEmailNotificationAction {
    @Autowired private lateinit var riskService: RiskService
    private val mapper = ProjectFieldNameMapper()

    override fun getItemName(): String = StringUtils.trim(bean!!.name, 100)

    override fun getProjectName(): String = bean!!.projectName

    override fun getCreateSubject(context: MailContext<SimpleRisk>): String = context.getMessage(
            RiskI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getUpdateSubject(context: MailContext<SimpleRisk>): String = context.getMessage(
            RiskI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getCommentSubject(context: MailContext<SimpleRisk>): String = context.getMessage(
            RiskI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getBeanInContext(notification: ProjectRelayEmailNotification): SimpleRisk? =
            riskService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleRisk>) {
        val emailNotification = context.emailNotification
        val summary = bean!!.name
        val summaryLink = ProjectLinkGenerator.generateRiskPreviewFullLink(siteUrl, bean!!.projectid, bean!!.id)

        val avatarId = if (projectMember != null) projectMember!!.memberAvatarId else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> RiskI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> RiskI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> RiskI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("projectName", bean!!.projectName)
        contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean!!.projectid))
        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    class ProjectFieldNameMapper : ItemFieldMapper() {
        init {
            put(Risk.Field.name, GenericI18Enum.FORM_NAME, isColSpan = true)
            put(Risk.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
            put(Risk.Field.probalitity, I18nFieldFormat(Risk.Field.probalitity.name, RiskI18nEnum.FORM_PROBABILITY,
                    RiskProbability::class.java))
            put(Risk.Field.consequence, I18nFieldFormat(Risk.Field.consequence.name, RiskI18nEnum.FORM_CONSEQUENCE, RiskConsequence::class.java))
            put(Risk.Field.startdate, DateFieldFormat(Risk.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
            put(Risk.Field.enddate, DateFieldFormat(Risk.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
            put(Risk.Field.duedate, DateFieldFormat(Risk.Field.duedate.name, GenericI18Enum.FORM_DUE_DATE))
            put(Risk.Field.status, I18nFieldFormat(Risk.Field.status.name, GenericI18Enum.FORM_STATUS,
                    StatusI18nEnum::class.java))
            put(Risk.Field.milestoneid, MilestoneFieldFormat(Risk.Field.milestoneid.name, MilestoneI18nEnum.SINGLE))
            put(Risk.Field.assignuser, AssigneeFieldFormat(Risk.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(Risk.Field.createduser, RaisedByFieldFormat(Risk.Field.createduser.name, RiskI18nEnum.FORM_RAISED_BY))
            put(Risk.Field.response, RiskI18nEnum.FORM_RESPONSE, isColSpan = true)
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {
        override fun formatField(context: MailContext<*>): String {
            val risk = context.wrappedBean as SimpleRisk
            return if (risk.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(risk.assignToUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(risk.saccountid),
                        risk.assignuser)
                val link = FormatUtils.newA(userLink, risk.assignedToUserFullName)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            return if (StringUtils.isBlank(value)) {
                Span().write()
            } else {
                val userService = AppContextUtil.getSpringBean(UserService::class.java)
                val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
                if (user != null) {
                    val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                    val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.accountId),
                            user.username)
                    val img = FormatUtils.newImg("avatar", userAvatarLink)
                    val link = FormatUtils.newA(userLink, user.displayName)
                    FormatUtils.newLink(img, link).write()
                } else value
            }
        }
    }

    class RaisedByFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {
        override fun formatField(context: MailContext<*>): String {
            val risk = context.wrappedBean as SimpleRisk
            return if (risk.createduser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(risk.raisedByUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(risk.saccountid),
                        risk.createduser)
                val link = FormatUtils.newA(userLink, risk.raisedByUserFullName)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            val userService = AppContextUtil.getSpringBean(UserService::class.java)
            val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
            return if (user != null) {
                val userAvatarLink = MailUtils.getAvatarLink(user.avatarid, 16)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(user.accountId),
                        user.username)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val link = FormatUtils.newA(userLink, user.displayName)
                FormatUtils.newLink(img, link).write()
            } else value
        }
    }

    class MilestoneFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val risk = context.wrappedBean as SimpleRisk
            return if (risk.milestoneid != null) {
                val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
                val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl, risk.projectid,
                        risk.milestoneid)
                val link = FormatUtils.newA(milestoneLink, risk.milestoneName)
                FormatUtils.newLink(img, link).write()
            } else Span().write()
        }

        override fun formatField(context: MailContext<*>, value: String): String {
            if (StringUtils.isBlank(value)) {
                return Span().write()
            }
            val milestoneId = value.toInt()
            val milestoneService = AppContextUtil.getSpringBean(MilestoneService::class.java)
            val milestone = milestoneService.findById(milestoneId, context.user.accountId)
            return if (milestone != null) {
                val img = Text(ProjectResources.getFontIconHtml(ProjectTypeConstants.MILESTONE))
                val milestoneLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(context.siteUrl,
                        milestone.projectid, milestone.id)
                val link = FormatUtils.newA(milestoneLink, milestone.name)
                return FormatUtils.newLink(img, link).write()
            } else value
        }
    }

}