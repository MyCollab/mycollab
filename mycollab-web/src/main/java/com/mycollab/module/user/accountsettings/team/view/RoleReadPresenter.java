package com.mycollab.module.user.accountsettings.team.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.mycollab.module.user.domain.Role;
import com.mycollab.module.user.domain.SimpleRole;
import com.mycollab.module.user.event.RoleEvent;
import com.mycollab.module.user.service.RoleService;
import com.mycollab.security.AccessPermissionFlag;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.DefaultPreviewFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewPermission;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.UI;

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
                if (Boolean.TRUE.equals(role.getIssystemrole()) || Boolean.TRUE.equals(role.getIsdefault())) {
                    NotificationUtil.showErrorNotification(UserUIContext.getMessage(RoleI18nEnum.ERROR_CAN_NOT_DELETE_SYSTEM_ROLE, role.getRolename()));
                } else {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
                                    roleService.removeWithSession(role, UserUIContext.getUsername(), AppUI.getAccountId());
                                    EventBusFactory.getInstance().post(new RoleEvent.GotoList(this, null));
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
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (UserUIContext.canRead(RolePermissionCollections.ACCOUNT_ROLE)) {
            RoleService roleService = AppContextUtil.getSpringBean(RoleService.class);
            SimpleRole role = roleService.findById((Integer) data.getParams(), AppUI.getAccountId());
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
