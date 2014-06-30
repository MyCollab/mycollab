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

import java.util.ArrayList;
import java.util.List;

import com.esofthead.mycollab.common.MyCollabSession;

import static com.esofthead.mycollab.common.MyCollabSession.CONTROLLER_REGISTRY;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ControllerRegistry {

	public static void addController(IController controler) {
		List<IController> controllerList = ((List<IController>) MyCollabSession
				.getVariable(CONTROLLER_REGISTRY));
		if (controllerList == null) {
			controllerList = new ArrayList<IController>();
			MyCollabSession.putVariable(CONTROLLER_REGISTRY, controllerList);
		}
		controllerList.add(controler);
	}

	public static void reset() {
		MyCollabSession.removeVariable(CONTROLLER_REGISTRY);
	}
}
