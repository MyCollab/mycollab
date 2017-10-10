package com.mycollab.module.project.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.DateSearchField
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.text.ParseException
import java.text.SimpleDateFormat

@RunWith(SpringJUnit4ClassRunner::class)
class GenericTaskServiceTest : IntegrationServiceTest() {
    @Autowired
    private lateinit var genericTaskService: ProjectTicketService

    @DataSet
    @Test
    fun testGenericTaskListFindPageable() {
        val criteria = ProjectTicketSearchCriteria()
        criteria.projectIds = SetSearchField(1)
        criteria.saccountid = NumberSearchField(1)
        val tasks = genericTaskService.findPageableListByCriteria(BasicSearchRequest<ProjectTicketSearchCriteria>(criteria)) as List<ProjectTicket>
        assertThat(tasks.size).isEqualTo(2)
        assertThat<ProjectTicket>(tasks).extracting("type", "name").contains(tuple("Project-Risk", "b"), tuple("Project-Bug", "name 1"))
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testCountTaskOverDue() {
        val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val d = df.parse("2014-01-23 10:49:49")
        val criteria = ProjectTicketSearchCriteria()
        criteria.dueDate = DateSearchField(d)
        criteria.projectIds = SetSearchField(1)
        criteria.saccountid = NumberSearchField(1)
        val tasks = genericTaskService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<ProjectTicket>
        assertThat(tasks.size).isEqualTo(1)
        assertThat<ProjectTicket>(tasks).extracting("type", "name").contains(tuple("Project-Risk", "b"))
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testListTaskOverDue() {
        val df = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val d = df.parse("2014-01-23 10:49:49")

        val criteria = ProjectTicketSearchCriteria()
        criteria.dueDate = DateSearchField(d)
        criteria.projectIds = SetSearchField(1)
        criteria.saccountid = NumberSearchField(1)
        val taskList = genericTaskService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<ProjectTicket>

        assertThat(taskList.size).isEqualTo(1)
        assertThat<ProjectTicket>(taskList).extracting("type", "name").contains(tuple("Project-Risk", "b"))
    }
}
