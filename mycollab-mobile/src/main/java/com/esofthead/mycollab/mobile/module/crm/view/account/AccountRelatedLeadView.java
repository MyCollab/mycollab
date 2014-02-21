package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadListDisplay;
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;

public class AccountRelatedLeadView extends AbstractRelatedListView<SimpleLead, LeadSearchCriteria> {
	private static final long serialVersionUID = -6563776375301107391L;
	private Account account;

	public AccountRelatedLeadView() {
		initUI();
	}

	private void initUI() {
		this.setCaption("Related Leads");
		this.tableItem = new LeadListDisplay("leadName");
		this.setContent(tableItem);
	}

	public void displayLeads(final Account account) {
		this.account = account;
		loadLeads();
	}

	private void loadLeads() {
		final LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setAccountId(new NumberSearchField(SearchField.AND, account
				.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadLeads();
	}

}
