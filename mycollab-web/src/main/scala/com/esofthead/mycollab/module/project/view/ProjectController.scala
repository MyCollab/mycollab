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
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum
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
import com.esofthead.mycollab.module.project.view.file.FilePresenter
import com.esofthead.mycollab.module.project.view.message.MessagePresenter
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData.SearchItem
import com.esofthead.mycollab.module.project.view.parameters._
import com.esofthead.mycollab.module.project.view.problem.IProblemPresenter
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
    bindTaskListEvents()
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
                val project: SimpleProject = event.getData.asInstanceOf[SimpleProject]
                CurrentProjectVariables.setProject(project)
                val presenter: ProjectDashboardPresenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
                presenter.go(projectView, new ProjectScreenData.Edit(project))
            }
        })

        this.register(new ApplicationEventListener[ProjectEvent.GotoTagListView] {
            @Subscribe def handle(event: ProjectEvent.GotoTagListView) {
                val tag: Tag = event.getData.asInstanceOf[Tag]
                val presenter: ProjectDashboardPresenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
                presenter.go(projectView, new ProjectScreenData.GotoTagList(tag))
            }
        })

        this.register(new ApplicationEventListener[ProjectEvent.GotoProjectSearchItemsView] {
            @Subscribe def handle(event: ProjectEvent.GotoProjectSearchItemsView): Unit = {
                val value = event.getData.asInstanceOf[String]
                val presenter: ProjectDashboardPresenter = PresenterResolver.getPresenter(classOf[ProjectDashboardPresenter])
                presenter.go(projectView, new SearchItem(value))
            }
        })
    }

    private def bindTaskListEvents(): Unit = {
        this.register(new ApplicationEventListener[TaskListEvent.GotoRead] {
            @Subscribe def handle(event: TaskListEvent.GotoRead) {
                val data: TaskGroupScreenData.Read = new TaskGroupScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoTaskList(data)
            }
        })

        this.register(new ApplicationEventListener[TaskListEvent.GotoEdit] {
            @Subscribe def handle(event: TaskListEvent.GotoEdit) {
                val data: TaskGroupScreenData.Edit = new TaskGroupScreenData.Edit(event.getData.asInstanceOf[TaskList])
                projectView.gotoTaskList(data)
            }
        })

        this.register(new ApplicationEventListener[TaskListEvent.GotoAdd] {
            @Subscribe def handle(event: TaskListEvent.GotoAdd) {
                val taskList: TaskList = new TaskList
                taskList.setProjectid(CurrentProjectVariables.getProjectId)
                taskList.setStatus(StatusI18nEnum.Open.name)
                val data: TaskGroupScreenData.Add = new TaskGroupScreenData.Add(taskList)
                projectView.gotoTaskList(data)
            }
        })
        this.register(new ApplicationEventListener[TaskListEvent.GotoTaskListScreen] {
            @Subscribe def handle(event: TaskListEvent.GotoTaskListScreen) {
                projectView.gotoTaskList(null)
            }
        })
        this.register(new ApplicationEventListener[TaskListEvent.ReoderTaskList] {
            @Subscribe def handle(event: TaskListEvent.ReoderTaskList) {
                val data: TaskGroupScreenData.ReorderTaskListRequest = new TaskGroupScreenData.ReorderTaskListRequest
                projectView.gotoTaskList(data)
            }
        })
        this.register(new ApplicationEventListener[TaskListEvent.GotoGanttChartView] {
            @Subscribe def handle(event: TaskListEvent.GotoGanttChartView) {
                val data: TaskGroupScreenData.GotoGanttChartView = new TaskGroupScreenData.GotoGanttChartView
                projectView.gotoTaskList(data)
            }
        })
    }

    private def bindTaskEvents(): Unit = {
        this.register(new ApplicationEventListener[TaskEvent.GotoRead] {
            @Subscribe def handle(event: TaskEvent.GotoRead) {
                val data: TaskScreenData.Read = new TaskScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoTaskList(data)
            }
        })
        this.register(new ApplicationEventListener[TaskEvent.GotoAdd] {
            @Subscribe def handle(event: TaskEvent.GotoAdd) {
                val param: AnyRef = event.getData
                var data: TaskScreenData.Add = null
                if (param.isInstanceOf[SimpleTask]) {
                    data = new TaskScreenData.Add(param.asInstanceOf[SimpleTask])
                }
                else {
                    data = new TaskScreenData.Add(new SimpleTask)
                }
                projectView.gotoTaskList(data)
            }
        })
        this.register(new ApplicationEventListener[TaskEvent.GotoEdit] {
            @Subscribe def handle(event: TaskEvent.GotoEdit) {
                val data: TaskScreenData.Edit = new TaskScreenData.Edit(event.getData.asInstanceOf[SimpleTask])
                projectView.gotoTaskList(data)
            }
        })
        this.register(new ApplicationEventListener[TaskEvent.GotoGanttChart] {
            @Subscribe def handle(event: TaskEvent.GotoGanttChart) {
                val data: TaskScreenData.GanttChart = new TaskScreenData.GanttChart
                projectView.gotoTaskList(data)
            }
        })
        this.register(new ApplicationEventListener[TaskEvent.Search] {
            @Subscribe def handle(event: TaskEvent.Search) {
                val data: TaskScreenData.Search = new TaskScreenData.Search(event.getData.asInstanceOf[TaskFilterParameter])
                projectView.gotoTaskList(data)
            }
        })
    }

    private def bindRiskEvents(): Unit = {
        this.register(new ApplicationEventListener[RiskEvent.GotoAdd] {
            @Subscribe def handle(event: RiskEvent.GotoAdd) {
                val data: RiskScreenData.Add = new RiskScreenData.Add(new Risk)
                projectView.gotoRiskView(data)
            }
        })
        this.register(new ApplicationEventListener[RiskEvent.GotoEdit] {
            @Subscribe def handle(event: RiskEvent.GotoEdit) {
                val data: RiskScreenData.Edit = new RiskScreenData.Edit(event.getData.asInstanceOf[Risk])
                projectView.gotoRiskView(data)
            }
        })
        this.register(new ApplicationEventListener[RiskEvent.GotoRead] {
            @Subscribe def handle(event: RiskEvent.GotoRead) {
                val data: RiskScreenData.Read = new RiskScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoRiskView(data)
            }
        })
        this.register(new ApplicationEventListener[RiskEvent.GotoList] {
            @Subscribe def handle(event: RiskEvent.GotoList) {
                val criteria: RiskSearchCriteria = new RiskSearchCriteria
                criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
                projectView.gotoRiskView(new RiskScreenData.Search(criteria))
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
                val criteria: ProblemSearchCriteria = new ProblemSearchCriteria
                criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
                val data: ProblemScreenData.Search = new ProblemScreenData.Search(criteria)
                val presenter: IProblemPresenter = PresenterResolver.getPresenter(classOf[IProblemPresenter])
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
                projectView.gotoBugView(null)
            }
        })
        this.register(new ApplicationEventListener[BugEvent.GotoAdd] {
            @Subscribe def handle(event: BugEvent.GotoAdd) {
                val data: BugScreenData.Add = new BugScreenData.Add(new SimpleBug)
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugEvent.GotoEdit] {
            @Subscribe def handle(event: BugEvent.GotoEdit) {
                val data: BugScreenData.Edit = new BugScreenData.Edit(event.getData.asInstanceOf[SimpleBug])
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugEvent.GotoRead] {
            @Subscribe def handle(event: BugEvent.GotoRead) {
                val data: BugScreenData.Read = new BugScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugEvent.GotoList] {
            @Subscribe def handle(event: BugEvent.GotoList) {
                val params: AnyRef = event.getData
                if (params == null) {
                    val criteria: BugSearchCriteria = new BugSearchCriteria
                    criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
                    criteria.setStatuses(new SetSearchField[String](SearchField.AND, Array[String](BugStatus.InProgress.name,
                        BugStatus.Open.name, BugStatus.ReOpened.name): _*))
                    val parameter: BugFilterParameter = new BugFilterParameter("Open Bugs", criteria)
                    projectView.gotoBugView(new BugScreenData.Search(parameter))
                }
                else if (params.isInstanceOf[BugScreenData.Search]) {
                    projectView.gotoBugView(params.asInstanceOf[BugScreenData.Search])
                }
                else {
                    throw new MyCollabException("Invalid search parameter: " + BeanUtility.printBeanObj(params))
                }
            }
        })
        this.register(new ApplicationEventListener[BugComponentEvent.GotoAdd] {
            @Subscribe def handle(event: BugComponentEvent.GotoAdd) {
                val data: ComponentScreenData.Add = new ComponentScreenData.Add(new Component)
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugComponentEvent.GotoEdit] {
            @Subscribe def handle(event: BugComponentEvent.GotoEdit) {
                val data: ComponentScreenData.Edit = new ComponentScreenData.Edit(event.getData.asInstanceOf[Component])
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugComponentEvent.GotoRead] {
            @Subscribe def handle(event: BugComponentEvent.GotoRead) {
                val data: ComponentScreenData.Read = new ComponentScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugComponentEvent.GotoList] {
            @Subscribe def handle(event: BugComponentEvent.GotoList) {
                val criteria: ComponentSearchCriteria = new ComponentSearchCriteria
                criteria.setProjectid(new NumberSearchField(CurrentProjectVariables.getProjectId))
                projectView.gotoBugView(new ComponentScreenData.Search(criteria))
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoAdd] {
            @Subscribe def handle(event: BugVersionEvent.GotoAdd) {
                val data: VersionScreenData.Add = new VersionScreenData.Add(new Version)
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoEdit] {
            @Subscribe def handle(event: BugVersionEvent.GotoEdit) {
                val data: VersionScreenData.Edit = new VersionScreenData.Edit(event.getData.asInstanceOf[Version])
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoRead] {
            @Subscribe def handle(event: BugVersionEvent.GotoRead) {
                val data: VersionScreenData.Read = new VersionScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoBugView(data)
            }
        })
        this.register(new ApplicationEventListener[BugVersionEvent.GotoList] {
            @Subscribe def handle(event: BugVersionEvent.GotoList) {
                val criteria: VersionSearchCriteria = new VersionSearchCriteria
                criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId))
                projectView.gotoBugView(new VersionScreenData.Search(criteria))
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
                projectView.gotoMilestoneView(data)
            }
        })
        this.register(new ApplicationEventListener[MilestoneEvent.GotoRead] {
            @Subscribe def handle(event: MilestoneEvent.GotoRead) {
                val data: MilestoneScreenData.Read = new MilestoneScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoMilestoneView(data)
            }
        })
        this.register(new ApplicationEventListener[MilestoneEvent.GotoList] {
            @Subscribe def handle(event: MilestoneEvent.GotoList) {
                val criteria: MilestoneSearchCriteria = new MilestoneSearchCriteria
                criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
                projectView.gotoMilestoneView(new MilestoneScreenData.Search(criteria))
            }
        })
        this.register(new ApplicationEventListener[MilestoneEvent.GotoEdit] {
            @Subscribe def handle(event: MilestoneEvent.GotoEdit) {
                val data: MilestoneScreenData.Edit = new MilestoneScreenData.Edit(event.getData.asInstanceOf[Milestone])
                projectView.gotoMilestoneView(data)
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
                projectView.gotoStandupReportView(data)
            }
        })
        this.register(new ApplicationEventListener[StandUpEvent.GotoList] {
            @Subscribe def handle(event: StandUpEvent.GotoList) {
                val criteria: StandupReportSearchCriteria = new StandupReportSearchCriteria
                criteria.setProjectId(new NumberSearchField(SearchField.AND, CurrentProjectVariables.getProjectId))
                criteria.setOnDate(new DateSearchField(SearchField.AND, new GregorianCalendar().getTime))
                projectView.gotoStandupReportView(new StandupScreenData.Search(criteria))
            }
        })
    }

    private def bindUserGroupEvents(): Unit = {
        this.register(new ApplicationEventListener[ProjectRoleEvent.GotoList] {
            @Subscribe def handle(event: ProjectRoleEvent.GotoList) {
                val project: SimpleProject = CurrentProjectVariables.getProject
                val criteria: ProjectRoleSearchCriteria = new ProjectRoleSearchCriteria
                criteria.setProjectId(new NumberSearchField(project.getId))
                projectView.gotoUsersAndGroup(new ProjectRoleScreenData.Search(criteria))
            }
        })
        this.register(new ApplicationEventListener[ProjectRoleEvent.GotoAdd] {
            @Subscribe def handle(event: ProjectRoleEvent.GotoAdd) {
                val data: ProjectRoleScreenData.Add = new ProjectRoleScreenData.Add(new ProjectRole)
                projectView.gotoUsersAndGroup(data)
            }
        })
        this.register(new ApplicationEventListener[ProjectRoleEvent.GotoEdit] {
            @Subscribe def handle(event: ProjectRoleEvent.GotoEdit) {
                val data: ProjectRoleScreenData.Add = new ProjectRoleScreenData.Add(event.getData.asInstanceOf[ProjectRole])
                projectView.gotoUsersAndGroup(data)
            }
        })
        this.register(new ApplicationEventListener[ProjectRoleEvent.GotoRead] {
            @Subscribe def handle(event: ProjectRoleEvent.GotoRead) {
                val data: ProjectRoleScreenData.Read = new ProjectRoleScreenData.Read(event.getData.asInstanceOf[Integer])
                projectView.gotoUsersAndGroup(data)
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoList] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoList) {
                val project: SimpleProject = CurrentProjectVariables.getProject
                val criteria: ProjectMemberSearchCriteria = new ProjectMemberSearchCriteria
                criteria.setProjectId(new NumberSearchField(project.getId))
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId))
                criteria.setStatus(new StringSearchField(ProjectMemberStatusConstants.ACTIVE))
                projectView.gotoUsersAndGroup(new ProjectMemberScreenData.Search(criteria))
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoRead] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoRead) {
                val data: ProjectMemberScreenData.Read = new ProjectMemberScreenData.Read(event.getData)
                projectView.gotoUsersAndGroup(data)
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoInviteMembers] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoInviteMembers) {
                val data: ProjectMemberScreenData.InviteProjectMembers = new ProjectMemberScreenData.InviteProjectMembers
                projectView.gotoUsersAndGroup(data)
            }
        })
        this.register(new ApplicationEventListener[ProjectMemberEvent.GotoEdit] {
            @Subscribe def handle(event: ProjectMemberEvent.GotoEdit) {
                val data: ProjectMemberScreenData.Add = new ProjectMemberScreenData.Add(event.getData.asInstanceOf[ProjectMember])
                projectView.gotoUsersAndGroup(data)
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
                projectView.gotoPageView(data)
            }
        })
        this.register(new ApplicationEventListener[PageEvent.GotoEdit] {
            @Subscribe def handle(event: PageEvent.GotoEdit) {
                val data: PageScreenData.Edit = new PageScreenData.Edit(event.getData.asInstanceOf[Page])
                projectView.gotoPageView(data)
            }
        })
        this.register(new ApplicationEventListener[PageEvent.GotoRead] {
            @Subscribe def handle(event: PageEvent.GotoRead) {
                val data: PageScreenData.Read = new PageScreenData.Read(event.getData.asInstanceOf[Page])
                projectView.gotoPageView(data)
            }
        })
        this.register(new ApplicationEventListener[PageEvent.GotoList] {
            @Subscribe def handle(event: PageEvent.GotoList) {
                projectView.gotoPageView(new PageScreenData.Search(event.getData.asInstanceOf[String]))
            }
        })
    }
}
