package com.mycollab.module.crm.view.cases

import com.mycollab.module.crm.domain.SimpleCase
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria
import com.mycollab.vaadin.web.ui.IListView

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface CaseListView : IListView<CaseSearchCriteria, SimpleCase>
