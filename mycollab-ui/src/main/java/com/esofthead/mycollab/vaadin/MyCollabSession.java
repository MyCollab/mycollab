package com.esofthead.mycollab.vaadin;

import org.infinispan.commons.api.BasicCache;

import com.esofthead.mycollab.cache.LocalCacheManager;
import com.vaadin.ui.UI;

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

	public static final String CURRENT_MODULE = "currentModule";

	public static final String CONTROLLER_REGISTRY = "CONTROLLER_REGISTRY";

	public static final String PRESENTER_VAL = "presenterMap";

	public static final String VIEW_MANAGER_VAL = "viewMap";

	public static final String CURRENT_APP = "currentApp";

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void putVariable(String key, Object value) {
		BasicCache<String, Object> cache = LocalCacheManager.getCache(UI
				.getCurrent().toString());
		cache.put(key, value);
	}

	/**
	 * 
	 * @param key
	 */
	public static void removeVariable(String key) {
		try {
			BasicCache<String, Object> cache = LocalCacheManager.getCache(UI
					.getCurrent().toString());
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
		BasicCache<String, Object> cache = LocalCacheManager.getCache(UI
				.getCurrent().toString());
		return cache.get(key);
	}

}
