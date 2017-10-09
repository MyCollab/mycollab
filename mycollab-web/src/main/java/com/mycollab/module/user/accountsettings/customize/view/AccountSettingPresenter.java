package com.mycollab.module.user.accountsettings.customize.view;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.user.accountsettings.view.AccountModule;
import com.mycollab.module.user.accountsettings.view.parameters.SettingExtScreenData;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.security.BooleanPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.mvp.IPresenter;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_THEME, impliedPermissionVal = BooleanPermissionFlag.TRUE)
public class AccountSettingPresenter extends AbstractPresenter<AccountSettingContainer> {
    private static final long serialVersionUID = 1L;

    public AccountSettingPresenter() {
        super(AccountSettingContainer.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AccountModule accountContainer = (AccountModule) container;
        accountContainer.gotoSubView(SettingUIConstants.GENERAL_SETTING);

        IPresenter<?> presenter;
        if (data instanceof SettingExtScreenData.GeneralSetting || data == null) {
            presenter = PresenterResolver.getPresenter(GeneralSettingPresenter.class);
        } else if (data instanceof SettingExtScreenData.ThemeCustomize) {
            presenter = PresenterResolver.getPresenter(IThemeCustomizePresenter.class);
        } else {
            throw new MyCollabException("Do not support screen data " + data);
        }

        presenter.go(view, data);
    }

}
