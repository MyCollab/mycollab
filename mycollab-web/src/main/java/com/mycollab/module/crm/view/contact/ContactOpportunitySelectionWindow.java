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
package com.mycollab.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.opportunity.OpportunitySearchPanel;
import com.mycollab.module.crm.view.opportunity.OpportunityTableDisplay;
import com.mycollab.module.crm.view.opportunity.OpportunityTableFieldDef;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.ui.Button;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactOpportunitySelectionWindow extends RelatedItemSelectionWindow<SimpleOpportunity, OpportunitySearchCriteria> {

    public ContactOpportunitySelectionWindow(ContactOpportunityListComp associateOpportunityList) {
        super("Select Opportunities", associateOpportunityList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        tableItem = new OpportunityTableDisplay(OpportunityTableFieldDef.selected(), Arrays.asList(
                OpportunityTableFieldDef.opportunityName(), OpportunityTableFieldDef.saleStage(),
                OpportunityTableFieldDef.expectedCloseDate()));

        Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close());
        selectBtn.setStyleName(WebUIConstants.BUTTON_ACTION);

        OpportunitySearchPanel searchPanel = new OpportunitySearchPanel();
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        this.bodyContent.with(searchPanel, selectBtn, tableItem);
    }
}
