/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.shell.view;

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.web.DesktopApplication;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MainViewPresenter extends AbstractPresenter<MainView> {
    private static final long serialVersionUID = 1L;

    public MainViewPresenter() {
        super(MainView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        // if user type remember URL, instead of going to main page, to to his url
        String url = ((DesktopApplication) UI.getCurrent()).getCurrentFragmentUrl();
        view.display();
        if (!UserUIContext.getInstance().getIsValidAccount()) {
            EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, new String[]{"billing"}));
        } else {
            if (!StringUtils.isBlank(url)) {
                if (url.startsWith("/")) {
                    url = url.substring(1);
                }
                ShellUrlResolver.ROOT().resolveFragment(url);
            } else {
                SimpleUser pref = UserUIContext.getUser();
                if (ModuleNameConstants.CRM.equals(pref.getLastModuleVisit())) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoCrmModule(this, null));
                } else if (ModuleNameConstants.ACCOUNT.equals(pref.getLastModuleVisit())) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoUserAccountModule(this, null));
                } else if (ModuleNameConstants.FILE.equals(pref.getLastModuleVisit())) {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoFileModule(this, null));
                } else {
                    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null));
                }
            }
        }
    }
}
