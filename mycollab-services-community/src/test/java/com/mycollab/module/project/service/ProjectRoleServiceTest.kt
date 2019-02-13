package com.mycollab.module.project.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@ExtendWith(DbUnitInitializerRule::class)
class ProjectRoleServiceTest(@Autowired val projectRoleService: ProjectRoleService) : IntegrationServiceTest() {

    @Test
    @DataSet
    fun testFindByCriteria() {
        val criteria = ProjectRoleSearchCriteria()
        criteria.projectId = NumberSearchField.equal(1)
        criteria.saccountid = NumberSearchField.equal(1)
        criteria.roleName = StringSearchField.and("role1")
        val roles = projectRoleService.findPageableListByCriteria(BasicSearchRequest(criteria))
        assertThat(roles.size).isEqualTo(1)

        assertThat(projectRoleService.getTotalCount(criteria)).isEqualTo(1)
    }

    @Test
    @DataSet
    fun testFindRole() {
        val role = projectRoleService.findById(1, 1)
        assertThat(role).extracting("rolename").contains("role1")
    }

    @Test
    @DataSet
    fun testFindProjectPermissions() {
        val permissions = projectRoleService.findProjectsPermissions("haiphucnguyen@gmail.com", Arrays.asList(1), 1)
        assertThat(permissions.size).isEqualTo(1)
        assertThat(permissions[0].item1).isEqualTo(1)
    }
}