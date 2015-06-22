/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.eventmanager

import com.esofthead.mycollab.core.MyCollabException
import com.google.common.eventbus.EventBus

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
abstract class EventBusFactory {
    protected def getInstanceInSession: EventBus
}

object EventBusFactory {
    private val eventBusFactoryImplClsName: String = "com.esofthead.mycollab.eventmanager.EventBusFactoryImpl"
    private var eventBusFactoryImpl: EventBusFactory = null

    try {
        val cls: Class[EventBusFactory] = Class.forName(eventBusFactoryImplClsName).asInstanceOf[Class[EventBusFactory]]
        eventBusFactoryImpl = cls.newInstance
    }
    catch {
        case e: Exception => throw new MyCollabException(e)
    }

    def getInstance(): EventBus = eventBusFactoryImpl.getInstanceInSession
}
