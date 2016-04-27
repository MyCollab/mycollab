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
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.mail.service.ExtMailService;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.IEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewPermission;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.server.Page;
import com.vaadin.ui.ComponentContainer;
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
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleUser>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleUser item) {
                save(item);
                ExtMailService mailService = ApplicationContextUtil.getSpringBean(ExtMailService.class);
                if (!mailService.isMailSetupValid()) {
                    UI.getCurrent().addWindow(new GetStartedInstructionWindow(item));
                }
                EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(final SimpleUser item) {
                save(item);
                EventBusFactory.getInstance().post(new UserEvent.GotoAdd(this, null));
            }
        });
    }

    private void save(SimpleUser user) {
        boolean isRefreshable = false;
        if (user.getUsername() != null && user.getUsername().equals(AppContext.getUsername())) {
            isRefreshable = true;
        }
        UserService userService = ApplicationContextUtil.getSpringBean(UserService.class);
        user.setAccountId(AppContext.getAccountId());
        user.setSubdomain(AppContext.getSubDomain());

        if (user.getStatus() == null) {
            user.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST);
        }

        if (user.getUsername() == null) {
            userService.saveUserAccount(user, AppContext.getAccountId(), AppContext.getUsername());
            NotificationUtil.showNotification(AppContext.getMessage(GenericI18Enum.HELP_SPAM_FILTER_PREVENT_TITLE),
                    AppContext.getMessage(GenericI18Enum.HELP_SPAM_FILTER_PREVENT_MESSAGE));
        } else {
            userService.updateUserAccount(user, AppContext.getAccountId());
        }
        if (isRefreshable) {
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
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