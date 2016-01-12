/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.view.contact;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunitySimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityTableDisplay;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityTableFieldDef;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.ui.Button;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactOpportunitySelectionWindow extends RelatedItemSelectionWindow<SimpleOpportunity, OpportunitySearchCriteria> {

    public ContactOpportunitySelectionWindow(ContactOpportunityListComp associateOpportunityList) {
        super("Select Opportunities", associateOpportunityList);
        this.setWidth("900px");
    }

    @Override
    protected void initUI() {
        tableItem = new OpportunityTableDisplay(
                OpportunityTableFieldDef.selected(), Arrays.asList(
                OpportunityTableFieldDef.opportunityName(),
                OpportunityTableFieldDef.saleStage(),
                OpportunityTableFieldDef.expectedCloseDate()));

        Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });
        selectBtn.setStyleName(UIConstants.BUTTON_ACTION);

        OpportunitySimpleSearchPanel opportunitySimpleSearchPanel = new OpportunitySimpleSearchPanel();
        opportunitySimpleSearchPanel.addSearchHandler(new SearchHandler<OpportunitySearchCriteria>() {

            @Override
            public void onSearch(OpportunitySearchCriteria criteria) {
                tableItem.setSearchCriteria(criteria);
            }

        });

        this.bodyContent.with(opportunitySimpleSearchPanel, selectBtn, tableItem);
    }
}
