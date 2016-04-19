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
import com.esofthead.mycollab.module.crm.domain.SimpleAccount
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import com.esofthead.mycollab.module.project.events.ProjectEvent.GotoMyProject
import com.esofthead.mycollab.module.project.events.{ClientEvent, ProjectEvent}
import com.esofthead.mycollab.module.project.view.client.IClientPresenter
import com.esofthead.mycollab.module.project.view.parameters.{ClientScreenData, ProjectScreenData}
import com.esofthead.mycollab.module.project.view.parameters.ClientScreenData.{Add, Read}
import com.esofthead.mycollab.vaadin.mvp.{AbstractController, PageActionChain, PresenterResolver, ScreenData}
import com.google.common.eventbus.Subscribe

/**
  * @author MyCollab Ltd.
  * @since 5.0.9
  */
class ProjectModuleController(val container: ProjectModule) extends AbstractController {
  this.register(new ApplicationEventListener[ProjectEvent.GotoMyProject]() {
    @Subscribe override def handle(event: GotoMyProject): Unit = {
      val presenter = PresenterResolver.getPresenter(classOf[ProjectViewPresenter])
      presenter.handleChain(container, event.getData.asInstanceOf[PageActionChain])
    }
  })

  this.register(new ApplicationEventListener[ProjectEvent.GotoList]() {
    @Subscribe override def handle(event: ProjectEvent.GotoList): Unit = {
      val presenter = PresenterResolver.getPresenter(classOf[UserDashboardPresenter])
      presenter.go(container, new ProjectScreenData.GotoList())
    }
  })

  this.register(new ApplicationEventListener[ClientEvent.GotoList]() {
    @Subscribe override def handle(event: ClientEvent.GotoList): Unit = {
      val presenter = PresenterResolver.getPresenter(classOf[IClientPresenter])
      val searchCriteria = new AccountSearchCriteria
      presenter.go(container, new ClientScreenData.Search(searchCriteria))
    }
  })

  this.register(new ApplicationEventListener[ClientEvent.GotoAdd]() {
    @Subscribe def handle(event: ClientEvent.GotoAdd): Unit = {
      val presenter = PresenterResolver.getPresenter(classOf[IClientPresenter])
      val account = new SimpleAccount
      presenter.go(container, new Add(account))
    }
  })

  this.register(new ApplicationEventListener[ClientEvent.GotoEdit]() {
    @Subscribe def handle(event: ClientEvent.GotoEdit): Unit = {
      val presenter = PresenterResolver.getPresenter(classOf[IClientPresenter])
      presenter.go(container, new ScreenData.Edit[Any](event.getData))
    }
  })

  this.register(new ApplicationEventListener[ClientEvent.GotoRead]() {
    @Subscribe def handle(event: ClientEvent.GotoRead): Unit = {
      val presenter = PresenterResolver.getPresenter(classOf[IClientPresenter])
      val clientId = event.getData.asInstanceOf[Integer]
      presenter.go(container, new Read(clientId))
    }
  })
}
