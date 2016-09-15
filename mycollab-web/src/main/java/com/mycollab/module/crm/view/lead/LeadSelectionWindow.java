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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import org.vaadin.viritin.button.MButton;
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
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(LeadI18nEnum.SINGLE)));
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
        tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name(), LeadTableFieldDef.status(),
                LeadTableFieldDef.assignedUser(), LeadTableFieldDef.accountName()));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("leadName", (source, itemId, columnId) -> {
            final SimpleLead lead = tableItem.getBeanByIndex(itemId);

            return new MButton(lead.getLeadName(), clickEvent -> {
                fieldSelection.fireValueChange(lead);
                close();
            }).withStyleName(WebUIConstants.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateTooltipLead(UserUIContext.getUserLocale(),
                    lead, MyCollabUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
        tableItem.setSearchCriteria(new LeadSearchCriteria());
    }
}
