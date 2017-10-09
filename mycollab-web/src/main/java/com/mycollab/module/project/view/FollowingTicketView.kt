package com.mycollab.module.project.view

import com.mycollab.module.project.domain.FollowingTicket
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria
import com.mycollab.vaadin.event.HasSearchHandlers
import com.mycollab.vaadin.web.ui.InitializingView
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface FollowingTicketView : InitializingView {
    fun displayTickets()

    val searchHandlers: HasSearchHandlers<FollowingTicketSearchCriteria>

    val pagedBeanTable: AbstractPagedBeanTable<FollowingTicketSearchCriteria, FollowingTicket>
}
