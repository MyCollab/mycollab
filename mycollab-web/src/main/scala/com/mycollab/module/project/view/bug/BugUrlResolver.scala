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
package com.mycollab.module.project.view.bug

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.{MyCollabException, ResourceNotFoundException}
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.ProjectLinkParams
import com.mycollab.module.project.events.ProjectEvent
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.{BugScreenData, ProjectScreenData}
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.service.BugService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.PageActionChain

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class BugUrlResolver extends ProjectUrlResolver {
  this.defaultUrlResolver = new ListUrlResolver
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("kanban", new KanbanUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)

  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val tokenizer = new UrlTokenizer(params(0))
      val projectId = tokenizer.getInt
      val query = tokenizer.getQuery
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new BugScreenData.GotoList(query))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      var projectId: Integer = 0
      var bugId: Integer = 0
      if (ProjectLinkParams.isValidParam(params(0))) {
        val prjShortName = ProjectLinkParams.getProjectShortName(params(0))
        val itemKey = ProjectLinkParams.getItemKey(params(0))
        val bugService = AppContextUtil.getSpringBean(classOf[BugService])
        val bug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppContext.getAccountId)
        if (bug != null) {
          projectId = bug.getProjectid
          bugId = bug.getId
        }
        else {
          throw new ResourceNotFoundException("Can not get bug with bugkey %d and project short name %s".format(itemKey, prjShortName))
        }
      }
      else {
        throw new MyCollabException("Invalid bug link " + params(0))
      }
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new BugScreenData.Read(bugId))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      var bug: SimpleBug = null
      if (ProjectLinkParams.isValidParam(params(0))) {
        val prjShortName = ProjectLinkParams.getProjectShortName(params(0))
        val itemKey = ProjectLinkParams.getItemKey(params(0))
        val bugService = AppContextUtil.getSpringBean(classOf[BugService])
        bug = bugService.findByProjectAndBugKey(itemKey, prjShortName, AppContext.getAccountId)
      }
      else {
        throw new MyCollabException("Invalid bug link: " + params(0))
      }
      if (bug == null) {
        throw new ResourceNotFoundException
      }
      val chain = new PageActionChain(new ProjectScreenData.Goto(bug.getProjectid), new BugScreenData.Edit(bug))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class AddUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = new UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new BugScreenData.Add(new SimpleBug))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class KanbanUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*): Unit = {
      val projectId = new UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new BugScreenData.GotoKanbanView)
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

}
