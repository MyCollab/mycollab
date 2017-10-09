package com.mycollab.module.project.view.parameters

import com.mycollab.module.project.domain.Milestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MilestoneScreenData {
    class Read(params: Int) : ScreenData<Int>(params)

    class Edit(params: Milestone) : ScreenData<Milestone>(params)

    class Add(params: Milestone) : ScreenData<Milestone>(params)

    class Search(params: MilestoneSearchCriteria) : ScreenData<MilestoneSearchCriteria>(params)

    class Roadmap() : ScreenData<Any>(null)

    class Kanban() : ScreenData<Any>(null)
}