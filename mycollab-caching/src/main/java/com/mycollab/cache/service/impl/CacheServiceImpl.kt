package com.mycollab.cache.service.impl

import com.mycollab.cache.service.CacheService
import com.mycollab.core.MyCollabException
import org.springframework.stereotype.Service

@Service
class CacheServiceImpl : CacheService {
    private val map = mutableMapOf<String, MutableMap<String, Any>>();

    override fun getValue(group: String, key: String): Any? = map[group]?.get(key)

    override fun putValue(group: String, key: String, value: Any) {
        var groupCache = map[group];
        if (groupCache == null) {
            groupCache = mutableMapOf();
        }
        groupCache[key] = value
    }

    override fun removeCacheItem(group: String, prefixKey: String) {

    }

    override fun removeCacheItems(group: String, vararg classes: Class<*>) {

    }
}
