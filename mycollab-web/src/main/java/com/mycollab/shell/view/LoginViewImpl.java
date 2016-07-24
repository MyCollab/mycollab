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

import com.ejt.vaadin.loginform.LoginForm;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.core.UserInvalidInputException;
import com.mycollab.core.utils.ExceptionUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.user.events.UserEvent;
import com.mycollab.shell.events.ShellEvent;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewEvent;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class LoginViewImpl extends AbstractPageView implements LoginView {
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
        }

        @Override
        protected Component createContent(TextField usernameField, PasswordField passwordField, Button loginBtn) {
            custom = CustomLayoutExt.createLayout("loginForm");
            custom.addStyleName("customLoginForm");
            custom.addComponent(usernameField, "usernameField");
            StringLengthValidator passwordValidator = new StringLengthValidator(
                    "Password length must be greater than 6", 6, Integer.MAX_VALUE, false);
            passwordField.addValidator(passwordValidator);
            custom.addComponent(passwordField, "passwordField");

            rememberMe = new CheckBox(AppContext.getMessage(ShellI18nEnum.OPT_REMEMBER_PASSWORD), false);
            custom.addComponent(rememberMe, "rememberMe");

            loginBtn.setStyleName(UIConstants.BUTTON_ACTION);
            loginBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            custom.addComponent(loginBtn, "loginButton");

            MButton forgotPasswordBtn = new MButton(AppContext.getMessage(ShellI18nEnum.BUTTON_FORGOT_PASSWORD),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.GotoForgotPasswordPage(this, null)))
                    .withStyleName(UIConstants.BUTTON_LINK);
            custom.addComponent(forgotPasswordBtn, "forgotLink");

            return custom;
        }

        @Override
        protected String getUserNameFieldCaption() {
            return AppContext.getMessage(GenericI18Enum.FORM_EMAIL);
        }

        @Override
        protected String getPasswordFieldCaption() {
            return AppContext.getMessage(ShellI18nEnum.FORM_PASSWORD);
        }

        // You can also override this method to handle the login directly, instead of using the event mechanism
        @Override
        protected void login(String userName, String password) {
            try {
                custom.removeComponent("customErrorMsg");
                LoginViewImpl.this.fireEvent(new ViewEvent<>(LoginViewImpl.this, new UserEvent.PlainLogin(
                        userName, password, rememberMe.getValue())));
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
        }
    }
}
