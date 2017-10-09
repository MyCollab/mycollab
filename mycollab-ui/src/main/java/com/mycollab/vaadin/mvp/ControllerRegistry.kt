package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.ui.MyCollabSession

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ControllerRegistry {
    @JvmStatic fun addController(controller: AbstractController)  {
        var controllerList = MyCollabSession.getCurrentUIVariable(MyCollabSession.CONTROLLER_REGISTRY) as MutableMap<Class<*>, AbstractController>?
        if (controllerList == null) {
            controllerList = mutableMapOf()
            MyCollabSession.putCurrentUIVariable(MyCollabSession.CONTROLLER_REGISTRY, controllerList)
        }
        controllerList[controller.javaClass]?.unregisterAll()
        controllerList.put(controller.javaClass, controller)
    }
}