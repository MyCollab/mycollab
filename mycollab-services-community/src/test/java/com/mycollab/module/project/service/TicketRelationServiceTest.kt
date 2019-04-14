package com.mycollab.module.project.service

import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.test.DataSet
import com.mycollab.test.rule.DbUnitInitializerRule
import com.mycollab.test.spring.IntegrationServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class, DbUnitInitializerRule::class)
class TicketRelationServiceTest : IntegrationServiceTest() {

    @Autowired
    private lateinit var ticketRelationService: TicketRelationService

    @DataSet
    @Test
    fun testFindRelatedTickets() {
        val ticketRelations = ticketRelationService.findRelatedTickets(1, ProjectTypeConstants.BUG)
        assertThat(ticketRelations.size).isEqualTo(3)
        assertThat(ticketRelations).extracting("ticketKey", "ticketid", "ticketName", "tickettype", "typeKey", "typeid", "typeName", "type", "rel", "ltr")
                .contains(tuple(2, 1, "Bug 1", "Project-Bug", 1, 1, "Task 1", "Project-Task", "Duplicated", true),
                        tuple(4, 2, "Task 2", "Project-Task", 2, 1, "Bug 1", "Project-Bug", "Block", false),
                        tuple(2, 1, "Bug 1", "Project-Bug", 3, 2, "Bug 2", "Project-Bug", "Block", true))
    }
}