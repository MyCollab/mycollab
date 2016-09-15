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
/**
 *
 */
package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.mobile.module.crm.ui.AbstractRelatedItemSelectionView;
import com.mycollab.mobile.module.crm.view.account.AccountListDisplay;
import com.mycollab.mobile.ui.AbstractRelatedListView;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class CampaignAccountSelectionView extends AbstractRelatedItemSelectionView<SimpleAccount, AccountSearchCriteria> {

    private static final long serialVersionUID = -801602909364348692L;

    public CampaignAccountSelectionView(AbstractRelatedListView<SimpleAccount, AccountSearchCriteria> relatedListView) {
        super(UserUIContext.getMessage(AccountI18nEnum.M_TITLE_SELECT_ACCOUNTS), relatedListView);
    }

    @Override
    protected void initUI() {
        this.itemList = new AccountListDisplay();
        this.itemList.setRowDisplayHandler((account, rowIndex) -> {
            final SelectableButton b = new SelectableButton(account.getAccountname());
            if (selections.contains(account))
                b.select();

            b.addClickListener(clickEvent -> {
                if (b.isSelected())
                    selections.add(account);
                else
                    selections.remove(account);
            });
            return b;
        });
    }

}
