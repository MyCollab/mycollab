package com.mycollab.module.project.dao

import com.mycollab.db.persistence.ISearchableDAO
import com.mycollab.module.project.domain.SimpleProjectMember
import com.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria
import com.mycollab.module.user.domain.SimpleUser
import org.apache.ibatis.annotations.Param

import java.util.Date

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface ProjectMemberMapperExt : ISearchableDAO<ProjectMemberSearchCriteria> {
    fun findMemberById(memberId: Int): SimpleProjectMember

    fun getUsersNotInProject(@Param("projectId") projectId: Int, @Param("sAccountId") sAccountId: Int): List<SimpleUser>

    fun getActiveUsersInProject(@Param("projectId") projectId: Int, @Param("sAccountId") sAccountId: Int): List<SimpleUser>

    fun findMemberByUsername(@Param("username") username: String, @Param("projectId") projectId: Int): SimpleProjectMember

    fun getActiveUsersInProjects(@Param("projectIds") projectIds: List<Int>, @Param("sAccountId") sAccountId: Int?): List<SimpleUser>

    fun getActiveUserOfProject(@Param("username") username: String, @Param("projectId") projectId: Int?,
                               @Param("sAccountId") sAccountId: Int?): SimpleUser

    fun findMembersHourlyInProject(@Param("projectId") projectId: Int?, @Param("sAccountId") sAccountId: Int?,
                                   @Param("start") start: Date, @Param("end") end: Date): List<SimpleProjectMember>
}
