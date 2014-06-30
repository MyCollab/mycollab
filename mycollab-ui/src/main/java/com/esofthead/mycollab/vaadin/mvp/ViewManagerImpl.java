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

package com.esofthead.mycollab.vaadin.mvp;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.core.MyCollabException;

import static com.esofthead.mycollab.common.MyCollabSession.VIEW_MANAGER_VAL;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
class ViewManagerImpl extends ViewManager {
	private static Logger log = LoggerFactory.getLogger(ViewManagerImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends CacheableComponent> T getViewInstance(
			final Class<T> viewClass) {
		try {
			Map<Class<?>, Object> viewMap = (Map<Class<?>, Object>) MyCollabSession
					.getVariable(VIEW_MANAGER_VAL);
			if (viewMap == null) {
				viewMap = new HashMap<Class<?>, Object>();
				MyCollabSession.putVariable(VIEW_MANAGER_VAL, viewMap);
			}

			T value = (T) viewMap.get(viewClass);
			if (value != null) {
				log.debug("Get implementation of view " + viewClass.getName()
						+ " is " + value.getClass().getName());
				return value;
			} else {
				for (Class<?> classInstance : viewClasses) {
					if (viewClass.isAssignableFrom(classInstance)) {

						value = (T) classInstance.newInstance();
						viewMap.put(viewClass, value);
						log.debug("Get implementation of view "
								+ viewClass.getName() + " is "
								+ value.getClass().getName());
						return value;
					}
				}

				throw new MyCollabException(
						"Can not find implementation of view class: "
								+ viewClass.getName());
			}
		} catch (Throwable e) {
			throw new MyCollabException("Can not create view class: "
					+ viewClass.getName(), e);
		}
	}

	@Override
	protected void clearViews() {
		MyCollabSession.removeVariable(VIEW_MANAGER_VAL);
	}
}
