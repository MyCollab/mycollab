package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class OpportunityListPresenter extends CrmListPresenter<OpportunityListView, OpportunitySearchCriteria, SimpleOpportunity> {

    private static final long serialVersionUID = 457053782202007112L;

    public OpportunityListPresenter() {
        super(OpportunityListView.class);
    }

}
