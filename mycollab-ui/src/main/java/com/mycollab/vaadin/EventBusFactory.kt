/**
 * mycollab-ui - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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