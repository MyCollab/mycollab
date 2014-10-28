/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.vaadin.mvp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public abstract class UrlResolver {
	private static final Logger LOG = LoggerFactory.getLogger(UrlResolver.class);

	private Map<String, UrlResolver> subResolvers;

	public void addSubResolver(String key, UrlResolver subResolver) {
		if (subResolvers == null) {
			subResolvers = new HashMap<String, UrlResolver>();
		}
		subResolvers.put(key, subResolver);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public UrlResolver getSubResolver(String key) {
		return subResolvers.get(key);
	}

	/**
	 * 
	 * @param params
	 */
	public void handle(String... params) {
		try {
			if (ArrayUtils.isNotEmpty(params)) {
				String key = params[0];
				if (subResolvers == null) {
					handlePage(params);
				} else {
					UrlResolver urlResolver = subResolvers.get(key);
					if (urlResolver == null) {
						throw new MyCollabException(
								"Can not register resolver key " + key
										+ " for Resolver: " + this);
					} else {
						List<String> paramList = Arrays.asList(params).subList(
								1, params.length);
						String[] nxtParams = paramList.toArray(new String[0]);

						LOG.debug("Handle url in resolver: " + urlResolver);
						urlResolver.handle(nxtParams);

					}
				}
			} else {
				handlePage(new String[0]);
			}
		} catch (Exception e) {
			LOG.error("Error while navigation", e);
			defaultPageErrorHandler();
			NotificationUtil.showRecordNotExistNotification();
		}
	}

	/**
	 * 
	 */
	abstract protected void defaultPageErrorHandler();

	/**
	 * 
	 * @param params
	 */
	protected void handlePage(String... params) {
		LOG.debug("Handle page: " + this + " with params: "
				+ BeanUtility.printBeanObj(params));
	}
}
