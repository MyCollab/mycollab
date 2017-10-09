package com.mycollab.module.project.dao;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.user.domain.BillingAccount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectTicketMapper extends ISearchableDAO<ProjectTicketSearchCriteria> {

    Integer getTotalCountFromRisk(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    Integer getTotalCountFromBug(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    Integer getTotalCountFromTask(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    Integer getTotalCountFromMilestone(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    List<BillingAccount> getAccountsHasOverdueAssignments(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    List<Integer> getProjectsHasOverdueAssignments(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    List<GroupItem> getAssigneeSummary(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    List<GroupItem> getPrioritySummary(@Param("searchCriteria") ProjectTicketSearchCriteria criteria);

    List<ProjectTicket> findTicketsByCriteria(@Param("searchCriteria") ProjectTicketSearchCriteria criteria,
                                              RowBounds rowBounds);
}
