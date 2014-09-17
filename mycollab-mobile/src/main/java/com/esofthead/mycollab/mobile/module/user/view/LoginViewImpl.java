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

import com.esofthead.mycollab.mobile.shell.ShellController;
import com.esofthead.mycollab.mobile.ui.AbstractMobileMainView;
import com.esofthead.mycollab.mobile.ui.UIConstants;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 */
@ViewComponent
public class LoginViewImpl extends AbstractMobileMainView implements LoginView {
	private static final long serialVersionUID = 1L;

	public LoginViewImpl() {
		super();

		initUI();
	}

	private void initUI() {
		this.setStyleName("login-view");
		this.setSizeFull();

		VerticalLayout contentLayout = new VerticalLayout();
		contentLayout.setStyleName("content-wrapper");
		contentLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		contentLayout.setMargin(true);
		contentLayout.setSpacing(true);
		contentLayout.setWidth("320px");

		Image mainLogo = new Image(null, new ThemeResource("icons/logo_m.png"));
		contentLayout.addComponent(mainLogo);

		Label introText = new Label(
				"MyCollab helps you do all your office jobs on the computers, phones and tablets you use");
		introText.setStyleName("intro-text");
		contentLayout.addComponent(introText);

		CssLayout welcomeTextWrapper = new CssLayout();
		welcomeTextWrapper.setStyleName("welcometext-wrapper");
		welcomeTextWrapper.setWidth("100%");
		welcomeTextWrapper.setHeight("15px");
		Label welcomeText = new Label("Welcome Back!");
		welcomeText.setWidth("150px");
		welcomeTextWrapper.addComponent(welcomeText);
		contentLayout.addComponent(welcomeTextWrapper);

		final EmailField emailField = new EmailField();
		emailField.setWidth("100%");
		emailField.setInputPrompt("E-mail Address");
		emailField.setStyleName("email-input");
		contentLayout.addComponent(emailField);

		final PasswordField pwdField = new PasswordField();
		pwdField.setWidth("100%");
		pwdField.setInputPrompt("Password");
		pwdField.setStyleName("password-input");
		contentLayout.addComponent(pwdField);

		final CheckBox rememberPassword = new CheckBox();
		rememberPassword.setWidth("100%");
		rememberPassword.setCaption("Remember password");
		rememberPassword.setValue(true);
		contentLayout.addComponent(rememberPassword);

		Button signInBtn = new Button("Sign In");
		signInBtn.setWidth("100%");
		signInBtn.addStyleName(UIConstants.BUTTON_BIG);
		signInBtn.addStyleName(UIConstants.COLOR_BLUE);
		signInBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(Button.ClickEvent event) {
				ShellController.doLogin(emailField.getValue(),
						pwdField.getValue(), rememberPassword.getValue());
			}
		});
		contentLayout.addComponent(signInBtn);

		Button createAccountBtn = new Button("Create Account");
		createAccountBtn.setWidth("100%");
		createAccountBtn.addStyleName(UIConstants.BUTTON_BIG);
		createAccountBtn.addStyleName(UIConstants.COLOR_GRAY);
		contentLayout.addComponent(createAccountBtn);

		this.addComponent(contentLayout);
	}
}
