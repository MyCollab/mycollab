package com.mycollab.mobile.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object MessageEvent {
    class GotoAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoRead(source: Any, val data: Any?) : ApplicationEvent(source)
}