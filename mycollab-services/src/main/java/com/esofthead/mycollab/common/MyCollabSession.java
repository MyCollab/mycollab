/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.common;

import org.infinispan.commons.api.BasicCache;

import com.esofthead.mycollab.cache.LocalCacheManager;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class MyCollabSession {

	public static final String EVENT_BUS_VAL = "eventBusVal";

	public static final String CURRENT_PROJECT = "project";

	public static final String PROJECT_MEMBER = "project_member";

	public static final String USER_TIMEZONE = "USER_TIMEZONE";

	public static final String USER_SHORT_DATE_FORMAT = "USER_SHORT_DATE_FORMAT";

	public static final String USER_DATE_FORMAT = "USER_DATE_FORMAT";

	public static final String USER_DATE_TIME_DATE_FORMAT = "USER_DATE_TIME_DATE_FORMAT";

	public static final String USER_DAY_MONTH_FORMAT = "USER_DAY_MONTH_FORMAT";

	public static final String CURRENT_MODULE = "currentModule";

	public static final String CONTROLLER_REGISTRY = "CONTROLLER_REGISTRY";

	public static final String HISTORY_VAL = "historyVal";

	public static final String PRESENTER_VAL = "presenterMap";

	public static final String VIEW_MANAGER_VAL = "viewMap";

	public static final String CURRENT_APP = "currentApp";

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void putVariable(String key, Object value) {
		BasicCache<String, Object> cache = LocalCacheManager
				.getCache(getSessionId());
		cache.put(key, value);
	}

	/**
	 * 
	 * @param key
	 */
	public static void removeVariable(String key) {
		try {
			BasicCache<String, Object> cache = LocalCacheManager
					.getCache(getSessionId());
			cache.remove(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Object getVariable(String key) {
		BasicCache<String, Object> cache = LocalCacheManager
				.getCache(getSessionId());
		return cache.get(key);
	}

	public static String getSessionId() {
		return SessionIdGenerator.getSessionId();
	}

}
