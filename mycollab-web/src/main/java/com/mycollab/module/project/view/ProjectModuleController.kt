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
import com.mycollab.common.domain.SimpleClient
import com.mycollab.common.service.ClientService
import com.mycollab.module.project.event.*
import com.mycollab.module.project.view.parameters.ClientScreenData
import com.mycollab.module.project.view.parameters.ProjectScreenData
import com.mycollab.module.project.view.parameters.ReportScreenData
import com.mycollab.module.project.view.parameters.StandupScreenData
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppUI
import com.mycollab.vaadin.ApplicationEventListener
import com.mycollab.vaadin.mvp.AbstractController
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.vaadin.mvp.PresenterResolver
import java.time.LocalDate

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class ProjectModuleController(val container: ProjectModule) : AbstractController() {
    init {
        this.register(object : ApplicationEventListener<ProjectEvent.GotoMyProject> {
            @Subscribe
            override fun handle(event: ProjectEvent.GotoMyProject) {
                val presenter = PresenterResolver.getPresenter(ProjectViewPresenter::class.java)
                presenter.handleChain(container, event.data as PageActionChain)
            }
        })

        this.register(object : ApplicationEventListener<ProjectEvent.GotoUserDashboard> {
            @Subscribe
            override fun handle(event: ProjectEvent.GotoUserDashboard) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, null)
            }
        })

        this.register(object : ApplicationEventListener<ProjectEvent.GotoList> {
            @Subscribe
            override fun handle(event: ProjectEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, ProjectScreenData.GotoList())
            }
        })

        this.register(object : ApplicationEventListener<FavoriteEvent.GotoList> {
            @Subscribe
            override fun handle(event: FavoriteEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, ProjectScreenData.GotoList())
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoList> {
            @Subscribe
            override fun handle(event: ClientEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, ClientScreenData.Search(null))
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoAdd> {
            @Subscribe
            override fun handle(event: ClientEvent.GotoAdd) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                val client = SimpleClient()
                presenter.go(container, ClientScreenData.Add(client))
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoEdit> {
            @Subscribe
            override fun handle(event: ClientEvent.GotoEdit) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                if (event.data is Int) {
                    val clientService = AppContextUtil.getSpringBean(ClientService::class.java)
                    val client = clientService.findById(event.data, AppUI.accountId)
                    if (client != null) {
                        presenter.go(container, ClientScreenData.Edit(client))
                    }
                } else if (event.data is SimpleClient) {
                    presenter.go(container, ClientScreenData.Edit(event.data))
                }
            }
        })

        this.register(object : ApplicationEventListener<ClientEvent.GotoRead> {
            @Subscribe
            override fun handle(event: ClientEvent.GotoRead) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                val clientId = event.data as Int
                presenter.go(container, ClientScreenData.Read(clientId))
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoConsole> {
            @Subscribe
            override fun handle(event: ReportEvent.GotoConsole) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, ReportScreenData.GotoConsole())
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoTimesheetReport> {
            @Subscribe
            override fun handle(event: ReportEvent.GotoTimesheetReport) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, ReportScreenData.GotoTimesheet())
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoWeeklyTimingReport> {
            @Subscribe
            override fun handle(event: ReportEvent.GotoWeeklyTimingReport) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, ReportScreenData.GotoWeeklyTiming())
            }
        })

        this.register(object : ApplicationEventListener<ReportEvent.GotoUserWorkloadReport> {
            @Subscribe
            override fun handle(event: ReportEvent.GotoUserWorkloadReport) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, ReportScreenData.GotoUserWorkload())
            }
        })

        this.register(object : ApplicationEventListener<StandUpEvent.GotoList> {
            @Subscribe
            override fun handle(event: StandUpEvent.GotoList) {
                val presenter = PresenterResolver.getPresenter(BoardContainerPresenter::class.java)
                presenter.go(container, StandupScreenData.Search(LocalDate.now()))
            }
        })
    }
}