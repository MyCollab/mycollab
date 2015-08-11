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
package com.esofthead.mycollab.module.project.view.task

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.project.events.ProjectEvent
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData.{GotoDashboard, GotoGanttChart}
import com.esofthead.mycollab.module.project.view.parameters.{ProjectScreenData, TaskScreenData}
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class ScheduleUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("dashboard", new DashboardUrlResolver)
    this.addSubResolver("task", new TaskUrlResolver)
    this.addSubResolver("gantt", new GanttUrlResolver)
    this.addSubResolver("kanban", new KanbanUrlResolver)
    this.defaultUrlResolver = new DashboardUrlResolver

    private class DashboardUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId = new UrlTokenizer(params(0)).getInt
            val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new GotoDashboard)
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class KanbanUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*): Unit = {
            val projectId = new UrlTokenizer(params(0)).getInt
            val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new TaskScreenData.GotoKanbanView)
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class GanttUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId = new UrlTokenizer(params(0)).getInt
            val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new GotoGanttChart)
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

}
