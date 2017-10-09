package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
class CampaignListDisplayView extends AbstractMobilePageView {
    private CampaignSearchCriteria criteria;
    private final CampaignListDisplay itemList;

    CampaignListDisplayView(CampaignSearchCriteria criteria) {
        this.criteria = criteria;
        itemList = new CampaignListDisplay();
        this.setContent(itemList);
        displayItems();
    }

    private void displayItems() {
        Integer numItems = itemList.search(criteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CampaignI18nEnum.SINGLE), numItems));
    }
}
