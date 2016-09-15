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

import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountSelectionField extends CustomField<Integer> implements FieldSelection<Account> {
    private static final long serialVersionUID = 1L;

    private TextField accountName = new TextField();
    private Account account = null;

    private void clearValue() {
        accountName.setValue("");
        this.account = null;
        this.setInternalValue(null);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        Object value = newDataSource.getValue();
        if (value instanceof Integer) {
            setAccountByVal((Integer) value);
            super.setPropertyDataSource(newDataSource);
        } else {
            super.setPropertyDataSource(newDataSource);
        }
    }

    @Override
    public void setValue(Integer value) {
        this.setAccountByVal(value);
        super.setValue(value);
    }

    private void setAccountByVal(Integer accountId) {
        AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
        SimpleAccount account = accountService.findById(accountId, MyCollabUI.getAccountId());
        if (account != null) {
            setInternalAccount(account);
        }
    }

    private void setInternalAccount(SimpleAccount account) {
        this.account = account;
        accountName.setValue(account.getAccountname());
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public void fireValueChange(Account data) {
        account = data;
        if (account != null) {
            accountName.setValue(account.getAccountname());
            setInternalValue(account.getId());
        }
    }

    @Override
    protected Component initContent() {
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();
        layout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        accountName.setNullRepresentation("");
        accountName.setEnabled(true);
        accountName.setWidth("100%");

        MButton browseBtn = new MButton("", clickEvent -> {
            AccountSelectionWindow accountWindow = new AccountSelectionWindow(AccountSelectionField.this);
            UI.getCurrent().addWindow(accountWindow);
            accountWindow.show();
        }).withIcon(FontAwesome.ELLIPSIS_H).withStyleName(WebUIConstants.BUTTON_OPTION, WebUIConstants.BUTTON_SMALL_PADDING);

        MButton clearBtn = new MButton("", clickEvent -> clearValue()).withIcon(FontAwesome.TRASH_O)
                .withStyleName(WebUIConstants.BUTTON_OPTION, WebUIConstants.BUTTON_SMALL_PADDING);

        layout.with(accountName, browseBtn, clearBtn).expand(accountName);
        return layout;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
