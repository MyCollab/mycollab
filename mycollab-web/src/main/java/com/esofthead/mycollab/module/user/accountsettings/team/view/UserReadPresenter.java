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

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.domain.User;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultPreviewFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewPermission;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_USER, impliedPermissionVal = AccessPermissionFlag.READ_ONLY)
public class UserReadPresenter extends AbstractPresenter<UserReadView> {
	private static final long serialVersionUID = 1L;

	public UserReadPresenter() {
		super(UserReadView.class);
	}

	@Override
	protected void postInitView() {
		view.getPreviewFormHandlers().addFormHandler(
				new DefaultPreviewFormHandler<User>() {
					@Override
					public void onAdd(User data) {
                        EventBusFactory.getInstance().post(
                                new UserEvent.GotoAdd(this, null));
					}

					@Override
					public void onEdit(User data) {
						EventBusFactory.getInstance().post(new UserEvent.GotoEdit(this, data));
					}

					@Override
					public void onDelete(final User data) {
						ConfirmDialogExt.show(UI.getCurrent(),
								AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
										AppContext.getSiteName()),
								AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
								AppContext.getMessage(GenericI18Enum.BUTTON_YES),
								AppContext.getMessage(GenericI18Enum.BUTTON_NO),
								new ConfirmDialog.Listener() {
									private static final long serialVersionUID = 1L;

									@Override
									public void onClose(ConfirmDialog dialog) {
										if (dialog.isConfirmed()) {
											UserService userService = ApplicationContextUtil
													.getSpringBean(UserService.class);
											userService.pendingUserAccount(data.getUsername(),
													AppContext.getAccountId());
											EventBusFactory.getInstance().post(
													new UserEvent.GotoList(this, null));
										}
									}
								});

					}

					@Override
					public void onClone(User data) {
						User cloneData = (User) data.copy();
						cloneData.setUsername(null);
						EventBusFactory.getInstance().post(new UserEvent.GotoAdd(this, cloneData));
					}

					@Override
					public void onCancel() {
						EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
					}
				});
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		if (AppContext.canRead(RolePermissionCollections.CRM_ACCOUNT)) {
			String username = (String) data.getParams();

			UserService userService = ApplicationContextUtil
					.getSpringBean(UserService.class);
			SimpleUser user = userService.findUserByUserNameInAccount(username,
					AppContext.getAccountId());
			if (user != null) {
				UserContainer userContainer = (UserContainer) container;
				userContainer.removeAllComponents();
				userContainer.addComponent(view.getWidget());
				view.previewItem(user);

				AccountSettingBreadcrumb breadcrumb = ViewManager
						.getCacheComponent(AccountSettingBreadcrumb.class);
				breadcrumb.gotoUserRead(user);
			} else {
				NotificationUtil.showErrorNotification(String.format("There is no user %s in this account", username));
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}

	}
}
