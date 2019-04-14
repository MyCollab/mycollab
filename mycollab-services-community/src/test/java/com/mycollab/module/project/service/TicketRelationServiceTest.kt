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
        assertThat(ticketRelations).extracting("ticketKey", "ticketid", "ticketName", "ticketStatus", "tickettype", "typeKey", "typeid", "typeName", "typeStatus", "type", "rel", "ltr")
                .contains(tuple(2, 1, "Bug 1", "ReOpen","Project-Bug", 1, 1, "Task 1", "Open","Project-Task", "Duplicated", true),
                        tuple(4, 2, "Task 2", "Closed","Project-Task", 2, 1, "Bug 1", "ReOpen", "Project-Bug", "Block", false),
                        tuple(2, 1, "Bug 1", "ReOpen", "Project-Bug", 3, 2, "Bug 2", "Open", "Project-Bug", "Block", true))
    }
}