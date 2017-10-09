package com.mycollab.module.project.service;

import com.mycollab.common.domain.GroupItem;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.core.cache.CacheEvict;
import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.IMassUpdateDAO;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.domain.criteria.TaskSearchCriteria;

import java.util.List;
import java.util.Map;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectTaskService extends IDefaultService<Integer, Task, TaskSearchCriteria> {

    @Cacheable
    SimpleTask findById(Integer taskId, @CacheKey Integer sAccountId);

    @Cacheable
    List<SimpleTask> findSubTasks(Integer parentTaskId, @CacheKey Integer sAccountId, SearchCriteria.OrderField orderField);

    @Cacheable
    SimpleTask findByProjectAndTaskKey(Integer taskKey, String projectShortName, @CacheKey Integer sAccountId);

    @Cacheable
    List<GroupItem> getPrioritySummary(@CacheKey TaskSearchCriteria criteria);

    Integer getCountOfOpenSubTasks(Integer taskId);

    @CacheEvict
    void massUpdateTaskStatuses(Integer parentTaskId, String status, @CacheKey Integer sAccountId);

    @Cacheable
    List<GroupItem> getStatusSummary(@CacheKey TaskSearchCriteria criteria);

    @Cacheable
    List<GroupItem> getAssignedTasksSummary(@CacheKey TaskSearchCriteria criteria);

    @CacheEvict
    void massUpdateTaskIndexes(List<Map<String, Integer>> mapIndexes, @CacheKey Integer sAccountId);

    void massUpdateStatuses(String oldStatus, String newStatus, Integer projectId, @CacheKey Integer sAccountId);
}
