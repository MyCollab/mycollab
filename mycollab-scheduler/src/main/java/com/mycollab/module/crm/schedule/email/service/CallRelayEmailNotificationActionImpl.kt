package com.mycollab.module.crm.schedule.email.service

import com.hp.gagawa.java.elements.Span
import com.mycollab.common.MonitorTypeConstants
import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.i18n.GenericI18Enum
import com.mycollab.core.MyCollabException
import com.mycollab.core.utils.StringUtils
import com.mycollab.html.FormatUtils
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.CallWithBLOBs
import com.mycollab.module.crm.domain.SimpleCall
import com.mycollab.module.crm.i18n.CallI18nEnum
import com.mycollab.module.crm.service.CallService
import com.mycollab.module.mail.MailUtils
import com.mycollab.module.user.AccountLinkGenerator
import com.mycollab.module.user.service.UserService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.CallRelayEmailNotificationAction
import com.mycollab.schedule.email.format.DateTimeFieldFormat
import com.mycollab.schedule.email.format.FieldFormat
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
class CallRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleCall>(), CallRelayEmailNotificationAction {
    @Autowired private lateinit var callService: CallService
    private val mapper: CallFieldNameMapper = CallFieldNameMapper()

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleCall? =
            callService.findById(notification.typeid.toInt(), notification.saccountid)

    override fun getCreateSubjectKey(): Enum<*> = CallI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = CallI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getItemName(): String = StringUtils.trim(bean!!.subject, 100)

    override fun buildExtraTemplateVariables(context: MailContext<SimpleCall>) {
        val summary = bean!!.subject
        val summaryLink = CrmLinkGenerator.generateCallPreviewFullLink(siteUrl, bean!!.id)

        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = userAvatar.write() + " " + emailNotification.changeByUserFullName
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> CallI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> CallI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> CallI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getUpdateSubjectKey(): Enum<*> = CallI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    class CallFieldNameMapper : ItemFieldMapper() {
        init {
            put(CallWithBLOBs.Field.subject, CallI18nEnum.FORM_SUBJECT, true)
            put(CallWithBLOBs.Field.status, GenericI18Enum.FORM_STATUS)
            put(CallWithBLOBs.Field.startdate, DateTimeFieldFormat(CallWithBLOBs.Field.startdate.name, CallI18nEnum.FORM_START_DATE_TIME))
            put(CallWithBLOBs.Field.typeid, CallI18nEnum.FORM_RELATED)
            put(CallWithBLOBs.Field.durationinseconds, GenericI18Enum.FORM_DURATION)
            put(CallWithBLOBs.Field.purpose, CallI18nEnum.FORM_PURPOSE)
            put(CallWithBLOBs.Field.assignuser, AssigneeFieldFormat(CallWithBLOBs.Field.assignuser.name, GenericI18Enum.FORM_ASSIGNEE))
            put(CallWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, true)
            put(CallWithBLOBs.Field.result, CallI18nEnum.FORM_RESULT, true)
        }
    }

    class AssigneeFieldFormat(fieldName: String, displayName: Enum<*>) : FieldFormat(fieldName, displayName) {

        override fun formatField(context: MailContext<*>): String {
            val call = context.wrappedBean as SimpleCall
            return if (call.assignuser != null) {
                val userAvatarLink = MailUtils.getAvatarLink(call.assignUserAvatarId, 16)
                val img = FormatUtils.newImg("avatar", userAvatarLink)
                val userLink = AccountLinkGenerator.generatePreviewFullUserLink(MailUtils.getSiteUrl(call.saccountid),
                        call.assignuser)
                val link = FormatUtils.newA(userLink, call.assignUserFullName)
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
                val user = userService.findUserByUserNameInAccount(value, context.user.accountId)
                if (user != null) {
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