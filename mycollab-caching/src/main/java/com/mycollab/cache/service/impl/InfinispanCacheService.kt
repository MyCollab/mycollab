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
package com.mycollab.cache.service.impl

import com.mycollab.cache.service.CacheService
import org.infinispan.AdvancedCache
import org.infinispan.commons.api.BasicCache
import org.infinispan.context.Flag
import org.infinispan.manager.EmbeddedCacheManager
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
@Service
class InfinispanCacheService(private val instance: EmbeddedCacheManager) : CacheService {

    override fun putValue(group: String, key: String, value: Any) {
        val cache = getCache(group)
        cache[key] = value
    }

    private fun getCache(group: String): BasicCache<String, Any> {
        var cache: BasicCache<String, Any> = instance.getCache(group)
        if (cache is AdvancedCache<*, *>) {
            cache = (cache as AdvancedCache<String, Any>).withFlags(Flag.IGNORE_RETURN_VALUES)
        }
        return cache
    }

    override fun getValue(group: String, key: String): Any? {
        val cache = getCache(group)
        return cache[key]
    }

    override fun removeCacheItem(group: String, prefixKey: String) {
        val cache = instance.getCache<String, Any>(group)
        LOG.debug("Remove cache has prefix $prefixKey in group $group")
        val keys = cache.keys
        keys.forEach {
            if (it.startsWith(prefixKey)) {
                LOG.debug("Remove cache key $it")
                cache.remove(it)
            }
        }
    }

    override fun removeCacheItems(group: String, vararg classes: Class<*>) {
        classes.forEach { removeCacheItem(group, it.name) }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(InfinispanCacheService::class.java)
    }
}
