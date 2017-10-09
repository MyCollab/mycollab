package com.mycollab.mobile.module.crm.view.cases

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.crm.domain.SimpleCase
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
interface CaseListView : IListView<CaseSearchCriteria, SimpleCase>
