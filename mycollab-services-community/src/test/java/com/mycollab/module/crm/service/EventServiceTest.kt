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
import com.mycollab.db.arguments.DateTimeSearchField
import com.mycollab.db.arguments.DateTimeSearchField.Companion.GREATER_THAN_EQUAL
import com.mycollab.db.arguments.DateTimeSearchField.Companion.LESS_THAN_EQUAL
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SearchField.Companion.AND
import com.mycollab.module.crm.domain.SimpleActivity
import com.mycollab.module.crm.domain.criteria.ActivitySearchCriteria
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
class EventServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var eventService: EventService

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testSearchByCriteria() {
        val criteria = ActivitySearchCriteria()
        criteria.saccountid = NumberSearchField.equal(1)

        val list = eventService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleActivity>
        assertThat(list.size).isEqualTo(1)
        assertThat<SimpleActivity>(list).extracting("id", "subject").contains(tuple(1, "aaa"))
    }

    @DataSet
    @Test
    @Throws(ParseException::class)
    fun testSearchByTimeRange() {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val startDate = format.parse("2012-11-11 00:00:00")
        val endDate = format.parse("2012-11-15 00:00:00")
        val criteria = ActivitySearchCriteria()
        criteria.startDate = DateTimeSearchField(AND, GREATER_THAN_EQUAL, startDate)
        criteria.endDate = DateTimeSearchField(AND, LESS_THAN_EQUAL, endDate)
        criteria.saccountid = NumberSearchField(1)

        val list = eventService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleActivity>
        assertThat(list.size).isEqualTo(1)
        assertThat<SimpleActivity>(list).extracting("id", "subject").contains(tuple(1, "aaa"))
    }
}
