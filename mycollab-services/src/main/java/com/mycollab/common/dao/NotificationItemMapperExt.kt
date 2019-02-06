package com.mycollab.common.dao

import com.mycollab.common.domain.NotificationItem
import org.apache.ibatis.annotations.Param

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
interface NotificationItemMapperExt {
    fun findTopUnreadNotificationItemsByUser(@Param("targetUser") targetUser: String, @Param("sAccountId") sAccountId: Int,
                                             @Param("top") num: Int): List<NotificationItem>
}