package com.mycollab.module.project.dao

import com.mycollab.module.project.domain.SimpleTicketRelation

interface TicketRelationMapperExt {
    fun findRelatedTickets(ticketId:Int, ticketType:String): List<SimpleTicketRelation>
}