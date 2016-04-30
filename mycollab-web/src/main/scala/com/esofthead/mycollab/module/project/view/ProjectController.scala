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

import com.esofthead.mycollab.common.domain.Tag
import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.core.arguments._
import com.esofthead.mycollab.core.utils.{BeanUtility, StringUtils}
import com.esofthead.mycollab.eventmanager.ApplicationEventListener
import com.esofthead.mycollab.module.page.domain.Page
import com.esofthead.mycollab.module.project.domain._
import com.esofthead.mycollab.module.project.domain.criteria._
import com.esofthead.mycollab.module.project.events.InvoiceEvent.GotoList
import com.esofthead.mycollab.module.project.events._
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus
import com.esofthead.mycollab.module.project.view.bug.BugPresenter
import com.esofthead.mycollab.module.project.view.file.FilePresenter
import com.esofthead.mycollab.module.project.view.message.MessagePresenter
import com.esofthead.mycollab.module.project.view.milestone.MilestonePresenter
import com.esofthead.mycollab.module.project.view.page.PagePresenter
import com.esofthead.mycollab.module.project.view.parameters.MilestoneScreenData.Roadmap
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData.GotoFavorite
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData.GotoDashboard
import com.esofthead.mycollab.module.project.view.parameters._
import com.esofthead.mycollab.module.project.view.risk.IRiskPresenter
import com.esofthead.mycollab.module.project.view.settings.UserSettingPresenter
import com.esofthead.mycollab.module.project.view.task.TaskPresenter
import com.esofthead.mycollab.module.project.view.time.IInvoiceListPresenter
import com.esofthead.mycollab.module.project.view.user.ProjectDashboardPresenter
import com.esofthead.mycollab.module.project.{CurrentProjectVariables, ProjectMemberStatusConstants}
import com.esofthead.mycollab.module.tracker.domain.criteria.{BugSearchCriteria, ComponentSearchCriteria, VersionSearchCriteria}
import com.esofthead.mycollab.module.tracker.domain.{Component, SimpleBug, Version}
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.{AbstractController, PresenterResolver}
import com.google.common.eventbus.Subscribe

/**
  * @author MyCollab Ltd.
  * @since 5.0.3
  */
class ProjectController(val projectView: ProjectView) extends AbstractController {
  bindProjectEvents()
  bindTaskEvents()
  bindRiskEvents()
  bindBugEvents()
  bindMessageEvents()
  bindMilestoneEvents()
  bindUserGroupEvents()
  bindFileEvents()
  bindTimeandInvoiceEvents()
  bindPageEvents()

