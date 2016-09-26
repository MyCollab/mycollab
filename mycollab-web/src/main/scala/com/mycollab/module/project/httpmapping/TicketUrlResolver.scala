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
package com.mycollab.module.project.httpmapping

import com.mycollab.common.UrlTokenizer
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.event.ProjectEvent
import com.mycollab.module.project.view.parameters.{ProjectScreenData, TicketScreenData}
import com.mycollab.vaadin.mvp.PageActionChain

/**
  * @author MyCollab Ltd
  * @since 5.4.3
  */
class TicketUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("dashboard", new DashboardUrlResolver)
  
  this.defaultUrlResolver = new DashboardUrlResolver
  
  private class DashboardUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val tokenizer = UrlTokenizer(params(0))
      val projectId = tokenizer.getInt
      val query = tokenizer.getQuery
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new TicketScreenData.GotoDashboard(query))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
}
