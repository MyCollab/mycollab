/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.shell.view;

import com.mycollab.configuration.EnDecryptHelper;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.service.UserService;
import com.mycollab.shell.view.ShellController;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ControllerRegistry;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.ui.MyCollabSession;
import com.mycollab.web.DesktopApplication;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import org.vaadin.viritin.util.BrowserCookie;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MainWindowContainer extends CssLayout {
    private static final long serialVersionUID = 1L;

    public MainWindowContainer() {
        this.setCaption(AppUI.getSiteName());
        ControllerRegistry.addController(new ShellController(this));
        this.setSizeFull();
        this.setDefaultView();
    }

    public void setContent(Component container) {
        this.removeAllComponents();
        this.addComponent(container);
    }

    private void setDefaultView() {
        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        int activeUsersCount = userService.getTotalActiveUsersInAccount(AppUI.getAccountId());
        if (activeUsersCount == 0) {
            this.setContent(new SetupNewInstanceView());
        } else {
            // Read previously stored cookie value
            BrowserCookie.detectCookieValue(DesktopApplication.ACCOUNT_COOKIE, value -> {
                if (StringUtils.isNotBlank(value)) {
                    String[] loginParams = value.split("\\$");
                    if (loginParams.length == 2) {
                        try {
                            ((DesktopApplication) UI.getCurrent()).doLogin(loginParams[0], EnDecryptHelper.decryptText(loginParams[1]), false);
                        } catch (Exception e) {
                            navigateToLoginView();
                        }
                    } else {
                        navigateToLoginView();
                    }
                } else {
                    try {
                        SimpleUser user = (SimpleUser) MyCollabSession.getSessionVariable(MyCollabSession.USER_VAL);
                        if (user != null) {
                            ((DesktopApplication) UI.getCurrent()).afterDoLogin(user);
                        } else {
                            authenticateWithTempCookieValue();
                        }
                    } catch (Exception e) {
                        navigateToLoginView();
                    }
                }
            });
        }
    }

    private void authenticateWithTempCookieValue() {
        BrowserCookie.detectCookieValue(DesktopApplication.TEMP_ACCOUNT_COOKIE, value -> {
            if (value != null && !value.equals("")) {
                String[] loginParams = value.split("\\$");
                if (loginParams.length == 2) {
                    try {
                        ((DesktopApplication) UI.getCurrent()).doLogin(loginParams[0], EnDecryptHelper.decryptText(loginParams[1]), false);
                    } catch (UserInvalidInputException e) {
                        navigateToLoginView();
                    }
                } else {
                    navigateToLoginView();
                }
            } else {
                navigateToLoginView();
            }
        });
    }

    private void navigateToLoginView() {
        final LoginPresenter presenter = PresenterResolver.getPresenter(LoginPresenter.class);
        LoginView loginView = presenter.getView();
        this.setContent(loginView);
    }
}
