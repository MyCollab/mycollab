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
