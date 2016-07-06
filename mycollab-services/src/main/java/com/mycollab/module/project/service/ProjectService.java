/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.service;

import com.mycollab.core.cache.CacheKey;
import com.mycollab.core.cache.Cacheable;
import com.mycollab.db.persistence.service.IDefaultService;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.ProjectRelayEmailNotification;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;

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

    String getSubdomainOfProject(Integer projectId);

    List<ProjectRelayEmailNotification> findProjectRelayEmailNotifications();

    Integer savePlainProject(Project record, String username);
}
