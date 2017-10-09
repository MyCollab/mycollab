package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TimeTrackingScreenData {
    class Search(param: ItemTimeLoggingSearchCriteria) : ScreenData<ItemTimeLoggingSearchCriteria>(param)
}