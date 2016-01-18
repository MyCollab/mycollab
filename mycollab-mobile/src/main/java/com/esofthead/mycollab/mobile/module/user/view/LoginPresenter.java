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
package com.esofthead.mycollab.mobile.module.user.view;

import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.mobile.module.user.events.UserEvent;
import com.esofthead.mycollab.mobile.mvp.AbstractPresenter;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageView;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class LoginPresenter extends AbstractPresenter<LoginView> {
    private static final long serialVersionUID = 1L;

    public LoginPresenter() {
        super(LoginView.class);
    }

    @Override
    protected void postInitView() {
        view.addViewListener(new PageView.ViewListener<UserEvent.PlainLogin>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void receiveEvent(ViewEvent<UserEvent.PlainLogin> event) {
                UserEvent.PlainLogin data = (UserEvent.PlainLogin) event.getData();
                ((MobileApplication) UI.getCurrent()).doLogin(data.getUsername(), data.getPassword(), data.isRememberMe());
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer navigationManager, ScreenData<?> data) {
        ((NavigationManager) navigationManager).navigateTo(view.getWidget());
        AppContext.addFragment("", "Login Page");
    }
}
