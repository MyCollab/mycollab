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
public class LeadCampaignSelectionWindow extends
RelatedItemSelectionWindow<SimpleCampaign, CampaignSearchCriteria> {

	public LeadCampaignSelectionWindow(LeadCampaignListComp associateLeadList) {
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
