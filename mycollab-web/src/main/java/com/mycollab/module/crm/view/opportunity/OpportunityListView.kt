package com.mycollab.module.crm.view.opportunity

import com.mycollab.module.crm.domain.SimpleOpportunity
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface OpportunityListView : IListView<OpportunitySearchCriteria, SimpleOpportunity>
