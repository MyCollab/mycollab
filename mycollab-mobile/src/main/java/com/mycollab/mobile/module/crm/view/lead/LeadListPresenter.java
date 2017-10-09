package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class LeadListPresenter extends CrmListPresenter<LeadListView, LeadSearchCriteria, SimpleLead> {
    private static final long serialVersionUID = 606706094457852518L;

    public LeadListPresenter() {
        super(LeadListView.class);
    }

}
