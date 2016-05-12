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
import com.esofthead.mycollab.core.UserInvalidInputException;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.shell.ShellController;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ControllerRegistry;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.ui.MyCollabSession;
import com.esofthead.mycollab.web.DesktopApplication;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import org.vaadin.viritin.util.BrowserCookie;

import java.io.Serializable;

import static com.esofthead.mycollab.vaadin.ui.MyCollabSession.USER_VAL;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MainWindowContainer extends CssLayout {
    private static final long serialVersionUID = 1L;

    public MainWindowContainer() {
        this.setCaption(AppContext.getSiteName());
        ControllerRegistry.addController(new ShellController(this));
        this.setSizeFull();
        this.setDefaultView();
    }

    public void setContent(ComponentContainer container) {
        this.removeAllComponents();
        this.addComponent(container);
    }

    private void setDefaultView() {
        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        int activeUsersCount = userService.getTotalActiveUsersInAccount(AppContext.getAccountId());
        if (activeUsersCount == 0) {
            this.setContent(new SetupNewInstanceView());
        } else {
            // Read previously stored cookie value
            BrowserCookie.detectCookieValue(DesktopApplication.ACCOUNT_COOKIE, new CookieCallbackSerializable() {
                @Override
                public void onValueDetected(String value) {
                    if (value != null && !value.equals("")) {
                        String[] loginParams = value.split("\\$");
                        if (loginParams.length == 2) {
                            try {
                                ((DesktopApplication) UI.getCurrent()).doLogin(loginParams[0], PasswordEncryptHelper.decryptText(loginParams[1]), false);
                            } catch (UserInvalidInputException e) {
                                navigateToLoginView();
                            }
                        } else {
                            navigateToLoginView();
                        }
                    } else {
                        try {
                            SimpleUser user = (SimpleUser) MyCollabSession.getSessionVariable(USER_VAL);
                            if (user != null) {
                                ((DesktopApplication) UI.getCurrent()).afterDoLogin(user);
                            } else {
                                authenticateWithTempCookieValue();
                            }
                        } catch (Exception e) {
                            navigateToLoginView();
                        }
                    }
                }
            });
        }
    }

    private void authenticateWithTempCookieValue() {
        BrowserCookie.detectCookieValue(DesktopApplication.TEMP_ACCOUNT_COOKIE, new CookieCallbackSerializable() {
            @Override
            public void onValueDetected(String value) {
                if (value != null && !value.equals("")) {
                    String[] loginParams = value.split("\\$");
                    if (loginParams.length == 2) {
                        try {
                            ((DesktopApplication) UI.getCurrent()).doLogin(loginParams[0], PasswordEncryptHelper.decryptText(loginParams[1]), false);
                        } catch (UserInvalidInputException e) {
                            navigateToLoginView();
                        }
                    } else {
                        navigateToLoginView();
                    }
                } else {
                    navigateToLoginView();
                }
            }
        });
    }

    private void navigateToLoginView() {
        final LoginPresenter presenter = PresenterResolver.getPresenter(LoginPresenter.class);
        LoginView loginView = presenter.getView();
        this.setContent(loginView);
    }

    interface CookieCallbackSerializable extends BrowserCookie.Callback, Serializable {

    }
}
