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
