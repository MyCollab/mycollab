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

import com.esofthead.mycollab.configuration.PasswordEncryptHelper;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.user.view.LoginPresenter;
import com.esofthead.mycollab.module.user.view.LoginView;
import com.esofthead.mycollab.shell.ShellController;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.web.DesktopApplication;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;

import javax.servlet.http.Cookie;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class MainWindowContainer extends CssLayout {
    private static final long serialVersionUID = 1L;

    private boolean isAutoLogin;

    public MainWindowContainer() {
        this.setCaption(SiteConfiguration.getSiteName());
        isAutoLogin = true;
        ControllerRegistry.addController(new ShellController(this));
        this.setDefaultView();
    }

    public void setContent(ComponentContainer container) {
        this.removeAllComponents();
        this.addComponent(container);
    }

    private void setDefaultView() {
        LoginPresenter presenter = PresenterResolver.getPresenter(LoginPresenter.class);
        LoginView loginView = presenter.getView();
        this.setStyleName("loginView");
        this.setSizeFull();
        this.setContent(loginView.getWidget());

        // Read previously stored cookie value
        if (isAutoLogin) {
            Cookie nameCookie = ((DesktopApplication) UI.getCurrent())
                    .getCookieByName(DesktopApplication.NAME_COOKIE);
            if (nameCookie != null) {
                String loginInfo = nameCookie.getValue();
                if (loginInfo != null) {
                    String[] loginParams = loginInfo.split("\\$");
                    if (loginParams.length == 2) {
                        try {
                            presenter.doLogin(loginParams[0],
                                    PasswordEncryptHelper.decryptText(loginParams[1]), false);
                        } catch (UserInvalidInputException e) {
                            // do nothing
                        } catch (Exception e) {
                            throw new MyCollabException(e);
                        }
                    }
                }
            }
        }
    }

    public void setAutoLogin(boolean isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }
}
