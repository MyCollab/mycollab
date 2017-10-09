package com.mycollab.shell.view;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.module.user.event.UserEvent.PlainLogin;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.PageView.ViewListener;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.event.ViewEvent;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.web.DesktopApplication;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LoginPresenter extends AbstractPresenter<LoginView> {
    private static final long serialVersionUID = 1L;

    public LoginPresenter() {
        super(LoginView.class);
    }

    @Override
    protected void postInitView() {
        view.addViewListener(new ViewListener<PlainLogin>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void receiveEvent(ViewEvent<PlainLogin> event) {
                PlainLogin data = (PlainLogin) event.getData();
                ((DesktopApplication) UI.getCurrent()).doLogin(data.getUsername(), data.getPassword(), data.isRememberMe());
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        MainWindowContainer windowContainer = (MainWindowContainer) container;
        windowContainer.setContent(view);
        AppUI.addFragment("user/login", LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale(), ShellI18nEnum.OPT_LOGIN_PAGE));
    }
}
