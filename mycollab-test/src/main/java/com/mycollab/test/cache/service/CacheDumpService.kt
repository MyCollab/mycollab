package com.mycollab.test.cache.service

import com.mycollab.cache.service.CacheService
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
@Service
@Profile("test")
class CacheDumpService: CacheService {
    override fun getValue(group: String, key: String): Any?  = null

    override fun putValue(group: String, key: String, value: Any) {}

    override fun removeCacheItem(group: String, prefixKey: String) {}

    override fun removeCacheItems(group: String, vararg classes: Class<*>) {}
}