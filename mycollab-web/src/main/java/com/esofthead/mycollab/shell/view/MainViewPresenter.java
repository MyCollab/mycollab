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
package com.esofthead.mycollab.shell.view;

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.esofthead.mycollab.web.DesktopApplication;
import com.vaadin.ui.ComponentContainer;
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
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        // if user type remember URL, instead of going to main page, to to his
        // url
        String url = ((DesktopApplication) UI.getCurrent()).getInitialUrl();
        view.display();
        if (!StringUtils.isBlank(url)) {
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            DesktopApplication.rootUrlResolver.navigateByFragement(url);
        } else {
            SimpleUser pref = AppContext.getUser();
            if (ModuleNameConstants.CRM.equals(pref.getLastModuleVisit())) {
                EventBusFactory.getInstance().post(
                        new ShellEvent.GotoCrmModule(this, null));
            } else if (ModuleNameConstants.ACCOUNT.equals(pref.getLastModuleVisit())) {
                EventBusFactory.getInstance().post(
                        new ShellEvent.GotoUserAccountModule(this, null));
            } else if (ModuleNameConstants.FILE.equals(pref.getLastModuleVisit())) {
                EventBusFactory.getInstance().post(
                        new ShellEvent.GotoFileModule(this, null));
            } else {
                EventBusFactory.getInstance().post(
                        new ShellEvent.GotoProjectModule(this, null));
            }
        }
    }
}
