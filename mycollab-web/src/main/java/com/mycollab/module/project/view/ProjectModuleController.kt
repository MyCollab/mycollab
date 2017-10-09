package com.mycollab.module.project.view

import com.google.common.eventbus.Subscribe
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.module.crm.domain.SimpleAccount
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import com.mycollab.module.project.event.ClientEvent
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.event.ReportEvent
import com.mycollab.module.project.event.StandUpEvent
import com.mycollab.module.project.view.client.IClientPresenter
import com.mycollab.module.project.view.parameters.ClientScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.ReportScreenData
import com.mycollab.module.project.view.parameters.StandupScreenData
import com.mycollab.module.project.view.reports.IReportPresenter
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.vaadin.mvp.PresenterResolver
import com.mycollab.vaadin.mvp.ScreenData
import java.util.*

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ProjectModuleController(val container: ProjectModule) : AbstractController() {
    init {
        this.register(object : ApplicationEventListener<ProjectEvent.GotoUserDashboard> {
            @Subscribe override fun handle(event: ProjectEvent.GotoUserDashboard) {
                val presenter = PresenterResolver.getPresenter(UserDashboardPresenter::class.java)
                presenter.go(container, null)
            }
        })

        this.register(object : ApplicationEventListener<ProjectEvent.GotoMyProject> {
            @Subscribe override fun handle(event: ProjectEvent.GotoMyProject) {
                val presenter = PresenterResolver.getPresenter(ProjectViewPresenter::class.java)
                presenter.handleChain(container, event.data as PageActionChain)
            }
        })

        this.register(object : ApplicationEventListener<ProjectEvent.GotoList> {
            @Subscribe override fun handle(event: ProjectEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(UserDashboardPresenter::class.java)
                presenter.go(container, ProjectScreenData.GotoList())
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoList> {
            @Subscribe override fun handle(event: ClientEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(IClientPresenter::class.java)
                val searchCriteria = AccountSearchCriteria()
                presenter.go(container, ClientScreenData.Search(searchCriteria))
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoAdd> {
            @Subscribe override fun handle(event: ClientEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(IClientPresenter::class.java)
                val account = SimpleAccount()
                presenter.go(container, ClientScreenData.Add(account))
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoEdit> {
            @Subscribe override fun handle(event: ClientEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(IClientPresenter::class.java)
                presenter.go(container, ScreenData.Edit(event.data))
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoRead> {
            @Subscribe override fun handle(event: ClientEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(IClientPresenter::class.java)
                val clientId = event.data as Int
                presenter.go(container, ClientScreenData.Read(clientId))
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoConsole> {
            @Subscribe override fun handle(event: ReportEvent.GotoConsole) {
                val presenter = PresenterResolver.getPresenter(IReportPresenter::class.java)
                presenter.go(container, null)
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoTimesheetReport> {
            @Subscribe override fun handle(event: ReportEvent.GotoTimesheetReport) {
                val presenter = PresenterResolver.getPresenter(IReportPresenter::class.java)
                presenter.go(container, ReportScreenData.GotoTimesheet())
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoWeeklyTimingReport> {
            @Subscribe override fun handle(event: ReportEvent.GotoWeeklyTimingReport) {
                val presenter = PresenterResolver.getPresenter(IReportPresenter::class.java)
                presenter.go(container, ReportScreenData.GotoWeeklyTiming())
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoUserWorkloadReport> {
            @Subscribe override fun handle(event: ReportEvent.GotoUserWorkloadReport) {
                val presenter = PresenterResolver.getPresenter(IReportPresenter::class.java)
                presenter.go(container, ReportScreenData.GotoUserWorkload())
            }
        })

        this.register(object : ApplicationEventListener<StandUpEvent.GotoList> {
            @Subscribe override fun handle(event: StandUpEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(IReportPresenter::class.java)
                presenter.go(container, StandupScreenData.Search(GregorianCalendar().time))
            }
        })
    }
}