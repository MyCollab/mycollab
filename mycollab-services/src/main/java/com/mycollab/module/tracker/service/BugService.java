package com.mycollab.module.tracker.service;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.tracker.domain.BugStatusGroupItem;
import com.mycollab.module.tracker.domain.BugWithBLOBs;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface BugService extends IDefaultService<Integer, BugWithBLOBs, BugSearchCriteria> {

    @Cacheable
    SimpleBug findById(Integer bugId, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleBug findByProjectAndBugKey(Integer bugKey, String projectShortName, @CacheKey Integer sAccountId);

    @Cacheable
    List<GroupItem> getStatusSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getPrioritySummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getAssignedDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getResolutionDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<BugStatusGroupItem> getBugStatusGroupItemBaseComponent(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getReporterDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getVersionDefectsSummary(@CacheKey BugSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getComponentDefectsSummary(@CacheKey BugSearchCriteria searchCriteria);

    @CacheEvict
    void massUpdateBugIndexes(List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId);
}
