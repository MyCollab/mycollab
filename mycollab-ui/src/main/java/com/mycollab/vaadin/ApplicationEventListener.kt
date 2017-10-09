package com.mycollab.vaadin

import java.util.*

/**
 * A listener that listens and is able to handle {@link ApplicationEvent
 * application event}.
 *
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
interface ApplicationEventListener<in ApplicationEvent> : EventListener {
    /**
     * Handles the given application event.
     *
     * @param event
     * The event to handle.
     */
    fun handle(event: ApplicationEvent)
}