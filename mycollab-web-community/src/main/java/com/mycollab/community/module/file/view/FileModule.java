/**
 * This file is part of mycollab-web-community.
 *
 * mycollab-web-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.community.module.file.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.file.view.IFileModule;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.view.NotPresentedView;
import com.mycollab.vaadin.web.ui.ServiceMenu;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
@ViewComponent
public class FileModule extends NotPresentedView implements IFileModule {
    private static final long serialVersionUID = 1L;

    private MHorizontalLayout serviceMenuContainer;
    private ServiceMenu serviceMenu;

    @Override
    public MHorizontalLayout buildMenu() {
        if (serviceMenuContainer == null) {
            serviceMenuContainer = new MHorizontalLayout();
            serviceMenu = new ServiceMenu();
            serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_PROJECT), clickEvent -> {
                EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, new String[]{"dashboard"}));
                serviceMenu.selectService(0);
            });

            serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_CRM),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null)));

            serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_DOCUMENT),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoFileModule(this, null)));


            serviceMenu.addService(UserUIContext.getMessage(GenericI18Enum.MODULE_PEOPLE),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"user", "list"})));

            serviceMenuContainer.with(serviceMenu);
        }
        serviceMenu.selectService(2);
        return serviceMenuContainer;
    }
}
