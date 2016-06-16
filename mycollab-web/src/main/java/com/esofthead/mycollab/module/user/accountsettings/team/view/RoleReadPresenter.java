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
package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.esofthead.mycollab.module.user.domain.Role;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.events.RoleEvent;
import com.esofthead.mycollab.module.user.service.RoleService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewPermission;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_ROLE, impliedPermissionVal = AccessPermissionFlag.READ_ONLY)
public class RoleReadPresenter extends AbstractPresenter<RoleReadView> {
    private static final long serialVersionUID = 1L;

    public RoleReadPresenter() {
        super(RoleReadView.class);
    }

    @Override
    protected void postInitView() {
        view.getPreviewFormHandlers().addFormHandler(new DefaultPreviewFormHandler<Role>() {
            @Override
            public void onAdd(Role role) {
                EventBusFactory.getInstance().post(new RoleEvent.GotoAdd(this, role));
            }

            @Override
            public void onEdit(Role data) {
                EventBusFactory.getInstance().post(new RoleEvent.GotoEdit(this, data));
            }

            @Override
            public void onDelete(final Role role) {
                if (Boolean.TRUE.equals(role.getIssystemrole())) {
                    NotificationUtil.showErrorNotification(AppContext.getMessage(RoleI18nEnum.ERROR_CAN_NOT_DELETE_SYSTEM_ROLE, role.getRolename()));
                } else {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
                                        roleService.removeWithSession(role, AppContext.getUsername(), AppContext.getAccountId());
                                        EventBusFactory.getInstance().post(new RoleEvent.GotoList(this, null));
                                    }
                                }
                            });
                }
            }

            @Override
            public void onClone(Role data) {
                Role cloneData = (Role) data.copy();
                cloneData.setRolename(null);
                cloneData.setIssystemrole(false);
                EventBusFactory.getInstance().post(new RoleEvent.GotoAdd(this, cloneData));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new RoleEvent.GotoList(this, null));
            }
        });
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        if (AppContext.canRead(RolePermissionCollections.ACCOUNT_ROLE)) {
            RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
            SimpleRole role = roleService.findById((Integer) data.getParams(), AppContext.getAccountId());
            if (role != null) {
                RoleContainer roleContainer = (RoleContainer) container;
                roleContainer.removeAllComponents();
                roleContainer.addComponent(view);
                view.previewItem(role);

                AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);
                breadcrumb.gotoRoleRead(role);
            } else {
                NotificationUtil.showRecordNotExistNotification();
            }
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }
}
