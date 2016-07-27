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
package com.mycollab.module.project.view.task

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.{MyCollabException, ResourceNotFoundException}
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.ProjectLinkParams
import com.mycollab.module.project.domain.SimpleTask
import com.mycollab.module.project.events.ProjectEvent
import com.mycollab.module.project.service.ProjectTaskService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.TaskScreenData.GotoDashboard
import com.mycollab.module.project.view.parameters.{ProjectScreenData, TaskScreenData}
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.PageActionChain

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class ScheduleUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("dashboard", new DashboardUrlResolver)
  this.addSubResolver("preview", new ReadUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("kanban", new KanbanUrlResolver)
  this.defaultUrlResolver = new DashboardUrlResolver
  
  private class DashboardUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val tokenizer = UrlTokenizer(params(0))
      val projectId = tokenizer.getInt
      val query = tokenizer.getQuery
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new GotoDashboard(query))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
  private class KanbanUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*): Unit = {
      val projectId = UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new TaskScreenData.GotoKanbanView)
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
  private class ReadUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      if (ProjectLinkParams.isValidParam(params(0))) {
        val prjShortName = ProjectLinkParams.getProjectShortName(params(0))
        val itemKey = ProjectLinkParams.getItemKey(params(0))
        val taskService = AppContextUtil.getSpringBean(classOf[ProjectTaskService])
        val task = taskService.findByProjectAndTaskKey(itemKey, prjShortName, AppContext.getAccountId)
        if (task != null) {
          val projectId = task.getProjectid
          val taskId = task.getId
          val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TaskScreenData.Read(taskId))
          EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
        }
        else {
          throw new ResourceNotFoundException(String.format("Can not find task with itemKey %d and project %s", itemKey, prjShortName))
        }
      } else {
        throw new MyCollabException("Invalid url " + params(0));
      }
    }
  }
  
  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      var task: SimpleTask = null
      val taskService = AppContextUtil.getSpringBean(classOf[ProjectTaskService])
      if (ProjectLinkParams.isValidParam(params(0))) {
        val prjShortName = ProjectLinkParams.getProjectShortName(params(0))
        val itemKey = ProjectLinkParams.getItemKey(params(0))
        task = taskService.findByProjectAndTaskKey(itemKey, prjShortName, AppContext.getAccountId)
      }
      else {
        throw new MyCollabException("Can not find task link " + params(0))
      }
      val chain = new PageActionChain(new ProjectScreenData.Goto(task.getProjectid),
        new TaskScreenData.Edit(task))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
  private class AddUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new TaskScreenData.Add(new SimpleTask))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
}
