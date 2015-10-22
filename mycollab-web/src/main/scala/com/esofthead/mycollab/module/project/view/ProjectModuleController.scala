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

import com.esofthead.mycollab.eventmanager.ApplicationEventListener
import com.esofthead.mycollab.module.project.events.ProjectEvent.GotoMyProject
import com.esofthead.mycollab.module.project.events.{CalendarEvent, FollowingTicketEvent, ProjectEvent, TimeTrackingEvent}
import com.esofthead.mycollab.module.project.view.parameters.FollowingTicketsScreenData
import com.esofthead.mycollab.vaadin.mvp.{AbstractController, PageActionChain, PresenterResolver, ScreenData}
import com.google.common.eventbus.Subscribe

/**
 * @author MyCollab Ltd.
 * @since 5.0.9
 */
class ProjectModuleController(val container: ProjectModule) extends AbstractController {
    bindProjectEvents()
    bindFollowingTicketEvents()
    bindTimeTrackingEvents()
    bindCalendarEvents()

    private def bindProjectEvents(): Unit = {
        this.register(new ApplicationEventListener[ProjectEvent.GotoMyProject]() {
            @Subscribe override def handle(event: GotoMyProject): Unit = {
                val presenter = PresenterResolver.getPresenter(classOf[ProjectViewPresenter])
                presenter.handleChain(container, event.getData.asInstanceOf[PageActionChain])
            }
        })
    }

    private def bindFollowingTicketEvents(): Unit = {
        this.register(new ApplicationEventListener[FollowingTicketEvent.GotoMyFollowingItems]() {
            @Subscribe def handle(event: FollowingTicketEvent.GotoMyFollowingItems) {
                val presenter = PresenterResolver.getPresenter(classOf[FollowingTicketPresenter])
                presenter.go(container, new FollowingTicketsScreenData.GotoMyFollowingItems(event.getData
                    .asInstanceOf[java.util.List[Integer]]))
            }
        })
    }

    private def bindTimeTrackingEvents(): Unit = {
        this.register(new ApplicationEventListener[TimeTrackingEvent.GotoTimeTrackingView]() {
            @Subscribe def handle(event: TimeTrackingEvent.GotoTimeTrackingView) {
                val presenter = PresenterResolver.getPresenter(classOf[TimeTrackingPresenter])
                presenter.go(container, new ScreenData[Any](event.getData))
            }
        })
    }

    private def bindCalendarEvents(): Unit = {
        this.register(new ApplicationEventListener[CalendarEvent.GotoCalendarView]() {
            @Subscribe def handle(event: CalendarEvent.GotoCalendarView) {
                val presenter = PresenterResolver.getPresenter(classOf[CalendarDashboardPresenter])
                presenter.go(container, new ScreenData[Any](event.getData))
            }
        })
    }
}
