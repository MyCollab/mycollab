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

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
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
class MilestoneServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var milestoneService: MilestoneService

    private val criteria: MilestoneSearchCriteria
        get() {
            val criteria = MilestoneSearchCriteria()
            criteria.saccountid = NumberSearchField(1)
            criteria.projectIds = SetSearchField(1)
            return criteria
        }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListMilestones() {
        val criteria = MilestoneSearchCriteria()
        criteria.saccountid = NumberSearchField(1)
        criteria.projectIds = SetSearchField(1)
        criteria.statuses = SetSearchField("Open")
        criteria.milestoneName = StringSearchField.and("milestone 1")

        val milestones = milestoneService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleMilestone>

        assertThat(milestones.size).isEqualTo(1)
        assertThat<SimpleMilestone>(milestones).extracting("id", "description", "createdUserFullName", "createdtime", "ownerFullName",
                "numTasks", "numOpenTasks", "numBugs", "numOpenBugs").contains(
                tuple(1, "milestone no1", "Hai Nguyen", dateFormat.parse("2014-10-01 00:00:00"), "Hai Nguyen", 1, 0,
                        2, 2))
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testGetListMilestonesByCriteria() {
        val milestones = milestoneService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleMilestone>

        assertThat(milestones.size).isEqualTo(4)
        assertThat<SimpleMilestone>(milestones).extracting("id", "description",
                "createdUserFullName", "createdtime", "ownerFullName",
                "numTasks", "numOpenTasks", "numBugs", "numOpenBugs").contains(
                tuple(4, "milestone no4", "Nghiem Le", dateFormat.parse("2014-10-04 00:00:00"), "Nghiem Le", 0, 0, 3, 3),
                tuple(3, "milestone no3", "Nghiem Le", dateFormat.parse("2014-10-03 00:00:00"), "Nghiem Le", 0, 0, 1, 1),
                tuple(2, "milestone no2", "Hai Nguyen", dateFormat.parse("2014-10-02 00:00:00"), "Hai Nguyen", 2, 0, 0, 0),
                tuple(1, "milestone no1", "Hai Nguyen", dateFormat.parse("2014-10-01 00:00:00"), "Hai Nguyen", 1, 0, 2, 2))
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testFindMilestoneById() {
        val milestone = milestoneService.findById(1, 1)
        assertThat(milestone!!.createdUserFullName).isEqualTo("Hai Nguyen")
        assertThat(milestone.numOpenBugs).isEqualTo(2)
    }

    @DataSet
    @Test
    fun testGetTotalCount() {
        val milestoneSize = milestoneService.getTotalCount(criteria)
        assertThat(milestoneSize).isEqualTo(4)
    }

    companion object {

        private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    }
}
