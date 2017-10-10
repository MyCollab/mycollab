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
package com.mycollab.module.crm.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.crm.domain.SimpleCase
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class CaseServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var caseService: CaseService

    @DataSet
    @Test
    fun testGetAll() {
        val criteria = CaseSearchCriteria()
        criteria.saccountid = NumberSearchField(1)

        val cases = caseService.findPageableListByCriteria(BasicSearchRequest<CaseSearchCriteria>(criteria)) as List<SimpleCase>

        assertThat(cases.size).isEqualTo(2)
        assertThat<SimpleCase>(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"))
    }

    @DataSet
    @Test
    fun testGetSearchCriteria() {
        val criteria = CaseSearchCriteria()
        criteria.accountId = NumberSearchField(1)
        criteria.assignUser = StringSearchField.and("admin")
        criteria.subject = StringSearchField.and("a")
        criteria.saccountid = NumberSearchField(1)

        val cases = caseService.findPageableListByCriteria(BasicSearchRequest<CaseSearchCriteria>(criteria)) as List<SimpleCase>

        assertThat(cases.size).isEqualTo(1)
        assertThat<SimpleCase>(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"))
    }

    @Test
    @DataSet
    fun testSearchAssignUsers() {
        val criteria = CaseSearchCriteria()
        criteria.assignUsers = SetSearchField("linh", "admin")
        criteria.saccountid = NumberSearchField(1)

        val cases = caseService.findPageableListByCriteria(BasicSearchRequest<CaseSearchCriteria>(criteria)) as List<SimpleCase>

        assertThat(cases.size).isEqualTo(2)
        assertThat<SimpleCase>(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"))
    }

    @Test
    @DataSet
    fun testSearchPriorities() {
        val criteria = CaseSearchCriteria()
        criteria.priorities = SetSearchField("High", "Medium")
        criteria.saccountid = NumberSearchField(1)

        val cases = caseService.findPageableListByCriteria(BasicSearchRequest<CaseSearchCriteria>(criteria)) as List<SimpleCase>

        assertThat(cases.size).isEqualTo(2)
        assertThat<SimpleCase>(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"))
    }

    @Test
    @DataSet
    fun testSearchStatuses() {
        val criteria = CaseSearchCriteria()
        criteria.statuses = SetSearchField("New", "Test Status")
        criteria.saccountid = NumberSearchField(1)

        val cases = caseService.findPageableListByCriteria(BasicSearchRequest<CaseSearchCriteria>(criteria)) as List<SimpleCase>

        assertThat(cases.size).isEqualTo(2)
        assertThat<SimpleCase>(cases).extracting("id", "subject", "status").contains(tuple(1, "abc", "New"), tuple(2, "a", "Test Status"))
    }
}
