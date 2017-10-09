package com.mycollab.vaadin.mvp

import com.mycollab.vaadin.EventBusFactory

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
open class AbstractController {
    private val eventBus = EventBusFactory.getInstance()
    private val subscribers: MutableSet<Any> = mutableSetOf()

    fun register(subscriber: Any) {
        eventBus.register(subscriber)
        subscribers.add(subscriber)
    }

    fun unregisterAll() {
        subscribers.forEach { eventBus.unregister(it) }
        subscribers.clear()
    }
}