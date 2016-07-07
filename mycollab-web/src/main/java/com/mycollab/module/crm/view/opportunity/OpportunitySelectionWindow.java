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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.ButtonLink;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunitySelectionWindow extends Window {
    private static final long serialVersionUID = 1L;

    private OpportunityTableDisplay tableItem;
    private FieldSelection fieldSelection;

    public OpportunitySelectionWindow(FieldSelection fieldSelection) {
        super("Opportunity Selection");
        this.setWidth("900px");
        this.fieldSelection = fieldSelection;
        this.setModal(true);
        this.setResizable(false);
    }

    public void show() {
        MVerticalLayout layout = new MVerticalLayout();

        createOpportunityList();
        OpportunitySimpleSearchPanel opportunitySimpleSearchPanel = new OpportunitySimpleSearchPanel();
        opportunitySimpleSearchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        layout.with(opportunitySimpleSearchPanel, tableItem);
        this.setContent(layout);

        tableItem.setSearchCriteria(new OpportunitySearchCriteria());
        center();
    }

    private void createOpportunityList() {
        tableItem = new OpportunityTableDisplay(Arrays.asList(OpportunityTableFieldDef.opportunityName(),
                OpportunityTableFieldDef.saleStage(), OpportunityTableFieldDef.accountName(),
                OpportunityTableFieldDef.assignUser()));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn(Opportunity.Field.opportunityname.name(), (source, itemId, columnId) -> {
            final SimpleOpportunity opportunity = tableItem.getBeanByIndex(itemId);

            ButtonLink b = new ButtonLink(opportunity.getOpportunityname(), clickEvent -> {
                fieldSelection.fireValueChange(opportunity);
                close();
            });
            b.setDescription(CrmTooltipGenerator.generateTooltipOpportunity(AppContext.getUserLocale(), AppContext.getDateFormat(),
                    opportunity, AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
            return b;
        });
    }
}
