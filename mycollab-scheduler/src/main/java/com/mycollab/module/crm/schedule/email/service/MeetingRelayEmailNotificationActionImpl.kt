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
import com.mycollab.html.LinkUtils
import com.mycollab.module.crm.CrmLinkGenerator
import com.mycollab.module.crm.domain.MeetingWithBLOBs
import com.mycollab.module.crm.domain.SimpleMeeting
import com.mycollab.module.crm.i18n.MeetingI18nEnum
import com.mycollab.module.crm.service.MeetingService
import com.mycollab.schedule.email.ItemFieldMapper
import com.mycollab.schedule.email.MailContext
import com.mycollab.schedule.email.crm.MeetingRelayEmailNotificationAction
import com.mycollab.schedule.email.format.DateTimeFieldFormat
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
class MeetingRelayEmailNotificationActionImpl : CrmDefaultSendingRelayEmailAction<SimpleMeeting>(), MeetingRelayEmailNotificationAction {
    @Autowired private lateinit var meetingService: MeetingService

    private val mapper = MeetingFieldNameMapper()

    override fun buildExtraTemplateVariables(context: MailContext<SimpleMeeting>) {
        val summary = bean!!.subject
        val summaryLink = CrmLinkGenerator.generateMeetingPreviewFullLink(siteUrl, bean!!.id)
        val emailNotification = context.emailNotification

        val avatarId = if (changeUser != null) changeUser!!.avatarid else ""
        val userAvatar = LinkUtils.newAvatar(avatarId)

        val makeChangeUser = "${userAvatar.write()} ${emailNotification.changeByUserFullName}"
        val actionEnum = when (emailNotification.action) {
            MonitorTypeConstants.CREATE_ACTION -> MeetingI18nEnum.MAIL_CREATE_ITEM_HEADING
            MonitorTypeConstants.UPDATE_ACTION -> MeetingI18nEnum.MAIL_UPDATE_ITEM_HEADING
            MonitorTypeConstants.ADD_COMMENT_ACTION -> MeetingI18nEnum.MAIL_COMMENT_ITEM_HEADING
            else -> throw MyCollabException("Not support action ${emailNotification.action}")
        }

        contentGenerator.putVariable("actionHeading", context.getMessage(actionEnum, makeChangeUser))
        contentGenerator.putVariable("name", summary)
        contentGenerator.putVariable("summaryLink", summaryLink)
    }

    override fun getCreateSubjectKey(): Enum<*> = MeetingI18nEnum.MAIL_CREATE_ITEM_SUBJECT

    override fun getUpdateSubjectKey(): Enum<*> = MeetingI18nEnum.MAIL_UPDATE_ITEM_SUBJECT

    override fun getCommentSubjectKey(): Enum<*> = MeetingI18nEnum.MAIL_COMMENT_ITEM_SUBJECT

    override fun getItemName(): String = StringUtils.trim(bean!!.subject, 100)

    override fun getItemFieldMapper(): ItemFieldMapper = mapper

    override fun getBeanInContext(notification: SimpleRelayEmailNotification): SimpleMeeting? =
            meetingService.findById(notification.typeid.toInt(), notification.saccountid)

    class MeetingFieldNameMapper : ItemFieldMapper() {
        init {
            put(MeetingWithBLOBs.Field.subject, MeetingI18nEnum.FORM_SUBJECT, isColSpan = true)
            put(MeetingWithBLOBs.Field.status, GenericI18Enum.FORM_STATUS)
            put(MeetingWithBLOBs.Field.startdate, DateTimeFieldFormat(MeetingWithBLOBs.Field.startdate.name,
                    MeetingI18nEnum.FORM_START_DATE_TIME))
            put(MeetingWithBLOBs.Field.location, MeetingI18nEnum.FORM_LOCATION)
            put(MeetingWithBLOBs.Field.enddate, DateTimeFieldFormat(MeetingWithBLOBs.Field.enddate.name,
                    MeetingI18nEnum.FORM_END_DATE_TIME))
            put(MeetingWithBLOBs.Field.description, GenericI18Enum.FORM_DESCRIPTION, isColSpan = true)
        }
    }
}