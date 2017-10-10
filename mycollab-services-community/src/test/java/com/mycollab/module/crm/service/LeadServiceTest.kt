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

import com.mycollab.module.crm.domain.SimpleLead
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple

@RunWith(SpringJUnit4ClassRunner::class)
class LeadServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var leadService: LeadService

    @DataSet
    @Test
    fun testSearchByCriteria() {
        val leads = leadService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, 2)) as List<SimpleLead>
        assertThat(leads.size).isEqualTo(2)
        assertThat<SimpleLead>(leads).extracting("id", "source").contains(tuple(1, "Cold Call"), tuple(2, "Employee"))
    }

    @DataSet
    @Test
    fun testGetTotalCounts() {
        Assert.assertEquals(2, leadService.getTotalCount(criteria))
    }

    private val criteria: LeadSearchCriteria
        get() {
            val criteria = LeadSearchCriteria()
            criteria.leadName = StringSearchField.and("Nguyen")
            criteria.saccountid = NumberSearchField(1)
            return criteria
        }

    @Test
    @DataSet
    fun testSearchLeadName() {
        val criteria = LeadSearchCriteria()
        criteria.leadName = StringSearchField.and("Nguyen Hai")
        criteria.saccountid = NumberSearchField(1)

        val leads = leadService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, 2)) as List<SimpleLead>
        assertThat(leads.size).isEqualTo(1)
        assertThat<SimpleLead>(leads).extracting("id", "source").contains(tuple(1, "Cold Call"))
    }

    @Test
    @DataSet
    fun testSearchAssignUser() {
        val criteria = LeadSearchCriteria()
        criteria.assignUsers = SetSearchField("linh", "hai")
        criteria.saccountid = NumberSearchField(1)

        val leads = leadService.findPageableListByCriteria(BasicSearchRequest(criteria, 0, 2)) as List<SimpleLead>
        assertThat(leads.size).isEqualTo(2)
        assertThat<SimpleLead>(leads).extracting("id", "source").contains(tuple(1, "Cold Call"), tuple(2, "Employee"))
    }
}
