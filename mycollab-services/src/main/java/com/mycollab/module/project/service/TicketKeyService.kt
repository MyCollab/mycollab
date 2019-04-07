package com.mycollab.module.project.service

/**
 * @author MyCollab Ltd
 * @since 7.0.2
 */
interface TicketKeyService {
    fun getMaxKey(projectId: Int): Int?

    fun saveKey(projectId: Int, ticketId:Int, ticketType:String, ticketKey: Int)
}