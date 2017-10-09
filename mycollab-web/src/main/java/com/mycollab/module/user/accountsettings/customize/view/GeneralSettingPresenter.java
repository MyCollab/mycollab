package com.mycollab.module.user.accountsettings.customize.view;

import com.mycollab.module.user.accountsettings.localization.AdminI18nEnum;
import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_THEME, impliedPermissionVal = BooleanPermissionFlag.TRUE)
public class GeneralSettingPresenter extends AbstractPresenter<GeneralSettingView> {
    public GeneralSettingPresenter() {
        super(GeneralSettingView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.isAdmin()) {
            AccountSettingContainer customizeContainer = (AccountSettingContainer) container;
            customizeContainer.gotoSubView(UserUIContext.getMessage(AdminI18nEnum.OPT_GENERAL_SETTINGS));
            view.displayView();
            AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);
            breadcrumb.gotoGeneralSetting();
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
