package com.mycollab.vaadin.event

import java.io.Serializable

/**
 * Collection contains handlers of do paging in list or table
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface HasPageableHandlers : Serializable {
    /**
     * Add page handler
     *
     * @param handler page handler
     */
    fun addPageableHandler(handler: PageableHandler)
}
