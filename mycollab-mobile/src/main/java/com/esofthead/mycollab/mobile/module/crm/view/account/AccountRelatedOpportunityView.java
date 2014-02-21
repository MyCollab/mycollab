package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.opportunity.OpportunityListDisplay;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;

public class AccountRelatedOpportunityView extends
AbstractRelatedListView<SimpleOpportunity, OpportunitySearchCriteria> {
	private static final long serialVersionUID = -5900127054425653263L;

	public AccountRelatedOpportunityView() {
		initUI();
	}

	private void initUI() {
		this.setCaption("Related Opportunities");
		tableItem = new OpportunityListDisplay("opportunityname");
		this.setContent(tableItem);
	}

	@Override
	public void refresh() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
