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
import com.esofthead.mycollab.module.project.domain.SimpleTaskList
import com.esofthead.mycollab.module.project.events.ProjectEvent
import com.esofthead.mycollab.module.project.service.ProjectTaskListService
import com.esofthead.mycollab.module.project.view.ProjectUrlResolver
import com.esofthead.mycollab.module.project.view.parameters.{TaskGroupScreenData, ProjectScreenData}
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class TaskGroupUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("preview", new ReadUrlResolver)
    this.addSubResolver("edit", new EditUrlResolver)

    private class ReadUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val taskgroupId: Int = token.getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new TaskGroupScreenData.Read(taskgroupId))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val taskgroupId: Int = token.getInt
            val taskGroupService: ProjectTaskListService = ApplicationContextUtil.getSpringBean(classOf[ProjectTaskListService])
            val taskgroup: SimpleTaskList = taskGroupService.findById(taskgroupId, AppContext.getAccountId)
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId),
                new TaskGroupScreenData.Edit(taskgroup))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }
}
