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

import java.util.Arrays;

import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignSimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignTableDisplay;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignTableFieldDef;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("serial")
class LeadCampaignSelectionWindow extends
		RelatedItemSelectionWindow<SimpleCampaign, CampaignSearchCriteria> {

	LeadCampaignSelectionWindow(LeadCampaignListComp associateLeadList) {
		super("Select Campaigns", associateLeadList);

		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		tableItem = new CampaignTableDisplay(CampaignTableFieldDef.selected,
				Arrays.asList(CampaignTableFieldDef.campaignname,
						CampaignTableFieldDef.status,
						CampaignTableFieldDef.type,
						CampaignTableFieldDef.endDate));

		Button selectBtn = new Button("Select", new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				close();
			}
		});
		selectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		CampaignSimpleSearchPanel campaignSimpleSearchPanel = new CampaignSimpleSearchPanel();
		campaignSimpleSearchPanel
				.addSearchHandler(new SearchHandler<CampaignSearchCriteria>() {

					@Override
					public void onSearch(CampaignSearchCriteria criteria) {
						tableItem.setSearchCriteria(criteria);
					}

				});

		this.bodyContent.addComponent(campaignSimpleSearchPanel);
		this.bodyContent.addComponent(selectBtn);
		this.bodyContent.addComponent(tableItem);
	}

}
