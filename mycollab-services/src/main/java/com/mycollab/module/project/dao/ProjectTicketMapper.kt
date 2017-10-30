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
package com.mycollab.module.project.dao

import com.mycollab.common.domain.GroupItem
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.module.user.domain.BillingAccount
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.session.RowBounds

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectTicketMapper : ISearchableDAO<ProjectTicketSearchCriteria> {

    fun getTotalCountFromRisk(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): Int

    fun getTotalCountFromBug(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): Int

    fun getTotalCountFromTask(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): Int

    fun getTotalCountFromMilestone(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): Int

    fun getAccountsHasOverdueAssignments(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): List<BillingAccount>

    fun getProjectsHasOverdueAssignments(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): List<Int>

    fun getAssigneeSummary(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): List<GroupItem>

    fun getPrioritySummary(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria): List<GroupItem>

    fun findTicketsByCriteria(@Param("searchCriteria") criteria: ProjectTicketSearchCriteria,
                              rowBounds: RowBounds): List<ProjectTicket>
}
