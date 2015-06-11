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
package com.esofthead.mycollab.mobile.module.project.view.task

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.mobile.module.project.ProjectUrlResolver
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent
import com.esofthead.mycollab.mobile.module.project.view.parameters.{TaskGroupScreenData, ProjectScreenData}
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class TaskModuleUrlResolver extends ProjectUrlResolver {
    addSubResolver("taskgroup", new TaskGroupUrlResolver)
    addSubResolver("task", new TaskUrlResolver)

    protected override def handlePage(params: String*) {
        val projectId: Int = new UrlTokenizer(params(0)).getInt
        val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskGroupScreenData.List)
        EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
    }
}
