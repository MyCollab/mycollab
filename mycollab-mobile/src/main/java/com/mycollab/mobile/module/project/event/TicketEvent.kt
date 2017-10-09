package com.mycollab.mobile.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TicketEvent {
    class GotoDashboard(source: Any, data: ProjectTicketSearchCriteria) : ApplicationEvent(source)
}