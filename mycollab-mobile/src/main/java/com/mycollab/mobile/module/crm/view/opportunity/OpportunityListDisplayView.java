package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
class OpportunityListDisplayView extends AbstractMobilePageView {
    private OpportunitySearchCriteria criteria;
    private final OpportunityListDisplay itemList;

    OpportunityListDisplayView(OpportunitySearchCriteria criteria) {
        this.criteria = criteria;
        itemList = new OpportunityListDisplay();
        this.setContent(itemList);
        displayItems();
    }

    private void displayItems() {
        Integer numItems = itemList.search(criteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(OpportunityI18nEnum.SINGLE), numItems));
    }
}
