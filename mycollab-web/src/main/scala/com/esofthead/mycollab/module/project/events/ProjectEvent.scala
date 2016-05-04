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
import com.esofthead.mycollab.module.project.domain.ProjectGenericItem

/**
  * @author MyCollab Ltd.
  * @since 5.0.3
  */
object ProjectEvent {

  class GotoAdd(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoEdit(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoList(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoMyProject(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoTagListView(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoFavoriteView(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class SelectFavoriteItem(source: AnyRef, data: ProjectGenericItem) extends ApplicationEvent(source, data) {}

  class TimeLoggingChangedEvent(source: AnyRef) extends ApplicationEvent(source, null) {}

  class GotoCalendarView(source: AnyRef) extends ApplicationEvent(source, null) {}

  class GotoGanttChart(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoDashboard(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

  class GotoUserDashboard(source: AnyRef, data: AnyRef) extends ApplicationEvent(source, data) {}

}
