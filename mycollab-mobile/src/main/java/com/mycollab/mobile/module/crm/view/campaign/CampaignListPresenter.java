package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class CampaignListPresenter extends CrmListPresenter<CampaignListView, CampaignSearchCriteria, SimpleCampaign> {
    private static final long serialVersionUID = 1327621011652399974L;

    public CampaignListPresenter() {
        super(CampaignListView.class);
    }

}
