package com.mycollab.vaadin.event

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface HasSelectionOptionHandlers {

    /**
     * @param handler
     */
    fun addSelectionOptionHandler(handler: SelectionOptionHandler)
}
