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

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.PRESENTER_VAL;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public final class PresenterResolver {
	private static final Logger LOG = LoggerFactory
			.getLogger(PresenterResolver.class);

	@SuppressWarnings("rawtypes")
	private static Set<Class<? extends IPresenter>> presenterClasses;

	static {
		LOG.debug("Scan presenter implementation");
		Reflections reflections = new Reflections("com.esofthead.mycollab");
		presenterClasses = reflections.getSubTypesOf(IPresenter.class);
	}

	public static void init() {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <P extends IPresenter> P getPresenter(Class<P> presenterClass) {
		Map<Class<?>, Object> presenterMap = (Map<Class<?>, Object>) MyCollabSession
				.getVariable(PRESENTER_VAL);
		if (presenterMap == null) {
			presenterMap = new HashMap<>();
			MyCollabSession.putVariable(PRESENTER_VAL, presenterMap);
		}

		P value = (P) presenterMap.get(presenterClass);
		if (value == null) {
			try {
				if (!presenterClass.isInterface()) {
					value = presenterClass.newInstance();
				} else {
					for (Class<?> classInstance : presenterClasses) {
						if (presenterClass.isAssignableFrom(classInstance)
								&& !classInstance.isInterface()) {

							value = (P) classInstance.newInstance();
							LOG.debug("Get implementation of presenter "
									+ presenterClass.getName() + " is "
									+ value.getClass().getName());
						}
					}
				}

				presenterMap.put(presenterClass, value);
				return value;
			} catch (Exception e) {
				throw new MyCollabException(e);
			}
		} else {
			return value;
		}
	}
}
