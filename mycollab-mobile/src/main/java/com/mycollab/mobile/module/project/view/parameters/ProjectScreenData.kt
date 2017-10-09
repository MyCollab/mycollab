package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectScreenData {
    class Goto(param: Int) : ScreenData<Int>(param)

    class Add(param: SimpleProject) : ScreenData<SimpleProject>(param)

    class Edit(param: Int) : ScreenData<Int>(param)

    class ProjectActivities(param: ActivityStreamSearchCriteria) : ScreenData<ActivityStreamSearchCriteria>(param)

    class GotoDashboard() : ScreenData<Any>(null)

    class AllActivities(param: ActivityStreamSearchCriteria) : ScreenData<ActivityStreamSearchCriteria>(param)
}