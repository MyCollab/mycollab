package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.cases.CaseListDisplay;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;

public class AccountRelatedCaseView extends AbstractRelatedListView<SimpleCase, CaseSearchCriteria> {
	private static final long serialVersionUID = -4559344487784697088L;

	public AccountRelatedCaseView() {
		initUI();
	}

	private void initUI() {
		this.setCaption("Related Cases");
		tableItem = new CaseListDisplay("subject");
		this.setContent(tableItem);
	}

	@Override
	public void refresh() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
