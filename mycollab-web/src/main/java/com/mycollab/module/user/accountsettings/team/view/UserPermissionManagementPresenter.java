package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.core.MyCollabException;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.module.user.accountsettings.view.AccountModule;
import com.mycollab.module.user.accountsettings.view.parameters.RoleScreenData;
import com.mycollab.module.user.accountsettings.view.parameters.UserScreenData;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
public class UserPermissionManagementPresenter extends AbstractPresenter<UserPermissionManagementView> {
    private static final long serialVersionUID = 1L;

    public UserPermissionManagementPresenter() {
        super(UserPermissionManagementView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        AccountModule accountModule = (AccountModule) container;
        accountModule.gotoSubView(SettingUIConstants.USERS);

        AbstractPresenter<?> presenter;
        if ((data == null) || (data instanceof UserScreenData.Read)
                || (data instanceof UserScreenData.Search)
                || (data instanceof UserScreenData.Add)
                || (data instanceof UserScreenData.Edit)) {
            presenter = PresenterResolver.getPresenter(UserPresenter.class);
        } else if ((data instanceof RoleScreenData.Read)
                || (data instanceof RoleScreenData.Add)
                || (data instanceof RoleScreenData.Edit)
                || (data instanceof RoleScreenData.Search)) {
            presenter = PresenterResolver.getPresenter(RolePresenter.class);
        } else {
            throw new MyCollabException("There is no presenter handle data " + BeanUtility.printBeanObj(data));
        }

        presenter.go(view, data);
    }
}
