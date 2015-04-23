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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.ButtonLinkLegacy;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.vaadin.maddon.layouts.MVerticalLayout;

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
        opportunitySimpleSearchPanel
                .addSearchHandler(new SearchHandler<OpportunitySearchCriteria>() {

                    @Override
                    public void onSearch(OpportunitySearchCriteria criteria) {
                        tableItem.setSearchCriteria(criteria);
                    }

                });
        layout.with(opportunitySimpleSearchPanel, tableItem);
        this.setContent(layout);

        tableItem.setSearchCriteria(new OpportunitySearchCriteria());
        center();
    }

    private void createOpportunityList() {
        tableItem = new OpportunityTableDisplay(Arrays.asList(
                OpportunityTableFieldDef.opportunityName,
                OpportunityTableFieldDef.saleStage,
                OpportunityTableFieldDef.accountName,
                OpportunityTableFieldDef.assignUser));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("opportunityname",
                new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public com.vaadin.ui.Component generateCell(
                            final Table source, final Object itemId,
                            final Object columnId) {
                        final SimpleOpportunity opportunity = tableItem
                                .getBeanByIndex(itemId);

                        ButtonLinkLegacy b = new ButtonLinkLegacy(opportunity
                                .getOpportunityname(),
                                new Button.ClickListener() {
                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public void buttonClick(
                                            final Button.ClickEvent event) {
                                        fieldSelection
                                                .fireValueChange(opportunity);
                                        OpportunitySelectionWindow.this.close();
                                    }
                                });
                        b.setDescription(CrmTooltipGenerator
                                .generateTooltipOpportunity(AppContext.getUserLocale(),
                                        opportunity, AppContext.getSiteUrl(),
                                        AppContext.getTimezone()));
                        return b;
                    }
                });
    }
}
