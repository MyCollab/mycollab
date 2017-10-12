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
package com.mycollab.common.service

import com.mycollab.common.domain.SimpleActivityStream
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class ActivityStreamServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var activityStreamService: ActivityStreamService

    @Test
    @DataSet
    fun testSearchActivityStreams() {
        val searchCriteria = ActivityStreamSearchCriteria()
        searchCriteria.moduleSet = SetSearchField("aa", "bb")
        searchCriteria.saccountid = NumberSearchField(1)

        val activities = activityStreamService.findPageableListByCriteria(BasicSearchRequest(searchCriteria))
        assertThat(activities.size).isEqualTo(3)
    }

    @Test
    @DataSet
    fun testQueryActivityWithComments() {
        val searchCriteria = ActivityStreamSearchCriteria()
        searchCriteria.moduleSet = SetSearchField("bb")
        searchCriteria.saccountid = NumberSearchField(1)

        val activities = activityStreamService.findPageableListByCriteria(BasicSearchRequest(searchCriteria)) as List<SimpleActivityStream>

        assertThat(activities.size).isEqualTo(1)
        assertThat<SimpleActivityStream>(activities).extracting("saccountid", "module", "action").contains(tuple(1, "bb", "update"))
    }
}
