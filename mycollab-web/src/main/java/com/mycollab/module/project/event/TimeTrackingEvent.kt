package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object TimeTrackingEvent {
    class TimeLoggingEntryChange(source: Any) : ApplicationEvent(source)

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)
}