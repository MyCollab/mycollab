package com.mycollab.module.crm.view.activity

import com.mycollab.module.crm.domain.SimpleActivity
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 * @author MyCollab Ltd.
 * @since 2.0
 */
interface ActivityListView : IListView<ActivitySearchCriteria, SimpleActivity>