  private def bindProjectEvents(): Unit = {
    this.register(new ApplicationEventListener[ProjectEvent.GotoEdit] {
      @Subscribe def handle(event: ProjectEvent.GotoEdit) {
        val project = event.getData.asInstanceOf[SimpleProject]
        CurrentProjectVariables.setProject(project)
        val presenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
        presenter.go(projectView, new ProjectScreenData.Edit(project))
      }
    })

    this.register(new ApplicationEventListener[ProjectEvent.GotoTagListView] {
      @Subscribe def handle(event: ProjectEvent.GotoTagListView) {
        val tag = event.getData.asInstanceOf[Tag]
        val presenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
        presenter.go(projectView, new ProjectScreenData.GotoTagList(tag))
      }
    })

    this.register(new ApplicationEventListener[ProjectEvent.GotoFavoriteView] {
      @Subscribe def handle(event: ProjectEvent.GotoFavoriteView) {
        val presenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
        presenter.go(projectView, new GotoFavorite())
      }
    })

    this.register(new ApplicationEventListener[ProjectEvent.GotoCalendarView] {
      @Subscribe override def handle(event: ProjectEvent.GotoCalendarView): Unit = {
        val data = new ProjectScreenData.GotoCalendarView
        val presenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[ProjectEvent.GotoGanttChart] {
      @Subscribe def handle(event: ProjectEvent.GotoGanttChart) {
        val data = new ProjectScreenData.GotoGanttChart
        val presenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[ProjectEvent.GotoDashboard] {
      @Subscribe def handle(event: ProjectEvent.GotoDashboard) {
        val presenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
        presenter.go(projectView, null)
      }
    })
  }

  private def bindTaskEvents(): Unit = {
    this.register(new ApplicationEventListener[TaskEvent.GotoRead] {
      @Subscribe def handle(event: TaskEvent.GotoRead) {
        val data = new TaskScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[TaskEvent.GotoAdd] {
      @Subscribe def handle(event: TaskEvent.GotoAdd) {
        val param: Any = event.getData
        var data: TaskScreenData.Add = null
        if (param.isInstanceOf[SimpleTask]) {
          data = new TaskScreenData.Add(param.asInstanceOf[SimpleTask])
        }
        else {
          data = new TaskScreenData.Add(new SimpleTask)
        }
        val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[TaskEvent.GotoEdit] {
      @Subscribe def handle(event: TaskEvent.GotoEdit) {
        val data = new TaskScreenData.Edit(event.getData.asInstanceOf[SimpleTask])
        val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[TaskEvent.GotoKanbanView] {
      @Subscribe override def handle(event: TaskEvent.GotoKanbanView): Unit = {
        val data = new TaskScreenData.GotoKanbanView
        val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[TaskEvent.GotoDashboard] {
      @Subscribe def handle(event: TaskEvent.GotoDashboard) {
        val data = new GotoDashboard()
        val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
        presenter.go(projectView, data)
      }
    })
  }

  private def bindRiskEvents(): Unit = {
    this.register(new ApplicationEventListener[RiskEvent.GotoAdd] {
      @Subscribe def handle(event: RiskEvent.GotoAdd) {
        val param = event.getData
        var data: RiskScreenData.Add = null
        if (param.isInstanceOf[SimpleRisk]) {
          data = new RiskScreenData.Add(param.asInstanceOf[SimpleRisk])
        }
        else {
          data = new RiskScreenData.Add(new SimpleRisk)
        }
        val presenter = PresenterResolver.getPresenter(classOf[IRiskPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[RiskEvent.GotoEdit] {
      @Subscribe def handle(event: RiskEvent.GotoEdit) {
        val data = new RiskScreenData.Edit(event.getData.asInstanceOf[Risk])
        val presenter = PresenterResolver.getPresenter(classOf[IRiskPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[RiskEvent.GotoRead] {
      @Subscribe def handle(event: RiskEvent.GotoRead) {
        val data = new RiskScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[IRiskPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[RiskEvent.GotoList] {
      @Subscribe def handle(event: RiskEvent.GotoList) {
        val criteria = new RiskSearchCriteria
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
        val presenter = PresenterResolver.getPresenter(classOf[IRiskPresenter])
        presenter.go(projectView, new RiskScreenData.Search(criteria))
      }
    })
  }

  private def bindBugEvents(): Unit = {
    this.register(new ApplicationEventListener[BugEvent.GotoAdd] {
      @Subscribe def handle(event: BugEvent.GotoAdd) {
        val data = new BugScreenData.Add(new SimpleBug)
        val presenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugEvent.GotoEdit] {
      @Subscribe def handle(event: BugEvent.GotoEdit) {
        val data = new BugScreenData.Edit(event.getData.asInstanceOf[SimpleBug])
        val presenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugEvent.GotoRead] {
      @Subscribe def handle(event: BugEvent.GotoRead) {
        val data = new BugScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[BugEvent.GotoKanbanView] {
      @Subscribe override def handle(event: BugEvent.GotoKanbanView): Unit = {
        val data = new BugScreenData.GotoKanbanView
        val presenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[BugEvent.GotoList] {
      @Subscribe def handle(event: BugEvent.GotoList) {
        val params: Any = event.getData
        val presenter = PresenterResolver.getPresenter(classOf[BugPresenter])
        if (params == null) {
          val criteria = new BugSearchCriteria
          criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
          criteria.setStatuses(new SetSearchField[String](BugStatus.Open.name, BugStatus.ReOpen.name, BugStatus.Resolved.name))
          presenter.go(projectView, new BugScreenData.Search(criteria))
        }
        else if (params.isInstanceOf[BugSearchCriteria]) {
          presenter.go(projectView, new BugScreenData.Search(params.asInstanceOf[BugSearchCriteria]))
        }
        else {
          throw new MyCollabException("Invalid search parameter: " + BeanUtility.printBeanObj(params))
        }
      }
    })
    this.register(new ApplicationEventListener[BugComponentEvent.GotoAdd] {
      @Subscribe def handle(event: BugComponentEvent.GotoAdd) {
        val data = new ComponentScreenData.Add(new Component)
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugComponentEvent.GotoEdit] {
      @Subscribe def handle(event: BugComponentEvent.GotoEdit) {
        val data = new ComponentScreenData.Edit(event.getData.asInstanceOf[Component])
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugComponentEvent.GotoRead] {
      @Subscribe def handle(event: BugComponentEvent.GotoRead) {
        val data = new ComponentScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugComponentEvent.GotoList] {
      @Subscribe def handle(event: BugComponentEvent.GotoList) {
        val criteria = new ComponentSearchCriteria
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, new ComponentScreenData.Search(criteria))
      }
    })
    this.register(new ApplicationEventListener[BugVersionEvent.GotoAdd] {
      @Subscribe def handle(event: BugVersionEvent.GotoAdd) {
        val data = new VersionScreenData.Add(new Version)
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugVersionEvent.GotoEdit] {
      @Subscribe def handle(event: BugVersionEvent.GotoEdit) {
        val data = new VersionScreenData.Edit(event.getData.asInstanceOf[Version])
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugVersionEvent.GotoRead] {
      @Subscribe def handle(event: BugVersionEvent.GotoRead) {
        val data = new VersionScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[BugVersionEvent.GotoList] {
      @Subscribe def handle(event: BugVersionEvent.GotoList) {
        val criteria = new VersionSearchCriteria
        criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, new VersionScreenData.Search(criteria))
      }
    })
  }

  private def bindMessageEvents(): Unit = {
    this.register(new ApplicationEventListener[MessageEvent.GotoRead] {
      @Subscribe def handle(event: MessageEvent.GotoRead) {
        val data = new MessageScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[MessageEvent.GotoList] {
      @Subscribe def handle(event: MessageEvent.GotoList) {
        val searchCriteria = new MessageSearchCriteria
        searchCriteria.setProjectids(new SetSearchField[Integer](CurrentProjectVariables.getProjectId))
        val data = new MessageScreenData.Search(searchCriteria)
        val presenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
        presenter.go(projectView, data)
      }
    })
  }

  private def bindMilestoneEvents(): Unit = {
    this.register(new ApplicationEventListener[MilestoneEvent.GotoAdd] {
      @Subscribe def handle(event: MilestoneEvent.GotoAdd) {
        val data = new MilestoneScreenData.Add(new SimpleMilestone)
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[MilestoneEvent.GotoRead] {
      @Subscribe def handle(event: MilestoneEvent.GotoRead) {
        val data = new MilestoneScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[MilestoneEvent.GotoList] {
      @Subscribe def handle(event: MilestoneEvent.GotoList) {
        val criteria = new MilestoneSearchCriteria
        criteria.setProjectIds(new SetSearchField[Integer](CurrentProjectVariables.getProjectId))
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(projectView, new MilestoneScreenData.Search(criteria))
      }
    })

    this.register(new ApplicationEventListener[MilestoneEvent.GotoRoadmap] {
      @Subscribe def handle(event: MilestoneEvent.GotoRoadmap) {
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(projectView, new Roadmap())
      }
    })

    this.register(new ApplicationEventListener[MilestoneEvent.GotoEdit] {
      @Subscribe def handle(event: MilestoneEvent.GotoEdit) {
        val data = new MilestoneScreenData.Edit(event.getData.asInstanceOf[Milestone])
        val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
        presenter.go(projectView, data)
      }
    })
  }

  private def bindUserGroupEvents(): Unit = {
    this.register(new ApplicationEventListener[ProjectRoleEvent.GotoList] {
      @Subscribe def handle(event: ProjectRoleEvent.GotoList) {
        val project = CurrentProjectVariables.getProject
        val criteria = new ProjectRoleSearchCriteria
        criteria.setProjectId(new NumberSearchField(project.getId))
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, new ProjectRoleScreenData.Search(criteria))
      }
    })
    this.register(new ApplicationEventListener[ProjectRoleEvent.GotoAdd] {
      @Subscribe def handle(event: ProjectRoleEvent.GotoAdd) {
        val data = new ProjectRoleScreenData.Add(new ProjectRole)
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectRoleEvent.GotoEdit] {
      @Subscribe def handle(event: ProjectRoleEvent.GotoEdit) {
        val data = new ProjectRoleScreenData.Add(event.getData.asInstanceOf[ProjectRole])
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectRoleEvent.GotoRead] {
      @Subscribe def handle(event: ProjectRoleEvent.GotoRead) {
        val data = new ProjectRoleScreenData.Read(event.getData.asInstanceOf[Integer])
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoList] {
      @Subscribe def handle(event: ProjectMemberEvent.GotoList) {
        val project = CurrentProjectVariables.getProject
        val criteria = new ProjectMemberSearchCriteria
        criteria.setProjectId(new NumberSearchField(project.getId))
        criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
        criteria.setStatus(StringSearchField.and(ProjectMemberStatusConstants.ACTIVE))
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, new ProjectMemberScreenData.Search(criteria))
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoRead] {
      @Subscribe def handle(event: ProjectMemberEvent.GotoRead) {
        val data = new ProjectMemberScreenData.Read(event.getData)
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoInviteMembers] {
      @Subscribe def handle(event: ProjectMemberEvent.GotoInviteMembers) {
        val data = new ProjectMemberScreenData.InviteProjectMembers
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[ProjectMemberEvent.GotoEdit] {
      @Subscribe def handle(event: ProjectMemberEvent.GotoEdit) {
        val data = new ProjectMemberScreenData.Add(event.getData.asInstanceOf[ProjectMember])
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[ProjectNotificationEvent.GotoList] {
      @Subscribe def handle(event: ProjectNotificationEvent.GotoList) {
        val data = new ProjectSettingScreenData.ViewSettings()
        val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
        presenter.go(projectView, data)
      }
    })

    this.register(new ApplicationEventListener[CustomizeUIEvent.UpdateFeaturesList] {
      @Subscribe def handle(event: CustomizeUIEvent.UpdateFeaturesList) {
        projectView.updateProjectFeatures()
      }
    })
  }

  private def bindFileEvents(): Unit = {
    this.register(new ApplicationEventListener[ProjectContentEvent.GotoDashboard] {
      @Subscribe def handle(event: ProjectContentEvent.GotoDashboard) {
        val presenter = PresenterResolver.getPresenter(classOf[FilePresenter])
        presenter.go(projectView, new FileScreenData.GotoDashboard)
      }
    })
  }

  private def bindTimeandInvoiceEvents(): Unit = {
    this.register(new ApplicationEventListener[InvoiceEvent.GotoList] {
      override def handle(event: GotoList): Unit = {
        val presenter = PresenterResolver.getPresenter(classOf[IInvoiceListPresenter])
        presenter.go(projectView, null)
      }
    })
  }

  private def bindPageEvents(): Unit = {
    this.register(new ApplicationEventListener[PageEvent.GotoAdd] {
      @Subscribe def handle(event: PageEvent.GotoAdd) {
        var pagePath = event.getData.asInstanceOf[String]
        if ("".equals(pagePath) || pagePath == null) {
          pagePath = CurrentProjectVariables.getCurrentPagePath + "/" + StringUtils.generateSoftUniqueId
        }
        val page = new Page
        page.setPath(pagePath)
        val data = new PageScreenData.Add(page)
        val presenter = PresenterResolver.getPresenter(classOf[PagePresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[PageEvent.GotoEdit] {
      @Subscribe def handle(event: PageEvent.GotoEdit) {
        val data = new PageScreenData.Edit(event.getData.asInstanceOf[Page])
        val presenter = PresenterResolver.getPresenter(classOf[PagePresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[PageEvent.GotoRead] {
      @Subscribe def handle(event: PageEvent.GotoRead) {
        val data = new PageScreenData.Read(event.getData.asInstanceOf[Page])
        val presenter = PresenterResolver.getPresenter(classOf[PagePresenter])
        presenter.go(projectView, data)
      }
    })
    this.register(new ApplicationEventListener[PageEvent.GotoList] {
      @Subscribe def handle(event: PageEvent.GotoList) {
        val presenter = PresenterResolver.getPresenter(classOf[PagePresenter])
        presenter.go(projectView, new PageScreenData.Search(event.getData.asInstanceOf[String]))
      }
    })
  }
}
