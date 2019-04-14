package com.mycollab.module.project.dao

import com.mycollab.module.project.domain.SimpleTicketRelation
import org.apache.ibatis.annotations.Param

interface TicketRelationMapperExt {
    fun findRelatedTickets(@Param("ticketId") ticketId:Int, @Param("ticketType")ticketType:String): List<SimpleTicketRelation>
}