package com.mycollab.module.project.service

import com.mycollab.db.arguments.BasicSearchRequest
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.module.project.domain.criteria.MessageSearchCriteria
import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired

@ExtendWith(DbUnitInitializerRule::class)
class MessageServiceTest(@Autowired val messageService: MessageService) : IntegrationServiceTest() {

    @Test
    @DataSet
    fun testFindMessages() {
        val criteria = MessageSearchCriteria()
        criteria.projectIds = SetSearchField(1, 2)
        assertThat(messageService.getTotalCount(criteria)).isEqualTo(3)
        assertThat(messageService.findPageableListByCriteria(BasicSearchRequest(criteria)).size).isEqualTo(3)
    }
}