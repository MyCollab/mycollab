/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mycollab.module.project.service;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.user.domain.BillingAccount;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectTicketService extends ISearchableService<ProjectTicketSearchCriteria> {
    List<BillingAccount> getAccountsHasOverdueAssignments(ProjectTicketSearchCriteria searchCriteria);

    List<Integer> getProjectsHasOverdueAssignments(ProjectTicketSearchCriteria searchCriteria);

    void updateAssignmentValue(ProjectTicket assignment, String username);

    void closeSubAssignmentOfMilestone(Integer milestoneId);

    ProjectTicket findTicket(String type, Integer typeId);

    @Cacheable
    List findTicketsByCriteria(@CacheKey BasicSearchRequest<ProjectTicketSearchCriteria> searchRequest);

    @Cacheable
    Integer getTotalTicketsCount(@CacheKey ProjectTicketSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getAssigneeSummary(@CacheKey ProjectTicketSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getPrioritySummary(@CacheKey ProjectTicketSearchCriteria criteria);

    void updateTicket(ProjectTicket ticket, String username);

    void updateMilestoneId(ProjectTicket ticket);

    void removeTicket(ProjectTicket ticket, String username);

    boolean isTicketIdSatisfyCriteria(String type, Integer typeId, ProjectTicketSearchCriteria criteria);
}
