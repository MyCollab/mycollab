package com.mycollab.schedule.email

import com.mycollab.common.domain.SimpleRelayEmailNotification

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
interface SendingRelayEmailNotificationAction {
    fun sendNotificationForCreateAction(notification: SimpleRelayEmailNotification)

    fun sendNotificationForUpdateAction(notification: SimpleRelayEmailNotification)

    fun sendNotificationForCommentAction(notification: SimpleRelayEmailNotification)
}