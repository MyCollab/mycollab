package com.mycollab.module.project.service;

import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.project.domain.ItemTimeLogging;
import com.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ItemTimeLoggingService extends IDefaultService<Integer, ItemTimeLogging, ItemTimeLoggingSearchCriteria> {

    @Cacheable
    Double getTotalHoursByCriteria(@CacheKey ItemTimeLoggingSearchCriteria criteria);

    @CacheEvict
    void batchSaveTimeLogging(List<ItemTimeLogging> timeLoggings, @CacheKey Integer sAccountId);

    @Cacheable
    Double getTotalBillableHoursByMilestone(Integer milestoneId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getTotalNonBillableHoursByMilestone(Integer milestoneId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getRemainHoursByMilestone(Integer milestoneId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getTotalBillableHoursByComponent(Integer componentId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getTotalNonBillableHoursByComponent(Integer componentId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getRemainHoursByComponent(Integer componentId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getTotalBillableHoursByVersion(Integer versionId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getTotalNonBillableHoursByVersion(Integer versionId, @CacheKey Integer sAccountId);

    @Cacheable
    Double getRemainHoursByVersion(Integer versionId, @CacheKey Integer sAccountId);
}
