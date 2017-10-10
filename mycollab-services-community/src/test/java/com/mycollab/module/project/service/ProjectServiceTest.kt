package com.mycollab.module.project.service

import com.mycollab.common.ModuleNameConstants
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.project.domain.Project
import com.mycollab.module.project.domain.ProjectActivityStream
import com.mycollab.module.project.domain.SimpleProject
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple

@RunWith(SpringJUnit4ClassRunner::class)
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
        project.projectstatus = "Open"
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
        Assert.assertEquals(projects.size.toLong(), 4)
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
        Assert.assertEquals(2, projects.size.toLong())
        assertThat(projects.size).isEqualTo(2)
        assertThat(projects).extracting("id", "name").contains(tuple(1, "A"), tuple(2, "B"))
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
}
