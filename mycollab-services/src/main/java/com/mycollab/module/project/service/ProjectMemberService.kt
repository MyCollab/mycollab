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
