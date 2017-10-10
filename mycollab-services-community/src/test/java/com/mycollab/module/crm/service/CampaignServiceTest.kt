package com.mycollab.module.crm.service

import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.db.arguments.StringSearchField
import com.mycollab.module.crm.domain.SimpleCampaign
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple

@RunWith(SpringJUnit4ClassRunner::class)
class CampaignServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var campaignService: CampaignService

    @DataSet
    @Test
    fun testSearchByCriteria() {
        val campaigns = campaignService.findPageableListByCriteria(BasicSearchRequest(criteria)) as List<SimpleCampaign>

        assertThat(campaigns.size).isEqualTo(2)
        assertThat<SimpleCampaign>(campaigns).extracting("id", "campaignname", "status").contains(tuple(1, "AAA", "a"), tuple(2, "ABB", "b"))
    }

    @DataSet
    @Test
    fun testGetTotalCounts() {
        val campaigns = campaignService.findPageableListByCriteria(BasicSearchRequest(criteria))

        assertThat(campaigns.size).isEqualTo(2)
    }

    private val criteria: CampaignSearchCriteria
        get() {
            val criteria = CampaignSearchCriteria()
            criteria.assignUser = StringSearchField.and("hai79")
            criteria.campaignName = StringSearchField.and("A")
            criteria.saccountid = NumberSearchField(1)
            criteria.assignUsers = SetSearchField("hai79", "linh")
            criteria.statuses = SetSearchField("a", "b")
            criteria.types = SetSearchField("a", "b")
            return criteria
        }
}
