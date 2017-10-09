package com.mycollab.mobile.module.project.view.parameters

import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TicketScreenData {
    class GotoDashboard(param: ProjectTicketSearchCriteria) : ScreenData<ProjectTicketSearchCriteria>(param)
}