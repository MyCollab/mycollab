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
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList.RowDisplayHandler;
import com.esofthead.mycollab.mobile.ui.AbstractSelectionView;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class OpportunitySelectionView extends AbstractSelectionView<SimpleOpportunity> {
    private static final long serialVersionUID = -4651110982471036490L;

    private OpportunitySearchCriteria searchCriteria;
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
        searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        itemList.setSearchCriteria(searchCriteria);
        SimpleOpportunity clearOpportunity = new SimpleOpportunity();
        itemList.getListContainer().addComponentAsFirst(rowHandler.generateRow(clearOpportunity, 0));
    }

    private class OpportunityRowDisplayHandler implements RowDisplayHandler<SimpleOpportunity> {

        @Override
        public Component generateRow(final SimpleOpportunity opportunity, int rowIndex) {
            Button b = new Button(opportunity.getOpportunityname(), new Button.ClickListener() {
                private static final long serialVersionUID = -8257474042598100147L;

                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    selectionField.fireValueChange(opportunity);
                    OpportunitySelectionView.this.getNavigationManager().navigateBack();
                }
            });
            if (opportunity.getId() == null)
                b.addStyleName("blank-item");
            return b;
        }

    }
}
