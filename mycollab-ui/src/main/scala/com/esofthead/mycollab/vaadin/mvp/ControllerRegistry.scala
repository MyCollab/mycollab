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
package com.esofthead.mycollab.vaadin.mvp

import com.esofthead.mycollab.vaadin.ui.MyCollabSession
import com.esofthead.mycollab.vaadin.ui.MyCollabSession._

import scala.collection.mutable._

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
object ControllerRegistry {
    def addController(controller: AbstractController): Unit = {
        var controllerList: Map[Class[_], AbstractController] = (MyCollabSession.getVariable(CONTROLLER_REGISTRY).asInstanceOf[Map[Class[_], AbstractController]])
        if (controllerList == null) {
            controllerList = Map().withDefaultValue(null)
            MyCollabSession.putVariable(CONTROLLER_REGISTRY, controllerList)
        }
        val existingController: AbstractController = controllerList(controller.getClass)
        if (existingController != null) {
            existingController.unregisterAll
        }
        controllerList += (controller.getClass -> controller)
    }
}
