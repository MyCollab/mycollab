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
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.fielddef.OpportunityTableFieldDef;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunitySalesStage;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.opportunity.OpportunitySearchPanel;
import com.mycollab.module.crm.view.opportunity.OpportunityTableDisplay;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class ContactOpportunitySelectionWindow extends RelatedItemSelectionWindow<SimpleOpportunity, OpportunitySearchCriteria> {

    public ContactOpportunitySelectionWindow(ContactOpportunityListComp associateOpportunityList) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(OpportunityI18nEnum.LIST)),
                associateOpportunityList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        tableItem = new OpportunityTableDisplay(OpportunityTableFieldDef.selected(), Arrays.asList(
                OpportunityTableFieldDef.opportunityName(), OpportunityTableFieldDef.saleStage(),
                OpportunityTableFieldDef.expectedCloseDate()));

        tableItem.addGeneratedColumn("opportunityname", (source, itemId, columnId) -> {
            final SimpleOpportunity opportunity = tableItem.getBeanByIndex(itemId);

            ELabel b = new ELabel(opportunity.getOpportunityname()).withStyleName(WebThemes.BUTTON_LINK)
                    .withDescription(CrmTooltipGenerator.generateTooltipOpportunity(UserUIContext.getUserLocale(),
                            MyCollabUI.getDateFormat(), opportunity, MyCollabUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
            if (OpportunitySalesStage.Closed_Won.name().equals(opportunity.getSalesstage()) ||
                    OpportunitySalesStage.Closed_Lost.name().equals(opportunity.getSalesstage())) {
                b.addStyleName(WebThemes.LINK_COMPLETED);
            } else {
                if (opportunity.isOverdue()) {
                    b.addStyleName(WebThemes.LINK_OVERDUE);
                }
            }

            return b;
        });

        MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_ACTION);

        OpportunitySearchPanel searchPanel = new OpportunitySearchPanel(false);
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        bodyContent.with(searchPanel, selectBtn, tableItem);
    }
}
