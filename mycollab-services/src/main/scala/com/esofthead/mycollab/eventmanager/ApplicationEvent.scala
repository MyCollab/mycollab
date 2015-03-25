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

import java.util.EventObject

/**
 * Serves as a parent for all application level events. It holds the source that
 * triggered the event and enforces each event implementation to provide an
 * appropriate description for the event.
 *
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
class ApplicationEvent(source: AnyRef, data: AnyRef) extends EventObject(source) {

  def getData: AnyRef = {
    return data
  }
}
