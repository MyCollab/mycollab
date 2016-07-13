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

import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.ButtonLink;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private CampaignTableDisplay tableItem;
    private FieldSelection<CampaignWithBLOBs> fieldSelection;

    public CampaignSelectionWindow(FieldSelection<CampaignWithBLOBs> fieldSelection) {
        super("Campaign Selection");
        this.withModal(true).withResizable(false).withWidth("1000px");
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createCampaignList();
        CampaignSearchPanel campaignSimpleSearchPanel = new CampaignSearchPanel();
        campaignSimpleSearchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        this.setContent(new MVerticalLayout(campaignSimpleSearchPanel, tableItem));
        tableItem.setSearchCriteria(new CampaignSearchCriteria());
        center();
    }

    private void createCampaignList() {
        tableItem = new CampaignTableDisplay(Arrays.asList(
                CampaignTableFieldDef.campaignname(), CampaignTableFieldDef.type(),
                CampaignTableFieldDef.status(), CampaignTableFieldDef.endDate(),
                CampaignTableFieldDef.assignUser()));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("campaignname", (source, itemId, columnId) -> {
            final SimpleCampaign campaign = tableItem.getBeanByIndex(itemId);

            ButtonLink campaignLink = new ButtonLink(campaign.getCampaignname(), clickEvent -> {
                fieldSelection.fireValueChange(campaign);
                close();
            });
            campaignLink.setDescription(CrmTooltipGenerator.generateTooltipCampaign(AppContext.getUserLocale(),
                    AppContext.getDateFormat(), campaign, AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
            return campaignLink;
        });
    }
}
