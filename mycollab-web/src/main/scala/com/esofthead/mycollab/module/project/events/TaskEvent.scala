/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.events

import com.esofthead.mycollab.eventmanager.ApplicationEvent
import com.esofthead.mycollab.module.project.domain.AssignWithPredecessors
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria
import com.esofthead.mycollab.module.project.view.task.gantt.GanttItemWrapper

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
object TaskEvent {

    class SearchRequest(source: AnyRef, data: TaskSearchCriteria) extends ApplicationEvent(source, data) {};

    class HasTaskChange(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class NewTaskAdded(source: AnyRef, data: Integer) extends ApplicationEvent(source, data) {}

    class Search(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoDashboard(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoAdd(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoEdit(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoRead(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoGanttChart(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoKanbanView(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

    class GotoCalendarView(source: AnyRef) extends ApplicationEvent(source, null) {}

}
