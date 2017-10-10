/**
 * mycollab-web - Parent pom providing dependency and plugin management for applications
		built with Maven
 * Copyright © ${project.inceptionYear} MyCollab (support@mycollab.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.event

import com.mycollab.vaadin.event.ApplicationEvent
import com.mycollab.module.project.domain.ProjectGenericItem

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
object ProjectEvent {
    class GotoAdd(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoEdit(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoList(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoMyProject(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoTagListView(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoFavoriteView(source: Any, val data: Any?) : ApplicationEvent(source)

    class SelectFavoriteItem(source: Any, val data: ProjectGenericItem) : ApplicationEvent(source)

    class TimeLoggingChangedEvent(source: Any) : ApplicationEvent(source)

    class GotoCalendarView(source: Any) : ApplicationEvent(source)

    class GotoGanttChart(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoDashboard(source: Any, val data: Any?) : ApplicationEvent(source)

    class GotoUserDashboard(source: Any, val data: Any?) : ApplicationEvent(source)
}