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

import java.util.List;

import com.esofthead.mycollab.core.cache.CacheEvict;
import com.esofthead.mycollab.core.cache.CacheKey;
import com.esofthead.mycollab.core.cache.Cacheable;
import com.esofthead.mycollab.core.persistence.service.IDefaultService;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.user.domain.SimpleUser;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface ProjectMemberService extends
		IDefaultService<Integer, ProjectMember, ProjectMemberSearchCriteria> {

	@Cacheable
	SimpleProjectMember findById(int memberId, @CacheKey int sAccountId);

	@Cacheable
	SimpleProjectMember findMemberByUsername(String username, int projectId,
			@CacheKey Integer sAccountId);

	@Cacheable
	List<SimpleUser> getUsersNotInProject(int projectId,
			@CacheKey Integer sAccountId);

	@Cacheable
	List<SimpleUser> getActiveUsersInProject(int projectId,
			@CacheKey Integer sAccountId);

	void inviteProjectMembers(String[] email, int projectId, int projectRoleId,
			String inviteUser, String inviteMessage, int sAccountId);

	@CacheEvict
	void acceptProjectInvitationByNewUser(String email, String password,
			Integer projectId, Integer projectRoleId, Integer sAccountId);
}
