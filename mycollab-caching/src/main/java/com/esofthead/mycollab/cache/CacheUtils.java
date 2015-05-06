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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.persistence.service.IService;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class CacheUtils {
	private static final Logger LOG = LoggerFactory.getLogger(CacheUtils.class);

	public static String constructParamsKey(Object[] args) {
		return JsonDeSerializer.toJson(args);
	}

	public static Class<?> getEnclosingServiceInterface(Class<?> serviceClass) {
		Class<?> cls = ClassUtils.getInterfaceInstanceOf(serviceClass, IService.class);
		return cls;
	}

	public static String getEnclosingServiceInterfaceName(Class<?> serviceClass) {
		return getEnclosingServiceInterface(serviceClass).getName();
	}

	public static void cleanCache(Integer accountId, String prefixKey) {
		LOG.debug("Remove cache account {}  and key {}", accountId, prefixKey);
		LocalCacheManager.removeCacheItems(accountId.toString(), prefixKey);
	}

	public static void cleanCaches(Integer accountId, Class<?>... classes) {
		for (Class<?> prefKey : classes) {
			cleanCache(accountId, prefKey.getName());
		}
	}

	public static boolean isInBlackList(Class<?> cls) {
		return (cls != null) && (cls.getAnnotation(IgnoreCacheClass.class) != null);
	}
}
