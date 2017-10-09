package com.mycollab.module.common.esb

import com.google.common.eventbus.AllowConcurrentEvents
import com.google.common.eventbus.Subscribe
import com.mycollab.cache.CleanCacheEvent
import com.mycollab.cache.service.CacheService
import com.mycollab.module.esb.GenericCommand
import org.springframework.stereotype.Component

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Component
class CleanCacheCommand(private val cacheService: CacheService) : GenericCommand() {

    @AllowConcurrentEvents
    @Subscribe
    fun cleanCaches(event: CleanCacheEvent) {
        cacheService.removeCacheItems(event.sAccountId.toString(), *event.cls)
    }
}