package com.mycollab.module.common.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.common.dao.NotificationItemMapper
import com.mycollab.common.domain.NotificationItem
import com.mycollab.common.domain.NotificationItemExample
import com.mycollab.module.esb.GenericCommand
import com.mycollab.module.project.event.UpdateNotificationItemReadStatusEvent
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class UpdateNotificationItemReadStatusCommand(private val notificationMapper: NotificationItemMapper) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun updateNotificationStatus(event: UpdateNotificationItemReadStatusEvent) {
        val example = NotificationItemExample()
        example.createCriteria().andModuleEqualTo(event.module).andTypeidEqualTo(event.typeId).
                andTypeEqualTo(event.type).andNotificationuserEqualTo(event.notifyUser)
        val notificationItem = NotificationItem()
        notificationItem.isread = true
        notificationMapper.updateByExampleSelective(notificationItem, example)
    }
}