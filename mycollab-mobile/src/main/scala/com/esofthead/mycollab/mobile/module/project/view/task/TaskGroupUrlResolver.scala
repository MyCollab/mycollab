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
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.mobile.module.project.ProjectUrlResolver
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent
import com.esofthead.mycollab.mobile.module.project.view.parameters.{TaskGroupScreenData, ProjectScreenData}
import com.esofthead.mycollab.module.project.domain.SimpleTaskList
import com.esofthead.mycollab.module.project.service.ProjectTaskListService
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.PageActionChain
import com.esofthead.mycollab.vaadin.ui.NotificationUtil

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
class TaskGroupUrlResolver extends ProjectUrlResolver {
    this.addSubResolver("list", new ListUrlResolver)
    this.addSubResolver("preview", new ReadUrlResolver)
    this.addSubResolver("add", new AddUrlResolver)
    this.addSubResolver("edit", new EditUrlResolver)

    private class ListUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskGroupScreenData.List)
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class ReadUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val taskListId: Int = token.getInt
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskGroupScreenData.Read(taskListId))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class AddUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val projectId: Int = new UrlTokenizer(params(0)).getInt
            val taskList: SimpleTaskList = new SimpleTaskList
            taskList.setProjectid(projectId)
            taskList.setStatus(StatusI18nEnum.Open.name)
            val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskGroupScreenData.Add(taskList))
            EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
    }

    private class EditUrlResolver extends ProjectUrlResolver {
        protected override def handlePage(params: String*) {
            val token: UrlTokenizer = new UrlTokenizer(params(0))
            val projectId: Int = token.getInt
            val taskListId: Int = token.getInt
            val service: ProjectTaskListService = ApplicationContextUtil.getSpringBean(classOf[ProjectTaskListService])
            val taskList: SimpleTaskList = service.findById(taskListId, AppContext.getAccountId)
            if (taskList != null) {
                val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskGroupScreenData.Edit(taskList))
                EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
            }
            else {
                NotificationUtil.showRecordNotExistNotification
            }
        }
    }

}
