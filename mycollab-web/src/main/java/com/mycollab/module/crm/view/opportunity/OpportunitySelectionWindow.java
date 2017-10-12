/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.fielddef.OpportunityTableFieldDef;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunitySelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private OpportunityTableDisplay tableItem;
    private FieldSelection fieldSelection;

    public OpportunitySelectionWindow(FieldSelection fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(OpportunityI18nEnum.SINGLE)));
        this.withModal(true).withResizable(false).withWidth("1000px").withCenter();
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createOpportunityList();
        OpportunitySearchPanel searchPanel = new OpportunitySearchPanel(false);
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        this.setContent(new MVerticalLayout(searchPanel, tableItem));

        tableItem.setSearchCriteria(new OpportunitySearchCriteria());
    }

    private void createOpportunityList() {
        tableItem = new OpportunityTableDisplay(Arrays.asList(OpportunityTableFieldDef.opportunityName,
                OpportunityTableFieldDef.saleStage, OpportunityTableFieldDef.accountName,
                OpportunityTableFieldDef.assignUser));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn(Opportunity.Field.opportunityname.name(), (source, itemId, columnId) -> {
            final SimpleOpportunity opportunity = tableItem.getBeanByIndex(itemId);

            return new MButton(opportunity.getOpportunityname(), clickEvent -> {
                fieldSelection.fireValueChange(opportunity);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateTooltipOpportunity(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                    opportunity, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
    }
}
