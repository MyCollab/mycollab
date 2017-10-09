package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class CaseListPresenter extends CrmListPresenter<CaseListView, CaseSearchCriteria, SimpleCase> {
    private static final long serialVersionUID = -7453209524304809914L;

    public CaseListPresenter() {
        super(CaseListView.class);
    }

}
