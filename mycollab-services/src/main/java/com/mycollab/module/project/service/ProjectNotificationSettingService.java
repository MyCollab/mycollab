package com.mycollab.module.project.service;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.ICrudService;
import com.mycollab.module.project.domain.ProjectNotificationSetting;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 4.0.0
 */
public interface ProjectNotificationSettingService extends ICrudService<Integer, ProjectNotificationSetting> {

    @Cacheable
    ProjectNotificationSetting findNotification(String username, Integer projectId, @CacheKey Integer sAccountId);

    @Cacheable
    List<ProjectNotificationSetting> findNotifications(Integer projectId, @CacheKey Integer sAccountId);
}
