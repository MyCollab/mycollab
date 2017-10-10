/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
