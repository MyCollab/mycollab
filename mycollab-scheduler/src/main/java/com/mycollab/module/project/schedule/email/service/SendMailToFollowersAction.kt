package com.mycollab.module.project.schedule.email.service

import com.hp.gagawa.java.elements.A
import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.domain.criteria.CommentSearchCriteria
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.common.service.AuditLogService
import com.mycollab.common.service.CommentService
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.project.ProjectLinkGenerator
import com.mycollab.module.project.domain.ProjectRelayEmailNotification
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.service.ProjectMemberService
import com.mycollab.module.project.service.ProjectService
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.SendingRelayEmailNotificationAction
import com.mycollab.schedule.email.format.WebItem
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
abstract class SendMailToFollowersAction<B> : SendingRelayEmailNotificationAction {
    @Autowired private val extMailService: ExtMailService? = null
    @Autowired private val projectService: ProjectService? = null
    @Autowired protected val projectMemberService: ProjectMemberService? = null
    @Autowired private val commentService: CommentService? = null
    @Autowired protected lateinit var contentGenerator: IContentGenerator
    @Autowired private val auditLogService: AuditLogService? = null

    protected var bean: B? = null
    protected var projectMember: SimpleProjectMember? = null
    protected lateinit var siteUrl: String
    private var projectId: Int? = null

