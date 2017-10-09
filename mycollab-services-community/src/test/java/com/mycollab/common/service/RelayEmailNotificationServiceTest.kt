package com.mycollab.common.service

import org.assertj.core.api.Assertions.assertThat

import org.springframework.beans.factory.annotation.Autowired

import com.mycollab.common.domain.SimpleRelayEmailNotification
import com.mycollab.common.domain.criteria.RelayEmailNotificationSearchCriteria
import com.mycollab.db.arguments.BasicSearchRequest

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:META-INF/spring/service-context-test.xml"})
class RelayEmailNotificationServiceTest {
    @Autowired
    private var relayEmailNotificationService: RelayEmailNotificationService? = null

    //@Test
    //@DataSet
    fun testRemoveItems() {
        val criteria = RelayEmailNotificationSearchCriteria()
        val items = relayEmailNotificationService!!
                .findPageableListByCriteria(BasicSearchRequest(
                        criteria, 0, Integer.MAX_VALUE))
        assertThat(items.size).isEqualTo(1)
    }
}
