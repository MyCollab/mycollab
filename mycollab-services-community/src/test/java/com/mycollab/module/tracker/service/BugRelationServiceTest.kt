package com.mycollab.module.tracker.service

import com.mycollab.module.tracker.domain.SimpleRelatedBug
import com.mycollab.test.DataSet
import com.mycollab.test.spring.IntegrationServiceTest
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import org.assertj.core.api.Assertions.assertThat

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@RunWith(SpringJUnit4ClassRunner::class)
class BugRelationServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var bugRelationService: BugRelationService

    @DataSet
    @Test
    fun testGetRelatedBugs() {
        val relatedBugs = bugRelationService.findRelatedBugs(1)
        assertThat(relatedBugs.size).isEqualTo(2)
    }
}
