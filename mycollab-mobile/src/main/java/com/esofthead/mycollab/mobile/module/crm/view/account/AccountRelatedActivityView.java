package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.mobile.module.crm.view.activity.ActivityListDisplay;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;

public class AccountRelatedActivityView extends AbstractRelatedListView<SimpleActivity, ActivitySearchCriteria> {
	private static final long serialVersionUID = 955474758141391716L;

	public AccountRelatedActivityView() {
		initUI();
	}

	private void initUI() {
		this.setCaption("Related Activities");
		tableItem = new ActivityListDisplay("subject");
		this.setContent(tableItem);
	}

	@Override
	public void refresh() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
