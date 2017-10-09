package com.mycollab.module.tracker.service;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;

/**
 * @author MyCollab Ltd
 * @since 1.0.0
 */
public interface ComponentService extends IDefaultService<Integer, Component, ComponentSearchCriteria> {

    @Cacheable
    SimpleComponent findById(Integer componentId, @CacheKey Integer sAccountId);
}
