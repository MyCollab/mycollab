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
package com.esofthead.mycollab.module.user.view;

import com.esofthead.mycollab.common.i18n.ShellI18nEnum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.jetty.ServerInstance;
import com.esofthead.mycollab.module.user.events.UserEvent.PlainLogin;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.web.CustomLayoutExt;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class LoginViewImpl extends AbstractPageView implements LoginView {

	private static final long serialVersionUID = 1L;

	public LoginViewImpl() {
		this.addComponent(new LoginForm());
	}

	class LoginForm extends CustomComponent {
		private static final long serialVersionUID = 1L;

		private final TextField usernameField;
		private final PasswordField passwordField;
		private final CheckBox rememberMe;

		public LoginForm() {
			final CustomLayout custom = CustomLayoutExt
					.createLayout("loginForm");
			custom.addStyleName("customLoginForm");
			usernameField = new TextField(
					AppContext.getMessage(ShellI18nEnum.FORM_EMAIL));

			custom.addComponent(usernameField, "usernameField");

			passwordField = new PasswordField(
					AppContext.getMessage(ShellI18nEnum.FORM_PASSWORD));
			StringLengthValidator passwordValidator = new StringLengthValidator(
					"Password length must be greater than 6", 6,
					Integer.MAX_VALUE, false);
			passwordField.addValidator(passwordValidator);
			passwordField.addShortcutListener(new ShortcutListener("Signin",
					ShortcutAction.KeyCode.ENTER, null) {
				private static final long serialVersionUID = 5094514575531426118L;

				@Override
				public void handleAction(Object sender, Object target) {
					if (target == passwordField) {
						try {
							custom.removeComponent("customErrorMsg");
							LoginViewImpl.this
									.fireEvent(new ViewEvent<>(
											LoginViewImpl.this, new PlainLogin(
													usernameField.getValue(),
													passwordField.getValue(),
													rememberMe.getValue())));
						} catch (MyCollabException e) {
							custom.addComponent(new Label(e.getMessage()),
									"customErrorMsg");

						} catch (Exception e) {
							throw new MyCollabException(e);
						}
					}
				}
			});

			custom.addComponent(passwordField, "passwordField");

			rememberMe = new CheckBox(
					AppContext.getMessage(ShellI18nEnum.OPT_REMEMBER_PASSWORD),
					false);
			custom.addComponent(rememberMe, "rememberMe");

			Button loginBtn = new Button(
					AppContext.getMessage(ShellI18nEnum.BUTTON_LOG_IN),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							try {

								custom.removeComponent("customErrorMsg");

								LoginViewImpl.this.fireEvent(new ViewEvent<>(
										LoginViewImpl.this, new PlainLogin(
												usernameField.getValue(),
												passwordField.getValue(),
												rememberMe.getValue())));
							} catch (MyCollabException e) {
								custom.addComponent(new Label(e.getMessage()),
										"customErrorMsg");

							} catch (Exception e) {
								throw new MyCollabException(e);
							}
						}
					});

			loginBtn.setStyleName(UIConstants.THEME_ORANGE_LINK);
			custom.addComponent(loginBtn, "loginButton");

			Button forgotPasswordBtn = new Button(
					AppContext.getMessage(ShellI18nEnum.BUTTON_FORGOT_PASSWORD),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ShellEvent.GotoForgotPasswordPage(this,
											null));
						}
					});
			forgotPasswordBtn.setStyleName("link");
			custom.addComponent(forgotPasswordBtn, "forgotLink");

			if (ServerInstance.getInstance().isFirstTimeRunner()) {
				LoginForm.this
						.setComponentError(new UserError(
								"For the first time using MyCollab, the default email/password is admin@mycollab.com/admin123. You should change email/password when you access MyCollab successfully."));
				ServerInstance.getInstance().setIsFirstTimeRunner(false);
			}

			this.setCompositionRoot(custom);
			this.setHeight("100%");
		}
	}
}
