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

import java.util.GregorianCalendar

import com.esofthead.mycollab.common.domain.Tag
import com.esofthead.mycollab.core.MyCollabException
import com.esofthead.mycollab.core.arguments._
import com.esofthead.mycollab.core.utils.{BeanUtility, StringUtils}
import com.esofthead.mycollab.eventmanager.ApplicationEventListener
import com.esofthead.mycollab.module.page.domain.Page
import com.esofthead.mycollab.module.project.domain._
import com.esofthead.mycollab.module.project.domain.criteria._
import com.esofthead.mycollab.module.project.events._
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus
import com.esofthead.mycollab.module.project.service.StandupReportService
import com.esofthead.mycollab.module.project.view.bug.TrackerPresenter
import com.esofthead.mycollab.module.project.view.file.FilePresenter
import com.esofthead.mycollab.module.project.view.message.MessagePresenter
import com.esofthead.mycollab.module.project.view.milestone.MilestonePresenter
import com.esofthead.mycollab.module.project.view.page.PagePresenter
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData.SearchItem
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData.GotoDashboard
import com.esofthead.mycollab.module.project.view.parameters._
import com.esofthead.mycollab.module.project.view.problem.IProblemPresenter
import com.esofthead.mycollab.module.project.view.risk.IRiskPresenter
import com.esofthead.mycollab.module.project.view.settings.UserSettingPresenter
import com.esofthead.mycollab.module.project.view.standup.IStandupPresenter
import com.esofthead.mycollab.module.project.view.task.TaskPresenter
import com.esofthead.mycollab.module.project.view.user.ProjectDashboardPresenter
import com.esofthead.mycollab.module.project.{CurrentProjectVariables, ProjectMemberStatusConstants}
import com.esofthead.mycollab.module.tracker.domain.criteria.{BugSearchCriteria, ComponentSearchCriteria, VersionSearchCriteria}
import com.esofthead.mycollab.module.tracker.domain.{Component, SimpleBug, Version}
import com.esofthead.mycollab.spring.ApplicationContextUtil
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
    bindProblemEvents()
    bindBugEvents()
    bindMessageEvents()
    bindMilestoneEvents()
    bindStandupEvents()
    bindUserGroupEvents()
    bindFileEvents()
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

        this.register(new ApplicationEventListener[ProjectEvent.GotoProjectSearchItemsView] {
            @Subscribe def handle(event: ProjectEvent.GotoProjectSearchItemsView): Unit = {
                val value = event.getData.asInstanceOf[String]
                val presenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
                presenter.go(projectView, new SearchItem(value))
            }
        })
    }

    private def bindTaskEvents(): Unit = {
        this.register(new ApplicationEventListener[TaskEvent.GotoRead] {
            @Subscribe def handle(event: TaskEvent.GotoRead) {
                val data: TaskScreenData.Read = new TaskScreenData.Read(event.getData.asInstanceOf[Integer])
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
                val data: TaskScreenData.Edit = new TaskScreenData.Edit(event.getData.asInstanceOf[SimpleTask])
                val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[TaskEvent.GotoGanttChart] {
            @Subscribe def handle(event: TaskEvent.GotoGanttChart) {
                val data: TaskScreenData.GotoGanttChart = new TaskScreenData.GotoGanttChart
                val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[TaskEvent.GotoKanbanView] {
            @Subscribe override def handle(event: TaskEvent.GotoKanbanView): Unit = {
                val data: TaskScreenData.GotoKanbanView = new TaskScreenData.GotoKanbanView
                val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
                presenter.go(projectView, data)
            }
        })

        this.register(new ApplicationEventListener[TaskEvent.GotoDashboard] {
            @Subscribe def handle(event: TaskEvent.GotoDashboard) {
                val data: TaskScreenData.GotoDashboard = new GotoDashboard()
                val presenter = PresenterResolver.getPresenter(classOf[TaskPresenter])
                presenter.go(projectView, data)
            }
        })
    }

    private def bindRiskEvents(): Unit = {
        this.register(new ApplicationEventListener[RiskEvent.GotoAdd] {
            @Subscribe def handle(event: RiskEvent.GotoAdd) {
                val data: RiskScreenData.Add = new RiskScreenData.Add(new Risk)
                val presenter = PresenterResolver.getPresenter(classOf[IRiskPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[RiskEvent.GotoEdit] {
            @Subscribe def handle(event: RiskEvent.GotoEdit) {
                val data: RiskScreenData.Edit = new RiskScreenData.Edit(event.getData.asInstanceOf[Risk])
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

    private def bindProblemEvents(): Unit = {
        this.register(new ApplicationEventListener[ProblemEvent.GotoAdd] {
            @Subscribe def handle(event: ProblemEvent.GotoAdd) {
                val data: ProblemScreenData.Add = new ProblemScreenData.Add(new Problem)
                val presenter: IProblemPresenter = PresenterResolver.getPresenter(classOf[IProblemPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[ProblemEvent.GotoRead] {
            @Subscribe def handle(event: ProblemEvent.GotoRead) {
                val data: ProblemScreenData.Read = new ProblemScreenData.Read(event.getData.asInstanceOf[Integer])
                val presenter: IProblemPresenter = PresenterResolver.getPresenter(classOf[IProblemPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[ProblemEvent.GotoList] {
            @Subscribe def handle(event: ProblemEvent.GotoList) {
                val criteria = new ProblemSearchCriteria
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
                val data = new ProblemScreenData.Search(criteria)
                val presenter = PresenterResolver.getPresenter(classOf[IProblemPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[ProblemEvent.GotoEdit] {
            @Subscribe def handle(event: ProblemEvent.GotoEdit) {
                val data: ProblemScreenData.Edit = new ProblemScreenData.Edit(event.getData.asInstanceOf[Problem])
                val presenter: IProblemPresenter = PresenterResolver.getPresenter(classOf[IProblemPresenter])
                presenter.go(projectView, data)
            }
        })
    }

    private def bindBugEvents(): Unit = {
        this.register(new ApplicationEventListener[BugEvent.GotoDashboard] {
            @Subscribe def handle(event: BugEvent.GotoDashboard) {
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, null)
            }
        })
        this.register(new ApplicationEventListener[BugEvent.GotoAdd] {
            @Subscribe def handle(event: BugEvent.GotoAdd) {
                val data: BugScreenData.Add = new BugScreenData.Add(new SimpleBug)
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugEvent.GotoEdit] {
            @Subscribe def handle(event: BugEvent.GotoEdit) {
                val data: BugScreenData.Edit = new BugScreenData.Edit(event.getData.asInstanceOf[SimpleBug])
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugEvent.GotoRead] {
            @Subscribe def handle(event: BugEvent.GotoRead) {
                val data: BugScreenData.Read = new BugScreenData.Read(event.getData.asInstanceOf[Integer])
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })

        this.register(new ApplicationEventListener[BugEvent.GotoKanbanView] {
            @Subscribe override def handle(event: BugEvent.GotoKanbanView): Unit = {
                val data: BugScreenData.GotoKanbanView = new BugScreenData.GotoKanbanView
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })

        this.register(new ApplicationEventListener[BugEvent.GotoList] {
            @Subscribe def handle(event: BugEvent.GotoList) {
                val params: Any = event.getData
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                if (params == null) {
                    val criteria: BugSearchCriteria = new BugSearchCriteria
                    criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
                    criteria.setStatuses(new SetSearchField[String](BugStatus.InProgress.name,
                        BugStatus.Open.name, BugStatus.ReOpened.name, BugStatus.Resolved.name))
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
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugComponentEvent.GotoEdit] {
            @Subscribe def handle(event: BugComponentEvent.GotoEdit) {
                val data = new ComponentScreenData.Edit(event.getData.asInstanceOf[Component])
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugComponentEvent.GotoRead] {
            @Subscribe def handle(event: BugComponentEvent.GotoRead) {
                val data = new ComponentScreenData.Read(event.getData.asInstanceOf[Integer])
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugComponentEvent.GotoList] {
            @Subscribe def handle(event: BugComponentEvent.GotoList) {
                val criteria: ComponentSearchCriteria = new ComponentSearchCriteria
                criteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId))
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, new ComponentScreenData.Search(criteria))
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoAdd] {
            @Subscribe def handle(event: BugVersionEvent.GotoAdd) {
                val data: VersionScreenData.Add = new VersionScreenData.Add(new Version)
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoEdit] {
            @Subscribe def handle(event: BugVersionEvent.GotoEdit) {
                val data: VersionScreenData.Edit = new VersionScreenData.Edit(event.getData.asInstanceOf[Version])
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoRead] {
            @Subscribe def handle(event: BugVersionEvent.GotoRead) {
                val data: VersionScreenData.Read = new VersionScreenData.Read(event.getData.asInstanceOf[Integer])
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoList] {
            @Subscribe def handle(event: BugVersionEvent.GotoList) {
                val criteria: VersionSearchCriteria = new VersionSearchCriteria
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
                val presenter = PresenterResolver.getPresenter(classOf[TrackerPresenter])
                presenter.go(projectView, new VersionScreenData.Search(criteria))
            }
        })
    }

    private def bindMessageEvents(): Unit = {
        this.register(new ApplicationEventListener[MessageEvent.GotoRead] {
            @Subscribe def handle(event: MessageEvent.GotoRead) {
                val data: MessageScreenData.Read = new MessageScreenData.Read(event.getData.asInstanceOf[Integer])
                val presenter: MessagePresenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[MessageEvent.GotoList] {
            @Subscribe def handle(event: MessageEvent.GotoList) {
                val searchCriteria: MessageSearchCriteria = new MessageSearchCriteria
                searchCriteria.setProjectids(new SetSearchField[Integer](CurrentProjectVariables.getProjectId))
                val data: MessageScreenData.Search = new MessageScreenData.Search(searchCriteria)
                val presenter: MessagePresenter = PresenterResolver.getPresenter(classOf[MessagePresenter])
                presenter.go(projectView, data)
            }
        })
    }

    private def bindMilestoneEvents(): Unit = {
        this.register(new ApplicationEventListener[MilestoneEvent.GotoAdd] {
            @Subscribe def handle(event: MilestoneEvent.GotoAdd) {
                val data: MilestoneScreenData.Add = new MilestoneScreenData.Add(new Milestone)
                val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[MilestoneEvent.GotoRead] {
            @Subscribe def handle(event: MilestoneEvent.GotoRead) {
                val data: MilestoneScreenData.Read = new MilestoneScreenData.Read(event.getData.asInstanceOf[Integer])
                val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[MilestoneEvent.GotoList] {
            @Subscribe def handle(event: MilestoneEvent.GotoList) {
                val criteria = new MilestoneSearchCriteria
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
                val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
                presenter.go(projectView, new MilestoneScreenData.Search(criteria))
            }
        })
        this.register(new ApplicationEventListener[MilestoneEvent.GotoEdit] {
            @Subscribe def handle(event: MilestoneEvent.GotoEdit) {
                val data: MilestoneScreenData.Edit = new MilestoneScreenData.Edit(event.getData.asInstanceOf[Milestone])
                val presenter = PresenterResolver.getPresenter(classOf[MilestonePresenter])
                presenter.go(projectView, data)
            }
        })
    }

    private def bindStandupEvents(): Unit = {
        this.register(new ApplicationEventListener[StandUpEvent.GotoAdd] {
            @Subscribe def handle(event: StandUpEvent.GotoAdd) {
                val reportService: StandupReportService = ApplicationContextUtil.getSpringBean(classOf[StandupReportService])
                var report: SimpleStandupReport = reportService.findStandupReportByDateUser(CurrentProjectVariables.getProjectId, AppContext.getUsername, new GregorianCalendar().getTime, AppContext.getAccountId)
                if (report == null) {
                    report = new SimpleStandupReport
                }
                val data: StandupScreenData.Add = new StandupScreenData.Add(report)
                val presenter = PresenterResolver.getPresenter(classOf[IStandupPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[StandUpEvent.GotoList] {
            @Subscribe def handle(event: StandUpEvent.GotoList) {
                val criteria = new StandupReportSearchCriteria
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
                criteria.setOnDate(new DateSearchField(new GregorianCalendar().getTime))
                val presenter = PresenterResolver.getPresenter(classOf[IStandupPresenter])
                presenter.go(projectView, new StandupScreenData.Search(criteria))
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
                val data: ProjectRoleScreenData.Read = new ProjectRoleScreenData.Read(event.getData.asInstanceOf[Integer])
                val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoList] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoList) {
                val project: SimpleProject = CurrentProjectVariables.getProject
                val criteria: ProjectMemberSearchCriteria = new ProjectMemberSearchCriteria
                criteria.setProjectId(new NumberSearchField(project.getId))
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
                criteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE))
                val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
                presenter.go(projectView, new ProjectMemberScreenData.Search(criteria))
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoRead] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoRead) {
                val data: ProjectMemberScreenData.Read = new ProjectMemberScreenData.Read(event.getData)
                val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoInviteMembers] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoInviteMembers) {
                val data: ProjectMemberScreenData.InviteProjectMembers = new ProjectMemberScreenData.InviteProjectMembers
                val presenter = PresenterResolver.getPresenter(classOf[UserSettingPresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoEdit] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoEdit) {
                val data: ProjectMemberScreenData.Add = new ProjectMemberScreenData.Add(event.getData.asInstanceOf[ProjectMember])
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
                val presenter: FilePresenter = PresenterResolver.getPresenter(classOf[FilePresenter])
                presenter.go(projectView, new FileScreenData.GotoDashboard)
            }
        })
    }

    private def bindPageEvents(): Unit = {
        this.register(new ApplicationEventListener[PageEvent.GotoAdd] {
            @Subscribe def handle(event: PageEvent.GotoAdd) {
                var pagePath: String = event.getData.asInstanceOf[String]
                if ("".equals(pagePath) || pagePath == null) {
                    pagePath = CurrentProjectVariables.getCurrentPagePath + "/" + StringUtils.generateSoftUniqueId
                }
                val page: Page = new Page
                page.setPath(pagePath)
                val data: PageScreenData.Add = new PageScreenData.Add(page)
                val presenter = PresenterResolver.getPresenter(classOf[PagePresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[PageEvent.GotoEdit] {
            @Subscribe def handle(event: PageEvent.GotoEdit) {
                val data: PageScreenData.Edit = new PageScreenData.Edit(event.getData.asInstanceOf[Page])
                val presenter = PresenterResolver.getPresenter(classOf[PagePresenter])
                presenter.go(projectView, data)
            }
        })
        this.register(new ApplicationEventListener[PageEvent.GotoRead] {
            @Subscribe def handle(event: PageEvent.GotoRead) {
                val data: PageScreenData.Read = new PageScreenData.Read(event.getData.asInstanceOf[Page])
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
