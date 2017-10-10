package com.mycollab.module.crm.schedule.email.service

import com.mycollab.common.domain.MailRecipientField
import com.mycollab.common.domain.SimpleAuditLog
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.domain.criteria.CommentSearchCriteria
import com.mycollab.common.i18n.MailI18nEnum
import com.mycollab.common.service.AuditLogService
import com.mycollab.common.service.CommentService
import com.mycollab.configuration.SiteConfiguration
import com.mycollab.core.utils.DateTimeUtils
import com.mycollab.core.utils.StringUtils
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.html.LinkUtils
import com.mycollab.i18n.LocalizationHelper
import com.mycollab.module.crm.service.CrmNotificationSettingService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.mail.service.ExtMailService
import com.mycollab.module.mail.service.IContentGenerator
import com.mycollab.module.user.domain.SimpleUser
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.SendingRelayEmailNotificationAction
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
abstract class CrmDefaultSendingRelayEmailAction<B> : SendingRelayEmailNotificationAction {
    @Autowired
    val extMailService: ExtMailService? = null

    @Autowired
    private lateinit var auditLogService: AuditLogService

    @Autowired
    val userService: UserService? = null

    @Autowired
    val notificationService: CrmNotificationSettingService? = null

    @Autowired
    val commentService: CommentService? = null

    @Autowired
    lateinit var contentGenerator: IContentGenerator

    protected var bean: B? = null
    protected var changeUser: SimpleUser? = null
    lateinit var siteUrl: String

