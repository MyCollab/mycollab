package com.mycollab.vaadin

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.SubscriberExceptionContext
import com.google.common.eventbus.SubscriberExceptionHandler
import com.mycollab.vaadin.ui.MyCollabSession
import org.slf4j.LoggerFactory

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object EventBusFactory {
    private val LOG = LoggerFactory.getLogger(EventBusFactory::class.java)

    @JvmStatic fun getInstance(): EventBus {
        var eventBus = MyCollabSession.getCurrentUIVariable(MyCollabSession.EVENT_BUS_VAL) as EventBus?
        if (eventBus == null) {
            eventBus = EventBus(SubscriberEventBusExceptionHandler())
            MyCollabSession.putCurrentUIVariable(MyCollabSession.EVENT_BUS_VAL, eventBus)
        }
        return eventBus
    }

    private class SubscriberEventBusExceptionHandler : SubscriberExceptionHandler {
        override fun handleException(e: Throwable, context: SubscriberExceptionContext) {
            LOG.error("Error", e)
        }
    }
}