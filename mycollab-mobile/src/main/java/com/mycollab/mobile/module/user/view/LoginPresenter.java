package com.mycollab.mobile.module.user.view;

import com.mycollab.common.i18n.ShellI18nEnum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.i18n.LocalizationHelper;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.mobile.module.user.event.UserEvent;
import com.mycollab.mobile.mvp.AbstractPresenter;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.event.ViewEvent;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public class LoginPresenter extends AbstractPresenter<LoginView> {
    private static final long serialVersionUID = 1L;

    public LoginPresenter() {
        super(LoginView.class);
    }

    @Override
    protected void postInitView() {
        getView().addViewListener(new PageView.ViewListener<UserEvent.PlainLogin>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void receiveEvent(ViewEvent<UserEvent.PlainLogin> event) {
                UserEvent.PlainLogin data = (UserEvent.PlainLogin) event.getData();
                ((MobileApplication) UI.getCurrent()).doLogin(data.getUsername(), data.getPassword(), data.isRememberMe());
            }
        });
    }

    @Override
    protected void onGo(HasComponents navigationManager, ScreenData<?> data) {
        ((NavigationManager) navigationManager).navigateTo(getView());
        AppUI.addFragment("", LocalizationHelper.getMessage(SiteConfiguration.getDefaultLocale(), ShellI18nEnum.OPT_LOGIN_PAGE));
    }
}
