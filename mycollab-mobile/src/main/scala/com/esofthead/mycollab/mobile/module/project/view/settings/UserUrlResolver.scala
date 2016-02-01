/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.project.view.settings

import com.esofthead.mycollab.common.UrlTokenizer
import com.esofthead.mycollab.core.arguments.{NumberSearchField, StringSearchField}
import com.esofthead.mycollab.eventmanager.EventBusFactory
import com.esofthead.mycollab.mobile.module.project.ProjectUrlResolver
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent
import com.esofthead.mycollab.mobile.module.project.view.parameters.{ProjectMemberScreenData, ProjectScreenData}
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria
import com.esofthead.mycollab.module.project.service.ProjectMemberService
import com.esofthead.mycollab.spring.ApplicationContextUtil
import com.esofthead.mycollab.vaadin.AppContext
import com.esofthead.mycollab.vaadin.mvp.PageActionChain

/**
  * @author MyCollab Ltd
  * @since 5.0.9
  */
class UserUrlResolver extends ProjectUrlResolver {
  this.addSubResolver("list", new ListUrlResolver)
  this.addSubResolver("preview", new PreviewUrlResolver)
  this.addSubResolver("edit", new EditUrlResolver)
  this.addSubResolver("invite", new InviteUrlResolver)

  private class ListUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val projectId = new UrlTokenizer(params(0)).getInt
      val memberSearchCriteria = new ProjectMemberSearchCriteria
      memberSearchCriteria.setProjectId(new NumberSearchField(projectId))
      memberSearchCriteria.setStatus(StringSearchField.and(ProjectMemberStatusConstants.ACTIVE))
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectMemberScreenData.Search(memberSearchCriteria))
      EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class PreviewUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token: UrlTokenizer = new UrlTokenizer(params(0))
      val projectId: Int = token.getInt
      val memberName: String = token.getString
      val chain: PageActionChain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectMemberScreenData.Read(memberName))
      EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class EditUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = new UrlTokenizer(params(0))
      val projectId = token.getInt
      val memberId = token.getInt
      val memberService = ApplicationContextUtil.getSpringBean(classOf[ProjectMemberService])
      val member = memberService.findById(memberId, AppContext.getAccountId)
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectMemberScreenData.Edit(member))
      EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

  private class InviteUrlResolver extends ProjectUrlResolver {
    protected override def handlePage(params: String*) {
      val token = new UrlTokenizer(params(0))
      val projectId = token.getInt
      val chain = new PageActionChain(new ProjectScreenData.Goto(projectId), new ProjectMemberScreenData.InviteProjectMembers)
      EventBusFactory.getInstance.post(new ProjectEvent.GotoMyProject(this, chain))
    }
  }

}
