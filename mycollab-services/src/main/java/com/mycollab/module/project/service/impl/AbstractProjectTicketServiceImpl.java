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
package com.mycollab.module.project.service.impl;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.utils.BeanUtility;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.db.persistence.service.DefaultSearchService;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.dao.ProjectTicketMapper;
import com.mycollab.module.project.dao.TaskMapper;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.Risk;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectTaskService;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.service.RiskService;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.module.user.domain.BillingAccount;
import com.mycollab.spring.AppContextUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class AbstractProjectTicketServiceImpl extends DefaultSearchService<ProjectTicketSearchCriteria>
        implements ProjectTicketService {

    @Autowired
    private ProjectTicketMapper projectTicketMapper;

    @Override
    public ISearchableDAO<ProjectTicketSearchCriteria> getSearchMapper() {
        return projectTicketMapper;
    }

    @Override
    public Integer getTotalCount(ProjectTicketSearchCriteria criteria) {
        return projectTicketMapper.getTotalCountFromRisk(criteria)
                + projectTicketMapper.getTotalCountFromBug(criteria)
                + projectTicketMapper.getTotalCountFromTask(criteria)
                + projectTicketMapper.getTotalCountFromMilestone(criteria);
    }

    @Override
    public Integer getTotalTicketsCount(@CacheKey ProjectTicketSearchCriteria criteria) {
        return projectTicketMapper.getTotalCountFromRisk(criteria)
                + projectTicketMapper.getTotalCountFromBug(criteria)
                + projectTicketMapper.getTotalCountFromTask(criteria);
    }

    @Override
    public boolean isTicketIdSatisfyCriteria(String type, Integer typeId, ProjectTicketSearchCriteria criteria) {
        ProjectTicketSearchCriteria newCriteria = BeanUtility.deepClone(criteria);
        newCriteria.setTypeIds(new SetSearchField<>(typeId));
        newCriteria.setTypes(new SetSearchField<>(type));
        if (ProjectTypeConstants.TASK.equals(type)) {
            return (projectTicketMapper.getTotalCountFromTask(newCriteria) > 0);
        } else if (ProjectTypeConstants.BUG.equals(type)) {
            return (projectTicketMapper.getTotalCountFromBug(newCriteria) > 0);
        } else if (ProjectTypeConstants.RISK.equals(type)) {
            return (projectTicketMapper.getTotalCountFromRisk(newCriteria) > 0);
        }
        return false;
    }

    @Override
    public List<BillingAccount> getAccountsHasOverdueAssignments(ProjectTicketSearchCriteria searchCriteria) {
        return projectTicketMapper.getAccountsHasOverdueAssignments(searchCriteria);
    }

    @Override
    public List<Integer> getProjectsHasOverdueAssignments(ProjectTicketSearchCriteria searchCriteria) {
        return projectTicketMapper.getProjectsHasOverdueAssignments(searchCriteria);
    }

    @Override
    public ProjectTicket findTicket(String type, Integer typeId) {
        ProjectTicketSearchCriteria searchCriteria = new ProjectTicketSearchCriteria();
        searchCriteria.setTypes(new SetSearchField<>(type));
        searchCriteria.setTypeIds(new SetSearchField<>(typeId));
        List<ProjectTicket> assignments = findAbsoluteListByCriteria(searchCriteria, 0, 1);
        return (assignments.size() > 0) ? assignments.get(0) : null;
    }

    @Override
    public List<GroupItem> getAssigneeSummary(@CacheKey ProjectTicketSearchCriteria criteria) {
        return projectTicketMapper.getAssigneeSummary(criteria);
    }

    @Override
    public List<GroupItem> getPrioritySummary(@CacheKey ProjectTicketSearchCriteria criteria) {
        return projectTicketMapper.getPrioritySummary(criteria);
    }

    @Override
    public List findTicketsByCriteria(@CacheKey BasicSearchRequest<ProjectTicketSearchCriteria> searchRequest) {
        return projectTicketMapper.findTicketsByCriteria(searchRequest.getSearchCriteria(),
                new RowBounds((searchRequest.getCurrentPage() - 1) * searchRequest.getNumberOfItems(),
                        searchRequest.getNumberOfItems()));
    }

    @Override
    public void updateTicket(ProjectTicket ticket, String username) {
        if (ticket.isTask()) {
            Task task = ProjectTicket.buildTask(ticket);
            AppContextUtil.getSpringBean(ProjectTaskService.class).updateSelectiveWithSession(task, username);
        } else if (ticket.isBug()) {
            BugWithBLOBs bug = ProjectTicket.buildBug(ticket);
            AppContextUtil.getSpringBean(BugService.class).updateSelectiveWithSession(bug, username);
        } else if (ticket.isRisk()) {
            Risk risk = ProjectTicket.buildRisk(ticket);
            AppContextUtil.getSpringBean(RiskService.class).updateSelectiveWithSession(risk, username);
        }
    }

    @Override
    public void updateMilestoneId(ProjectTicket ticket) {

    }

    @Override
    public void removeTicket(ProjectTicket ticket, String username) {
        if (ticket.isTask()) {
            Task task = ProjectTicket.buildTask(ticket);
            AppContextUtil.getSpringBean(ProjectTaskService.class).removeWithSession(task, username, ticket.getsAccountId());
        } else if (ticket.isBug()) {
            BugWithBLOBs bug = ProjectTicket.buildBug(ticket);
            AppContextUtil.getSpringBean(BugService.class).removeWithSession(bug, username, ticket.getsAccountId());
        } else if (ticket.isRisk()) {
            Risk risk = ProjectTicket.buildRisk(ticket);
            AppContextUtil.getSpringBean(RiskService.class).removeWithSession(risk, username, ticket.getsAccountId());
        }
    }
}
