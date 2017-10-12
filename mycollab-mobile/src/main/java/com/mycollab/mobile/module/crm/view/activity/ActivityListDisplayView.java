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
