package com.mycollab.module.project.service;

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IService;
import com.mycollab.module.project.domain.ProjectActivityStream;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public interface ProjectActivityStreamService extends IService {
    @Cacheable
    int getTotalActivityStream(@CacheKey ActivityStreamSearchCriteria criteria);

    @Cacheable
    List<ProjectActivityStream> getProjectActivityStreams(@CacheKey BasicSearchRequest<ActivityStreamSearchCriteria> searchRequest);
}
