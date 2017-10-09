package com.mycollab.module.project.service;


import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IService;
import com.mycollab.module.project.domain.AssignWithPredecessors;
import com.mycollab.module.project.domain.TaskPredecessor;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.0.0
 */
public interface GanttAssignmentService extends IService {
    @Cacheable
    List<AssignWithPredecessors> getTaskWithPredecessors(List<Integer> projectIds, @CacheKey Integer sAccountId);

    @CacheEvict
    void massUpdateGanttItems(List<AssignWithPredecessors> ganttItems, @CacheKey Integer sAccountId);

    @CacheEvict void massDeleteGanttItems(List<AssignWithPredecessors> ganttItems, @CacheKey Integer sAccountId);

    @CacheEvict
    void massUpdatePredecessors(Integer taskSourceId, List<TaskPredecessor> predecessors, @CacheKey Integer sAccountId);

    @CacheEvict
    void massDeletePredecessors(List<TaskPredecessor> predecessors, @CacheKey Integer sAccountId);
}
