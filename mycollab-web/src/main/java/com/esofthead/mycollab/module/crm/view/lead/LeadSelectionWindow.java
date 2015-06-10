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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.domain.Lead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
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
public class LeadSelectionWindow extends Window {
    private static final long serialVersionUID = 1L;
    private LeadTableDisplay tableItem;
    private FieldSelection<Lead> fieldSelection;

    public LeadSelectionWindow(FieldSelection<Lead> fieldSelection) {
        super("Lead Selection");
        this.setWidth("800px");
        this.fieldSelection = fieldSelection;
        this.setModal(true);
        this.setResizable(false);
    }

    public void show() {
        MVerticalLayout layout = new MVerticalLayout();

        createLeadList();

        LeadSimpleSearchPanel leadSimpleSearchPanel = new LeadSimpleSearchPanel();
        leadSimpleSearchPanel
                .addSearchHandler(new SearchHandler<LeadSearchCriteria>() {

                    @Override
                    public void onSearch(LeadSearchCriteria criteria) {
                        tableItem.setSearchCriteria(criteria);
                    }

                });
        layout.with(leadSimpleSearchPanel, tableItem);
        this.setContent(layout);

        tableItem.setSearchCriteria(new LeadSearchCriteria());
        center();
    }

    @SuppressWarnings("serial")
    private void createLeadList() {
        tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name(),
                LeadTableFieldDef.status(), LeadTableFieldDef.assignedUser(),
                LeadTableFieldDef.accountName()));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("leadName", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(final Table source,
                                                        final Object itemId, final Object columnId) {
                final SimpleLead lead = tableItem.getBeanByIndex(itemId);

                ButtonLinkLegacy b = new ButtonLinkLegacy(lead.getLeadName(),
                        new Button.ClickListener() {

                            @Override
                            public void buttonClick(
                                    final Button.ClickEvent event) {
                                fieldSelection.fireValueChange(lead);
                                LeadSelectionWindow.this.close();
                            }
                        });
                b.setDescription(CrmTooltipGenerator.generateTooltipLead(
                        AppContext.getUserLocale(), lead,
                        AppContext.getSiteUrl(), AppContext.getTimezone()));
                return b;
            }
        });
    }
}
