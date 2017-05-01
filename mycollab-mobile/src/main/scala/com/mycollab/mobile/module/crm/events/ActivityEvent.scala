/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.crm.events

import com.mycollab.events.ApplicationEvent

/**
  * @author MyCollab Ltd
  * @since 5.4.9
  */
object ActivityEvent {

  class TaskAdd(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class TaskEdit(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class TaskRead(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class MeetingAdd(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class MeetingEdit(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class MeetingRead(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class CallAdd(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class CallEdit(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class CallRead(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoList(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoRelatedItems(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

}
