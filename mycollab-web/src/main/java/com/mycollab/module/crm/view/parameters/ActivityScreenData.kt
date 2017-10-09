package com.mycollab.module.crm.view.parameters

import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

object ActivityScreenData {

    class GotoCalendar : ScreenData<Any>(null)

    class GotoActivityList(criteria: ActivitySearchCriteria) : ScreenData<ActivitySearchCriteria>(criteria)
}
