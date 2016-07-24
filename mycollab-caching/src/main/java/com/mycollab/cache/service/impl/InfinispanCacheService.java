/**
 * This file is part of mycollab-caching.
 *
 * mycollab-caching is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-caching is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-caching.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.cache.service.impl;

import com.mycollab.cache.service.CacheService;
import org.apache.commons.collections.CollectionUtils;
import org.infinispan.AdvancedCache;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.context.Flag;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.1.4
 */
@Service
@Order(value = 1)
public class InfinispanCacheService implements CacheService, InitializingBean {
    private static Logger LOG = LoggerFactory.getLogger(InfinispanCacheService.class);

    private DefaultCacheManager instance;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream configInputStream;
            configInputStream = InfinispanCacheService.class.getClassLoader().getResourceAsStream("infinispan-local.xml");

            if (configInputStream == null) {
                configInputStream = InfinispanCacheService.class.getClassLoader().getResourceAsStream("infinispan-default.xml");
            }
            instance = new DefaultCacheManager(configInputStream);

        } catch (Exception e) {
            LOG.debug("Error while set up infinispan cache manager. Will initiate the default", e);
            instance = new DefaultCacheManager();
        }
    }

    @Override
    public void putValue(String group, String key, Object value) {
        BasicCache<String, Object> cache = getCache(group);
        cache.put(key, value);
    }

    private BasicCache<String, Object> getCache(String group) {
        BasicCache<String, Object> cache = instance.getCache(group);
        if (cache instanceof AdvancedCache) {
            cache = ((AdvancedCache<String, Object>) cache).withFlags(Flag.IGNORE_RETURN_VALUES);
        }
        return cache;
    }

    @Override
    public Object getValue(String group, String key) {
        BasicCache<String, Object> cache = getCache(group);
        return cache.get(key);
    }

    @Override
    public void removeCacheItem(String id, String prefixKey) {
        BasicCache<String, Object> cache = instance.getCache(id);
        LOG.debug("Remove cache has prefix {} in group {}", prefixKey, id);
        Set<String> keys = cache.keySet();
        if (CollectionUtils.isNotEmpty(keys)) {

            String[] keyArr = keys.toArray(new String[0]);
            for (int i = 0; i < keyArr.length; i++) {
                if (keyArr[i].startsWith(prefixKey)) {
                    LOG.debug("Remove cache key {}", keyArr[i]);
                    cache.remove(keyArr[i]);
                }
            }
        }
    }

    @Override
    public void removeCacheItems(String group, Class<?>... classes) {
        for (Class<?> prefKey : classes) {
            removeCacheItem(group, prefKey.getName());
        }
    }
}
