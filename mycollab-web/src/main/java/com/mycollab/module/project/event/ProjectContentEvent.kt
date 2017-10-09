package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectContentEvent {
    class GotoDashboard(source: Any) : ApplicationEvent(source)
}