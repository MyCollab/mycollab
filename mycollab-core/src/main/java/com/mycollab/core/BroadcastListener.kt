package com.mycollab.core

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
interface BroadcastListener {
    /**
     * @param message
     */
    fun broadcast(message: BroadcastMessage)
}
