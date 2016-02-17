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
import com.esofthead.mycollab.module.project.domain.TaskPredecessor
import com.esofthead.mycollab.module.project.view.assignments.gantt.GanttItemWrapper

/**
  * @author MyCollab Ltd
  * @since 5.1.3
  */
object GanttEvent {

  class ClearGanttItemsNeedUpdate(source: AnyRef) extends ApplicationEvent(source, null) {}

  class UpdateGanttItem(source: AnyRef, data: GanttItemWrapper) extends ApplicationEvent(source, data) {}

  class DeleteGanttItemUpdateToQueue(source: AnyRef, data: GanttItemWrapper) extends ApplicationEvent(source, data) {}

  class AddGanttItemUpdateToQueue(source: AnyRef, data: GanttItemWrapper) extends ApplicationEvent(source, data) {}

  class ModifyPredecessors(source: GanttItemWrapper, predecessors: java.util.List[TaskPredecessor]) extends ApplicationEvent(source, predecessors) {}

}
