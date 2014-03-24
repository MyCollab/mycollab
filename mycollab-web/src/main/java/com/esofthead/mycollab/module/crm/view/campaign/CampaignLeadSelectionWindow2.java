package com.esofthead.mycollab.module.crm.view.campaign;

import java.util.Arrays;

import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow2;
import com.esofthead.mycollab.module.crm.view.lead.LeadSimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.lead.LeadTableDisplay;
import com.esofthead.mycollab.module.crm.view.lead.LeadTableFieldDef;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 */
@SuppressWarnings("serial")
public class CampaignLeadSelectionWindow2 extends
RelatedItemSelectionWindow2<SimpleLead, LeadSearchCriteria> {

	public CampaignLeadSelectionWindow2(CampaignLeadListComp2 associateLeadList) {
		super("Select Leads", associateLeadList);

		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		tableItem = new LeadTableDisplay(LeadTableFieldDef.selected,
				Arrays.asList(LeadTableFieldDef.name, LeadTableFieldDef.status,
						LeadTableFieldDef.email, LeadTableFieldDef.phoneoffice));

		Button selectBtn = new Button("Select", new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				close();
			}
		});
		selectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		LeadSimpleSearchPanel leadSimpleSearchPanel = new LeadSimpleSearchPanel();
		leadSimpleSearchPanel
		.addSearchHandler(new SearchHandler<LeadSearchCriteria>() {

			@Override
			public void onSearch(LeadSearchCriteria criteria) {
				tableItem.setSearchCriteria(criteria);
			}

		});

		this.bodyContent.addComponent(leadSimpleSearchPanel);
		this.bodyContent.addComponent(selectBtn);
		this.bodyContent.addComponent(tableItem);
	}
}
