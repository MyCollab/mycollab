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

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.crm.domain.SimpleOpportunity
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class OpportunityServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var opportunityService: OpportunityService

    @DataSet
    @Test
    fun testSearchByCriteria() {
        val opportunities = opportunityService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleOpportunity>

        assertThat(opportunities.size).isEqualTo(2)
        assertThat<SimpleOpportunity>(opportunities).extracting("id", "salesstage", "source").contains(
                tuple(1, "1", "Cold Call"), tuple(2, "2", "Employee"))
    }

    @DataSet
    @Test
    fun testGetTotalCount() {
        val opportunities = opportunityService.findPageableListByCriteria(BasicSearchRequest(criteria))
        assertThat(opportunities.size).isEqualTo(2)
    }

    private val criteria: OpportunitySearchCriteria
        get() {
            val criteria = OpportunitySearchCriteria()
            criteria.accountId = NumberSearchField(1)
            criteria.campaignId = NumberSearchField(1)
            criteria.opportunityName = StringSearchField.and("aa")
            criteria.saccountid = NumberSearchField(1)
            return criteria
        }

    @Test
    @DataSet
    fun testSearchAssignUsers() {
        val criteria = OpportunitySearchCriteria()
        criteria.assignUsers = SetSearchField("hai", "linh")
        criteria.saccountid = NumberSearchField(1)

        val opportunities = opportunityService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleOpportunity>

        assertThat(opportunities.size).isEqualTo(2)
        assertThat<SimpleOpportunity>(opportunities).extracting("id", "salesstage", "source").contains(
                tuple(1, "1", "Cold Call"), tuple(2, "2", "Employee"))
    }
}
