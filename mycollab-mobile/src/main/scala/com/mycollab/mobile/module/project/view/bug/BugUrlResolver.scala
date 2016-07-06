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
package com.mycollab.mobile.module.project.view.bug

import com.mycollab.common.UrlTokenizer
import com.mycollab.core.{MyCollabException, ResourceNotFoundException}
import com.mycollab.db.arguments.{NumberSearchField, SetSearchField}
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.mobile.module.project.ProjectUrlResolver
import com.mycollab.mobile.module.project.events.ProjectEvent
import com.mycollab.mobile.module.project.view.parameters.{BugScreenData, ProjectScreenData}
import com.mycollab.module.project.ProjectLinkParams
import com.mycollab.module.project.i18n.OptionI18nEnum.BugStatus
import com.mycollab.module.tracker.domain.SimpleBug
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria
import com.mycollab.module.tracker.service.BugService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.PageActionChain

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class BugUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new DashboardUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)

  private class DashboardUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = new UrlTokenizer(params(0)).getInt
      val criteria = new BugSearchCriteria
      criteria.setProjectId(new NumberSearchField(projectId))
      criteria.setStatuses(new SetSearchField[String](BugStatus.Open.name, BugStatus.ReOpen.name))
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new BugScreenData.Search(criteria))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      var projectId = 0
      var bugId = 0
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
        throw new MyCollabException("Invalid bug link %s".format(params(0)))
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
        throw new MyCollabException("Invalid bug link: %s".format(params(0)))
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

}
