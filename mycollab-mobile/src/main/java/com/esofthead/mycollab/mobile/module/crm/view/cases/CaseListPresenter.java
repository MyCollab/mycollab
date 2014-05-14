package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.mobile.ui.ListPresenter;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;

public class CaseListPresenter extends
		ListPresenter<CaseListView, CaseSearchCriteria, SimpleCase> {
	private static final long serialVersionUID = -7453209524304809914L;

	public CaseListPresenter() {
		super(CaseListView.class);
	}

}
