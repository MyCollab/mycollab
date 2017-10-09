package com.mycollab.module.tracker.dao;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.persistence.ISearchableDAO;
import com.mycollab.module.tracker.domain.BugStatusGroupItem;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface BugMapperExt extends ISearchableDAO<BugSearchCriteria> {

    SimpleBug getBugById(int bugid);

    SimpleBug findByProjectAndBugKey(@Param("bugkey") int bugKey, @Param("prjShortName") String projectShortName,
                                     @Param("sAccountId") int sAccountId);

    List<GroupItem> getStatusSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getPrioritySummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getAssignedDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getResolutionDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getReporterDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getVersionDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<GroupItem> getComponentDefectsSummary(@Param("searchCriteria") BugSearchCriteria criteria);

    List<BugStatusGroupItem> getBugStatusGroupItemBaseComponent(@Param("searchCriteria") BugSearchCriteria criteria);

    Integer getMaxKey(int projectId);
}
