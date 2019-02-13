/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.module.user.accountsettings.view.AccountModule;
import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.event.RoleEvent;
import com.mycollab.module.user.service.RoleService;
import com.mycollab.module.user.ui.SettingUIConstants;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_ROLE, impliedPermissionVal = AccessPermissionFlag.READ_WRITE)
public class RoleAddPresenter extends AbstractPresenter<RoleAddView> {
    private static final long serialVersionUID = 1L;

    public RoleAddPresenter() {
        super(RoleAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<Role>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(Role item) {
                save(item);
                EventBusFactory.getInstance().post(new RoleEvent.GotoList(this, null));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new RoleEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(Role item) {
                save(item);
                EventBusFactory.getInstance().post(new RoleEvent.GotoAdd(this, null));
            }
        });
    }

    public void save(Role item) {
        RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
        item.setSaccountid(AppUI.getAccountId());

        if (item.getId() == null) {
            roleService.saveWithSession(item, UserUIContext.getUsername());
        } else {
            roleService.updateWithSession(item, UserUIContext.getUsername());
        }

        roleService.savePermission(item.getId(), view.getPermissionMap(), item.getSaccountid());
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canWrite(RolePermissionCollections.ACCOUNT_ROLE)) {
            AccountModule accountModule = (AccountModule) container;
            accountModule.gotoSubView(SettingUIConstants.ROLES, view);

            Role role = (Role) data.getParams();
            AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);

            if (role.getId() == null) {
                role.setSaccountid(AppUI.getAccountId());
                breadcrumb.gotoRoleAdd();
            } else {
                breadcrumb.gotoRoleEdit(role);
            }
            view.editItem(role);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
