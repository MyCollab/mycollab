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
package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.mycollab.module.crm.service.EventService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
public class RelatedActivityNavigatorButton extends NavigationButton {
    private ActivitySearchCriteria criteria;

    public RelatedActivityNavigatorButton() {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY), 0));
        this.addClickListener(navigationButtonClickEvent -> {
            if (criteria != null) {
                getNavigationManager().navigateTo(new ActivityListDisplayView(criteria));
            }
        });
    }

    void displayTotalActivities(ActivitySearchCriteria criteria) {
        this.criteria = criteria;
        EventService eventService = AppContextUtil.getSpringBean(EventService.class);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CrmCommonI18nEnum.TAB_ACTIVITY), eventService.getTotalCount(criteria)));
    }

    public void displayRelatedByAccount(Integer accountId) {
        ActivitySearchCriteria searchCriteria = new ActivitySearchCriteria();
        searchCriteria.setType(StringSearchField.and(CrmTypeConstants.ACCOUNT));
        searchCriteria.setTypeid(NumberSearchField.equal(accountId));
        displayTotalActivities(searchCriteria);
    }
}