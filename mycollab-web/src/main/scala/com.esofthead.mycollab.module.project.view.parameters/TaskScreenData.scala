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
package com.esofthead.mycollab.module.project.view.parameters

import com.esofthead.mycollab.module.project.domain.Task
import com.esofthead.mycollab.vaadin.mvp.ScreenData

/**
 * @author MyCollab Ltd.
 * @since 5.0.3
 */
object TaskScreenData {
    
    class Read(param: Integer) extends ScreenData[Integer](param) {}
    
    class Edit(param: Task) extends ScreenData[Task](param) {}
    
    class Add(param: Task) extends ScreenData[Task](param) {}

    class GotoDashboard extends ScreenData {}
    
    class GotoGanttChart extends ScreenData {}
    
    class GotoKanbanView extends ScreenData {}

    class GotoCalendarView extends ScreenData {}
    
}
