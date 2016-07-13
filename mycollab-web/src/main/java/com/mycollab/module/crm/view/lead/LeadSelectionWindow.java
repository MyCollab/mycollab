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
package com.mycollab.module.crm.view.lead;

import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.SearchHandler;
import com.mycollab.vaadin.web.ui.ButtonLink;
import com.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private LeadTableDisplay tableItem;
    private FieldSelection<Lead> fieldSelection;

    public LeadSelectionWindow(FieldSelection<Lead> fieldSelection) {
        super("Lead Selection");
        this.withModal(true).withResizable(false).withWidth("1000px").withCenter();
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createLeadList();

        LeadSearchPanel searchPanel = new LeadSearchPanel();
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        this.setContent(new MVerticalLayout(searchPanel, tableItem));
    }

    private void createLeadList() {
        tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name(),
                LeadTableFieldDef.status(), LeadTableFieldDef.assignedUser(),
                LeadTableFieldDef.accountName()));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("leadName", (source, itemId, columnId) -> {
                final SimpleLead lead = tableItem.getBeanByIndex(itemId);

                ButtonLink b = new ButtonLink(lead.getLeadName(), clickEvent -> {
                    fieldSelection.fireValueChange(lead);
                    close();
                });
                b.setDescription(CrmTooltipGenerator.generateTooltipLead(AppContext.getUserLocale(), lead,
                        AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
                return b;
        });
        tableItem.setSearchCriteria(new LeadSearchCriteria());
    }
}
