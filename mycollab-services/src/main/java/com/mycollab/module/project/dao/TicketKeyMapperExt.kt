package com.mycollab.module.project.dao

import com.mycollab.module.project.domain.TicketKey

/**
 * @author MyCollab
 * @since 7.0.2
 */
interface TicketKeyMapperExt {
    fun getMaxKey(projectId: Int): Int?

    fun getNextKey(projectId: Int, currentKey:Int): Int?

    fun getPreviousKey(projectId: Int, currentKey: Int): Int?

    fun getTicketKeyByPrjShortNameAndKey(sAccountId: Int, prjShortName:String, ticketKey:Int): TicketKey
}