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
package com.esofthead.mycollab.cache;

import org.apache.commons.collections.CollectionUtils;
import org.infinispan.AdvancedCache;
import org.infinispan.commons.api.BasicCache;
import org.infinispan.context.Flag;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Set;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LocalCacheManager {
    private static final Logger LOG = LoggerFactory.getLogger(LocalCacheManager.class);

    private static String GLOBAL_CACHE = "global";

    private static DefaultCacheManager instance;

    static {
        try {
            InputStream configInputStream;
            configInputStream = LocalCacheManager.class.getClassLoader().getResourceAsStream("infinispan-local.xml");

            if (configInputStream == null) {
                configInputStream = LocalCacheManager.class.getClassLoader().getResourceAsStream("infinispan-default.xml");
            }
            instance = new DefaultCacheManager(configInputStream);

        } catch (Exception e) {
            LOG.debug("Error while set up infinispan cache manager. Will initiate the default", e);
            instance = new DefaultCacheManager();
        }

    }

    public static BasicCache<String, Object> getCache(String id) {
        BasicCache<String, Object> cache = instance.getCache(id);
        if (cache instanceof AdvancedCache) {
            cache = ((AdvancedCache<String, Object>) cache).withFlags(Flag.IGNORE_RETURN_VALUES);
        }
        return cache;
    }

    public static BasicCache<Object, Object> getCache() {
        return instance.getCache(GLOBAL_CACHE);
    }

    public static DefaultCacheManager getCacheManager() {
        return instance;
    }

    static void removeCacheItems(String id, String prefixKey) {
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
}
