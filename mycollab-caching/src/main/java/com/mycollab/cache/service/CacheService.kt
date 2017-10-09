package com.mycollab.cache.service

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
interface CacheService {

    fun getValue(group: String, key: String): Any?

    fun putValue(group: String, key: String, value: Any)

    fun removeCacheItem(group: String, prefixKey: String)

    fun removeCacheItems(group: String, vararg classes: Class<*>)
}
