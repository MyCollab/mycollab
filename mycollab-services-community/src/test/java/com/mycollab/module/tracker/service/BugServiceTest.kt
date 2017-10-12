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
package com.mycollab.module.tracker.service

import com.mycollab.db.arguments.*
import com.mycollab.module.tracker.domain.BugWithBLOBs
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
class BugServiceTest : IntegrationServiceTest() {

    @Autowired
    private val bugService: BugService? = null

    @DataSet
    @Test
    fun testGetListBugs() {
        val criteria = BugSearchCriteria()
        criteria.saccountid = NumberSearchField.equal(1)
        val bugs = bugService!!.findPageableListByCriteria(BasicSearchRequest<BugSearchCriteria>(criteria)) as List<SimpleBug>

        assertThat(bugs.size).isEqualTo(3)
        assertThat<SimpleBug>(bugs).extracting("id", "detail", "name").contains(
                tuple(1, "detail 1", "name 1"),
                tuple(2, "detail 2", "name 2"),
                tuple(3, "detail 3", "name 3"))
    }

    @DataSet
    @Test
    fun testSearchDefectsByUserCriteria() {
        val criteria = BugSearchCriteria()
        criteria.assignuser = StringSearchField.and("user1")
        criteria.loguser = StringSearchField.and("admin")
        criteria.name = StringSearchField.and("name")
        criteria.detail = StringSearchField.and("detail")

        val bugs = bugService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleBug>
        assertThat(bugs.size).isEqualTo(1)
        assertThat<SimpleBug>(bugs).extracting("id", "detail", "name").contains(tuple(2, "detail 2", "name 2"))
    }

    @DataSet
    @Test
    fun testGetExtBug() {
        val bug = bugService!!.findById(1, 1)
        assertThat(bug.loguserFullName).isEqualTo("Nguyen Hai")
        assertThat(bug.assignuserFullName).isEqualTo("Nguyen Hai")
        assertThat(bug.affectedVersions.size).isEqualTo(1)
        assertThat(bug.fixedVersions.size).isEqualTo(2)
        assertThat(bug.components.size).isEqualTo(1)
    }

    @DataSet
    @Test
    fun testSearchByComponents() {
        val criteria = BugSearchCriteria()
        criteria.componentids = SetSearchField(1, 2)

        val bugs = bugService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleBug>

        assertThat(bugs.size).isEqualTo(1)
        assertThat<SimpleBug>(bugs).extracting("id", "detail", "name").contains(
                tuple(1, "detail 1", "name 1"))
    }

    @DataSet
    @Test
    fun testGetComponentDefectsSummary() {
        val criteria = BugSearchCriteria()
        criteria.projectId = NumberSearchField(1)
        bugService!!.getComponentDefectsSummary(criteria)
    }

    @DataSet
    @Test
    fun testSearchByVersions() {
        val criteria = BugSearchCriteria()
        criteria.fixedversionids = SetSearchField(1, 2, 3)
        criteria.affectedversionids = SetSearchField(1, 2, 3)

        val bugs = bugService!!.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleBug>

        assertThat(bugs.size).isEqualTo(1)
        assertThat<SimpleBug>(bugs).extracting("id", "detail", "name").contains(tuple(1, "detail 1", "name 1"))
    }

    @DataSet
    @Test
    fun testSearchByAssignedUser() {
        val criteria = BugSearchCriteria()
        val assignedDefectsSummary = bugService!!.getAssignedDefectsSummary(criteria)

        assertThat(assignedDefectsSummary.size).isEqualTo(2)
        assertThat(assignedDefectsSummary).extracting("groupid", "value", "extraValue").contains(tuple("admin", 1.0, null),
                tuple("user1", 2.0, null))
    }

    @DataSet
    @Test
    fun testSearchByDateCriteria2() {
        val criteria = BugSearchCriteria()
        val date = GregorianCalendar()
        date.set(Calendar.YEAR, 2009)
        date.set(Calendar.MONTH, 0)
        date.set(Calendar.DAY_OF_MONTH, 2)

        criteria.updatedDate = DateSearchField(date.time)

        assertThat(bugService!!.findPageableListByCriteria(BasicSearchRequest(criteria, 0, Integer.MAX_VALUE)).size).isEqualTo(0)
    }

    @DataSet
    @Test
    fun testBugStatus() {
        val criteria = BugSearchCriteria()
        val groupItems = bugService!!.getStatusSummary(criteria)
        assertThat(groupItems.size).isEqualTo(1)
        assertThat(groupItems).extracting("groupid", "value", "extraValue").contains(tuple("1", 3.0, null))
    }

    @Test
    @DataSet
    fun testSaveBug() {
        val bug = BugWithBLOBs()
        bug.name = "summary4"
        bug.status = "aaa"
        bug.projectid = 1
        bug.saccountid = 1
        val bugId = bugService!!.saveWithSession(bug, "admin")
        assertThat(bugService.findById(bugId, 1).name).isEqualTo("summary4")
    }
}
