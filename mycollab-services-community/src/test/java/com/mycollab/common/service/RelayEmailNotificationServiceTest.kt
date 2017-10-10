package com.mycollab.common.service

import com.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner::class)
class RelayEmailNotificationServiceTest : IntegrationServiceTest() {
    @Autowired
    private lateinit var relayEmailNotificationService: RelayEmailNotificationService

    @Test
    @DataSet
    fun testRemoveItems() {
        val criteria = RelayEmailNotificationSearchCriteria()
        val items = relayEmailNotificationService.findPageableListByCriteria(BasicSearchRequest(
                criteria, 0, Integer.MAX_VALUE))
        assertThat(items.size).isEqualTo(1)
    }
}
