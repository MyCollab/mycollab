package com.mycollab.module.project.view.parameters

import com.mycollab.module.tracker.domain.Component
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ComponentScreenData {
    class Read(params: Int) : ScreenData<Int>(params)

    class Add(params: Component) : ScreenData<Component>(params)

    class Edit(params: Component) : ScreenData<Component>(params)

    class Search(params: ComponentSearchCriteria) : ScreenData<ComponentSearchCriteria>(params)
}