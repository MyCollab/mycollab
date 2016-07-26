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
package com.mycollab.module.project.view.risk

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.domain.SimpleRisk
import com.mycollab.module.project.domain.criteria.RiskSearchCriteria
import com.mycollab.module.project.events.ProjectEvent
import com.mycollab.module.project.service.RiskService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.{ProjectScreenData, RiskScreenData}
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.spring.AppContextUtil

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class RiskUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)

  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = UrlTokenizer(params(0)).getInt
      val riskSearchCriteria = new RiskSearchCriteria
      riskSearchCriteria.setProjectId(new NumberSearchField(projectId))
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new RiskScreenData.Search(riskSearchCriteria))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val projectId = token.getInt
      val riskId = token.getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new RiskScreenData.Read(riskId))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class AddUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new RiskScreenData.Add(new SimpleRisk))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val projectId = token.getInt
      val riskId = token.getInt
      val riskService = AppContextUtil.getSpringBean(classOf[RiskService])
      val risk = riskService.findById(riskId, AppContext.getAccountId)
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new RiskScreenData.Edit(risk))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

}
