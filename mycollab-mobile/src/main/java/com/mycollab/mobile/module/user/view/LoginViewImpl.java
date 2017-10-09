package com.mycollab.mobile.module.user.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.MyCollabException;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.mobile.module.user.event.UserEvent;
import com.mycollab.mobile.ui.AbstractMobileMainView;
import com.mycollab.mobile.ui.MobileUIConstants;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.event.ViewEvent;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.vaadin.addon.touchkit.ui.EmailField;
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
        this.setStyleName("login-view");
        this.setSizeFull();
        initUI();
    }

    private void initUI() {
        MVerticalLayout contentLayout = new MVerticalLayout().withStyleName("content-wrapper").withFullWidth();
        contentLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

        Image mainLogo = new Image(null, AccountAssetsResolver.createLogoResource(AppUI.getBillingAccount().getLogopath(), 150));
        contentLayout.addComponent(mainLogo);

        CssLayout welcomeTextWrapper = new CssLayout();
        ELabel welcomeText = new ELabel(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.BUTTON_LOG_IN))
                .withStyleName("h1");
        welcomeTextWrapper.addComponent(welcomeText);
        contentLayout.addComponent(welcomeText);

        final EmailField emailField = new EmailField();
        new Dom(emailField).setAttribute("placeholder", LocalizationHelper.getMessage(AppUI.getDefaultLocale(),
                GenericI18Enum.FORM_EMAIL));
        emailField.setWidth("100%");
        contentLayout.addComponent(emailField);

        final PasswordField pwdField = new PasswordField();
        pwdField.setWidth("100%");
        new Dom(pwdField).setAttribute("placeholder", LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.FORM_PASSWORD));
        contentLayout.addComponent(pwdField);

        final CheckBox rememberPassword = new CheckBox(LocalizationHelper.getMessage(AppUI.getDefaultLocale(),
                ShellI18nEnum.OPT_REMEMBER_PASSWORD), true);
        rememberPassword.setWidth("100%");
        contentLayout.addComponent(rememberPassword);

        MButton signInBtn = new MButton(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.BUTTON_LOG_IN), clickEvent -> {
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
