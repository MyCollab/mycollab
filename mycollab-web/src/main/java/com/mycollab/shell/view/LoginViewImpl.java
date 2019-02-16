/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.shell.view;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.ExceptionUtils;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.web.CustomLayoutExt;
import com.mycollab.web.DesktopApplication;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class LoginViewImpl extends AbstractVerticalPageView implements LoginView {
    private static final long serialVersionUID = 1L;

    public LoginViewImpl() {
        this.setStyleName("loginView");
        this.withSpacing(true);
        this.setSizeFull();
        this.addComponent(new LoginFormContainer());
    }

    class LoginFormContainer extends LoginForm {
        private static final long serialVersionUID = 1L;

        private CustomLayout custom;
        private CheckBox rememberMe;

        LoginFormContainer() {
            this.setSizeFull();
            this.addLoginListener((LoginListener) loginEvent -> {
                String username = loginEvent.getLoginParameter("username");
                String password = loginEvent.getLoginParameter("password");
                try {
                    custom.removeComponent("customErrorMsg");
                    ((DesktopApplication) UI.getCurrent()).doLogin(username, password, rememberMe.getValue());
                } catch (MyCollabException e) {
                    custom.addComponent(new Label(e.getMessage()), "customErrorMsg");
                } catch (Exception e) {
                    UserInvalidInputException userInvalidException = ExceptionUtils.getExceptionType(e, UserInvalidInputException.class);
                    if (userInvalidException != null) {
                        custom.addComponent(new Label(userInvalidException.getMessage()), "customErrorMsg");
                    } else {
                        throw new MyCollabException(e);
                    }
                }
            });
        }

        @Override
        protected Component createContent(TextField usernameField, PasswordField passwordField, Button loginBtn) {
            custom = CustomLayoutExt.createLayout("loginForm");
            Resource logoResource = AccountAssetsResolver.createLogoResource(AppUI.getBillingAccount().getLogopath(), 150);
            custom.addComponent(new Image(null, logoResource), "logo-here");
            custom.addComponent(ELabel.h1(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.BUTTON_LOG_IN))
                    .withUndefinedWidth(), "form-header");
            custom.addStyleName("customLoginForm");
            custom.addComponent(usernameField, "usernameField");
            custom.addComponent(passwordField, "passwordField");

            rememberMe = new CheckBox(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.OPT_REMEMBER_PASSWORD),
                    false);
            custom.addComponent(rememberMe, "rememberMe");

            loginBtn.setStyleName(WebThemes.BUTTON_ACTION);
            loginBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            custom.addComponent(loginBtn, "loginButton");

            MButton forgotPasswordBtn = new MButton(LocalizationHelper.getMessage(AppUI.getDefaultLocale(),
                    ShellI18nEnum.BUTTON_FORGOT_PASSWORD), clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoForgotPasswordPage(this, null)))
                    .withStyleName(WebThemes.BUTTON_LINK);
            custom.addComponent(forgotPasswordBtn, "forgotLink");

            custom.addComponent(ELabel.html(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.OPT_REMEMBER_PASSWORD,
                    ShellI18nEnum.OPT_SIGNIN_MYCOLLAB)), "newToUs");
            custom.addComponent(ELabel.html(new A("https://www.mycollab.com/pricing/", "_blank").appendText
                    (LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.ACTION_CREATE_ACCOUNT)).write())
                    .withUndefinedWidth(), "createAccountLink");

            return custom;
        }

        @Override
        public String getUsernameCaption() {
            return LocalizationHelper.getMessage(AppUI.getDefaultLocale(), GenericI18Enum.FORM_EMAIL);
        }

        @Override
        public String getPasswordCaption() {
            return LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.FORM_PASSWORD);
        }

        @Override
        public String getLoginButtonCaption() {
            return LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.BUTTON_LOG_IN);
        }
    }
}
