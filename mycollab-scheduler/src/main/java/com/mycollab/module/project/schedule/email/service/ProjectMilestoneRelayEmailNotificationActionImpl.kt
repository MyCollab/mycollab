package com.mycollab.module.project.schedule.email.service

import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.domain.Milestone
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.i18n.MilestoneI18nEnum
import com.mycollab.module.project.i18n.OptionI18nEnum
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.format.DateFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
import com.mycollab.schedule.email.format.I18nFieldFormat
import com.mycollab.schedule.email.project.ProjectMilestoneRelayEmailNotificationAction
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
class ProjectMilestoneRelayEmailNotificationActionImpl() : SendMailToAllMembersAction<SimpleMilestone>(), ProjectMilestoneRelayEmailNotificationAction {

    @Autowired private lateinit var milestoneService: MilestoneService

    private val mapper = MilestoneFieldNameMapper()

    override fun getItemName(): String = StringUtils.trim(bean!!.name, 100)

    override fun getProjectName(): String = bean!!.projectName

    override fun getCreateSubject(context: MailContext<SimpleMilestone>): String = context.getMessage(
            MilestoneI18nEnum.MAIL_CREATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getUpdateSubject(context: MailContext<SimpleMilestone>): String = context.getMessage(
            MilestoneI18nEnum.MAIL_UPDATE_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getCommentSubject(context: MailContext<SimpleMilestone>): String = context.getMessage(
            MilestoneI18nEnum.MAIL_COMMENT_ITEM_SUBJECT, bean!!.projectName, context.changeByUserFullName, getItemName())

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getBeanInContext(notification: ProjectRelayEmailNotification): SimpleMilestone =
            milestoneService.findById(notification.typeid.toInt(), notification.saccountid)

    class MilestoneFieldNameMapper() : ItemFieldMapper() {
        init {
            put(Milestone.Field.name, GenericI18Enum.FORM_NAME, isColSpan = true)
            put(Milestone.Field.status, I18nFieldFormat(Milestone.Field.status.name, GenericI18Enum.FORM_STATUS,
                    OptionI18nEnum.MilestoneStatus::class.java))
            put(Milestone.Field.assignuser, AssigneeFieldFormat(Milestone.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(Milestone.Field.startdate, DateFieldFormat(Milestone.Field.startdate.name, GenericI18Enum.FORM_START_DATE))
            put(Milestone.Field.enddate, DateFieldFormat(Milestone.Field.enddate.name, GenericI18Enum.FORM_END_DATE))
            put(Milestone.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val milestone = context.wrappedBean as SimpleMilestone
            return if (milestone.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(milestone.ownerAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(milestone.saccountid),
                        milestone.assignuser)
                val link = FormatUtils.newA(userLink, milestone.ownerFullName)
                FormatUtils.newLink(img, link).write()
            } else {
                Span().write()
            }
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

    override fun buildExtraTemplateVariables(context: MailContext<SimpleMilestone>) {
        val emailNotification = context.emailNotification

        val summary = bean!!.name
        val summaryLink = ProjectLinkGenerator.generateMilestonePreviewFullLink(siteUrl, bean!!.projectid, bean!!.id)

        val avatarId = if (projectMember != null) projectMember!!.memberAvatarId else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)
        val makeChangeUser = userAvatar.write() + " " + emailNotification.changeByUserFullName

        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> MilestoneI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> MilestoneI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> MilestoneI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("projectName", bean!!.projectName)
        contentGenerator.putVariable("projectNotificationUrl", ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, bean!!.projectid))
        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }
}