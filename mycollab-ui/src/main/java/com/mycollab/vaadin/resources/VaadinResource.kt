package com.mycollab.vaadin.resources

import com.vaadin.server.Resource

/**
 * @author MyCollab Ltd.
 * @since 4.5.1
 */
interface VaadinResource {
    fun getStreamResource(documentPath: String): Resource
}
