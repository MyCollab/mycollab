package com.mycollab.module.tracker.service;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface VersionService extends IDefaultService<Integer, Version, VersionSearchCriteria> {
    @Cacheable
    SimpleVersion findById(Integer versionId, @CacheKey Integer sAccountId);
}
