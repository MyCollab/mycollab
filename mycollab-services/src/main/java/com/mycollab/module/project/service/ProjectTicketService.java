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
