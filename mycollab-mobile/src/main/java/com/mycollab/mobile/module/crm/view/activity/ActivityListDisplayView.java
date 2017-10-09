package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
public class ActivityListDisplayView extends AbstractMobilePageView {
    private ActivitySearchCriteria criteria;
    private final ActivityListDisplay itemList;

    public ActivityListDisplayView(ActivitySearchCriteria criteria) {
        this.criteria = criteria;
        itemList = new ActivityListDisplay();
        this.setContent(itemList);
        displayItems();
    }

    private void displayItems() {
        Integer numItems = itemList.search(criteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY), numItems));
    }
}
