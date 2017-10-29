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
package com.mycollab.module.project.view.ticket

import com.mycollab.common.UrlTokenizer
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.TicketScreenData
import com.mycollab.vaadin.EventBusFactory
import com.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class TicketUrlResolver : ProjectUrlResolver() {
    init {
        this.addSubResolver("dashboard", DashboardUrlResolver())
        this.addSubResolver("kanban", KanbanUrlResolver())
        this.defaultUrlResolver = DashboardUrlResolver()
    }

    private class DashboardUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val tokenizer = UrlTokenizer(params[0])
            val projectId = tokenizer.getInt()
            val query = tokenizer.query
            val chain = PageActionChain(ProjectScreenData.Goto(projectId), TicketScreenData.GotoDashboard(query))
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class KanbanUrlResolver : ProjectUrlResolver() {
        override fun handlePage(vararg params: String) {
            val projectId = UrlTokenizer(params[0]).getInt()
            val chain = PageActionChain(ProjectScreenData.Goto(projectId),
                    TicketScreenData.GotoKanbanView())
            EventBusFactory.getInstance().post(ProjectEvent.GotoMyProject(this, chain))
        }
    }
}