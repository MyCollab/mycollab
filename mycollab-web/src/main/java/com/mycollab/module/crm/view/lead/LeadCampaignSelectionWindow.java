/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.fielddef.CampaignTableFieldDef;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.campaign.CampaignSearchPanel;
import com.mycollab.module.crm.view.campaign.CampaignTableDisplay;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;
import java.util.GregorianCalendar;

import static com.mycollab.module.crm.i18n.OptionI18nEnum.CampaignStatus;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class LeadCampaignSelectionWindow extends RelatedItemSelectionWindow<SimpleCampaign, CampaignSearchCriteria> {

    LeadCampaignSelectionWindow(LeadCampaignListComp associateLeadList) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, CampaignI18nEnum.LIST), associateLeadList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        tableItem = new CampaignTableDisplay(CampaignTableFieldDef.selected,
                Arrays.asList(CampaignTableFieldDef.campaignname, CampaignTableFieldDef.status,
                        CampaignTableFieldDef.type, CampaignTableFieldDef.endDate));

        tableItem.addGeneratedColumn("campaignname", (source, itemId, columnId) -> {
            final SimpleCampaign campaign = tableItem.getBeanByIndex(itemId);

            ELabel b = new ELabel(campaign.getCampaignname()).withStyleName(WebThemes.BUTTON_LINK)
                    .withDescription(CrmTooltipGenerator.generateTooltipCampaign(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                            campaign, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));

            if (CampaignStatus.Completed.name().equals(campaign.getStatus())) {
                b.addStyleName(WebThemes.LINK_COMPLETED);
            } else {
                if (campaign.getEnddate() != null && (campaign.getEnddate().before(new GregorianCalendar().getTime()))) {
                    b.addStyleName(WebThemes.LINK_OVERDUE);
                }
            }
            return b;
        });

        MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_ACTION);

        CampaignSearchPanel searchPanel = new CampaignSearchPanel(false);
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        bodyContent.with(searchPanel, selectBtn, tableItem);
    }
}
