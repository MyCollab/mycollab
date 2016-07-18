/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.eventmanager

import com.google.common.eventbus.{EventBus, SubscriberExceptionContext, SubscriberExceptionHandler}
import com.mycollab.vaadin.ui.MyCollabSession
import com.mycollab.vaadin.ui.MyCollabSession._
import org.slf4j.LoggerFactory

/**
  * @author MyCollab Ltd.
  * @since 5.0.3
  */

object EventBusFactory {
  
  private val LOG = LoggerFactory.getLogger(EventBusFactory.getClass)
  
  def getInstance(): EventBus = {
    var eventBus = MyCollabSession.getCurrentUIVariable(EVENT_BUS_VAL).asInstanceOf[EventBus]
    if (eventBus == null) {
      eventBus = new EventBus(new SubscriberEventBusExceptionHandler)
      MyCollabSession.putCurrentUIVariable(EVENT_BUS_VAL, eventBus)
    }
    eventBus
  }
  
  private class SubscriberEventBusExceptionHandler extends SubscriberExceptionHandler {
    def handleException(e: Throwable, context: SubscriberExceptionContext) {
      LOG.error("Error", e)
    }
  }
  
}
