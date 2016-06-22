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

package com.esofthead.mycollab.module.project.service;

import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IDefaultService;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.user.domain.SimpleUser;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectMemberService extends IDefaultService<Integer, ProjectMember, ProjectMemberSearchCriteria> {

    @Cacheable
    SimpleProjectMember findById(Integer memberId, @CacheKey Integer sAccountId);

    @Cacheable
    boolean isUserBelongToProject(String username, Integer projectId, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleProjectMember findMemberByUsername(String username, Integer projectId, @CacheKey Integer sAccountId);

    @Cacheable
    SimpleUser getActiveUserOfProject(String username, Integer projectId, @CacheKey Integer sAccountId);

    @Cacheable
    List<SimpleUser> getUsersNotInProject(Integer projectId, @CacheKey Integer sAccountId);

    @Cacheable
    List<SimpleUser> getActiveUsersInProject(Integer projectId, @CacheKey Integer sAccountId);

    @Cacheable
    List<SimpleUser> getActiveUsersInProjects(List<Integer> projectIds, @CacheKey Integer sAccountId);

    void inviteProjectMembers(String[] email, Integer projectId, Integer projectRoleId,
                              String inviteUser, String inviteMessage, Integer sAccountId);

    List<SimpleProjectMember> findMembersHourlyInProject(Integer projectId, Integer sAccountId, Date start, Date end);
}
