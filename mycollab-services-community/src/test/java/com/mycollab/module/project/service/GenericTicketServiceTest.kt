package com.mycollab.module.project.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.domain.criteria.ProjectGenericItemSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired

@ExtendWith(DbUnitInitializerRule::class)
class GenericTicketServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var projectGenericItemService: ProjectGenericItemService

    @DataSet
    @Test
    fun testFindItems() {
        val criteria = ProjectGenericItemSearchCriteria()
        criteria.prjKeys = SetSearchField(1)
        criteria.types = SetSearchField(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK, ProjectTypeConstants.RISK,
                ProjectTypeConstants.MESSAGE)
        val items = projectGenericItemService.findPageableListByCriteria(BasicSearchRequest(criteria))
        assertThat(items.size).isEqualTo(6)
        assertThat(items).extracting("name", "projectName").contains(tuple("Risk 1", "a"))
    }

    @DataSet
    @Test
    fun testGetCount() {
        val criteria = ProjectGenericItemSearchCriteria()
        criteria.prjKeys = SetSearchField(1)
        criteria.types = SetSearchField(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK, ProjectTypeConstants.RISK,
                ProjectTypeConstants.MESSAGE)
        val totalCount = projectGenericItemService.getTotalCount(criteria)
        assertThat(totalCount).isEqualTo(6)
    }
}