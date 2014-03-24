package com.esofthead.mycollab.module.crm.view.contact;

import java.util.Arrays;

import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.ui.components.RelatedItemSelectionWindow2;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunitySimpleSearchPanel;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityTableDisplay;
import com.esofthead.mycollab.module.crm.view.opportunity.OpportunityTableFieldDef;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 */
@SuppressWarnings("serial")
public class ContactOpportunitySelectionWindow2
extends
RelatedItemSelectionWindow2<SimpleOpportunity, OpportunitySearchCriteria> {

	public ContactOpportunitySelectionWindow2(
			ContactOpportunityListComp2 associateOpportunityList) {
		super("Select Opportunities", associateOpportunityList);

		this.setWidth("900px");
	}

	@Override
	protected void initUI() {
		tableItem = new OpportunityTableDisplay(
				OpportunityTableFieldDef.selected, Arrays.asList(
						OpportunityTableFieldDef.opportunityName,
						OpportunityTableFieldDef.saleStage,
						OpportunityTableFieldDef.expectedCloseDate));

		Button selectBtn = new Button("Select", new Button.ClickListener() {

			@Override
			public void buttonClick(Button.ClickEvent event) {
				close();
			}
		});
		selectBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		OpportunitySimpleSearchPanel opportunitySimpleSearchPanel = new OpportunitySimpleSearchPanel();
		opportunitySimpleSearchPanel
		.addSearchHandler(new SearchHandler<OpportunitySearchCriteria>() {

			@Override
			public void onSearch(OpportunitySearchCriteria criteria) {
				tableItem.setSearchCriteria(criteria);
			}

		});

		this.bodyContent.addComponent(opportunitySimpleSearchPanel);
		this.bodyContent.addComponent(selectBtn);
		this.bodyContent.addComponent(tableItem);
	}
}
