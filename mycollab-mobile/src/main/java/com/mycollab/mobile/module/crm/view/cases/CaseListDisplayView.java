package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
class CaseListDisplayView extends AbstractMobilePageView {
    private CaseSearchCriteria criteria;
    private final CaseListDisplay itemList;

    CaseListDisplayView(CaseSearchCriteria criteria) {
        this.criteria = criteria;
        itemList = new CaseListDisplay();
        this.setContent(itemList);
        displayItems();
    }

    private void displayItems() {
        Integer numItems = itemList.search(criteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CaseI18nEnum.SINGLE), numItems));
    }
}
