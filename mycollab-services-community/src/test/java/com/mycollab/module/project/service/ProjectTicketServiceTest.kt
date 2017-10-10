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
import com.mycollab.db.arguments.RangeDateSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.module.project.domain.ProjectTicket
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.joda.time.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class ProjectTicketServiceTest : IntegrationServiceTest() {
    @Autowired
    private lateinit var projectTicketService: ProjectTicketService

    @DataSet
    @Test
    fun testGetAccountsHasOverdueAssignments() {
        val criteria = ProjectTicketSearchCriteria()
        criteria.saccountid = null
        val now = LocalDate()
        val rangeDateSearchField = RangeDateSearchField(now.minusDays(10000).toDate(), now.toDate())
        criteria.dateInRange = rangeDateSearchField
        val accounts = projectTicketService.getAccountsHasOverdueAssignments(criteria)
        assertThat(accounts).isNotEmpty().hasSize(2)
        assertThat(accounts).extracting("subdomain", "id").contains(tuple("a", 1), tuple("b", 2))
    }

    @DataSet
    @Test
    fun testGetAssignments() {
        val criteria = ProjectTicketSearchCriteria()
        criteria.saccountid = NumberSearchField.equal(2)
        criteria.projectIds = SetSearchField(3)
        val tickets = projectTicketService.findTicketsByCriteria(BasicSearchRequest(criteria)) as List<ProjectTicket>
        assertThat<ProjectTicket>(tickets).hasSize(3)
        assertThat<ProjectTicket>(tickets).extracting("name", "assignUser", "assignUserFullName", "assignUserAvatarId", "type")
                .contains(tuple("Task 4", "linhduong", "Duong Linh", "linh123", "Project-Task"),
                        tuple("Bug 1", "hai79", "Nguyen Hai", null, "Project-Bug"),
                        tuple("Risk 1", "hai79", "Nguyen Hai", null, "Project-Risk"))
    }

    @DataSet
    @Test
    fun testGetAssigneeSummary() {
        val criteria = ProjectTicketSearchCriteria()
        criteria.saccountid = NumberSearchField.equal(2)
        criteria.projectIds = SetSearchField(3)
        val groupItems = projectTicketService.getAssigneeSummary(criteria)
        assertThat(groupItems).hasSize(2)
        assertThat(groupItems).extracting("groupid", "value", "extraValue").contains(tuple("hai79", 2.0, null), tuple("linhduong", 1.0, "linh123"))
    }
}
