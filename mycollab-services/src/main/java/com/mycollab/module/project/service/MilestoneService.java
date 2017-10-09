package com.mycollab.module.project.service;

import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.project.domain.Milestone;
import com.mycollab.module.project.domain.SimpleMilestone;
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0.0
 */
public interface MilestoneService extends IDefaultService<Integer, Milestone, MilestoneSearchCriteria> {

    @Cacheable
    SimpleMilestone findById(Integer milestoneId, @CacheKey Integer sAccountId);

    @CacheEvict
    void massUpdateOptionIndexes(List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId);
}
