package com.mycollab.module.project.view.finance

import com.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria
import com.mycollab.vaadin.mvp.PageView

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
interface ITimeTrackingContainer : PageView {
    fun setSearchCriteria(searchCriteria: ItemTimeLoggingSearchCriteria)
}