package com.mycollab.module.crm.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple

import com.mycollab.module.crm.domain.SimpleCrmTask
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.module.crm.domain.criteria.CrmTaskSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest

@RunWith(SpringJUnit4ClassRunner::class)
class TaskServiceTest : IntegrationServiceTest() {

    @Autowired
    private val taskService: TaskService? = null

    @DataSet
    @Test
    fun testSearchByCriteria() {
        val tasks = taskService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleCrmTask>

        assertThat(tasks.size).isEqualTo(1)
        assertThat<SimpleCrmTask>(tasks).extracting("id", "status", "subject").contains(tuple(1, "Completed", "aaa"))
    }

    @DataSet
    @Test
    fun testGetTotalCounts() {
        val tasks = taskService!!.findPageableListByCriteria(BasicSearchRequest(criteria))
        assertThat(tasks.size).isEqualTo(1)
    }

    private val criteria: CrmTaskSearchCriteria
        get() {
            val criteria = CrmTaskSearchCriteria()
            criteria.saccountid = NumberSearchField(1)
            return criteria
        }
}
