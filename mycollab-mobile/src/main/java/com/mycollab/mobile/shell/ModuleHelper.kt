/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
