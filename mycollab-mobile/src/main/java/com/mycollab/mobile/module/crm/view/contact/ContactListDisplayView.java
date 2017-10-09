package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.vaadin.UserUIContext;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
class ContactListDisplayView extends AbstractMobilePageView {
    private ContactSearchCriteria criteria;
    private final ContactListDisplay itemList;

    ContactListDisplayView(ContactSearchCriteria criteria) {
        this.criteria = criteria;
        itemList = new ContactListDisplay();
        this.setContent(itemList);
        displayItems();
    }

    private void displayItems() {
        Integer numItems = itemList.search(criteria);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(ContactI18nEnum.SINGLE), numItems));
    }
}
