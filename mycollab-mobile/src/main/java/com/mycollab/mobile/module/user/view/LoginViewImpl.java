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
package com.mycollab.mobile.module.user.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.mobile.module.user.events.UserEvent;
import com.mycollab.mobile.ui.AbstractMobileMainView;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.mvp.ViewEvent;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.addon.touchkit.ui.EmailField;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.vaadin.jouni.dom.Dom;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
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

        MVerticalLayout contentLayout = new MVerticalLayout().withStyleName("content-wrapper").withFullWidth();
        contentLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Image mainLogo = new Image(null, new ThemeResource("icons/logo_m.png"));
        contentLayout.addComponent(mainLogo);

        CssLayout welcomeTextWrapper = new CssLayout();
        ELabel welcomeText = new ELabel(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), ShellI18nEnum.BUTTON_LOG_IN))
                .withStyleName("h1");
        welcomeTextWrapper.addComponent(welcomeText);
        contentLayout.addComponent(welcomeText);

        final EmailField emailField = new EmailField();
        new Dom(emailField).setAttribute("placeholder", LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(),
                GenericI18Enum.FORM_EMAIL));
        emailField.setWidth("100%");
        contentLayout.addComponent(emailField);

        final PasswordField pwdField = new PasswordField();
        pwdField.setWidth("100%");
        new Dom(pwdField).setAttribute("placeholder", LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), ShellI18nEnum.FORM_PASSWORD));
        contentLayout.addComponent(pwdField);

        final CheckBox rememberPassword = new CheckBox(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(),
                ShellI18nEnum.OPT_REMEMBER_PASSWORD), true);
        rememberPassword.setWidth("100%");
        contentLayout.addComponent(rememberPassword);

        MButton signInBtn = new MButton(LocalizationHelper.getMessage(MyCollabUI.getDefaultLocale(), ShellI18nEnum.BUTTON_LOG_IN), clickEvent -> {
            try {
                LoginViewImpl.this.fireEvent(new ViewEvent<>(LoginViewImpl.this, new UserEvent.PlainLogin(
                        emailField.getValue(), pwdField.getValue(), rememberPassword.getValue())));
            } catch (Exception e) {
                throw new MyCollabException(e);
            }
        }).withStyleName(MobileUIConstants.BUTTON_ACTION);
        contentLayout.addComponent(signInBtn);

        this.addComponent(contentLayout);
    }
}
