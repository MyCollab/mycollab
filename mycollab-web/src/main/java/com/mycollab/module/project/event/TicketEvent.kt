/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TicketEvent {
    class GotoDashboard(source: Any, val data: Any?) : ApplicationEvent(source)

    class NewTicketAdded(source: Any, val typeVal: String, val typeIdVal: Int) : ApplicationEvent(source)

    class SearchRequest(source: Any, val searchCriteria: ProjectTicketSearchCriteria) : ApplicationEvent(source)

    class HasTicketPropertyChanged(source: Any, val data: String) : ApplicationEvent(source)
}