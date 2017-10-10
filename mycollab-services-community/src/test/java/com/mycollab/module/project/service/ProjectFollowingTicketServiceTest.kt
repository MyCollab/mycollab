/**
 * mycollab-services-community - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.project.domain.FollowingTicket
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria
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
class ProjectFollowingTicketServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var projectFollowingTicketService: ProjectFollowingTicketService

    private val criteria: FollowingTicketSearchCriteria
        get() {
            val criteria = FollowingTicketSearchCriteria()
            criteria.extraTypeIds = SetSearchField(1, 2)
            criteria.saccountid = NumberSearchField(1)
            criteria.user = StringSearchField.and("hainguyen@esofthead.com")
            return criteria
        }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListProjectFollowingTicket() {
        val projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<FollowingTicket>
        assertThat<FollowingTicket>(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")),
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Risk", "risk 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Risk", "risk 4", DATE_FORMAT.parse("2014-09-24 00:00:00")))
        assertThat(projectFollowingTickets.size).isEqualTo(12)
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListProjectFollowingTicketBySummary() {
        val criteria = criteria
        criteria.name = StringSearchField.and("1")
        val projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<FollowingTicket>
        assertThat<FollowingTicket>(projectFollowingTickets).extracting("type", "name", "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")))
        assertThat(projectFollowingTickets.size).isEqualTo(3)
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListProjectFollowingTicketOfTaskAndBug() {
        val criteria = criteria
        criteria.types = SetSearchField("Project-Task", "Project-Bug")
        val projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<FollowingTicket>
        assertThat<FollowingTicket>(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")),
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")))
        assertThat(projectFollowingTickets.size).isEqualTo(8)
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListProjectFollowingTicketOfTask() {
        val criteria = criteria
        criteria.type = StringSearchField.and("Project-Task")
        val projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<FollowingTicket>
        assertThat<FollowingTicket>(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Task", "task 1", DATE_FORMAT.parse("2014-10-21 00:00:00")),
                tuple("Project-Task", "task 2", DATE_FORMAT.parse("2014-10-22 00:00:00")),
                tuple("Project-Task", "task 3", DATE_FORMAT.parse("2014-09-21 00:00:00")),
                tuple("Project-Task", "task 4", DATE_FORMAT.parse("2014-09-22 00:00:00")))
        assertThat(projectFollowingTickets.size).isEqualTo(4)
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListProjectFollowingTicketOfRisk() {
        val criteria = criteria
        criteria.type = StringSearchField.and("Project-Risk")
        val projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<FollowingTicket>

        assertThat<FollowingTicket>(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Risk", "risk 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Risk", "risk 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Risk", "risk 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Risk", "risk 4", DATE_FORMAT.parse("2014-09-24 00:00:00")))
        assertThat(projectFollowingTickets.size).isEqualTo(4)
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListProjectFollowingTicketOfBug() {
        val criteria = criteria
        criteria.type = StringSearchField.and("Project-Bug")
        val projectFollowingTickets = projectFollowingTicketService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<FollowingTicket>

        assertThat<FollowingTicket>(projectFollowingTickets).extracting("type", "name",
                "monitorDate").contains(
                tuple("Project-Bug", "bug 1", DATE_FORMAT.parse("2014-10-23 00:00:00")),
                tuple("Project-Bug", "bug 2", DATE_FORMAT.parse("2014-10-24 00:00:00")),
                tuple("Project-Bug", "bug 3", DATE_FORMAT.parse("2014-09-23 00:00:00")),
                tuple("Project-Bug", "bug 4", DATE_FORMAT.parse("2014-09-24 00:00:00")))
        assertThat(projectFollowingTickets.size).isEqualTo(4)
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetTotalCount() {
        assertThat(projectFollowingTicketService.getTotalCount(criteria)).isEqualTo(12)
    }

    companion object {

        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    }
}
