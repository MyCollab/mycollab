package com.mycollab.module.crm.view.lead

import com.mycollab.module.crm.domain.SimpleLead
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
interface LeadListView : IListView<LeadSearchCriteria, SimpleLead>
