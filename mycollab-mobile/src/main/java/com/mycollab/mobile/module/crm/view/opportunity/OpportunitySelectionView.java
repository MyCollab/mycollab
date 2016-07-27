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
package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.mycollab.mobile.ui.AbstractSelectionView;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class OpportunitySelectionView extends AbstractSelectionView<SimpleOpportunity> {
    private static final long serialVersionUID = -4651110982471036490L;

    private OpportunityListDisplay itemList;

    private OpportunityRowDisplayHandler rowHandler = new OpportunityRowDisplayHandler();

    public OpportunitySelectionView() {
        super();
        createUI();
        this.setCaption(AppContext.getMessage(OpportunityI18nEnum.M_VIEW_OPPORTUNITY_NAME_LOOKUP));
    }

    public void createUI() {
        itemList = new OpportunityListDisplay();
        itemList.setWidth("100%");
        itemList.setRowDisplayHandler(rowHandler);
        this.setContent(itemList);
    }

    @Override
    public void load() {
        OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
        itemList.search(searchCriteria);
        SimpleOpportunity clearOpportunity = new SimpleOpportunity();
        itemList.getListContainer().addComponentAsFirst(rowHandler.generateRow(clearOpportunity, 0));
    }

    private class OpportunityRowDisplayHandler implements RowDisplayHandler<SimpleOpportunity> {

        @Override
        public Component generateRow(final SimpleOpportunity opportunity, int rowIndex) {
            return new Button(opportunity.getOpportunityname(), clickEvent -> {
                selectionField.fireValueChange(opportunity);
                OpportunitySelectionView.this.getNavigationManager().navigateBack();
            });
        }
    }
}
