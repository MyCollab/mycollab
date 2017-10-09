package com.mycollab.mobile.module.crm.view.activity;

import com.mycollab.mobile.module.crm.ui.CrmListPresenter;
import com.mycollab.module.crm.domain.SimpleActivity;
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public class ActivityListPresenter extends CrmListPresenter<ActivityListView, ActivitySearchCriteria, SimpleActivity> {
	private static final long serialVersionUID = 3484893839683355658L;

	public ActivityListPresenter() {
		super(ActivityListView.class);
	}

}
