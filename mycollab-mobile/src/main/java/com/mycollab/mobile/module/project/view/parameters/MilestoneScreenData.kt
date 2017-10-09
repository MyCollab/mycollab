package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MilestoneScreenData {
    class Search(param: MilestoneSearchCriteria) : ScreenData<MilestoneSearchCriteria>(param)

    class Read(param: Int) : ScreenData<Int>(param)

    class Edit(param: SimpleMilestone) : ScreenData<SimpleMilestone>(param)

    class Add(param: SimpleMilestone) : ScreenData<SimpleMilestone>(param)
}