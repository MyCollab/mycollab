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
package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountSelectionView extends AbstractSelectionView<Account> {
    private static final long serialVersionUID = 1L;

    private AccountListDisplay itemList;

    private AccountRowDisplayHandler rowHandler = new AccountRowDisplayHandler();

    public AccountSelectionView() {
        super();
        createUI();
        this.setCaption(UserUIContext.getMessage(AccountI18nEnum.M_VIEW_ACCOUNT_NAME_LOOKUP));
    }

    private void createUI() {
        itemList = new AccountListDisplay();
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        itemList.search(searchCriteria);

        SimpleAccount clearAccount = new SimpleAccount();
        itemList.getListContainer().addComponentAsFirst(rowHandler.generateRow(clearAccount, 0));
    }

    private class AccountRowDisplayHandler implements RowDisplayHandler<SimpleAccount> {

        @Override
        public Component generateRow(final SimpleAccount account, int rowIndex) {
            Button b = new Button(account.getAccountname(), clickEvent -> {
                selectionField.fireValueChange(account);
                AccountSelectionView.this.getNavigationManager().navigateBack();
            });
            if (account.getId() == null)
                b.addStyleName("blank-item");
            return b;
        }

    }
}
