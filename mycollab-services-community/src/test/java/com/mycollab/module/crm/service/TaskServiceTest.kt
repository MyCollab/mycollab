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
    private lateinit var taskService: TaskService

    @DataSet
    @Test
    fun testSearchByCriteria() {
        val tasks = taskService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleCrmTask>

        assertThat(tasks.size).isEqualTo(1)
        assertThat<SimpleCrmTask>(tasks).extracting("id", "status", "subject").contains(tuple(1, "Completed", "aaa"))
    }

    @DataSet
    @Test
    fun testGetTotalCounts() {
        val tasks = taskService.findPageableListByCriteria(BasicSearchRequest(criteria))
        assertThat(tasks.size).isEqualTo(1)
    }

    private val criteria: CrmTaskSearchCriteria
        get() {
            val criteria = CrmTaskSearchCriteria()
            criteria.saccountid = NumberSearchField(1)
            return criteria
        }
}
