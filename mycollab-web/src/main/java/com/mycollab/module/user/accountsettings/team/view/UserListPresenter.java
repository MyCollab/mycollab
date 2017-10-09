package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_USER, impliedPermissionVal = AccessPermissionFlag.READ_ONLY)
public class UserListPresenter extends AbstractPresenter<UserListView> {
    private static final long serialVersionUID = 1L;

    public UserListPresenter() {
        super(UserListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserContainer userContainer = (UserContainer) container;
        userContainer.removeAllComponents();
        userContainer.addComponent(view);

        UserSearchCriteria criteria;
        if (data == null) {
            criteria = new UserSearchCriteria();
            criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
            criteria.setRegisterStatuses(new SetSearchField<>(RegisterStatusConstants.ACTIVE,
                    RegisterStatusConstants.NOT_LOG_IN_YET));
        } else {
            criteria = (UserSearchCriteria) data.getParams();
        }

        view.setSearchCriteria(criteria);

        AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);
        breadcrumb.gotoUserList();
    }
}
