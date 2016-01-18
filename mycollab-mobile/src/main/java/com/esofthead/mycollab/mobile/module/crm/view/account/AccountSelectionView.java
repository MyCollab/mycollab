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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountSelectionView extends AbstractSelectionView<Account> {
    private static final long serialVersionUID = 1L;

    private AccountSearchCriteria searchCriteria;
    private AccountListDisplay itemList;

    private AccountRowDisplayHandler rowHandler = new AccountRowDisplayHandler();

    public AccountSelectionView() {
        super();
        createUI();
        this.setCaption(AppContext.getMessage(AccountI18nEnum.M_VIEW_ACCOUNT_NAME_LOOKUP));
    }

    private void createUI() {
        itemList = new AccountListDisplay();
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        searchCriteria = new AccountSearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
                AppContext.getAccountId()));

        itemList.search(searchCriteria);

        SimpleAccount clearAccount = new SimpleAccount();
        itemList.getListContainer().addComponentAsFirst(
                rowHandler.generateRow(clearAccount, 0));
    }

    private class AccountRowDisplayHandler implements RowDisplayHandler<SimpleAccount> {

        @Override
        public Component generateRow(final SimpleAccount account, int rowIndex) {
            Button b = new Button(account.getAccountname(), new Button.ClickListener() {
                private static final long serialVersionUID = -6728215631308893324L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    selectionField.fireValueChange(account);
                    AccountSelectionView.this.getNavigationManager().navigateBack();
                }
            });
            if (account.getId() == null)
                b.addStyleName("blank-item");
            return b;
        }

    }
}
