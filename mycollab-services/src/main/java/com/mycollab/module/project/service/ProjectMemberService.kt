/**
 * mycollab-services - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.service

import com.mycollab.core.cache.CacheKey
import com.mycollab.core.cache.Cacheable
import com.mycollab.db.persistence.service.IDefaultService
import com.mycollab.module.project.domain.ProjectMember
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria
import com.mycollab.module.user.domain.SimpleUser

import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectMemberService : IDefaultService<Int, ProjectMember, ProjectMemberSearchCriteria> {

    @Cacheable
    fun findById(memberId: Int, @CacheKey sAccountId: Int): SimpleProjectMember?

    @Cacheable
    fun isUserBelongToProject(username: String, projectId: Int, @CacheKey sAccountId: Int): Boolean

    @Cacheable
    fun findMemberByUsername(username: String, projectId: Int, @CacheKey sAccountId: Int): SimpleProjectMember?

    @Cacheable
    fun getActiveUserOfProject(username: String, projectId: Int, @CacheKey sAccountId: Int): SimpleUser?

    @Cacheable
    fun getUsersNotInProject(projectId: Int?, @CacheKey sAccountId: Int?): List<SimpleUser>

    @Cacheable
    fun getActiveUsersInProject(projectId: Int?, @CacheKey sAccountId: Int?): List<SimpleUser>

    @Cacheable
    fun getActiveUsersInProjects(projectIds: List<Int>, @CacheKey sAccountId: Int?): List<SimpleUser>

    fun inviteProjectMembers(email: Array<String>, projectId: Int, projectRoleId: Int?,
                             inviteUser: String, inviteMessage: String, sAccountId: Int)

    fun findMembersHourlyInProject(projectId: Int?, sAccountId: Int?, start: Date, end: Date): List<SimpleProjectMember>
}
