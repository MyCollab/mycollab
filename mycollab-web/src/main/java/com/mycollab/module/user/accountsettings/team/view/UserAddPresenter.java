/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.core.utils.RandomPasswordGenerator;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.billing.RegisterStatusConstants;
import com.mycollab.module.billing.UserStatusConstants;
import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.module.user.domain.SimpleUser;
import com.mycollab.module.user.events.UserEvent;
import com.mycollab.module.user.service.UserService;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.DefaultEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.server.Page;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_USER, impliedPermissionVal = AccessPermissionFlag.READ_WRITE)
public class UserAddPresenter extends AbstractPresenter<UserAddView> {
    private static final long serialVersionUID = 1L;

    public UserAddPresenter() {
        super(UserAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<SimpleUser>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleUser item) {
                save(item);
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
            }
        });
    }

    private void save(SimpleUser user) {
        boolean isRefreshable = false;
        if (user.getUsername() != null && user.getUsername().equals(UserUIContext.getUsername())) {
            isRefreshable = true;
        }

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        user.setAccountId(MyCollabUI.getAccountId());
        user.setSubdomain(MyCollabUI.getSubDomain());

        if (user.getStatus() == null) {
            user.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST);
        }

        if (user.getRegisterstatus() == null) {
            user.setRegisterstatus(RegisterStatusConstants.NOT_LOG_IN_YET);
        }

        if (user.getUsername() == null) {
            if (user.getPassword() == null) {
                user.setPassword(RandomPasswordGenerator.generateRandomPassword());
            }
            String userPassword = user.getPassword();
            userService.saveUserAccount(user, user.getRoleid(), MyCollabUI.getSubDomain(), MyCollabUI.getAccountId(), UserUIContext.getUsername(), true);
            UI.getCurrent().addWindow(new NewUserAddedWindow(user, userPassword));
        } else {
            userService.updateUserAccount(user, MyCollabUI.getAccountId());
            EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
        }

        if (isRefreshable) {
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        UserContainer userContainer = (UserContainer) container;
        userContainer.removeAllComponents();
        userContainer.addComponent(view);

        SimpleUser user = (SimpleUser) data.getParams();
        if (user.getUsername() != null) {
            view.editItem(user, false);
        } else {
            view.editItem(user);
        }

        AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);

        if (user.getUsername() == null) {
            breadcrumb.gotoUserAdd();
        } else {
            breadcrumb.gotoUserEdit(user);
        }
    }
}