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
package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.mobile.ui.AbstractMobilePageView;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
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
                UserUIContext.getMessage(ContactI18nEnum.SINGLE), numItems));
    }
}
