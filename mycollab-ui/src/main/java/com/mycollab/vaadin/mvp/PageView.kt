package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.event.ViewEvent
import com.vaadin.ui.HasComponents
import com.vaadin.util.ReflectTools
import java.io.Serializable
import java.util.*

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
interface PageView : HasComponents, CacheableComponent {

    fun <E> addViewListener(listener: ViewListener<E>)

    interface ViewListener<E> : EventListener, Serializable {

        fun receiveEvent(event: ViewEvent<E>)

        companion object {
            val viewInitMethod = ReflectTools.findMethod(ViewListener::class.java, "receiveEvent", ViewEvent::class.java)
        }
    }
}
