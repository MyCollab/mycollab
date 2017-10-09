package com.mycollab.shell.view;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.module.mail.service.ExtMailService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ForgotPasswordPresenter extends AbstractPresenter<ForgotPasswordView> {
    private static final long serialVersionUID = 1L;

    public ForgotPasswordPresenter() {
        super(ForgotPasswordView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        MainWindowContainer windowContainer = (MainWindowContainer) container;
        windowContainer.setContent(view);

        ExtMailService extMailService = AppContextUtil.getSpringBean(ExtMailService.class);
        if (!extMailService.isMailSetupValid()) {
            NotificationUtil.showErrorNotification(UserUIContext.getMessage(ShellI18nEnum.WINDOW_SMTP_CONFIRM_SETUP_FOR_USER));
        }

        AppUI.addFragment("user/forgotpassword", UserUIContext.getMessage(ShellI18nEnum.OPT_FORGOT_PASSWORD_VIEW_TITLE));
    }
}
