/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
class AccountListDisplayView extends AbstractMobilePageView {
    private AccountSearchCriteria criteria;
    private final AccountListDisplay itemList;

    AccountListDisplayView(AccountSearchCriteria criteria) {
        this.criteria = criteria;
        itemList = new AccountListDisplay();
        this.setContent(itemList);
        displayItems();
    }

    private void displayItems() {
        Integer numItems = itemList.search(criteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(AccountI18nEnum.SINGLE), numItems));
    }
}
