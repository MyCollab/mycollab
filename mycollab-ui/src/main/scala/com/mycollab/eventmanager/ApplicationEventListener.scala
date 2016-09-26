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

import java.util.EventListener

import com.mycollab.events.ApplicationEvent

/**
  * A listener that listens and is able to handle {@link ApplicationEvent
 * application event}.
  *
  * @author MyCollab Ltd.
  * @since 5.0.3
  */
trait ApplicationEventListener[E <: ApplicationEvent] extends EventListener with Serializable {
  /**
    * Handles the given application event.
    *
    * @param event
    * The event to handle.
    */
  def handle(event: E)
}
