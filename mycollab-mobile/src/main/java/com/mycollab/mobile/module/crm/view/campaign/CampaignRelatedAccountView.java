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
package com.mycollab.mobile.module.crm.view.campaign;

import com.esofthead.vaadin.navigationbarquickmenu.NavigationBarQuickMenu;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.module.crm.view.account.AccountListDisplay;
import com.mycollab.mobile.shell.events.ShellEvent;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class CampaignRelatedAccountView extends AbstractRelatedListView<SimpleAccount, AccountSearchCriteria> {
    private static final long serialVersionUID = -6830593270870716952L;
    private SimpleCampaign campaign;

    public CampaignRelatedAccountView() {
        super();
        setCaption(UserUIContext.getMessage(AccountI18nEnum.M_TITLE_RELATED_ACCOUNTS));
        this.itemList = new AccountListDisplay();
        this.setContent(itemList);
    }

    public void displayAccounts(SimpleCampaign campaign) {
        this.campaign = campaign;
        loadAccounts();
    }

    private void loadAccounts() {
        AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
        searchCriteria.setSaccountid(NumberSearchField.equal(MyCollabUI.getAccountId()));
        searchCriteria.setCampaignId(NumberSearchField.equal(campaign.getId()));
        this.itemList.search(searchCriteria);
    }

    @Override
    public void refresh() {
        loadAccounts();
    }

    @Override
    protected Component createRightComponent() {
        final NavigationBarQuickMenu addAccount = new NavigationBarQuickMenu();
        addAccount.setStyleName("add-btn");

        MVerticalLayout addButtons = new MVerticalLayout().withFullWidth();

        Button newAccountBtn = new Button(UserUIContext.getMessage(AccountI18nEnum.NEW), clickEvent -> fireNewRelatedItem(""));
        addButtons.addComponent(newAccountBtn);

        Button selectAccount = new Button(UserUIContext.getMessage(AccountI18nEnum.M_TITLE_SELECT_ACCOUNTS), clickEvent -> {
            CampaignAccountSelectionView accountSelectionView = new CampaignAccountSelectionView(CampaignRelatedAccountView.this);
            AccountSearchCriteria criteria = new AccountSearchCriteria();
            criteria.setSaccountid(NumberSearchField.equal(MyCollabUI.getAccountId()));
            accountSelectionView.setSearchCriteria(criteria);
            EventBusFactory.getInstance().post(new ShellEvent.PushView(CampaignRelatedAccountView.this, accountSelectionView));
        });
        addButtons.addComponent(selectAccount);

        addAccount.setContent(addButtons);
        return addAccount;
    }
}
