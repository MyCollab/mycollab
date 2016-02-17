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

package com.esofthead.mycollab.module.project.dao;

import com.esofthead.mycollab.core.persistence.ISearchableDAO;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectMemberMapperExt extends ISearchableDAO<ProjectMemberSearchCriteria> {
    SimpleProjectMember findMemberById(int memberId);

    List<SimpleUser> getUsersNotInProject(@Param("projectId") int projectId, @Param("sAccountId") int sAccountId);

    List<SimpleUser> getActiveUsersInProject(@Param("projectId") int projectId, @Param("sAccountId") int sAccountId);

    SimpleProjectMember findMemberByUsername(@Param("username") String username, @Param("projectId") int projectId);

    List<SimpleUser> getActiveUsersInProjects(@Param("projectIds") List<Integer> projectIds, @Param("sAccountId") Integer sAccountId);

    SimpleUser getActiveUserOfProject(@Param("username") String username, @Param("projectId") Integer projectId);
}
