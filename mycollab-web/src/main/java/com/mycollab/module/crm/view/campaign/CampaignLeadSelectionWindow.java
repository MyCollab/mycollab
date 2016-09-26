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
package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.fielddef.LeadTableFieldDef;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.lead.LeadSearchPanel;
import com.mycollab.module.crm.view.lead.LeadTableDisplay;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.ui.Button;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public class CampaignLeadSelectionWindow extends RelatedItemSelectionWindow<SimpleLead, LeadSearchCriteria> {

    public CampaignLeadSelectionWindow(CampaignLeadListComp associateLeadList) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(LeadI18nEnum.LIST)), associateLeadList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        tableItem = new LeadTableDisplay(LeadTableFieldDef.selected(),
                Arrays.asList(LeadTableFieldDef.name(), LeadTableFieldDef.status(),
                        LeadTableFieldDef.email(), LeadTableFieldDef.phoneoffice()));

        Button selectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close());
        selectBtn.setStyleName(WebUIConstants.BUTTON_ACTION);

        LeadSearchPanel searchPanel = new LeadSearchPanel();
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        bodyContent.with(searchPanel, selectBtn, tableItem);
    }
}
