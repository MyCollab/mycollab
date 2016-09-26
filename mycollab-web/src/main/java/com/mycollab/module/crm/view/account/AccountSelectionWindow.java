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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.fielddef.AccountTableFieldDef;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private AccountTableDisplay tableItem;
    private FieldSelection<Account> fieldSelection;

    public AccountSelectionWindow(FieldSelection<Account> fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(AccountI18nEnum.SINGLE)));
        this.fieldSelection = fieldSelection;
        this.withModal(true).withResizable(false).withWidth("900px");
    }

    public void show() {
        createAccountList();

        AccountSearchPanel accountSimpleSearchPanel = new AccountSearchPanel();
        accountSimpleSearchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        MVerticalLayout layout = new MVerticalLayout(accountSimpleSearchPanel, tableItem);
        this.setContent(layout);

        tableItem.setSearchCriteria(new AccountSearchCriteria());
        center();
    }

    private void createAccountList() {
        tableItem = new AccountTableDisplay(Arrays.asList(AccountTableFieldDef.accountname(), AccountTableFieldDef.city(),
                AccountTableFieldDef.assignUser()));

        tableItem.setWidth("100%");
        tableItem.setDisplayNumItems(10);

        tableItem.addGeneratedColumn("accountname", (source, itemId, columnId) -> {
            final SimpleAccount account = tableItem.getBeanByIndex(itemId);

            return new MButton(account.getAccountname(), clickEvent -> {
                fieldSelection.fireValueChange(account);
                close();
            }).withStyleName(WebUIConstants.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateToolTipAccount(
                    UserUIContext.getUserLocale(), account, MyCollabUI.getSiteUrl()));
        });
    }
}
