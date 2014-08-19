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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.EditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.HistoryViewManager;
import com.esofthead.mycollab.vaadin.mvp.NullViewState;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewPermission;
import com.esofthead.mycollab.vaadin.mvp.ViewState;
import com.esofthead.mycollab.vaadin.ui.AbstractPresenter;
import com.vaadin.ui.ComponentContainer;

/**
 * 
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
		view.getEditFormHandlers().addFormHandler(
				new EditFormHandler<SimpleUser>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onSave(final SimpleUser item) {
						save(item);
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBusFactory.getInstance().post(
									new UserEvent.GotoList(this, null));
						}
					}

					@Override
					public void onCancel() {
						ViewState viewState = HistoryViewManager.back();
						if (viewState instanceof NullViewState) {
							EventBusFactory.getInstance().post(
									new UserEvent.GotoList(this, null));
						}
					}

					@Override
					public void onSaveAndNew(final SimpleUser item) {
						save(item);
						EventBusFactory.getInstance().post(
								new UserEvent.GotoAdd(this, null));
					}
				});
	}

	public void save(SimpleUser item) {
		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);

		item.setAccountId(AppContext.getAccountId());

		if (item.getStatus() == null) {
			item.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST);
		}

		if (item.getUsername() == null) {
			userService.saveUserAccount(item, AppContext.getAccountId(),
					AppContext.getUsername());
		} else {
			userService.updateUserAccount(item, AppContext.getAccountId());
		}

	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		UserContainer userContainer = (UserContainer) container;
		userContainer.removeAllComponents();
		userContainer.addComponent(view.getWidget());

		SimpleUser user = (SimpleUser) data.getParams();
		view.editItem(user);

		AccountSettingBreadcrumb breadcrumb = ViewManager
				.getCacheComponent(AccountSettingBreadcrumb.class);

		if (user.getUsername() == null) {
			breadcrumb.gotoUserAdd();
		} else {
			breadcrumb.gotoUserEdit(user);
		}

	}
}