    override fun sendNotificationForCreateAction(notification: SimpleRelayEmailNotification) {
        val notifiers = getListNotifyUserWithFilter(notification)
        if ((notifiers != null) && notifiers.isNotEmpty()) {
            onInitAction(notification)
            bean = getBeanInContext(notification)
            if (bean != null) {
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))
                notifiers.forEach { user ->
                    val notifierFullName = user.displayName
                    if (StringUtils.isBlank(notifierFullName)) {
                        LOG.error("Can not find user $user of notification $notification")
                        return
                    }
                    val context = MailContext<B>(notification, user, siteUrl)
                    val subject = context.getMessage(getCreateSubjectKey(), context.changeByUserFullName, getItemName())
                    context.wrappedBean = bean
                    contentGenerator.putVariable("context", context)
                    contentGenerator.putVariable("mapper", getItemFieldMapper())
                    contentGenerator.putVariable("userName", notifierFullName)
                    contentGenerator.putVariable("userName", notifierFullName)
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    buildExtraTemplateVariables(context)
                    val userMail = MailRecipientField(user.email, user.username)
                    val recipients = listOf(userMail)
                    extMailService!!.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), recipients,
                            subject, contentGenerator.parseFile("mailCrmItemCreatedNotifier.ftl", context.locale))
                }
            }
        }
    }

    override fun sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification) {
        val notifiers = getListNotifyUserWithFilter(notification)
        if ((notifiers != null) && notifiers.isNotEmpty()) {
            onInitAction(notification)
            bean = getBeanInContext(notification)
            if (bean != null) {
                contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))
                val searchCriteria = CommentSearchCriteria()
                searchCriteria.type = StringSearchField.and(notification.type)
                searchCriteria.typeId = StringSearchField.and(notification.typeid)
                searchCriteria.saccountid = null
                val comments = commentService!!.findPageableListByCriteria(BasicSearchRequest<CommentSearchCriteria>(searchCriteria, 0, 5))
                contentGenerator.putVariable("lastComments", comments)

                notifiers.forEach { user ->
                    val notifierFullName = user.displayName
                    if (notifierFullName == null) {
                        LOG.error("Can not find user $user of notification $notification")
                        return
                    }
                    val context = MailContext<B>(notification, user, siteUrl)
                    if (comments.isNotEmpty()) {
                        contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(context.locale,
                                MailI18nEnum.Last_Comments_Value, "" + comments.size))
                    }
                    contentGenerator.putVariable("Changes", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Changes))
                    contentGenerator.putVariable("Field", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Field))
                    contentGenerator.putVariable("Old_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Old_Value))
                    contentGenerator.putVariable("New_Value", LocalizationHelper.getMessage(context.locale, MailI18nEnum.New_Value))
                    contentGenerator.putVariable("userName", notifierFullName)
                    contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                            DateTimeUtils.getCurrentYear()))
                    val subject = context.getMessage(getUpdateSubjectKey(), context.changeByUserFullName, getItemName())
                    val auditLog = auditLogService.findLastestLogs(context.typeid.toInt(), context.saccountid)
                    contentGenerator.putVariable("historyLog", auditLog ?: SimpleAuditLog())
                    context.wrappedBean = bean
                    buildExtraTemplateVariables(context)
                    contentGenerator.putVariable("context", context)
                    contentGenerator.putVariable("mapper", getItemFieldMapper())
                    val userMail = MailRecipientField(user.email, user.username)
                    val recipients = listOf(userMail)
                    extMailService!!.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), recipients,
                            subject, contentGenerator.parseFile("mailCrmItemUpdatedNotifier.ftl", context.locale))
                }
            }
        }
    }

    override fun sendNotificationForCommentAction(notification: SimpleRelayEmailNotification) {
        val notifiers = getListNotifyUserWithFilter(notification)
        if ((notifiers != null) && notifiers.isNotEmpty()) {
            onInitAction(notification)

            val searchCriteria = CommentSearchCriteria()
            searchCriteria.type = StringSearchField.and(notification.type)
            searchCriteria.typeId = StringSearchField.and(notification.typeid)
            searchCriteria.saccountid = null
            val comments = commentService!!.findPageableListByCriteria(BasicSearchRequest<CommentSearchCriteria>(searchCriteria, 0, 5))
            contentGenerator.putVariable("lastComments", comments)
            contentGenerator.putVariable("logoPath", LinkUtils.accountLogoPath(notification.saccountid, notification.accountLogo))

            notifiers.forEach lit@ { user ->
                val notifierFullName = user.displayName
                if (notifierFullName.isNullOrBlank()) {
                    LOG.error("Can not find user $user of notification $notification")
                    return@lit
                }
                val context = MailContext<B>(notification, user, siteUrl)
                contentGenerator.putVariable("lastCommentsValue", LocalizationHelper.getMessage(context.locale,
                        MailI18nEnum.Last_Comments_Value, "" + comments.size))
                contentGenerator.putVariable("copyRight", LocalizationHelper.getMessage(context.locale, MailI18nEnum.Copyright,
                        DateTimeUtils.getCurrentYear()))

                bean = getBeanInContext(notification)
                context.wrappedBean = bean
                buildExtraTemplateVariables(context)
                val subject = context.getMessage(getCommentSubjectKey(), context.changeByUserFullName, getItemName())
                val userMail = MailRecipientField(user.email, user.username)
                val recipients = listOf(userMail)
                extMailService!!.sendHTMLMail(SiteConfiguration.getNotifyEmail(), SiteConfiguration.getDefaultSiteName(), recipients,
                        subject, contentGenerator.parseFile("mailCrmItemAddNoteNotifier.ftl", context.locale))
            }
        }
    }

    private fun getListNotifyUserWithFilter(notification: SimpleRelayEmailNotification): List<SimpleUser>? {
        return notification.notifyUsers
    }

    private fun onInitAction(notification: SimpleRelayEmailNotification) {
        siteUrl = MailUtils.getSiteUrl(notification.saccountid)
        changeUser = userService!!.findUserByUserNameInAccount(notification.changeby, notification.saccountid)
    }

    abstract protected fun getBeanInContext(notification: SimpleRelayEmailNotification): B?

    abstract protected fun buildExtraTemplateVariables(context: MailContext<B>)

    abstract protected fun getCreateSubjectKey(): Enum<*>

    abstract protected fun getUpdateSubjectKey(): Enum<*>

    abstract protected fun getCommentSubjectKey(): Enum<*>

    abstract protected fun getItemName(): String

    abstract protected fun getItemFieldMapper(): ItemFieldMapper

    companion object {
        val LOG = LoggerFactory.getLogger(CrmDefaultSendingRelayEmailAction::class.java)
    }
}