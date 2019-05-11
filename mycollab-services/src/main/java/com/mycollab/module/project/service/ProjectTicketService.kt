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
 * along with this program.  If not, see <http:></http:>//www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.service

import com.mycollab.common.domain.GroupItem
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.persistence.service.ISearchableService
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.module.user.domain.BillingAccount

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectTicketService : ISearchableService<ProjectTicketSearchCriteria> {
    fun getAccountsHasOverdueAssignments(searchCriteria: ProjectTicketSearchCriteria): List<BillingAccount>

    fun getProjectsHasOverdueAssignments(searchCriteria: ProjectTicketSearchCriteria): List<Int>

    fun updateAssignmentValue(assignment: ProjectTicket, username: String)

    fun closeSubAssignmentOfMilestone(milestoneId: Int)

    fun findTicket(type: String, typeId: Int): ProjectTicket?

    @Cacheable
    fun findTicketsByCriteria(@CacheKey searchRequest: BasicSearchRequest<ProjectTicketSearchCriteria>): List<*>

    @Cacheable
    fun getTotalTicketsCount(@CacheKey criteria: ProjectTicketSearchCriteria): Int

    @Cacheable
    fun getAssigneeSummary(@CacheKey criteria: ProjectTicketSearchCriteria): List<GroupItem>

    @Cacheable
    fun getPrioritySummary(@CacheKey criteria: ProjectTicketSearchCriteria): List<GroupItem>

    fun updateTicket(ticket: ProjectTicket, username: String)

    fun updateMilestoneId(ticket: ProjectTicket)

    fun removeTicket(ticket: ProjectTicket, username: String)

    fun isTicketIdSatisfyCriteria(type: String, typeId: Int, criteria: ProjectTicketSearchCriteria): Boolean
}
