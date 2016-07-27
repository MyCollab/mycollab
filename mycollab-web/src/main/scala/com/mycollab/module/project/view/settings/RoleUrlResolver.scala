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
package com.mycollab.module.project.view.settings

import com.mycollab.common.UrlTokenizer
import com.mycollab.db.arguments.NumberSearchField
import com.mycollab.eventmanager.EventBusFactory
import com.mycollab.module.project.domain.ProjectRole
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria
import com.mycollab.module.project.events.ProjectEvent
import com.mycollab.module.project.service.ProjectRoleService
import com.mycollab.module.project.view.ProjectUrlResolver
import com.mycollab.module.project.view.parameters.{ProjectRoleScreenData, ProjectScreenData}
import com.mycollab.spring.AppContextUtil
import com.mycollab.vaadin.AppContext
import com.mycollab.vaadin.mvp.PageActionChain

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class RoleUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new ListUrlResolver())
  this.addSubResolver("preview", new PreviewUrlResolver())
  this.addSubResolver("edit", new EditUrlResolver())
  this.addSubResolver("add", new AddUrlResolver())
  
  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = UrlTokenizer(params(0)).getInt
      val roleSearchCriteria = new ProjectRoleSearchCriteria
      roleSearchCriteria.setProjectId(new NumberSearchField(projectId))
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectRoleScreenData.Search(roleSearchCriteria))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val projectId = token.getInt
      val roleId = token.getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectRoleScreenData.Read(roleId))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val projectId = token.getInt
      val roleId = token.getInt
      val roleService = AppContextUtil.getSpringBean(classOf[ProjectRoleService])
      val role = roleService.findById(roleId, AppContext.getAccountId)
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectRoleScreenData.Add(role))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
  private class AddUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = UrlTokenizer(params(0))
      val projectId = token.getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectRoleScreenData.Add(new ProjectRole()))
      EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }
  
}
