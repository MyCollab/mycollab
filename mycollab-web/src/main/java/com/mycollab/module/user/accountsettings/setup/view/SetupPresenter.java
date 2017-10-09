package com.mycollab.module.user.accountsettings.setup.view;

import com.mycollab.module.user.accountsettings.view.AccountModule;
import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.0.5
 */
public class SetupPresenter extends AbstractPresenter<SetupView> {
    public SetupPresenter() {
        super(SetupView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.isAdmin()) {
            AccountModule accountContainer = (AccountModule) container;
            accountContainer.gotoSubView(SettingUIConstants.SETUP);
            view.displaySetup();

            AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);
            breadcrumb.gotoSetup();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
