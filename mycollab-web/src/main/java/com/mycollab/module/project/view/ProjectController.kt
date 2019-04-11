/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view

import com.google.common.eventbus.Subscribe
import com.mycollab.common.domain.Tag
import com.mycollab.core.MyCollabException
import com.mycollab.core.ResourceNotFoundException
import com.mycollab.core.utils.StringUtils
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.module.page.domain.Page
import com.mycollab.module.project.CurrentProjectVariables
import com.mycollab.module.project.ProjectMemberStatusConstants
import com.mycollab.module.project.ProjectTypeConstants
import com.mycollab.module.project.dao.TicketKeyMapper
import com.mycollab.module.project.domain.*
import com.mycollab.module.project.domain.criteria.*
import com.mycollab.module.project.event.*
import com.mycollab.module.project.service.BugService
import com.mycollab.module.project.service.TaskService
import com.mycollab.module.project.service.RiskService
import com.mycollab.module.project.view.bug.BugAddPresenter
import com.mycollab.module.project.view.bug.BugReadPresenter
import com.mycollab.module.project.view.finance.IInvoiceListPresenter
import com.mycollab.module.project.view.message.MessageListPresenter
import com.mycollab.module.project.view.message.MessageReadPresenter
import com.mycollab.module.project.view.milestone.MilestoneAddPresenter
import com.mycollab.module.project.view.milestone.MilestoneListPresenter
import com.mycollab.module.project.view.milestone.MilestoneReadPresenter
import com.mycollab.module.project.view.milestone.MilestoneRoadmapPresenter
import com.mycollab.module.project.view.page.PageAddPresenter
import com.mycollab.module.project.view.page.PageListPresenter
import com.mycollab.module.project.view.page.PageReadPresenter
import com.mycollab.module.project.view.parameters.*
import com.mycollab.module.project.view.risk.IRiskAddPresenter
import com.mycollab.module.project.view.risk.IRiskReadPresenter
import com.mycollab.module.project.view.settings.*
import com.mycollab.module.project.view.task.TaskAddPresenter
import com.mycollab.module.project.view.task.TaskReadPresenter
import com.mycollab.module.project.view.ticket.ITicketKanbanPresenter
import com.mycollab.module.project.view.ticket.TicketDashboardPresenter
import com.mycollab.module.project.view.user.ProjectDashboardPresenter
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PresenterResolver

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ProjectController(val projectView: ProjectView) : AbstractController() {
    init {
        bindProjectEvents()
        bindTicketEvents()
        bindTaskEvents()
        bindRiskEvents()
        bindBugEvents()
        bindMessageEvents()
        bindMilestoneEvents()
        bindUserGroupEvents()
        bindTimeandInvoiceEvents()
        bindPageEvents()
    }

    private fun bindProjectEvents() {
        this.register(object : ApplicationEventListener<ProjectEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: ProjectEvent.GotoEdit) {
                val project = event.data as SimpleProject
                CurrentProjectVariables.project = project
                val presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter::class.java)
                presenter.go(projectView, ProjectScreenData.Edit(project))
            }
        })

        this.register(object : ApplicationEventListener<ProjectEvent.GotoTagListView> {
            @Subscribe
            override fun handle(event: ProjectEvent.GotoTagListView) {
                val tag = event.data as? Tag
                val presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter::class.java)
                presenter.go(projectView, ProjectScreenData.GotoTagList(tag))
            }
        })

        this.register(object : ApplicationEventListener<ProjectEvent.GotoFavoriteView> {
            @Subscribe
            override fun handle(event: ProjectEvent.GotoFavoriteView) {
                val presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter::class.java)
                presenter.go(projectView, ProjectScreenData.GotoFavorite())
            }
        })

        this.register(object : ApplicationEventListener<ProjectEvent.GotoDashboard> {
            @Subscribe
            override fun handle(event: ProjectEvent.GotoDashboard) {
                val presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter::class.java)
                presenter.go(projectView, null)
            }
        })
    }

    private fun bindTicketEvents() {
        this.register(object : ApplicationEventListener<TicketEvent.GotoDashboard> {
            @Subscribe
            override fun handle(event: TicketEvent.GotoDashboard) {
                val data = TicketScreenData.GotoDashboard(event.data)
                val presenter = PresenterResolver.getPresenter(TicketDashboardPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<TicketEvent.GotoRead> {
            @Subscribe
            override fun handle(event: TicketEvent.GotoRead) {
                val ticketKeyMapper = AppContextUtil.getSpringBean(TicketKeyMapper::class.java)
                val ex = TicketKeyExample()
                ex.createCriteria().andProjectidEqualTo(event.projectId).andTicketkeyEqualTo(event.ticketKey)
                val ticketKeys = ticketKeyMapper.selectByExample(ex)
                when (ticketKeys.size) {
                    0 -> throw ResourceNotFoundException("Not found ticketKey with projectId=${event.projectId} and ticketKey=${event.ticketKey}")
                    1 -> {
                        val ticketKey = ticketKeys[0]
                        when (ticketKey.tickettype) {
                            ProjectTypeConstants.RISK -> {
                                val riskService = AppContextUtil.getSpringBean(RiskService::class.java)
                                val risk = riskService.findById(ticketKey.ticketid, AppUI.accountId)
                                if (risk != null) {
                                    val presenter = PresenterResolver.getPresenter(IRiskReadPresenter::class.java)
                                    presenter.go(projectView, RiskScreenData.Read(risk))
                                } else throw ResourceNotFoundException("Can not find risk with id = ${ticketKey.ticketid} in account ${AppUI.accountId}")
                            }
                            ProjectTypeConstants.TASK -> {
                                val taskService = AppContextUtil.getSpringBean(TaskService::class.java)
                                val task = taskService.findById(ticketKey.ticketid, AppUI.accountId)
                                if (task != null) {
                                    val presenter = PresenterResolver.getPresenter(TaskReadPresenter::class.java)
                                    presenter.go(projectView, TaskScreenData.Read(task))
                                } else throw ResourceNotFoundException("Can not find task with id = ${ticketKey.ticketid} in account ${AppUI.accountId}")
                            }
                            ProjectTypeConstants.BUG -> {
                                val bugService = AppContextUtil.getSpringBean(BugService::class.java)
                                val bug = bugService.findById(ticketKey.ticketid, AppUI.accountId)
                                if (bug != null) {
                                    val presenter = PresenterResolver.getPresenter(BugReadPresenter::class.java)
                                    presenter.go(projectView, BugScreenData.Read(bug))
                                } else throw ResourceNotFoundException("Can not find bug with id = ${ticketKey.ticketid} in account ${AppUI.accountId}")
                            }
                            else -> throw MyCollabException("Not support ticket type ${ticketKey.tickettype}")
                        }
                    }
                    else -> throw MyCollabException("Find more than one ticketKeys with projectId=${event.projectId} and ticketKey=${event.ticketKey}")
                }

            }
        })
    }

    private fun bindTaskEvents() {
        this.register(object : ApplicationEventListener<TaskEvent.GotoRead> {
            @Subscribe
            override fun handle(event: TaskEvent.GotoRead) {
                val data = TaskScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(TaskReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<TaskEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: TaskEvent.GotoAdd) {
                val param = event.data
                val data = if (param is SimpleTask) TaskScreenData.Add(param) else TaskScreenData.Add(SimpleTask())
                val presenter = PresenterResolver.getPresenter(TaskAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<TaskEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: TaskEvent.GotoEdit) {
                val data = TaskScreenData.Edit(event.data as SimpleTask)
                val presenter = PresenterResolver.getPresenter(TaskAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<TaskEvent.GotoKanbanView> {
            @Subscribe
            override fun handle(event: TaskEvent.GotoKanbanView) {
                val data = TicketScreenData.GotoKanbanView()
                val presenter = PresenterResolver.getPresenter(ITicketKanbanPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
    }

    private fun bindRiskEvents() {
        this.register(object : ApplicationEventListener<RiskEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: RiskEvent.GotoAdd) {
                val param = event.data
                val data = if (param is SimpleRisk) RiskScreenData.Add(param) else RiskScreenData.Add(SimpleRisk())
                val presenter = PresenterResolver.getPresenter(IRiskAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<RiskEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: RiskEvent.GotoEdit) {
                val data = RiskScreenData.Edit(event.data as Risk)
                val presenter = PresenterResolver.getPresenter(IRiskAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<RiskEvent.GotoRead> {
            @Subscribe
            override fun handle(event: RiskEvent.GotoRead) {
                val data = RiskScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(IRiskReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
    }

    private fun bindBugEvents() {
        this.register(object : ApplicationEventListener<BugEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: BugEvent.GotoAdd) {
                val data = BugScreenData.Add(SimpleBug())
                val presenter = PresenterResolver.getPresenter(BugAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: BugEvent.GotoEdit) {
                val data = BugScreenData.Edit(event.data as SimpleBug)
                val presenter = PresenterResolver.getPresenter(BugAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugEvent.GotoRead> {
            @Subscribe
            override fun handle(event: BugEvent.GotoRead) {
                val data = BugScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(BugReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<BugComponentEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: BugComponentEvent.GotoAdd) {
                val data = ComponentScreenData.Add(Component())
                val presenter = PresenterResolver.getPresenter(ComponentAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugComponentEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: BugComponentEvent.GotoEdit) {
                val data = ComponentScreenData.Edit(event.data as Component)
                val presenter = PresenterResolver.getPresenter(ComponentAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugComponentEvent.GotoRead> {
            @Subscribe
            override fun handle(event: BugComponentEvent.GotoRead) {
                val data = ComponentScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(ComponentReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugComponentEvent.GotoList> {
            @Subscribe
            override fun handle(event: BugComponentEvent.GotoList) {
                val criteria = ComponentSearchCriteria()
                criteria.projectId = NumberSearchField(CurrentProjectVariables.projectId)
                val presenter = PresenterResolver.getPresenter(ComponentListPresenter::class.java)
                presenter.go(projectView, ComponentScreenData.Search(criteria))
            }
        })
        this.register(object : ApplicationEventListener<BugVersionEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: BugVersionEvent.GotoAdd) {
                val data = VersionScreenData.Add(Version())
                val presenter = PresenterResolver.getPresenter(VersionAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugVersionEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: BugVersionEvent.GotoEdit) {
                val data = VersionScreenData.Edit(event.data as Version)
                val presenter = PresenterResolver.getPresenter(VersionAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugVersionEvent.GotoRead> {
            @Subscribe
            override fun handle(event: BugVersionEvent.GotoRead) {
                val data = VersionScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(VersionReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<BugVersionEvent.GotoList> {
            @Subscribe
            override fun handle(event: BugVersionEvent.GotoList) {
                val criteria = VersionSearchCriteria()
                criteria.projectId = NumberSearchField(CurrentProjectVariables.projectId)
                val presenter = PresenterResolver.getPresenter(VersionListPresenter::class.java)
                presenter.go(projectView, VersionScreenData.Search(criteria))
            }
        })
    }

    private fun bindMessageEvents() {
        this.register(object : ApplicationEventListener<MessageEvent.GotoRead> {
            @Subscribe
            override fun handle(event: MessageEvent.GotoRead) {
                val data = MessageScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(MessageReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<MessageEvent.GotoList> {
            @Subscribe
            override fun handle(event: MessageEvent.GotoList) {
                val searchCriteria = MessageSearchCriteria()
                searchCriteria.projectIds = SetSearchField(CurrentProjectVariables.projectId)
                val data = MessageScreenData.Search(searchCriteria)
                val presenter = PresenterResolver.getPresenter(MessageListPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
    }

    private fun bindMilestoneEvents() {
        this.register(object : ApplicationEventListener<MilestoneEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: MilestoneEvent.GotoAdd) {
                val data = MilestoneScreenData.Add(SimpleMilestone())
                val presenter = PresenterResolver.getPresenter(MilestoneAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<MilestoneEvent.GotoRead> {
            @Subscribe
            override fun handle(event: MilestoneEvent.GotoRead) {
                val data = MilestoneScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(MilestoneReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<MilestoneEvent.GotoList> {
            @Subscribe
            override fun handle(event: MilestoneEvent.GotoList) {
                val criteria = MilestoneSearchCriteria()
                criteria.projectIds = SetSearchField(CurrentProjectVariables.projectId)
                val presenter = PresenterResolver.getPresenter(MilestoneListPresenter::class.java)
                presenter.go(projectView, MilestoneScreenData.Search(criteria))
            }
        })

        this.register(object : ApplicationEventListener<MilestoneEvent.GotoRoadmap> {
            @Subscribe
            override fun handle(event: MilestoneEvent.GotoRoadmap) {
                val presenter = PresenterResolver.getPresenter(MilestoneRoadmapPresenter::class.java)
                presenter.go(projectView, MilestoneScreenData.Roadmap())
            }
        })

        this.register(object : ApplicationEventListener<MilestoneEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: MilestoneEvent.GotoEdit) {
                val data = MilestoneScreenData.Edit(event.data as Milestone)
                val presenter = PresenterResolver.getPresenter(MilestoneAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
    }

    private fun bindUserGroupEvents() {
        this.register(object : ApplicationEventListener<ProjectRoleEvent.GotoList> {
            @Subscribe
            override fun handle(event: ProjectRoleEvent.GotoList) {
                val project = CurrentProjectVariables.project
                val criteria = ProjectRoleSearchCriteria()
                criteria.projectId = NumberSearchField(project!!.id)
                val presenter = PresenterResolver.getPresenter(ProjectRoleListPresenter::class.java)
                presenter.go(projectView, ProjectRoleScreenData.Search(criteria))
            }
        })
        this.register(object : ApplicationEventListener<ProjectRoleEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: ProjectRoleEvent.GotoAdd) {
                val data = ProjectRoleScreenData.Add(ProjectRole())
                val presenter = PresenterResolver.getPresenter(ProjectRoleAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<ProjectRoleEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: ProjectRoleEvent.GotoEdit) {
                val data = ProjectRoleScreenData.Add(event.data as ProjectRole)
                val presenter = PresenterResolver.getPresenter(ProjectRoleAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<ProjectRoleEvent.GotoRead> {
            @Subscribe
            override fun handle(event: ProjectRoleEvent.GotoRead) {
                val data = ProjectRoleScreenData.Read(event.data as Int)
                val presenter = PresenterResolver.getPresenter(ProjectRoleReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<ProjectMemberEvent.GotoList> {
            @Subscribe
            override fun handle(event: ProjectMemberEvent.GotoList) {
                val criteria = ProjectMemberSearchCriteria()
                criteria.projectIds = SetSearchField(event.projectId)
                criteria.saccountid = NumberSearchField(AppUI.accountId)
                criteria.statuses = SetSearchField(ProjectMemberStatusConstants.ACTIVE, ProjectMemberStatusConstants.NOT_ACCESS_YET)
                val presenter = PresenterResolver.getPresenter(ProjectMemberListPresenter::class.java)
                presenter.go(projectView, ProjectMemberScreenData.Search(criteria))
            }
        })
        this.register(object : ApplicationEventListener<ProjectMemberEvent.GotoRead> {
            @Subscribe
            override fun handle(event: ProjectMemberEvent.GotoRead) {
                val data = ProjectMemberScreenData.Read(event.data)
                val presenter = PresenterResolver.getPresenter(ProjectMemberReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<ProjectMemberEvent.GotoInviteMembers> {
            @Subscribe
            override fun handle(event: ProjectMemberEvent.GotoInviteMembers) {
                val data = ProjectMemberScreenData.InviteProjectMembers()
                val presenter = PresenterResolver.getPresenter(ProjectMemberInvitePresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<ProjectMemberEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: ProjectMemberEvent.GotoEdit) {
                val data = ProjectMemberScreenData.Add(event.data as ProjectMember)
                val presenter = PresenterResolver.getPresenter(ProjectMemberEditPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<ProjectNotificationEvent.GotoList> {
            @Subscribe
            override fun handle(event: ProjectNotificationEvent.GotoList) {
                val data = ProjectSettingScreenData.ViewSettings()
                val presenter = PresenterResolver.getPresenter(ProjectCustomPresenter::class.java)
                presenter.go(projectView, data)
            }
        })

        this.register(object : ApplicationEventListener<CustomizeUIEvent.UpdateFeaturesList> {
            @Subscribe
            override fun handle(event: CustomizeUIEvent.UpdateFeaturesList) {
                projectView.updateProjectFeatures()
            }
        })
    }

    private fun bindTimeandInvoiceEvents() {
        this.register(object : ApplicationEventListener<InvoiceEvent.GotoList> {
            @Subscribe
            override fun handle(event: InvoiceEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(IInvoiceListPresenter::class.java)
                presenter.go(projectView, null)
            }
        })
    }

    private fun bindPageEvents() {
        this.register(object : ApplicationEventListener<PageEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: PageEvent.GotoAdd) {
                var pagePath = event.data as? String
                if ("" == pagePath || pagePath == null) {
                    pagePath = "${CurrentProjectVariables.currentPagePath}/${StringUtils.generateSoftUniqueId()}"
                }
                val page = Page()
                page.path = pagePath
                val data = PageScreenData.Add(page)
                val presenter = PresenterResolver.getPresenter(PageAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<PageEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: PageEvent.GotoEdit) {
                val data = PageScreenData.Edit(event.data as Page)
                val presenter = PresenterResolver.getPresenter(PageAddPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<PageEvent.GotoRead> {
            @Subscribe
            override fun handle(event: PageEvent.GotoRead) {
                val data = PageScreenData.Read(event.data as Page)
                val presenter = PresenterResolver.getPresenter(PageReadPresenter::class.java)
                presenter.go(projectView, data)
            }
        })
        this.register(object : ApplicationEventListener<PageEvent.GotoList> {
            @Subscribe
            override fun handle(event: PageEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(PageListPresenter::class.java)
                presenter.go(projectView, PageScreenData.Search(event.data))
            }
        })
    }
}