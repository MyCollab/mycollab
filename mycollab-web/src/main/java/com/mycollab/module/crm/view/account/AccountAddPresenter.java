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
package com.mycollab.module.crm.view.account;

import com.mycollab.common.UrlEncodeDecoder;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.ResourceNotFoundException;
import com.mycollab.core.SecureAccessException;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.event.AccountEvent;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.view.CrmGenericPresenter;
import com.mycollab.module.crm.view.CrmModule;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.IEditFormHandler;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountAddPresenter extends CrmGenericPresenter<AccountAddView> {
    private static final long serialVersionUID = 1L;

    public AccountAddPresenter() {
        super(AccountAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new IEditFormHandler<SimpleAccount>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleAccount account) {
                int accountId = saveAccount(account);
                EventBusFactory.getInstance().post(new AccountEvent.GotoRead(this, accountId));
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new AccountEvent.GotoList(this, null));
            }

            @Override
            public void onSaveAndNew(final SimpleAccount account) {
                saveAccount(account);
                EventBusFactory.getInstance().post(new AccountEvent.GotoAdd(this, null));
            }
        });
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        CrmModule.navigateItem(CrmTypeConstants.ACCOUNT);
        if (UserUIContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) {
            SimpleAccount account = null;
            if (data.getParams() instanceof SimpleAccount) {
                account = (SimpleAccount) data.getParams();
            } else if (data.getParams() instanceof Integer) {
                AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                account = accountService.findById((Integer) data.getParams(), MyCollabUI.getAccountId());
            }

            if (account == null) {
                throw new ResourceNotFoundException();
            }

            super.onGo(container, data);
            view.editItem(account);
            if (account.getId() == null) {
                MyCollabUI.addFragment("crm/account/add", UserUIContext.getMessage(GenericI18Enum.BROWSER_ADD_ITEM_TITLE,
                        UserUIContext.getMessage(AccountI18nEnum.SINGLE)));

            } else {
                MyCollabUI.addFragment("crm/account/edit/" + UrlEncodeDecoder.encode(account.getId()),
                        UserUIContext.getMessage(GenericI18Enum.BROWSER_EDIT_ITEM_TITLE,
                                UserUIContext.getMessage(AccountI18nEnum.SINGLE), account.getAccountname()));
            }
        } else {
            throw new SecureAccessException();
        }
    }

    private int saveAccount(Account account) {
        AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);

        account.setSaccountid(MyCollabUI.getAccountId());
        if (account.getId() == null) {
            accountService.saveWithSession(account, UserUIContext.getUsername());
        } else {
            accountService.updateWithSession(account, UserUIContext.getUsername());
        }
        return account.getId();
    }
}
