package com.mycollab.module.project.service;

import com.mycollab.db.arguments.SearchRequest;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.project.domain.SimpleStandupReport;
import com.mycollab.module.project.domain.StandupReportStatistic;
import com.mycollab.module.project.domain.StandupReportWithBLOBs;
import com.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.mycollab.module.user.domain.SimpleUser;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface StandupReportService extends IDefaultService<Integer, StandupReportWithBLOBs, StandupReportSearchCriteria> {
    @Cacheable
    SimpleStandupReport findById(Integer standupId, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleStandupReport findStandupReportByDateUser(Integer projectId, String username, Date onDate, @CacheKey Integer sAccountId);

    @Cacheable
    List<SimpleUser> findUsersNotDoReportYet(Integer projectId, Date onDate, @CacheKey Integer sAccountId);

    List<StandupReportStatistic> getProjectReportsStatistic(List<Integer> projectIds, Date onDate, SearchRequest searchRequest);
}
