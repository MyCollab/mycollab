package com.mycollab.module.project.service

import com.mycollab.module.project.domain.TicketKey

/**
 * @author MyCollab Ltd
 * @since 7.0.2
 */
interface TicketKeyService {

    fun getTicketKeyByPrjShortNameAndKey(sAccountId: Int, prjShortName:String, key:Int): TicketKey?

    fun getMaxKey(projectId: Int): Int?

    fun getNextKey(projectId: Int, currentKey:Int): Int?

    fun getPreviousKey(projectId: Int, currentKey: Int): Int?

    fun saveKey(projectId: Int, ticketId:Int, ticketType:String, ticketKey: Int)
}