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
package com.mycollab.mobile.module.project

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria
import com.mycollab.common.{ModuleNameConstants, UrlTokenizer}
import com.mycollab.db.arguments.{NumberSearchField, SetSearchField}
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.mobile.module.project.events.ProjectEvent
import com.mycollab.mobile.module.project.view.bug.BugUrlResolver
import com.mycollab.mobile.module.project.view.message.MessageUrlResolver
import com.mycollab.mobile.module.project.view.milestone.MilestoneUrlResolver
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData
import com.mycollab.mobile.module.project.view.parameters.ProjectScreenData.AllActivities
import com.mycollab.mobile.module.project.view.risk.RiskUrlResolver
import com.mycollab.mobile.module.project.view.settings.UserUrlResolver
import com.mycollab.mobile.module.project.view.task.TaskUrlResolver
import com.mycollab.mobile.shell.ModuleHelper
import com.mycollab.mobile.shell.events.ShellEvent
import com.mycollab.module.project.service.ProjectService
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.{PageActionChain, UrlResolver}

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class ProjectUrlResolver extends UrlResolver {
  def build: UrlResolver = {
    this.addSubResolver("dashboard", new DashboardUrlResolver)
    this.addSubResolver("activities", new ActivityUrlResolver)
    this.addSubResolver("message", new MessageUrlResolver)
    this.addSubResolver("milestone", new MilestoneUrlResolver)
    this.addSubResolver("task", new TaskUrlResolver)
    this.addSubResolver("bug", new BugUrlResolver)
    this.addSubResolver("risk", new RiskUrlResolver)
    this.addSubResolver("user", new UserUrlResolver)
    return this
  }

  override def handle(params: String*) {
    if (!ModuleHelper.isCurrentProjectModule) {
      EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, params))
    }
    else {
      super.handle(params: _*)
    }
  }

  protected def defaultPageErrorHandler() {
    EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null))
  }

  protected override def handlePage(params: String*) {
    super.handlePage(params: _*)
    EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null))
  }

  class DashboardUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      if (params.length == 0) {
        EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(this, null))
      }
      else {
        val projectId = new UrlTokenizer(params(0)).getInt
        val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectScreenData.GotoDashboard)
        EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
      }
    }
  }

  class ActivityUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      if (params.length == 0) {
        val prjService = AppContextUtil.getSpringBean(classOf[ProjectService])
        val prjKeys = prjService.getProjectKeysUserInvolved(AppContext.getUsername, AppContext.getAccountId)
        val searchCriteria = new ActivityStreamSearchCriteria()
        searchCriteria.setModuleSet(new SetSearchField(ModuleNameConstants.PRJ))
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
        searchCriteria.setExtraTypeIds(new SetSearchField(prjKeys))

        val data = new AllActivities(searchCriteria)
        EventBusFactory.getInstance().post(new ProjectEvent.AllActivities(this, data))
      }
      else {
        val projectId = new UrlTokenizer(params(0)).getInt
        val searchCriteria = new ActivityStreamSearchCriteria()
        searchCriteria.setModuleSet(new SetSearchField(ModuleNameConstants.PRJ));
        searchCriteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
        searchCriteria.setExtraTypeIds(new SetSearchField(projectId))
        val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectScreenData.ProjectActivities(searchCriteria))
        EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
      }
    }
  }

}
