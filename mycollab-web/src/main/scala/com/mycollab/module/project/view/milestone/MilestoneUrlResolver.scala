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
package com.mycollab.module.project.view.milestone

import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.domain.SimpleMilestone
import com.mycollab.module.project.domain.criteria.MilestoneSearchCriteria
import com.mycollab.module.project.events.ProjectEvent
import com.mycollab.module.project.service.MilestoneService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.{MilestoneScreenData, ProjectScreenData}
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.PageActionChain
import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.SetSearchField
import com.mycollab.spring.AppContextUtil

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class MilestoneUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("add", new AddUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)

  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = UrlTokenizer(params(0)).getInt
      val milestoneSearchCriteria = new MilestoneSearchCriteria
      milestoneSearchCriteria.setProjectIds(new SetSearchField[Integer](projectId))
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new MilestoneScreenData.Search(milestoneSearchCriteria))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class AddUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = UrlTokenizer(params(0)).getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId),
        new MilestoneScreenData.Add(new SimpleMilestone))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val projectId = token.getInt
      val milestoneId = token.getInt
      val milestoneService = AppContextUtil.getSpringBean(classOf[MilestoneService])
      val milestone = milestoneService.findById(milestoneId, AppContext.getAccountId)
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new MilestoneScreenData.Edit(milestone))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val projectId = token.getInt
      val milestoneId = token.getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new MilestoneScreenData.Read(milestoneId))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

}
