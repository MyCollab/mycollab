package com.mycollab.mobile.module.crm.view.lead

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.crm.domain.SimpleLead
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
interface LeadListView : IListView<LeadSearchCriteria, SimpleLead>
