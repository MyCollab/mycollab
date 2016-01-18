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
package com.esofthead.mycollab.mobile.module.crm.view;

import com.esofthead.mycollab.mobile.ui.AbstractMobileMainView;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 4.2
 */
@ViewComponent
public class CrmModule extends AbstractMobileMainView {
    private static final long serialVersionUID = 1741055981807436733L;

    public CrmModule() {
        ControllerRegistry.addController(new CrmModuleController((NavigationManager) UI.getCurrent().getContent()));
    }
}
