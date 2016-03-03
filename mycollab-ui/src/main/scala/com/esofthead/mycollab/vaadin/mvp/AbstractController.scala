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
package com.esofthead.mycollab.vaadin.mvp

import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.google.common.eventbus.EventBus

import scala.collection.mutable._

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
@SerialVersionUID(1L)
class AbstractController extends Serializable {
  private val eventBus: EventBus = EventBusFactory.getInstance()
  private val subscribers: Buffer[AnyRef] = Buffer();

  def register(subscriber: AnyRef): Unit = {
    eventBus.register(subscriber)
    subscribers += subscriber
  }

  def unregisterAll {
    subscribers.foreach(subscriber => eventBus.unregister(subscriber))
  }
}