    override fun sendNotificationForCreateAction(notification: SimpleRelayEmailNotification) {
        val projectRelayEmailNotification = notification as ProjectRelayEmailNotification
        val notifiers = getListNotifyUsersWithFilter(projectRelayEmailNotification)
        if (notifiers.isNotEmpty()) {
            onInitAction(projectRelayEmailNotification)
            bean = getBeanInContext(projectRelayEmailNotification)
            if (bean != null) {
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))
                notifiers.forEach { user ->
                    val context = MailContext<B>(notification, user, siteUrl)
                    context.wrappedBean = bean
                    buildExtraTemplateVariables(context)
                    contentGenerator.putVariable("context", context)
                    contentGenerator.putVariable("mapper", getItemFieldMapper())
                    contentGenerator.putVariable("userName", user.displayName)
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
                    val userMail = MailRecipientField(user.email, user.username)
                    val recipients = arrayListOf(userMail)
                    extMailService!!.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), recipients,
                            getCreateSubject(context), contentGenerator.parseFile("mailProjectItemCreatedNotifier.ftl", context.locale))
                }
            }
        }
    }

    override fun sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification) {
        val projectRelayEmailNotification = notification as ProjectRelayEmailNotification
        val notifiers = getListNotifyUsersWithFilter(projectRelayEmailNotification)
        if (notifiers.isNotEmpty()) {
            onInitAction(projectRelayEmailNotification)
            bean = getBeanInContext(projectRelayEmailNotification)
            if (bean != null) {
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))

                val auditLog = auditLogService!!.findLastestLog(notification.typeid.toInt(), notification.saccountid)
                contentGenerator.putVariable("historyLog", auditLog)
                contentGenerator.putVariable("mapper", getItemFieldMapper())
                val searchCriteria = CommentSearchCriteria()
                searchCriteria.type = StringSearchField.and(notification.type)
                searchCriteria.typeId = StringSearchField.and(notification.typeid)
                searchCriteria.saccountid = null
                val comments = commentService!!.findPageableListByCriteria(BasicSearchRequest<CommentSearchCriteria>(searchCriteria, 0, 5))
                contentGenerator.putVariable("lastComments", comments)

                notifiers.forEach {
                    val context = MailContext<B>(notification, it, siteUrl)
                    context.wrappedBean = bean
                    buildExtraTemplateVariables(context)
                    contentGenerator.putVariable("context", context)
                    if (comments.isNotEmpty()) {
                        contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Last_Comments_Value, "" + comments.size))
                    }
                    contentGenerator.putVariable("Changes", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Changes))
                    contentGenerator.putVariable("Field", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Field))
                    contentGenerator.putVariable("Old_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Old_Value))
                    contentGenerator.putVariable("New_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.New_Value))
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
                    val userMail = MailRecipientField(it.email, it.username)
                    val recipients = arrayListOf(userMail)
                    extMailService!!.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), recipients,
                            getUpdateSubject(context), contentGenerator.parseFile("mailProjectItemUpdatedNotifier.ftl", context.locale))
                }
            }
        }
    }

    override fun sendNotificationForCommentAction(notification: SimpleRelayEmailNotification) {
        val projectRelayEmailNotification = notification as ProjectRelayEmailNotification
        val notifiers = getListNotifyUsersWithFilter(projectRelayEmailNotification)
        if (notifiers.isNotEmpty()) {
            onInitAction(projectRelayEmailNotification)
            bean = getBeanInContext(projectRelayEmailNotification)
            if (bean != null) {
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))
                val searchCriteria = CommentSearchCriteria()
                searchCriteria.type = StringSearchField.and(notification.type)
                searchCriteria.typeId = StringSearchField.and(notification.typeid)
                searchCriteria.saccountid = null
                val comments = commentService!!.findPageableListByCriteria(BasicSearchRequest<CommentSearchCriteria>(searchCriteria, 0, 5))
                contentGenerator.putVariable("lastComments", comments)

                notifiers.forEach { user ->
                    val context = MailContext<B>(notification, user, siteUrl)
                    context.wrappedBean = bean
                    buildExtraTemplateVariables(context)
                    val userLocale = LocalizationHelper.getLocaleInstance(user.language)
                    contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(userLocale, MailI18nEnum.Last_Comments_Value, "" + comments.size))
                    contentGenerator.putVariable("comment", context.emailNotification)
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    contentGenerator.putVariable("Project_Footer", getProjectFooter(context))
                    val userMail = MailRecipientField(user.email, user.username)
                    val toRecipients = arrayListOf(userMail)
                    extMailService!!.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), toRecipients,
                            getCommentSubject(context), contentGenerator.parseFile("mailProjectItemCommentNotifier.ftl", context.locale))
                }
            }
        }
    }

    private fun onInitAction(notification: ProjectRelayEmailNotification) {
        projectId = notification.projectId
        siteUrl = MailUtils.getSiteUrl(notification.saccountid)
        val relatedProject = projectService!!.findById(notification.projectId, notification.saccountid)
        val projectHyperLink = WebItem(relatedProject.name, ProjectLinkGenerator.generateProjectFullLink(siteUrl, relatedProject.id))
        contentGenerator.putVariable("projectHyperLink", projectHyperLink)
        projectMember = projectMemberService!!.findMemberByUsername(notification.changeby, notification.projectId,
                notification.saccountid)
    }

    abstract protected fun getBeanInContext(notification: ProjectRelayEmailNotification): B

    abstract protected fun getItemName(): String

    abstract protected fun getProjectName(): String

    private fun getProjectFooter(context: MailContext<B>): String = LocalizationHelper.getMessage(context.locale,
            MailI18nEnum.Project_Footer, getProjectName(), getProjectNotificationSettingLink(context))

    private fun getProjectNotificationSettingLink(context: MailContext<B>): String {
        return A(ProjectLinkGenerator.generateProjectSettingFullLink(siteUrl, projectId)).
                appendText(LocalizationHelper.getMessage(context.locale, MailI18nEnum.Project_Notification_Setting)).write()
    }

    abstract protected fun buildExtraTemplateVariables(context: MailContext<B>)

    abstract protected fun getItemFieldMapper(): ItemFieldMapper

    abstract protected fun getCreateSubject(context: MailContext<B>): String

    abstract protected fun getUpdateSubject(context: MailContext<B>): String

    abstract protected fun getCommentSubject(context: MailContext<B>): String

    abstract fun getListNotifyUsersWithFilter(notification: ProjectRelayEmailNotification): List<SimpleUser>
}