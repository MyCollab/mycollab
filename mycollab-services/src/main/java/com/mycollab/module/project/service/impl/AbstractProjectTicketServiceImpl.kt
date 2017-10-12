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
package com.mycollab.module.project.service.impl

import com.mycollab.common.domain.GroupItem
import com.mycollab.core.cache.CacheKey
import com.mycollab.core.utils.BeanUtility
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.db.persistence.service.DefaultSearchService
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.ProjectTicketMapper
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.module.project.service.ProjectTaskService
import com.mycollab.module.project.service.ProjectTicketService
import com.mycollab.module.project.service.RiskService
import com.mycollab.module.tracker.service.BugService
import com.mycollab.module.user.domain.BillingAccount
import com.mycollab.spring.AppContextUtil
import org.apache.ibatis.session.RowBounds
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
abstract class AbstractProjectTicketServiceImpl : DefaultSearchService<ProjectTicketSearchCriteria>(), ProjectTicketService {

    @Autowired
    private lateinit var projectTicketMapper: ProjectTicketMapper

    override val searchMapper: ISearchableDAO<ProjectTicketSearchCriteria>
        get() = projectTicketMapper

    override fun getTotalCount(criteria: ProjectTicketSearchCriteria): Int {
        return projectTicketMapper.getTotalCountFromRisk(criteria) +
                projectTicketMapper.getTotalCountFromBug(criteria) +
                projectTicketMapper.getTotalCountFromTask(criteria) +
                projectTicketMapper.getTotalCountFromMilestone(criteria)
    }

    override fun getTotalTicketsCount(@CacheKey criteria: ProjectTicketSearchCriteria): Int? {
        return projectTicketMapper.getTotalCountFromRisk(criteria) +
                projectTicketMapper.getTotalCountFromBug(criteria)!! +
                projectTicketMapper.getTotalCountFromTask(criteria)!!
    }

    override fun isTicketIdSatisfyCriteria(type: String, typeId: Int?, criteria: ProjectTicketSearchCriteria): Boolean {
        val newCriteria = BeanUtility.deepClone(criteria)
        newCriteria.typeIds = SetSearchField(typeId)
        newCriteria.types = SetSearchField(type)
        return when (type) {
            ProjectTypeConstants.TASK -> projectTicketMapper.getTotalCountFromTask(newCriteria) > 0
            ProjectTypeConstants.BUG -> projectTicketMapper.getTotalCountFromBug(newCriteria) > 0
            ProjectTypeConstants.RISK -> projectTicketMapper.getTotalCountFromRisk(newCriteria) > 0
            else -> false
        }
    }

    override fun getAccountsHasOverdueAssignments(searchCriteria: ProjectTicketSearchCriteria): List<BillingAccount> {
        return projectTicketMapper.getAccountsHasOverdueAssignments(searchCriteria)
    }

    override fun getProjectsHasOverdueAssignments(searchCriteria: ProjectTicketSearchCriteria): List<Int> {
        return projectTicketMapper.getProjectsHasOverdueAssignments(searchCriteria)
    }

    override fun findTicket(type: String, typeId: Int?): ProjectTicket? {
        val searchCriteria = ProjectTicketSearchCriteria()
        searchCriteria.types = SetSearchField(type)
        searchCriteria.typeIds = SetSearchField(typeId)
        val assignments = findAbsoluteListByCriteria(searchCriteria, 0, 1) as List<ProjectTicket>
        return if (assignments.isNotEmpty()) assignments[0] else null
    }

    override fun getAssigneeSummary(@CacheKey criteria: ProjectTicketSearchCriteria): List<GroupItem> {
        return projectTicketMapper.getAssigneeSummary(criteria)
    }

    override fun getPrioritySummary(@CacheKey criteria: ProjectTicketSearchCriteria): List<GroupItem> {
        return projectTicketMapper.getPrioritySummary(criteria)
    }

    override fun findTicketsByCriteria(@CacheKey searchRequest: BasicSearchRequest<ProjectTicketSearchCriteria>): List<*> {
        return projectTicketMapper.findTicketsByCriteria(searchRequest.searchCriteria,
                RowBounds((searchRequest.currentPage - 1) * searchRequest.numberOfItems,
                        searchRequest.numberOfItems))
    }

    override fun updateTicket(ticket: ProjectTicket, username: String) {
        when {
            ticket.isTask -> {
                val task = ProjectTicket.buildTask(ticket)
                AppContextUtil.getSpringBean(ProjectTaskService::class.java).updateSelectiveWithSession(task, username)
            }
            ticket.isBug -> {
                val bug = ProjectTicket.buildBug(ticket)
                AppContextUtil.getSpringBean(BugService::class.java).updateSelectiveWithSession(bug, username)
            }
            ticket.isRisk -> {
                val risk = ProjectTicket.buildRisk(ticket)
                AppContextUtil.getSpringBean(RiskService::class.java).updateSelectiveWithSession(risk, username)
            }
        }
    }

    override fun updateMilestoneId(ticket: ProjectTicket) {

    }

    override fun removeTicket(ticket: ProjectTicket, username: String) {
        when {
            ticket.isTask -> {
                val task = ProjectTicket.buildTask(ticket)
                AppContextUtil.getSpringBean(ProjectTaskService::class.java).removeWithSession(task, username, ticket.getsAccountId())
            }
            ticket.isBug -> {
                val bug = ProjectTicket.buildBug(ticket)
                AppContextUtil.getSpringBean(BugService::class.java).removeWithSession(bug, username, ticket.getsAccountId())
            }
            ticket.isRisk -> {
                val risk = ProjectTicket.buildRisk(ticket)
                AppContextUtil.getSpringBean(RiskService::class.java).removeWithSession(risk, username, ticket.getsAccountId())
            }
        }
    }
}
