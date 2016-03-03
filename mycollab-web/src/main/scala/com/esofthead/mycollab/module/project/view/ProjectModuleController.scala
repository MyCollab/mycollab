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
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria
import com.esofthead.mycollab.module.project.events.ProjectEvent.GotoMyProject
import com.esofthead.mycollab.module.project.events.{ClientEvent, ProjectEvent}
import com.esofthead.mycollab.module.project.view.client.IClientPresenter
import com.esofthead.mycollab.module.project.view.parameters.ClientScreenData
import com.esofthead.mycollab.vaadin.mvp.{AbstractController, PageActionChain, PresenterResolver}
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

  this.register(new ApplicationEventListener[ClientEvent.GotoList]() {
    @Subscribe override def handle(event: ClientEvent.GotoList): Unit = {
      val presenter = PresenterResolver.getPresenter(classOf[IClientPresenter])
      val searchCriteria = new AccountSearchCriteria
      presenter.go(container, new ClientScreenData.Search(searchCriteria))
    }
  })
}
