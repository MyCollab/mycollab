package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
class LeadListDisplayView extends AbstractMobilePageView {
    private LeadSearchCriteria criteria;
    private final LeadListDisplay itemList;

    LeadListDisplayView(LeadSearchCriteria criteria) {
        this.criteria = criteria;
        itemList = new LeadListDisplay();
        this.setContent(itemList);
        displayItems();
    }

    private void displayItems() {
        Integer numItems = itemList.search(criteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(LeadI18nEnum.SINGLE), numItems));
    }
}
