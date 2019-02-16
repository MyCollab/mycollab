package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 7.0.0
 */
object FavoriteEvent {

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)
}