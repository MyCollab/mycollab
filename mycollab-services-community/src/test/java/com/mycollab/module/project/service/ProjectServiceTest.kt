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

import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.project.domain.Project
import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, DbUnitInitializerRule::class)
class ProjectServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var projectService: ProjectService

    @Autowired
    private lateinit var projectActivityStreamService: ProjectActivityStreamService

    @DataSet
    @Test
    fun testSaveProject() {
        val project = Project()
        project.saccountid = 1
        project.name = "Example"
        project.status = "Open"
        project.shortname = "abc"
        val projectId = projectService.saveWithSession(project, "admin")
        assertThat(projectId).isGreaterThan(0)
    }

    @DataSet
    @Test
    fun testGetListProjects() {
        val criteria = ProjectSearchCriteria()
        criteria.saccountid = NumberSearchField(1)
        val projects = projectService.findPageableListByCriteria(BasicSearchRequest<ProjectSearchCriteria>(criteria)) as List<SimpleProject>
        Assertions.assertEquals(projects.size.toLong(), 4)
        assertThat<SimpleProject>(projects).extracting("id", "name").contains(tuple(1, "A"),
                tuple(2, "B"), tuple(3, "C"), tuple(4, "D"))
    }

    @DataSet
    @Test
    fun testGetListProjectsByCriteria() {
        val criteria = ProjectSearchCriteria()
        criteria.saccountid = NumberSearchField(1)

        val projects = projectService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleProject>
        assertThat(projects.size).isEqualTo(4)
        assertThat<SimpleProject>(projects).extracting("id", "name").contains(tuple(1, "A"),
                tuple(2, "B"), tuple(3, "C"), tuple(4, "D"))
    }

    @DataSet
    @Test
    fun testGetListProjectsByUsername() {
        val criteria = ProjectSearchCriteria()
        criteria.involvedMember = StringSearchField.and("admin")
        criteria.saccountid = NumberSearchField(1)

        val projects = projectService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleProject>

        assertThat(projects.size).isEqualTo(2)
        assertThat<SimpleProject>(projects).extracting("id", "name").contains(tuple(1, "A"), tuple(2, "B"))
    }

    @DataSet
    @Test
    fun testGetProjectsUserInvolved() {
        val projects = projectService.getProjectsUserInvolved("admin", 1)
        assertThat(projects.size).isEqualTo(2)
        assertThat(projects).extracting("id", "name").contains(tuple(1, "A"), tuple(2, "B"))
    }

    @DataSet
    @Test
    fun testGetProjectKeysUserInvolved() {
        val keys = projectService.getProjectKeysUserInvolved("admin", 1)
        assertThat(keys.size).isEqualTo(2)
        assertThat(keys).contains(1, 2)

        val allPrjKeys = projectService.getProjectKeysUserInvolved(null, 1)
        assertThat(allPrjKeys.size).isEqualTo(4)
        assertThat(allPrjKeys).contains(1, 2, 3, 4)
    }

    @DataSet
    @Test
    fun testGetActivityStreams() {
        val criteria = ActivityStreamSearchCriteria()
        criteria.moduleSet = SetSearchField(ModuleNameConstants.PRJ)
        criteria.extraTypeIds = SetSearchField(4)
        criteria.saccountid = NumberSearchField(1)
        val streams = projectActivityStreamService.getProjectActivityStreams(BasicSearchRequest(criteria))

        assertThat(streams.size).isEqualTo(3)
        assertThat(streams).extracting("type", "typeid", "itemKey").contains(
                tuple("Project-Bug", "1", 20), tuple("Project-Task", "1", 10),
                tuple("Project-Risk", "1", null))
    }

    @DataSet
    @Test
    fun testFindProjectWithCustomer() {
        val project = projectService.findById(3, 1)
        assertThat(project).extracting("clientName", "clientid", "leadFullName", "shortname").contains("a", 1, "Nguyen Hai", "bbb")
    }
}
