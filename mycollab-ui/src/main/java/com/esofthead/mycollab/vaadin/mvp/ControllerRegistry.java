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

import com.esofthead.mycollab.vaadin.ui.MyCollabSession;

import java.util.HashMap;
import java.util.Map;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.CONTROLLER_REGISTRY;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ControllerRegistry {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void addController(AbstractController controller) {
		Map<Class, AbstractController> controllerList = ((Map<Class, AbstractController>) MyCollabSession
				.getVariable(CONTROLLER_REGISTRY));
		if (controllerList == null) {
			controllerList = new HashMap<>();
			MyCollabSession.putVariable(CONTROLLER_REGISTRY, controllerList);
		}
		AbstractController existingController = controllerList.get(controller
				.getClass());
		if (existingController != null) {
			existingController.unregisterAll();
		}
		controllerList.put(controller.getClass(), controller);
	}
}
