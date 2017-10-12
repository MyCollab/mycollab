/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.event

import com.mycollab.vaadin.event.ApplicationEvent

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ActivityEvent {
    class GotoCalendar(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoTodoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class TaskAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class TaskEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class TaskRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class MeetingAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class MeetingEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class MeetingRead(source: Any, val data: Any?) : ApplicationEvent(source)

    class CallAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class CallEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class CallRead(source: Any, val data: Any?) : ApplicationEvent(source)
}