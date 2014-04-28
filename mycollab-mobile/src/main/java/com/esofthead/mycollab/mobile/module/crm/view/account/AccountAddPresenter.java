/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.mobile.module.crm.ui.CrmGenericPresenter;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.ui.NotificationUtil;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;

public class AccountAddPresenter extends CrmGenericPresenter<AccountAddView> {
	private static final long serialVersionUID = -3664699848882470039L;

	public AccountAddPresenter() {
		super(AccountAddView.class);
	}

	@Override
	protected void onGo(MobileNavigationManager container, ScreenData<?> data) {
		if (AppContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) {

			SimpleAccount account = null;
			if (data.getParams() instanceof SimpleAccount) {
				account = (SimpleAccount) data.getParams();
			} else if (data.getParams() instanceof Integer) {
				AccountService accountService = ApplicationContextUtil
						.getSpringBean(AccountService.class);
				account = accountService.findById((Integer) data.getParams(),
						AppContext.getAccountId());
				if (account == null) {
					NotificationUtil.showRecordNotExistNotification();
					return;
				}
			}

			super.onGo(container, data);
			view.editItem(account);
			if (account.getId() == null) {
				AppContext.addFragment("crm/account/add", LocalizationHelper
						.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
								"Account"));

			} else {
				AppContext.addFragment(
						"crm/account/edit/"
								+ UrlEncodeDecoder.encode(account.getId()),
						LocalizationHelper.getMessage(
								GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
								"Account", account.getAccountname()));
			}
		} else {
			NotificationUtil.showMessagePermissionAlert();
		}
	}

}
