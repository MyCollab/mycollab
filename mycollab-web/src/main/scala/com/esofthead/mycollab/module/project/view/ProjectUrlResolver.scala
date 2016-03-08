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
package com.esofthead.mycollab.module.project.view

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.module.project.events.ProjectEvent
import com.esofthead.mycollab.module.project.service.ProjectService
import com.esofthead.mycollab.module.project.view.bug.BugUrlResolver
import com.esofthead.mycollab.module.project.view.client.ClientUrlResolver
import com.esofthead.mycollab.module.project.view.file.ProjectFileUrlResolver
import com.esofthead.mycollab.module.project.view.message.MessageUrlResolver
import com.esofthead.mycollab.module.project.view.milestone.MilestoneUrlResolver
import com.esofthead.mycollab.module.project.view.page.PageUrlResolver
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData.{GotoCalendarView, GotoGanttChart}
import com.esofthead.mycollab.module.project.view.parameters.{MilestoneScreenData, ProjectScreenData}
import com.esofthead.mycollab.module.project.view.risk.RiskUrlResolver
import com.esofthead.mycollab.module.project.view.settings._
import com.esofthead.mycollab.module.project.view.standup.StandupUrlResolver
import com.esofthead.mycollab.module.project.view.task.ScheduleUrlResolver
import com.esofthead.mycollab.module.project.view.time.TimeUrlResolver
import com.esofthead.mycollab.shell.events.ShellEvent
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.{PageActionChain, UrlResolver}
import com.esofthead.mycollab.vaadin.web.ui.ModuleHelper

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class ProjectUrlResolver extends UrlResolver {
  def build: UrlResolver = {
    this.addSubResolver("dashboard", new ProjectDashboardUrlResolver)
    this.addSubResolver("edit", new ProjectEditUrlResolver)
    this.addSubResolver("tag", new ProjectTagUrlResolver)
    this.addSubResolver("gantt", new GanttUrlResolver)
    this.addSubResolver("message", new MessageUrlResolver)
    this.addSubResolver("milestone", new MilestoneUrlResolver)
    this.addSubResolver("task", new ScheduleUrlResolver)
    this.addSubResolver("bug", new BugUrlResolver)
    this.addSubResolver("page", new PageUrlResolver)
    this.addSubResolver("risk", new RiskUrlResolver)
    this.addSubResolver("standup", new StandupUrlResolver)
    this.addSubResolver("user", new UserUrlResolver)
    this.addSubResolver("role", new RoleUrlResolver)
    this.addSubResolver("setting", new SettingUrlResolver)
    this.addSubResolver("time", new TimeUrlResolver)
    this.addSubResolver("file", new ProjectFileUrlResolver)
    this.addSubResolver("component", new ComponentUrlResolver)
    this.addSubResolver("version", new VersionUrlResolver)
    this.addSubResolver("roadmap", new RoadmapUrlResolver)
    this.addSubResolver("calendar", new CalendarUrlResolver)
    this.addSubResolver("client", new ClientUrlResolver)
    return this
  }

  override def handle(params: String*) {
    if (!ModuleHelper.isCurrentProjectModule) {
      EventBusFactory.getInstance.post(new ShellEvent.GotoProjectModule(this, params))
    }
    else {
      super.handle(params: _*)
    }
  }

  protected def defaultPageErrorHandler {
    EventBusFactory.getInstance.post(new ShellEvent.GotoProjectModule(this, null))
  }

  class ProjectTagUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = new UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new ProjectScreenData.GotoTagList(null))
      EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  class ProjectDashboardUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      if (params.length == 0) {
        EventBusFactory.getInstance.post(new ShellEvent.GotoProjectModule(this, null))
      } else {
        val projectId = new UrlTokenizer(params(0)).getInt
        val chain = new PageActionChain(new ProjectScreenData.Goto(projectId))
        EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
      }
    }
  }

  class ProjectEditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      if (params.length == 0) {
        EventBusFactory.getInstance.post(new ShellEvent.GotoProjectModule(this, null))
      } else {
        val projectId = new UrlTokenizer(params(0)).getInt
        val prjService = ApplicationContextUtil.getSpringBean(classOf[ProjectService])
        val project = prjService.findById(projectId, AppContext.getAccountId)
        if (project != null) {
          val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectScreenData.Edit(project))
          EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
        }
      }
    }
  }

  class RoadmapUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      if (params.length == 0) {
        EventBusFactory.getInstance.post(new ShellEvent.GotoProjectModule(this, null))
      } else {
        val projectId = new UrlTokenizer(params(0)).getInt
        val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new MilestoneScreenData.Roadmap())
        EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
      }
    }
  }

  private class CalendarUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*): Unit = {
      if (params.size > 0) {
        val projectId = new UrlTokenizer(params(0)).getInt
        val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new GotoCalendarView)
        EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
      }
    }
  }

  private class GanttUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = new UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new GotoGanttChart)
      EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

}
