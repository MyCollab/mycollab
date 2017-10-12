/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
