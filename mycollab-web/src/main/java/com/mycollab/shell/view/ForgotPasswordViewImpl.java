package com.mycollab.shell.view;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.domain.User;
import com.mycollab.module.user.service.UserService;
import com.mycollab.shell.event.ShellEvent;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AccountAssetsResolver;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.web.CustomLayoutExt;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Resource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.button.MButton;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ForgotPasswordViewImpl extends AbstractVerticalPageView implements ForgotPasswordView {
    private static final long serialVersionUID = 1L;

    public ForgotPasswordViewImpl() {
        this.addComponent(new ForgotPwdForm());
    }

    private class ForgotPwdForm extends CustomComponent {
        private static final long serialVersionUID = 1L;
        private final TextField nameOrEmailField;

        ForgotPwdForm() {
            CustomLayout customLayout = CustomLayoutExt.createLayout("forgotPassword");
            customLayout.setStyleName("forgotPwdForm");

            Resource logoResource = AccountAssetsResolver.createLogoResource(AppUI.getBillingAccount().getLogopath(), 150);
            customLayout.addComponent(new Image(null, logoResource), "logo-here");

            customLayout.addComponent(new ELabel(LocalizationHelper.getMessage(AppUI.getDefaultLocale(),
                    ShellI18nEnum.BUTTON_FORGOT_PASSWORD)), "formHeader");
            customLayout.addComponent(ELabel.html(LocalizationHelper.getMessage(AppUI.getDefaultLocale(),
                    ShellI18nEnum.OPT_FORGOT_PASSWORD_INTRO)), "intro-text");
            nameOrEmailField = new TextField(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), GenericI18Enum.FORM_EMAIL));
            customLayout.addComponent(nameOrEmailField, "nameoremail");

            MButton sendEmail = new MButton(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.BUTTON_RESET_PASSWORD), clickEvent -> {
                String username = nameOrEmailField.getValue();
                if (StringUtils.isValidEmail(username)) {
                    UserService userService = AppContextUtil.getSpringBean(UserService.class);
                    User user = userService.findUserByUserName(username);

                    if (user == null) {
                        NotificationUtil.showErrorNotification(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), GenericI18Enum.ERROR_USER_IS_NOT_EXISTED, username));
                    } else {
                        userService.requestToResetPassword(user.getUsername());
                        NotificationUtil.showNotification(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), GenericI18Enum.OPT_SUCCESS),
                                LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.OPT_EMAIL_SENDER_NOTIFICATION));
                        EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null));
                    }
                } else {
                    NotificationUtil.showErrorNotification(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ErrorI18nEnum.NOT_VALID_EMAIL, username));
                }
            }).withStyleName(WebThemes.BUTTON_ACTION).withClickShortcut(ShortcutAction.KeyCode.ENTER);
            customLayout.addComponent(sendEmail, "loginButton");

            MButton memoBackBtn = new MButton(LocalizationHelper.getMessage(AppUI.getDefaultLocale(), ShellI18nEnum.BUTTON_IGNORE_RESET_PASSWORD),
                    clickEvent -> EventBusFactory.getInstance().post(new ShellEvent.LogOut(this, null)))
                    .withStyleName(WebThemes.BUTTON_LINK);
            customLayout.addComponent(memoBackBtn, "forgotLink");

            this.setCompositionRoot(customLayout);
        }
    }
}
