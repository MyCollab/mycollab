package com.mycollab.common.service

import com.mycollab.common.domain.NotificationItem
import com.mycollab.db.persistence.service.ICrudService

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
interface NotificationItemService : ICrudService<Int, NotificationItem> {
    fun batchInsertItems(notificationUsers: List<String>, module:String, type: String, typeId: String,
                         messages: List<String>, sAccountId: Int)

    fun markNotificationRead(targetUser: String, module: String, type: String, typeId: String)

    fun findUnreadNotificationItemsByUser(targetUser: String, sAccountId: Int): List<NotificationItem>
}