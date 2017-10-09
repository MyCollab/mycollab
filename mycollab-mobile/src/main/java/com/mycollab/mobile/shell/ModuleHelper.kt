package com.mycollab.mobile.shell

import com.mycollab.mobile.module.crm.view.CrmModule
import com.mycollab.mobile.module.project.view.ProjectModule
import com.mycollab.vaadin.mvp.IModule
import com.mycollab.vaadin.ui.MyCollabSession

import com.mycollab.vaadin.ui.MyCollabSession.CURRENT_MODULE

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
object ModuleHelper {

    @JvmStatic var currentModule: IModule?
        get() = MyCollabSession.getCurrentUIVariable(CURRENT_MODULE) as IModule?
        set(module) = MyCollabSession.putCurrentUIVariable(CURRENT_MODULE, module)

    @JvmStatic val isCurrentProjectModule: Boolean
        get() {
            val module = currentModule
            return module != null && module is ProjectModule
        }

    @JvmStatic val isCurrentCrmModule: Boolean
        get() {
            val module = currentModule
            return module != null && module is CrmModule
        }
}
