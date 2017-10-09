package com.mycollab.module.project.service;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.user.domain.BillingAccount;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectService extends IDefaultService<Integer, Project, ProjectSearchCriteria> {

    @Cacheable
    List<Integer> getProjectKeysUserInvolved(String username, @CacheKey Integer sAccountId);

    @Cacheable
    List<SimpleProject> getProjectsUserInvolved(String username, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleProject findById(Integer projectId, @CacheKey Integer sAccountId);

    Integer getTotalActiveProjectsOfInvolvedUsers(String username, @CacheKey Integer sAccountId);

    @Cacheable
    Integer getTotalActiveProjectsInAccount(@CacheKey Integer sAccountId);

    BillingAccount getAccountInfoOfProject(Integer projectId);

    List<ProjectRelayEmailNotification> findProjectRelayEmailNotifications();

    Integer savePlainProject(Project record, String username);
}
