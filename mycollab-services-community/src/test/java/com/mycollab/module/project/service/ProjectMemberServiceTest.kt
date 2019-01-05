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
package com.mycollab.module.project.service

import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class, DbUnitInitializerRule::class)
class ProjectMemberServiceTest : IntegrationServiceTest() {
    @Autowired
    private lateinit var projectMemberService: ProjectMemberService

    @DataSet
    @Test
    fun testGetActiveMembersInProject() {
        val activeUsers = projectMemberService.getActiveUsersInProject(1, 1)
        assertThat(activeUsers.size).isEqualTo(1)
        assertThat(activeUsers).extracting("username").contains("user1")
    }

    @DataSet
    @Test
    fun testGetMembersNotInProject() {
        val users = projectMemberService.getUsersNotInProject(1, 1)

        assertThat(users.size).isEqualTo(2)
        assertThat(users).extracting("username").contains("user2", "user3")
    }

    @DataSet
    @Test
    fun testGetProjectMembersInProjects() {
        val users = projectMemberService.getActiveUsersInProjects(Arrays.asList(1, 2), 1)

        assertThat(users.size).isEqualTo(3)
        assertThat(users).extracting("username").contains("user1", "user2", "user3")
    }

    @DataSet
    @Test
    fun testGetUsersNotInProjects() {
        val users = projectMemberService.getUsersNotInProject(1, 1)
        assertThat(users.size).isEqualTo(2)
        assertThat(users).extracting("username").contains("user2", "user3")
    }

    @DataSet
    @Test
    fun testFindMemberByUsername() {
        val member = projectMemberService.findMemberByUsername("user1", 1, 1)
        assertThat(member).extracting("projectid", "status", "username").contains(1, "Active", "user1")
    }
}
