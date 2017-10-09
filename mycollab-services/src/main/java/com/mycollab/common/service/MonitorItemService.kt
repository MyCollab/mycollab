package com.mycollab.common.service

import com.mycollab.cache.IgnoreCacheClass
import com.mycollab.common.domain.MonitorItem
import com.mycollab.common.domain.criteria.MonitorSearchCriteria
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.user.domain.SimpleUser

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@IgnoreCacheClass
interface MonitorItemService : IDefaultService<Int, MonitorItem, MonitorSearchCriteria> {

    fun isUserWatchingItem(username: String, type: String, typeId: Int?): Boolean

    fun getWatchers(type: String, typeId: Int?): List<SimpleUser>

    fun saveMonitorItems(monitorItems: Collection<MonitorItem>)
}
