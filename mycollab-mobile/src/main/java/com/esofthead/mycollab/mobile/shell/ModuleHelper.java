/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.shell;

import com.esofthead.mycollab.mobile.module.crm.view.CrmContainerView;
import com.esofthead.mycollab.mobile.module.project.view.ProjectModule;
import com.esofthead.mycollab.vaadin.mvp.IModule;
import com.esofthead.mycollab.vaadin.ui.MyCollabSession;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.CURRENT_MODULE;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */
public class ModuleHelper {

    public static void setCurrentModule(IModule module) {
        MyCollabSession.putCurrentUIVariable(CURRENT_MODULE, module);
    }

    public static IModule getCurrentModule() {
        return (IModule) MyCollabSession.getCurrentUIVariable(CURRENT_MODULE);
    }

    public static boolean isCurrentProjectModule() {
        IModule module = getCurrentModule();
        return (module != null) && (module instanceof ProjectModule);
    }

    public static boolean isCurrentCrmModule() {
        IModule module = getCurrentModule();
        return (module != null) && (module instanceof CrmContainerView);
    }
}
