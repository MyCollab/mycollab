package com.mycollab.mobile.module.project.view.ticket

import com.mycollab.mobile.ui.IListView
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
interface TicketListView : IListView<ProjectTicketSearchCriteria, ProjectTicket>
