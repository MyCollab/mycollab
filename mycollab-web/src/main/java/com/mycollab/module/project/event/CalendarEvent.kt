package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object CalendarEvent {
    class SearchRequest(source: Any, val data: ProjectTicketSearchCriteria) : ApplicationEvent(source)
}